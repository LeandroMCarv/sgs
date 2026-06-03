package com.desafio.sgs.service;

import com.desafio.sgs.dto.SolicitacaoListaDTO;
import com.desafio.sgs.repository.SolicitacaoRepository;
import com.desafio.sgs.model.StatusSolicitacao;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class SolicitacaoService {
	private final SolicitacaoRepository solicitacaoRepository;
	
	//Injecao da dependencia
	public SolicitacaoService(SolicitacaoRepository solicitacaoRepository) {
		this.solicitacaoRepository = solicitacaoRepository;
	}
	
	public List<SolicitacaoListaDTO> listarSolicitacoesComFiltros(String status, Integer categoriaId, LocalDate dataInicio, LocalDate dataFim) {
		if (status != null && status.trim().isEmpty()) {
			status = null;
		}
		
		if (dataInicio != null && dataFim != null) {
			if (dataFim.isBefore(dataInicio)) {
				throw new IllegalArgumentException("A data final não pode ser anterior à data inicial.");
			}
		}
		
        return solicitacaoRepository.listarComFiltros(status, categoriaId, dataInicio, dataFim);
    }
	
	public void atualizarStatus(Integer id, String novoStatusStr) {
        if (novoStatusStr == null || novoStatusStr.trim().isEmpty()) {
            throw new IllegalArgumentException("O novo status não pode ser nulo ou vazio.");
        }

        StatusSolicitacao novoStatus;
        
        //Conversão de status recebido em enum
        try {
            novoStatus = StatusSolicitacao.valueOf(novoStatusStr.toUpperCase().trim());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Status inválido. Escolha um status válido pelo sistema.");
        }

        com.desafio.sgs.model.Solicitacao solicitacao = solicitacaoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Solicitação não encontrada com o ID: " + id));

        StatusSolicitacao statusAtual = solicitacao.getStatus();

        //Transicoes nao permitidas
        if (statusAtual == StatusSolicitacao.REJEITADO || statusAtual == StatusSolicitacao.CANCELADO) {
            throw new IllegalStateException("Não é possível alterar o status de uma solicitação que já está como " + statusAtual + ".");
        }

        //Transicoes permitidas
        boolean transicaoValida = false;

        if (statusAtual == StatusSolicitacao.SOLICITADO) {
            if (novoStatus == StatusSolicitacao.LIBERADO || novoStatus == StatusSolicitacao.REJEITADO) {
                transicaoValida = true;
            }
        }

        else if (statusAtual == StatusSolicitacao.LIBERADO) {
            if (novoStatus == StatusSolicitacao.APROVADO || novoStatus == StatusSolicitacao.REJEITADO) {
                transicaoValida = true;
            }
        }

        else if (statusAtual == StatusSolicitacao.APROVADO) {
            if (novoStatus == StatusSolicitacao.CANCELADO) {
                transicaoValida = true;
            }
        }

        //Validacao final
        if (!transicaoValida) {
            throw new IllegalStateException("Transição de status inválida! Não é permitido mudar de " + statusAtual + " para " + novoStatus + ".");
        }

        solicitacao.setStatus(novoStatus);
        solicitacaoRepository.save(solicitacao);
    }
}