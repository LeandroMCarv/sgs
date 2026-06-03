package com.desafio.sgs.controller;

import com.desafio.sgs.dto.SolicitacaoListaDTO;
import com.desafio.sgs.service.SolicitacaoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/solicitacoes")
public class SolicitacaoController {
	private final SolicitacaoService solicitacaoService;
	
	//Injeçao de dependencia
	public SolicitacaoController(SolicitacaoService solicitacaoService) {
        this.solicitacaoService = solicitacaoService;
    }
	
	//GET http://localhost:8080/api/solicitacoes
	@GetMapping
	public ResponseEntity<List<SolicitacaoListaDTO>> listarComFiltros(
			@RequestParam(required = false) String status,
			@RequestParam(required = false) Integer categoriaId,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim){
		List<SolicitacaoListaDTO> resultado = solicitacaoService.listarSolicitacoesComFiltros(status, categoriaId, dataInicio, dataFim);
        return ResponseEntity.ok(resultado);
	}
	
	//PATCH http://localhost:8080/api/solicitacoes/{id}/status?novoStatus=LIBERADO
	@PatchMapping("/{id}/status")
	public ResponseEntity<String> atualizarStatus(
			@PathVariable Integer id,
			@RequestParam String novoStatus){
		try {
			solicitacaoService.atualizarStatus(id, novoStatus);
			return ResponseEntity.ok("Status atualizado com sucesso para " + novoStatus + ".");
		}catch(IllegalArgumentException | IllegalStateException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
}
