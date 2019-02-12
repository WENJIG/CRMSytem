package cn.wenjig.crm.common.config;

import cn.wenjig.crm.common.enums.PermissionManage;
import cn.wenjig.crm.service.PermissionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * 配置spring Security , 这里使用了javaConfig实现, 而不使用xml
 * 三种细粒度注解 JSR-250 , prePostEnabled , securedEnabled 。这里开启 JSR-250
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    /**
     * 这里采用 PermissionServiceImpl来自定义认证服务, 自定义验证服务, 全局登录管理服务
     * @return PermissionServiceImpl 实现了 PermissionService, UserDetailsService, AuthenticationProvider
     */
    private PermissionService setLocalUserService() {
        // JdbcUserDetailsManager
        return PermissionManage.RUNTIME.getPermissionService();
    }

    /**
     * 将自定义的认证方式配置进 AuthenticationManagerBuilder
     * 如果想采用 jdbc 而不是 jpa 的方式,可以直接使用UserDetailsService的子类 JdbcUserDetailsManager
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService((UserDetailsService) setLocalUserService());
        auth.authenticationProvider((AuthenticationProvider) setLocalUserService());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .authorizeRequests()
                    .antMatchers("/login/", "/css/**", "/dist/**", "/images/**", "/js/**", "/favicon.ico").permitAll()
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .usernameParameter("account")
                    .passwordParameter("password")
                    .loginPage("/login/")
                    .successForwardUrl("/index/")
                    .permitAll()
                    .and()
                .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/login/out", "GET"))
                    .logoutSuccessUrl("/login/")
                    .invalidateHttpSession(true)
                    .permitAll()
                    .and()
                .exceptionHandling()
                    .accessDeniedPage("/exception/403")
                    .and()
                .sessionManagement()
                    .invalidSessionUrl("/login/?repeatLog")
                    .maximumSessions(1)
                    .maxSessionsPreventsLogin(false)
                    .sessionRegistry(sessionRegistry())
                    .and()
                .and()
                .httpBasic();

    }
}
