package com.algaworks.algamoney.api.security.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GeradorSenha {

	public static void main(String[] args) {
		BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
		String passEncoded = bCrypt.encode("alex100994");
		System.out.println(passEncoded);

	}

}
