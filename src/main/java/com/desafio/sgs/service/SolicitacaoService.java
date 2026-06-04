package com.desafio.sgs.service;

import com.desafio.sgs.dto.SolicitacaoDetalheDTO;
import com.desafio.sgs.dto.SolicitacaoListaDTO;
import com.desafio.sgs.model.Solicitacao;
import com.desafio.sgs.model.StatusSolicitacao;
import com.desafio.sgs.model.Categoria;
import com.desafio.sgs.model.Solicitante;
import com.desafio.sgs.repository.SolicitacaoRepository;
import com.desafio.sgs.repository.CategoriaRepository;
import com.desafio.sgs.repository.SolicitanteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;

@Service
public class SolicitacaoService {

    private final SolicitacaoRepository solicitacaoRepository;
    private final CategoriaRepository categoriaRepository;
    private final SolicitanteRepository solicitanteRepository;

    //Injeção de dependências
    public SolicitacaoService(SolicitacaoRepository solicitacaoRepository,
                              CategoriaRepository categoriaRepository,
                              SolicitanteRepository solicitanteRepository) {
        this.solicitacaoRepository = solicitacaoRepository;
        this.categoriaRepository = categoriaRepository;
        this.solicitanteRepository = solicitanteRepository;
    }

    public List<SolicitacaoListaDTO> listarSolicitacoesComFiltros(String status, Integer categoriaId,
                                                                   LocalDate dataInicio, LocalDate dataFim) {
        if (status != null && status.trim().isEmpty()) {
            status = null;
        }

        if (dataInicio != null && dataFim != null && dataFim.isBefore(dataInicio)) {
            throw new IllegalArgumentException("A data final não pode ser anterior à data inicial.");
        }

        return solicitacaoRepository.listarComFiltros(status, categoriaId, dataInicio, dataFim);
    }

    public SolicitacaoDetalheDTO buscarDetalhePorId(Integer id) {
        Solicitacao s = solicitacaoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Solicitação não encontrada com o ID: " + id));

        return new SolicitacaoDetalheDTO(
                s.getId(),
                s.getSolicitante().getNome(),
                s.getSolicitante().getCpfCnpj(),
                s.getCategoria().getNome(),
                s.getDescricao(),
                s.getValor(),
                s.getDataSolicitacao(),
                s.getStatus().name()
        );
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

        Solicitacao solicitacao = solicitacaoRepository.findById(id)
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
        } else if (statusAtual == StatusSolicitacao.LIBERADO) {
            if (novoStatus == StatusSolicitacao.APROVADO || novoStatus == StatusSolicitacao.REJEITADO) {
                transicaoValida = true;
            }
        } else if (statusAtual == StatusSolicitacao.APROVADO) {
            if (novoStatus == StatusSolicitacao.CANCELADO) {
                transicaoValida = true;
            }
        }

        if (!transicaoValida) {
            throw new IllegalStateException("Transição inválida: não é permitido mudar de " + statusAtual + " para " + novoStatus + ".");
        }

        solicitacao.setStatus(novoStatus);
        solicitacaoRepository.save(solicitacao);
    }

    @Transactional
    public void salvarSolicitacao(String descricao, Integer solicitanteId, Integer categoriaId, BigDecimal valor) {
        if (descricao == null || descricao.trim().isEmpty()) {
            throw new IllegalArgumentException("A descrição da solicitação não pode estar vazia.");
        }
        if (solicitanteId == null || categoriaId == null) {
            throw new IllegalArgumentException("É necessário selecionar um solicitante e uma categoria válidos.");
        }
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor da solicitação deve ser maior que zero.");
        }

        Solicitante solicitante = solicitanteRepository.findById(solicitanteId)
                .orElseThrow(() -> new IllegalArgumentException("Solicitante não encontrado com o ID: " + solicitanteId));

        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada com o ID: " + categoriaId));

        Solicitacao novaSolicitacao = new Solicitacao();
        novaSolicitacao.setDescricao(descricao.trim());
        novaSolicitacao.setSolicitante(solicitante);
        novaSolicitacao.setCategoria(categoria);
        novaSolicitacao.setValor(valor);
        novaSolicitacao.setDataSolicitacao(LocalDateTime.now());
        novaSolicitacao.setStatus(StatusSolicitacao.SOLICITADO);

        solicitacaoRepository.save(novaSolicitacao);
    }
}
