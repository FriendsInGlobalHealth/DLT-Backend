package dlt.dltbackendmaster.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 
 * @author derciobucuane
 *
 */
@EnableWebSecurity
@Configuration
public class SecurityConfigurations extends WebSecurityConfigurerAdapter{

	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;
	
	@Autowired
	private EntryPointUnauthorizedHandler entryPointUnauthorizedHandler;
	
	@Autowired
	private final TokenAuthenticationService tokenAuthenticationService;
	
	public SecurityConfigurations() {
		this.tokenAuthenticationService = new TokenAuthenticationService(userDetailsServiceImpl);
	}

	@Override
	@Autowired
	protected void configure(AuthenticationManagerBuilder auth) throws Exception{
		auth.userDetailsService(userDetailsServiceImpl);
	}
	
	@Override
	protected void configure(HttpSecurity http)throws Exception{
		http.csrf().disable()
			.exceptionHandling().authenticationEntryPoint(entryPointUnauthorizedHandler).and()
			.anonymous().and()
			.servletApi().and()
			.authorizeRequests()
			
			.antMatchers("/").permitAll()
            .antMatchers("/resources/**").permitAll()
            	//allow anonymous POSTs to login
            .antMatchers(HttpMethod.POST, "/api/login").permitAll()
            .antMatchers(HttpMethod.OPTIONS, "/api/login").permitAll()
            .antMatchers(HttpMethod.OPTIONS, "/api/**").permitAll()
            	//allow anonymous GETs to API
            .antMatchers(HttpMethod.GET, "/api/**").authenticated()
            .antMatchers(HttpMethod.POST, "/api/**").permitAll()
            .antMatchers(HttpMethod.PUT, "/api/**").authenticated()
            .antMatchers(HttpMethod.OPTIONS).permitAll()
            .regexMatchers(HttpMethod.OPTIONS, "").permitAll()
            .anyRequest().permitAll().and()
            	//all other request need to be authenticated
            	// custom JSON based authentication by POST of {"username":"<name>","password":"<password>"} which sets the token header upon authentication
            .addFilterBefore(new StatelessLoginFilter("/api/login", tokenAuthenticationService, userDetailsServiceImpl, authenticationManager()), 
            		UsernamePasswordAuthenticationFilter.class)
            	// custom Token based authentication based on the header previously given to the client
            .addFilterBefore(new StatelessAuthenticationFilter(tokenAuthenticationService), UsernamePasswordAuthenticationFilter.class)
            .headers().cacheControl().and();
            
	}
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception{
		return super.authenticationManagerBean();
	}
	
	@Override
	protected UserDetailsService userDetailsService() {
		return userDetailsServiceImpl;
	}
	
	@Bean
	public TokenAuthenticationService tokenAuthenticationService() {
		return tokenAuthenticationService;
	}
}