package br.mackenzie.bibliotecamack.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.mackenzie.bibliotecamack.model.Categoria;
import br.mackenzie.bibliotecamack.service.CategoriaService;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    // Endpoint para cadastrar uma nova categoria
    @PostMapping
    public ResponseEntity<String> cadastrarCategoria(@RequestBody Categoria categoria) {
        if (categoria.getNome() == null || categoria.getNome().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("O nome da categoria é obrigatório.");
        }
        categoriaService.create(categoria);
        return ResponseEntity.status(HttpStatus.CREATED).body("Categoria cadastrada com sucesso!");
    }

    // Endpoint para buscar uma categoria pelo nome exato
    @GetMapping("/buscar")
    public ResponseEntity<Categoria> buscarPorNome(@RequestParam String nome) {
        Categoria categoria = categoriaService.findByNome(nome);
        if (categoria == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(categoria);
    }

    // Endpoint para ver o total de categorias cadastradas
    @GetMapping("/total")
    public ResponseEntity<Long> obterTotalCategorias() {
        long total = categoriaService.getTotalCategorias();
        return ResponseEntity.ok(total);
    }
}