package io.pivotal.alex.pal.paltracker.tracker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private boolean httpsDisabled;

    public SecurityConfiguration(@Value("${HTTPS_DISABLED}") boolean httpsDisabled) {
        this.httpsDisabled = httpsDisabled;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        if (httpsDisabled) {
            http.csrf().disable().authorizeRequests().antMatchers("/**").hasRole("USER")
                    .and().httpBasic().and().requiresChannel().anyRequest();
        } else {
            http.authorizeRequests().antMatchers("/").hasRole("USER")
                    .and().httpBasic().and().requiresChannel().anyRequest().requiresSecure();
        }
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("user").password("password").roles("USER");
    }
}
