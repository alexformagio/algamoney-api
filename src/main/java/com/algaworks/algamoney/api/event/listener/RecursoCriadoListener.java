package com.algaworks.algamoney.api.event.listener;

import java.net.URI;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.algaworks.algamoney.api.event.RecursoCriadoEvento;

@Component
public class RecursoCriadoListener implements ApplicationListener<RecursoCriadoEvento>{

	@Override
	public void onApplicationEvent(RecursoCriadoEvento recursoCriado) {
		HttpServletResponse response = recursoCriado.getResponse();
		Long codigo = recursoCriado.getCodigo();
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{codigo}")
				  .buildAndExpand(codigo).toUri();
		response.setHeader("Location", uri.toASCIIString());
		
		
	}

}
