package com.algaworks.algamoney.api.token;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.catalina.util.ParameterMap;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RefreshTokenCookiePreProcessor implements Filter{


	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
	   HttpServletRequest req = (HttpServletRequest) request;
	   
	   if("/oauth/token".equalsIgnoreCase(req.getRequestURI())
			   && "refresh_token".equalsIgnoreCase(req.getParameter("grant_type"))
			   && req.getCookies() != null) {
		   for(Cookie cookie: req.getCookies()) {
			   if(cookie.getName().equals("refresh_token")) {
				   String refreshToken = cookie.getValue();
				   req = new MyServletRequestWrapper(req, refreshToken);
			   }
		   }
	   }
		
	   chain.doFilter(req, response);
	}
	

	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}
	
	static class MyServletRequestWrapper extends HttpServletRequestWrapper{
		private String refreshToken;
		
		public MyServletRequestWrapper(HttpServletRequest req, String refToken) {
			super(req);
			this.refreshToken = refToken;
		}
		
	    public Map<String, String[]> getParameterMap(){
	    	ParameterMap<String, String[]> map = new ParameterMap<>(getRequest().getParameterMap());
	    	map.put("refresh_token", new String[] {refreshToken} );
	    	map.setLocked(true);
	    	return map;
	    }
	}	

}
