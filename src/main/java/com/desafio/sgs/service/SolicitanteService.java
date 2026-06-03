package com.desafio.sgs.service;

import com.desafio.sgs.model.Solicitante;
import com.desafio.sgs.repository.SolicitanteRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SolicitanteService {
	private SolicitanteRepository solicitanteRepository;
	
	//Injecao de dependencia
	public SolicitanteService(SolicitanteRepository solicitanteRepository) {
		this.solicitanteRepository = solicitanteRepository;
	}
	
	public List<Solicitante> listarTodos(){
		return solicitanteRepository.findAll();
	}
	
	public Solicitante buscaPorId(Integer id) {
		return solicitanteRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Solicitante com o ID: " + id + " não encontrado!"));
	}
}
