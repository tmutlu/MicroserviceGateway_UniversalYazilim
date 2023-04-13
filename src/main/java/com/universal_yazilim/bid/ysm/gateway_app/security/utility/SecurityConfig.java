package com.universal_yazilim.bid.ysm.gateway_app.security.utility;

import com.universal_yazilim.bid.ysm.gateway_app.model.service.AbstractProductService;
import com.universal_yazilim.bid.ysm.gateway_app.security.jwt.JWTAuthenticationFilter;
import com.universal_yazilim.bid.ysm.gateway_app.utility.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*
    web tabanlı güvenlik sağlamak için
    @EnableWebSecurity "annotation"ı
    ve WebSecurityConfigurerAdapter sınıfı
    kullanıldı.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    @Autowired
    private UserDetailsService userDetailsService;

    @Value("${service.security.secure-key-username}")
    private String secureKeyUsername;

    @Value("${service.security.secure-key-password}")
    private String secureKeyPassword;

    /*
        kaynaklar arası paylaşım için:
        CORS (cross origin resource sharing) politikasi

        CORS konfigurasyonu ile
            izin verilen kaynaklar,
            izin verilen metotlar,
            ve izin verilen yollari belirleyecegiz.

        ornegin: Burasi sayesinde, uygulama yalnizca GET istegi izni verilebilir.
                Belirli bir alan adı varsa, hostlar yazlnizca ona gore kisitlanabilir.

        CORS tarafindan bir istege izin verilmiyorsa, istek engellenir.

        Bu bir Java bean (bkz. @Bean annotion) oldugu icin,
        bundan yeni nesneler olusturulup tum aplikasyon bazinda kullanilabilir.
     */
    @Bean
    public WebMvcConfigurer corsConfigurer()
    {
        return new WebMvcConfigurer()
        {
            @Override
            public void addCorsMappings(CorsRegistry registry)
            {
                registry.addMapping("/**")
                        .allowedOrigins("*") // or: localhost
                        .allowedMethods("*"); // or: POST, GET etc.
            }
        };
    }

    // ******21
    /*
        HTTP filtre zincirine eklendi.
     */
    @Bean
    public JWTAuthenticationFilter createJWTAuthenticationFilter()
    {
        return new JWTAuthenticationFilter();
    }

    // ör: https://www.baeldung.com/security-none-filters-none-access-permitAll
    // ör: https://stackoverflow.com/questions/30819337/multiple-antmatchers-in-spring-security
    // ******20
    // Session kullanılmayacak. JSON Web Token kullanılacak.
    @Override
    protected void configure(HttpSecurity httpSecurity)
    {
        try {
            super.configure(httpSecurity);

            // CSRF -> Cross Site Request Forgery
            httpSecurity.csrf().disable();

        /*
            SessionCreationPolicy.ALWAYS -> Framework, session yoksa mutlaka oluşturur.

            SessionCreationPolicy.NEVER -> Framework, yeni bir session hiçbir zaman oluşturmaz.
            Ne var ki, eğer halihazırda bir session varsa, onu kullanır.

            SessionCreationPolicy.IFREQUIRED -> Framework, gerekliyse session oluşturur. (varsayılan hâl)

            SessionCreationPolicy.STATELESS -> Framework, yeni bir session hiçbir zaman oluşturmaz ve kullanmaz.
         */
            httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        /*
            İstemciler (clients), web sunucusu tarafından desteklenen istekleri
            öğrenmek için OPTIONS isteği (HttpMethod.OPTIONS) yollar.
            Burada, web sunucusu tarafından desteklenen isteklere izin verildi.
         */
            httpSecurity.authorizeRequests()
                    .antMatchers(HttpMethod.OPTIONS).permitAll()
                    .antMatchers("/api/authentication/**").permitAll()
                    .anyRequest().authenticated();
        }
        catch (IllegalArgumentException e)
        {
            Util.showGeneralExceptionInfo(e);
        }
        catch (Exception e)
        {
            Util.showGeneralExceptionInfo(e);
        }
    }

    // ******19
    /*
        kimlik doğrulama,
        authentication ile ilgili
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        try
        {
            auth.userDetailsService(userDetailsService).passwordEncoder(createPasswordEncoder());
        }
        catch (Exception e)
        {
            Util.showGeneralExceptionInfo(e);
        }
    }


    // ******6 -> AbstractAuthenticationService
    @Override
    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    public AuthenticationManager authenticationManagerBean() throws Exception
    {
        try
        {
            return super.authenticationManagerBean();
        }
        catch (Exception e)
        {
            Util.createGeneralExceptionInfo(e);
            return null;
        }
    }

    @Bean
    public PasswordEncoder createPasswordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
}
