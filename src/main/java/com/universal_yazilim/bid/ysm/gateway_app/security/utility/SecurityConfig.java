package com.universal_yazilim.bid.ysm.gateway_app.security.utility;

import com.universal_yazilim.bid.ysm.gateway_app.utility.Util;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    @Value("${service.security.secure-key-username}")
    private String secureKeyUsername;

    @Value("${service.security.secure-key-password}")
    private String secureKeyPassword;


    @Bean
    public PasswordEncoder createPasswordEncoder()
    {
        return new BCryptPasswordEncoder();
    }


    /*
        Uygulamanın kullanıcı ad ve parola bilgilerinin
        belirlenen bilgiler olması için
        bu metot "overrid" edilir.
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        PasswordEncoder encoder = new BCryptPasswordEncoder();

        // https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/in-memory.html
        // https://www.baeldung.com/spring-security-5-default-password-encoder
        auth.inMemoryAuthentication()
                .passwordEncoder(encoder)
                .withUser(secureKeyUsername)
                .password(encoder.encode(secureKeyPassword))
                .roles("USER");
    }

    // Session kullanılmayacak. JSON Web Token kullanılacak.
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception
    {
        super.configure(httpSecurity);

        // CSRF -> Cross Site Request Forgery
        httpSecurity.csrf().disable();
    }
}
