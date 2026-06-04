package com.desafio.sgs.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SolicitacaoDetalheDTO {

    private Integer id;
    private String nomeSolicitante;
    private String cpfCnpjSolicitante;
    private String nomeCategoria;
    private String descricao;
    private BigDecimal valor;
    private LocalDateTime dataSolicitacao;
    private String status;

    public SolicitacaoDetalheDTO(Integer id, String nomeSolicitante, String cpfCnpjSolicitante,
                                  String nomeCategoria, String descricao, BigDecimal valor,
                                  LocalDateTime dataSolicitacao, String status) {
        this.id = id;
        this.nomeSolicitante = nomeSolicitante;
        this.cpfCnpjSolicitante = cpfCnpjSolicitante;
        this.nomeCategoria = nomeCategoria;
        this.descricao = descricao;
        this.valor = valor;
        this.dataSolicitacao = dataSolicitacao;
        this.status = status;
    }

    public Integer getId() { return id; }
    public String getNomeSolicitante() { return nomeSolicitante; }
    public String getCpfCnpjSolicitante() { return cpfCnpjSolicitante; }
    public String getNomeCategoria() { return nomeCategoria; }
    public String getDescricao() { return descricao; }
    public BigDecimal getValor() { return valor; }
    public LocalDateTime getDataSolicitacao() { return dataSolicitacao; }
    public String getStatus() { return status; }
}
