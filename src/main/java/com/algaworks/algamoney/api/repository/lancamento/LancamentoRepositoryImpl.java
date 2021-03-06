package com.algaworks.algamoney.api.repository.lancamento;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import com.algaworks.algamoney.api.model.Lancamento;
import com.algaworks.algamoney.api.repository.filter.LancamentoFilter;
import com.algaworks.algamoney.api.repository.projection.ResumoLancamento;

public class LancamentoRepositoryImpl implements LancamentoRepositoryQuery{
	
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Lancamento> criteria = builder.createQuery(Lancamento.class);
		Root<Lancamento> root = criteria.from(Lancamento.class);
		
		//criar as restrições
		Predicate[] predicate = criarRestricoes(lancamentoFilter, builder, root);
		criteria.where(predicate);
		
		TypedQuery<Lancamento> query = entityManager.createQuery(criteria);
		adicionandoRestricoes(query, pageable);
		return new PageImpl<>(query.getResultList(), pageable, total(lancamentoFilter));
	}
	
	@Override
	public Page<ResumoLancamento> resumir(LancamentoFilter lancamentoFilter, Pageable pageable) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ResumoLancamento> criteria = builder.createQuery(ResumoLancamento.class);
		Root<Lancamento> root = criteria.from(Lancamento.class);
		
		criteria.select(builder.construct(ResumoLancamento.class, 
				root.get("codigo"), root.get("descricao"),
				root.get("dataPagamento"), root.get("dataVencimento"),
				root.get("valor"), root.get("tipo"), 
				root.get("categoria").get("nome"), 
				root.get("pessoa").get("nome")));
		
		//criar as restrições
		Predicate[] predicate = criarRestricoes(lancamentoFilter, builder, root);
		criteria.where(predicate);
		
		TypedQuery<ResumoLancamento> query = entityManager.createQuery(criteria);
		adicionandoRestricoes(query, pageable);
		return new PageImpl<>(query.getResultList(), pageable, total(lancamentoFilter));		
	}
	

	private Long total(LancamentoFilter lancamentoFilter) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<Lancamento> root = criteria.from(Lancamento.class);
		
		Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);
		criteria.where(predicates);
		criteria.select(builder.count(root));
		
		return entityManager.createQuery(criteria).getSingleResult();
	}

	private void adicionandoRestricoes(TypedQuery<?> query, Pageable pageable) {
		int currentPage = pageable.getPageNumber();
		int recordsPerPage = pageable.getPageSize();
		int primeiroRegistroDaPagina = currentPage * recordsPerPage;
		
		query.setFirstResult(primeiroRegistroDaPagina);
		query.setMaxResults(recordsPerPage);
		
	}

	private Predicate[] criarRestricoes(LancamentoFilter lancamentoFilter, CriteriaBuilder builder,
			Root<Lancamento> root) {
		List<Predicate> predicates = new ArrayList<>();
		if(!StringUtils.isEmpty(lancamentoFilter.getDescricao())) {
			predicates.add(builder.like(
					builder.lower(root.get("descricao")),
					"%" + lancamentoFilter.getDescricao().toLowerCase()+"%")
			);
		}
		
		if(lancamentoFilter.getDataVencimentoDe() != null) {
			predicates.add(builder.greaterThanOrEqualTo(root.get("dataVencimento"),
					lancamentoFilter.getDataVencimentoDe()));
		}
		
		if(lancamentoFilter.getDataVencimentoAte() != null) {
			predicates.add(builder.lessThanOrEqualTo(root.get("dataVencimento"),
					lancamentoFilter.getDataVencimentoAte()));
		}		
		
		return predicates.toArray(new Predicate[predicates.size()]);
	}


}
