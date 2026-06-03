package com.desafio.sgs.repository;

import com.desafio.sgs.model.Solicitacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Integer> ,SolicitacaoRepositoryCustom{

}