package com.desafio.sgs.controller;

import com.desafio.sgs.dto.SolicitacaoDetalheDTO;
import com.desafio.sgs.dto.SolicitacaoListaDTO;
import com.desafio.sgs.service.SolicitacaoService;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/solicitacoes")
public class SolicitacaoController {

    private final SolicitacaoService solicitacaoService;

    //Injeção de dependência
    public SolicitacaoController(SolicitacaoService solicitacaoService) {
        this.solicitacaoService = solicitacaoService;
    }

    //GET http://localhost:8080/api/solicitacoes
    @GetMapping
    public ResponseEntity<List<SolicitacaoListaDTO>> listarComFiltros(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer categoriaId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {

        try {
            List<SolicitacaoListaDTO> resultado = solicitacaoService.listarSolicitacoesComFiltros(status, categoriaId, dataInicio, dataFim);
            return ResponseEntity.ok(resultado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    //GET http://localhost:8080/api/solicitacoes/{id}
    @GetMapping("/{id}")
    public ResponseEntity<SolicitacaoDetalheDTO> buscarPorId(@PathVariable Integer id) {
        try {
            SolicitacaoDetalheDTO detalhe = solicitacaoService.buscarDetalhePorId(id);
            return ResponseEntity.ok(detalhe);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    //POST http://localhost:8080/api/solicitacoes
    @PostMapping
    public ResponseEntity<String> cadastrar(
            @RequestParam @NotBlank String descricao,
            @RequestParam @NotNull Integer solicitanteId,
            @RequestParam @NotNull Integer categoriaId,
            @RequestParam @NotNull @DecimalMin("0.01") BigDecimal valor) {

        try {
            solicitacaoService.salvarSolicitacao(descricao, solicitanteId, categoriaId, valor);
            return ResponseEntity.status(HttpStatus.CREATED).body("Solicitação cadastrada com sucesso.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //PATCH http://localhost:8080/api/solicitacoes/{id}/status?novoStatus=LIBERADO
    @PatchMapping("/{id}/status")
    public ResponseEntity<String> atualizarStatus(
            @PathVariable Integer id,
            @RequestParam String novoStatus) {

        try {
            solicitacaoService.atualizarStatus(id, novoStatus);
            return ResponseEntity.ok("Status atualizado com sucesso para " + novoStatus + ".");
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
