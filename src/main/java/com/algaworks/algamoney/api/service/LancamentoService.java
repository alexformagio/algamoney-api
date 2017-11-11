package com.algaworks.algamoney.api.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.algaworks.algamoney.api.model.Lancamento;
import com.algaworks.algamoney.api.model.Pessoa;
import com.algaworks.algamoney.api.repository.LancamentoRepository;
import com.algaworks.algamoney.api.repository.PessoaRepository;
import com.algaworks.algamoney.api.service.exception.PessoaInativaOuInexistenteException;

@Service
public class LancamentoService {
	
	@Autowired
	private LancamentoRepository lancamentoRepo;
	@Autowired
	private PessoaRepository pessoaRepo;

	public Lancamento salvar(Lancamento lanc) {
		Pessoa pessoa = pessoaRepo.findOne(lanc.getPessoa().getCodigo());
		if (pessoa == null || pessoa.isInativo()) {
			throw new PessoaInativaOuInexistenteException();
		}
		return lancamentoRepo.save(lanc);
	}

	public Lancamento atualizar(Long codigo, Lancamento lancamento) {
		Lancamento lancExistente = buscarLancamentoExistente(codigo);
		if(!lancamento.getPessoa().equals(lancExistente.getPessoa())) {
			validarPessoa(lancamento);
		}
		
		BeanUtils.copyProperties(lancamento, lancExistente, "codigo");
		return lancamentoRepo.save(lancExistente);
	}
	
	private Lancamento buscarLancamentoExistente(Long codigo) {
		Lancamento lanc = lancamentoRepo.getOne(codigo);
		if (lanc == null) {
			throw new IllegalArgumentException();
		}
		return lanc;
	}
	
	private void validarPessoa(Lancamento lancamento) {
		Pessoa pessoa = null;
		if(lancamento.getPessoa().getCodigo() != null) {
				pessoa = pessoaRepo.getOne(lancamento.getPessoa().getCodigo());	
		}
		
		if(pessoa == null || pessoa.isInativo()) {
			throw new PessoaInativaOuInexistenteException();
		}
	}
	
	

}
