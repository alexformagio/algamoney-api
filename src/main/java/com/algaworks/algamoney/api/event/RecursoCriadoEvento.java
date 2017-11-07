package com.algaworks.algamoney.api.event;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationEvent;

public class RecursoCriadoEvento extends ApplicationEvent{
	
	private HttpServletResponse response;
	private Long codigo;

	public RecursoCriadoEvento(Object source, HttpServletResponse response, Long codigo) {
		super(source);
		this.response = response;
		this.codigo = codigo;
	}
	
	

	public HttpServletResponse getResponse() {
		return response;
	}



	public Long getCodigo() {
		return codigo;
	}



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
