package com.ar.pckart.config;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.ar.pckart.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomFilter implements Filter{

	private final JwtService jwtService;
	
	@Override
	public void doFilter(ServletRequest request, 
			ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
System.err.println("FILTERR   OIVMVN");
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		final String authHeader = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);
		final String jwt;
		final String userName;
		if(authHeader == null || !authHeader.startsWith("Bearer ")) {
			
			System.err.println("wqeFsffsaffasILTERR   OIVMVN");
			//chain.doFilter(request, response);
			//return;
			
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad Request to Products Service");
			return;
		}
		jwt = authHeader.split(" ")[1].trim();
		try {
			userName = jwtService.extractUsername(jwt);
			
			if(userName == null || 
					!jwtService.isTokenValid(jwt, 
							httpRequest.getHeader("Username"))) {
				HttpServletResponse httpResponse = (HttpServletResponse) response;
				httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad Request to Products Service");
				return;
			}
		}catch(ExpiredJwtException ex) {
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			httpResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
			
			ErrorResponse errorResponse = new ErrorResponse();
			errorResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			errorResponse.setMessage("Token has Expired : product section");
			
			ObjectMapper objectMapper = new ObjectMapper();
			String jsonError = objectMapper.writeValueAsString(errorResponse);
			
			httpResponse.getWriter().write(jsonError);
			return;
		}catch(Exception ex) {
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			httpResponse.getWriter().write("An Error occured");
			return;
		}
		
		chain.doFilter(request, response);
	}

	
}
