package vttp2022.paf.EcommerceStore;

import java.util.Collections;
import java.util.Map;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter{
    
    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {

       
        System.out.println("SOMETHING WAS RAN HERE");
        httpSecurity
            .antMatcher("/**").authorizeRequests()
            .antMatchers("/", "/signIn","/login").permitAll()
            .anyRequest().authenticated()
            .and()
            .oauth2Login().defaultSuccessUrl("/success");
        
    }


    
}
