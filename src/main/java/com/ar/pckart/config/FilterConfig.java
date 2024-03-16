package com.ar.pckart.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.ar.pckart.service.JwtService;

@Configuration
public class FilterConfig {
	
	@Autowired JwtService jwtService;
	
	@Value("${filter.url.patterns}")
	private String[] URL_PATTERNS;
	
	@Value("${cors.set.allowed.origins}")
	private String[] CROSS_ORIGIN_URLS;
	
	@Bean
	public FilterRegistrationBean<CustomFilter> customFilterRegistrationBean(){
		FilterRegistrationBean<CustomFilter> registrationBean =
				new FilterRegistrationBean<>();
		registrationBean.setFilter(new CustomFilter(jwtService));
		registrationBean.addUrlPatterns(URL_PATTERNS);  // "/*"
		return registrationBean;
	}
	
	@Bean
	public CorsFilter corsFilter() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowCredentials(true);
		corsConfiguration.setAllowedOrigins(Arrays.asList(CROSS_ORIGIN_URLS));
		corsConfiguration.setAllowedHeaders(Arrays.asList(
					"Origin","Access-Control-Allow-Origin", "Content-Type",
					"Accept","Authorization","Origin, Accept","X-Requested-With",
					"Access-Control-Request-Method","Access-Control-Request-Headers"
				));
		corsConfiguration.setExposedHeaders(Arrays.asList(
					"Origin","Content-Type","Accept","Authorization",
					"Access-Control-Allow-Origin","Access-Control-Allow-Origin",
					"Access-Control-Allow-Credentials"
				));
		corsConfiguration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","OPTIONS"));
		UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
		urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
		return new CorsFilter(urlBasedCorsConfigurationSource);
	}
	
}
