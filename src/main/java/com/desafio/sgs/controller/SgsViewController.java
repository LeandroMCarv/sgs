package com.desafio.sgs.controller;

import com.desafio.sgs.dto.SolicitacaoDetalheDTO;
import com.desafio.sgs.model.Categoria;
import com.desafio.sgs.model.Solicitante;
import com.desafio.sgs.service.CategoriaService;
import com.desafio.sgs.service.SolicitacaoService;
import com.desafio.sgs.service.SolicitanteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class SgsViewController {

    private final SolicitacaoService solicitacaoService;
    private final SolicitanteService solicitanteService;
    private final CategoriaService categoriaService;

    public SgsViewController(SolicitacaoService solicitacaoService,
                              SolicitanteService solicitanteService,
                              CategoriaService categoriaService) {
        this.solicitacaoService = solicitacaoService;
        this.solicitanteService = solicitanteService;
        this.categoriaService = categoriaService;
    }

    // Tela principal — listagem com filtros
    @GetMapping("/")
    public String index(Model model) {
        List<Categoria> categorias = categoriaService.listarTodas();
        model.addAttribute("categorias", categorias);
        return "index";
    }

    // Tela de cadastro de nova solicitação
    @GetMapping("/cadastro")
    public String cadastro(Model model) {
        List<Solicitante> solicitantes = solicitanteService.listarTodos();
        List<Categoria> categorias = categoriaService.listarTodas();
        model.addAttribute("solicitantes", solicitantes);
        model.addAttribute("categorias", categorias);
        return "cadastro";
    }

    // Tela de detalhamento de uma solicitação
    @GetMapping("/solicitacoes/{id}")
    public String detalhe(@PathVariable Integer id, Model model) {
        try {
            SolicitacaoDetalheDTO detalhe = solicitacaoService.buscarDetalhePorId(id);
            model.addAttribute("solicitacao", detalhe);
            return "detalhe";
        } catch (IllegalArgumentException e) {
            return "redirect:/";
        }
    }
}
