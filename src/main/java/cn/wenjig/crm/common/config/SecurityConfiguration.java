package cn.wenjig.crm.common.config;

import cn.wenjig.crm.common.local.PermissionManage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 配置spring Security , 这里使用了javaConfig实现, 而不使用xml
 * 三种细粒度注解 JSR-250 , prePostEnabled , securedEnabled 。这里开启 JSR-250
 */
@Configuration
@EnableGlobalMethodSecurity(jsr250Enabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 这里采用实现了UserDetailsService接口的 PermissionServiceImpl来自定义认证服务
     * @return PermissionServiceImpl 实现了 PermissionService, UserDetailsService
     */
    private UserDetailsService setLocalUserService() {
        // JdbcUserDetailsManager
        return (UserDetailsService) PermissionManage.RUNTIME.getPermissionService();
    }

    /**
     * 将自定义的认证方式配置进 AuthenticationManagerBuilder
     * 如果想采用 jdbc 而不是 jpa 的方式,可以直接使用UserDetailsService的子类 JdbcUserDetailsManager
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(setLocalUserService());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .authorizeRequests()
                    .antMatchers("/login/", "/css/**", "/dist/**", "/images/**", "/js/**").permitAll()
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage("/login/")
                    .permitAll()
                    .and()
                .logout()
                    .logoutUrl("/login/out")
                    .logoutSuccessUrl("/login/")
                    .permitAll()
                    .and()
                .httpBasic();

    }
}
