package com.desafio.sgs.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SolicitacaoListaDTO {
	private Integer id;
	private String nomeSolicitante;
	private String cpfCnpjSolicitante;
	private String nomeCategoria;
	private String status;
	private BigDecimal valor;
	private LocalDateTime dataSolicitacao;
	
	public SolicitacaoListaDTO(Integer id, String nomeSolicitante, String cpfCnpjSolicitante, String nomeCategoria, String status, BigDecimal valor, LocalDateTime dataSolicitacao) {
		this.id = id;
        this.nomeSolicitante = nomeSolicitante;
        this.cpfCnpjSolicitante = cpfCnpjSolicitante;
        this.nomeCategoria = nomeCategoria;
        this.status = status;
        this.valor = valor;
        this.dataSolicitacao = dataSolicitacao;
	}
	
	public Integer getId() { return id; }
    public String getNomeSolicitante() { return nomeSolicitante; }
    public String getCpfCnpjSolicitante() { return cpfCnpjSolicitante; }
    public String getNomeCategoria() { return nomeCategoria; }
    public String getStatus() { return status; }
    public BigDecimal getValor() { return valor; }
    public LocalDateTime getDataSolicitacao() { return dataSolicitacao; }
}