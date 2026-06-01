package com.desafio.sgs.model;

import jakarta.persistence.*;

@Entity
@Table(name="solicitante")
public class Solicitante {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(nullable = false, length = 100)
	private String nome;
	
	@Column(name = "cpf_cnpj", nullable = false, unique = true, length = 20)
	private String cpfCnpj;
	
	public Solicitante() {}
	
	public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCpfCnpj() { return cpfCnpj; }
    public void setCpfCnpj(String cpfCnpj) { this.cpfCnpj = cpfCnpj; }
}