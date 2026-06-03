package com.desafio.sgs.service;

import com.desafio.sgs.model.Categoria;
import com.desafio.sgs.repository.CategoriaRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoriaService {
	private CategoriaRepository categoriaRepository;
	
	//Injecao de dependencia
	public CategoriaService(CategoriaRepository categoriaRepository) {
		this.categoriaRepository = categoriaRepository;
	}
	
	public List<Categoria> listarTodas(){
		return categoriaRepository.findAll();
	}
	
	public Categoria buscarPorId(Integer id) {
		return categoriaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Categoria com o ID: " + id + " é inexistente!"));
	}
}
