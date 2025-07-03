package de.oetting.wwp.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.UrlResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

@EnableWebSecurity
@Configuration
@EnableMethodSecurity
public class DefaultSecurityConfig {

    private static final Logger LOG= LoggerFactory.getLogger(DefaultSecurityConfig.class);
    @Bean
    public DataSourceInitializer dataSourceInitializer(DataSource dataSource) {

        ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
        if (!databaseAlreadyInitialized(dataSource)) {
            var usersSql = new UrlResource(JdbcDaoImpl.class.getResource("/" + JdbcDaoImpl.DEFAULT_USER_SCHEMA_DDL_LOCATION));
            resourceDatabasePopulator.addScript(usersSql);
            var groupsSql = DefaultSecurityConfig.class.getResource("/sql/group_init.sql");
            resourceDatabasePopulator.addScript(new UrlResource(groupsSql));
        }

        DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
        dataSourceInitializer.setDataSource(dataSource);
        dataSourceInitializer.setDatabasePopulator(resourceDatabasePopulator);
        return dataSourceInitializer;
    }

    @Bean
    @DependsOn("dataSourceInitializer")
    UserDetailsManager userDetailsManager(DataSource dataSource) {
       return new JdbcUserDetailsManager(dataSource);
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder, UserDetailsService userDetailsService)
            throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthorizationFilter authorizationFilter) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests -> {
                    requests.requestMatchers("/api/auth/**").permitAll();
                    requests.requestMatchers("/api/**").authenticated();
                    requests.anyRequest().permitAll();
                })
                .sessionManagement(customizer -> customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
     return new BCryptPasswordEncoder();
    }

    private boolean databaseAlreadyInitialized(DataSource dataSource) {
        try {
            new JdbcTemplate(dataSource).queryForList("select 1 from USERS limit 1");
            LOG.info("Database is initialized");
            return true;
        } catch (Exception e) {
            LOG.info("Database is not initialized");
            return false;
        }
    }


}
