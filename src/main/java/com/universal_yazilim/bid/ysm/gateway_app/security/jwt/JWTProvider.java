package com.universal_yazilim.bid.ysm.gateway_app.security.jwt;

import com.universal_yazilim.bid.ysm.gateway_app.security.model.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

// ******8 generateToken() metodu ile kimliği doğrulanmış kullanıcı için (UserPrinciple tipinde)
// JSON Web Token (JWT) oluşturulur.
// -> AuthenticationService'teki generateJWT() metodu tamamlanır.
@Component
public class JWTProvider implements JWTProvidable
{

    @Value("${authentication.jwt.expiration-in-ms}")
    private Long JWT_EXPIRATION_MS ;
    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    /*
        Java security icin ozel ve genel anahtar kodlayiciya ihtiyac var.
        ozel anahtar kodlama icin PKCS8EncodedKeySpec sinifini
        acik anahtar kodlama icin X509EncodedKeySpec sinifi
     */
    public JWTProvider(@Value("${authentication.jwt.private-key}") String strPrivateKey,
                       @Value("${authentication.jwt.public-key}") String strPublicKey)
    {
        Base64.Decoder decoder = Base64.getDecoder();

        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(decoder.decode(strPrivateKey.getBytes()));
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(decoder.decode(strPublicKey.getBytes()));


        try
        {
            KeyFactory keyFactory = getKeyFactory();

            privateKey = keyFactory.generatePrivate(privateKeySpec);
            publicKey = keyFactory.generatePublic(publicKeySpec);
        }
        catch (InvalidKeySpecException e)
        {
            throw new RuntimeException(ERR_JWT_INVALID_KEY, e);
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new RuntimeException(ERR_JWT_ALGORITHM, e);
        }
    }

    // Token sona erme tarihi su andan once ise
    @Override
    public boolean isValidToken(HttpServletRequest request)
    {
        String token = resolveToken(request);

        if (token == null)
        {
            return false;
        }

        Claims claims = Jwts.parserBuilder().setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        // Son kullanma tarihi kontrolu, expired olmussa valid degildir.
        if(claims.getExpiration().before(new Date()))
        {
            return false;
        }

        return true;
    }


    private String resolveToken(HttpServletRequest request)
    {
        String bearerToken = request.getHeader(HEADER_STRING);
        if(bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX))
        {
            return bearerToken.substring(TOKEN_START_DIGIT);
        }

        return null;
    }

    @Override
    public Authentication getAuthentication(HttpServletRequest request)
    {

        String token = resolveToken(request);
        if(token == null)
        {
            return null;
        }


        Claims claims = Jwts.parserBuilder().setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody(); // (Token'dan kullanici detaylari alinir.)

        // subject olarak kullanici ad olacak
        String userName = claims.getSubject();

        Integer userID = claims.get("id", Integer.class);


        // rolleri ","e gore bolduk
        // ONEMLI: Spring Security ve JSON Web Token'da kullanmak icin rolleri
        // ROLE_ ile baslamalidir. ROLE_ on eki yoksa, eklenir kimlik dogrulamasina.
        List<GrantedAuthority> authorities
                = Arrays.stream(claims.get("roles").toString().split(","))
                .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        UserDetails userDetails = new UserPrincipal(userID, userName, null);

        return userName != null ? new UsernamePasswordAuthenticationToken(userDetails, null, authorities) : null;

    }

    /*
        NOT: AuthenticationService'de signInAndReturnJWT()'de kullanilir.
    */
    // (UserPrinciple nesnelerini bekler bu metot)
    // UserPrinciple nesneleri -> oturum acma isleminden sonra olusturulur.
    // Kimligi dogrulanmis kullanicilarin kimlik bilgilerini (credentials) ve rollerini icerir.
    @Override
    public String generateToken(UserPrincipal user)
    {
        // yetkililer tanimlandi
        String authorities = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining());

        // kullanici ad JWT nesnesinin ana tanimlayicisi olacak -> subject'i (setSubject())
        // talep metodu (claim())
        return Jwts.builder().setSubject(user.getUsername())
                .claim("id", user.getId())
                .claim("roles", authorities)
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_MS))
                // burada bir algoritma ile imzaliyoruz using SHA-512 (min key length 2048 - o secildi basta)
                .signWith(privateKey, SignatureAlgorithm.RS512)
                .compact();

    }
}
