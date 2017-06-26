package su.hotty.editor.config;

import su.hotty.editor.security.CustomAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/hotty/privacypolicy").permitAll()
                .antMatchers("/hotty/api/v1/sendmailblocks/**").permitAll()
                .antMatchers("/hotty/api/v1/captcha/**").permitAll()
                .antMatchers("/hotty/api/v1/blocks/**").permitAll()
                .antMatchers("/hotty/api/v1/sites/**").permitAll()
                .antMatchers("/hotty/api/v1/pages/**").permitAll()
                .antMatchers("/hotty/api/v1/files/**").permitAll()
                .antMatchers("/hotty/api/v1/users/**").permitAll()
                .antMatchers("/hotty/blocks").permitAll()
                .antMatchers("/blocks").permitAll()
                .antMatchers("/hotty/").permitAll()

                .antMatchers("/hotty/content/img/**").permitAll()
                .antMatchers("/hotty/content/video/**").permitAll()
                .antMatchers("/hotty/content/files/**").permitAll()

                //.antMatchers("/hotty/app/**").permitAll()
                .antMatchers("/hotty/app/bower_components/**").permitAll()
                .antMatchers("/hotty/app/favicon/**").permitAll()
                .antMatchers("/hotty/app/png/**").permitAll()
                .antMatchers("/hotty/app/svg/**").permitAll()
                .antMatchers("/hotty/app/app.css").permitAll()

                .antMatchers("/hotty/app/login.js").permitAll()

                .antMatchers("/hotty/getimg/**").permitAll()
                .antMatchers("/hotty/api/v1/content/**").permitAll()

                .anyRequest().authenticated()
                .and().csrf().disable()

                .formLogin()
                .loginPage("/hotty/app/login")
                .loginProcessingUrl("/hotty/authenticate")
                .defaultSuccessUrl("/hotty/app/index.html", true)
                .permitAll()
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/hotty/app/login");

//                .loginPage("/hotty/app/login")
//                .loginProcessingUrl("/hotty/authenticate")
//                .defaultSuccessUrl("/hotty/app/index.html", true)
//                .loginProcessingUrl("/hotty/authenticate")
//                .defaultSuccessUrl("/hotty/app/index.html", true)
//                .usernameParameter("username")
//                .passwordParameter("password")
//                .permitAll()
//                .and()
//                .logout()
//                .logoutRequestMatcher(new AntPathRequestMatcher("/hotty/logout"))
//                .logoutSuccessUrl("/hotty/app/login");
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customAuthenticationProvider);
    }
}