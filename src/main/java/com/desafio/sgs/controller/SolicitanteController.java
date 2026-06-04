package com.desafio.sgs.controller;

import com.desafio.sgs.model.Solicitante;
import com.desafio.sgs.service.SolicitanteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/solicitantes")
public class SolicitanteController {
	private final SolicitanteService solicitanteService;
	
	//Injecao de dependencia
	public SolicitanteController(SolicitanteService solicitanteService) {
		this.solicitanteService = solicitanteService;
	}
	
	//GET http://localhost:8080/api/solicitante
	@GetMapping
	public ResponseEntity<List<Solicitante>> listarTodos(){
		List<Solicitante> solicitantes = solicitanteService.listarTodos();
        return ResponseEntity.ok(solicitantes);
	}
}
