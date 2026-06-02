package com.desafio.sgs.repository;

import com.desafio.sgs.dto.SolicitacaoListaDTO;
import java.time.LocalDate;
import java.util.List;

public interface SolicitacaoRepositoryCustom {
	List<SolicitacaoListaDTO> listarComFiltros(String status, Integer categoriaId, LocalDate dataInicio, LocalDate dataFim);
}