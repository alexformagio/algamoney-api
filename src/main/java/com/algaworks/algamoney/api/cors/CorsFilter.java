package com.algaworks.algamoney.api.cors;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.algaworks.algamoney.api.config.property.AlgamoneyApiProperty;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter implements Filter{
	@Autowired AlgamoneyApiProperty algamoneyApiProperty;

	private String allowedOrigin = algamoneyApiProperty.getOrigemPermitida();
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		
	   	HttpServletResponse response = (HttpServletResponse) resp;
	   	HttpServletRequest request = (HttpServletRequest) req;
	   	
	   	response.setHeader("Access-Control-Allow-Origin", allowedOrigin);
	   	response.setHeader("Access-Control-Allow-Credentials", "true");
	   	
	   	if("OPTIONS".equals(request.getMethod()) && 
	   	    allowedOrigin.equals(request.getHeader("Origin"))) {
	   		
		   	response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, OPTIONS");
		   	response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, Accept");
		   	response.setHeader("Access-Control-Max-Age", "3600");
		   	
		   	response.setStatus(HttpServletResponse.SC_OK);
		   	
		   	
		   	
	   	}else {
	   		chain.doFilter(request, response);
	   	}
		
	}
	
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
