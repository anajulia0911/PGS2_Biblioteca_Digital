package br.mackenzie.bibliotecamack.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.mackenzie.bibliotecamack.model.Livro;
import br.mackenzie.bibliotecamack.service.LivroService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/livros")
public class LivroController {

    @Autowired
    private LivroService livroService;

    // Lista todos os livros do banco
    @GetMapping
    public ResponseEntity<Iterable<Livro>> listarTodos() {
        return ResponseEntity.ok(livroService.buscarTodos());
    }

    // Busca preview de um livro na API sem salvar
    @GetMapping("/preview/{isbn}")
    public ResponseEntity<?> previewIsbn(@PathVariable String isbn) {
        try {
            Livro livro = livroService.buscarPreviewPorIsbn(isbn);
            return ResponseEntity.ok(livro);
        } catch (RuntimeException e) {
            if (e.getMessage().equals("DUPLICADO")) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Este livro já está cadastrado no acervo.");
            }
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(e.getMessage());
        }
    }

    // ENDPOINT DE CONSULTA AGREGADA
    // Une dados locais do banco com dados em tempo real da Open Library
    @GetMapping("/agregado")
    public ResponseEntity<?> consultaAgregada() {
        try {
            List<Map<String, Object>> resultado = livroService.consultaAgregada();
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro na consulta agregada: " + e.getMessage());
        }
    }

    // Salva via ISBN
    @PostMapping("/isbn/{isbn}")
    public ResponseEntity<?> salvarPorIsbn(@PathVariable String isbn) {
        try {
            Livro livro = livroService.salvarComOpenLibrary(isbn);
            return ResponseEntity.status(HttpStatus.CREATED).body(livro);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(e.getMessage());
        }
    }

    // Deleta por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletar(@PathVariable Long id) {
        try {
            livroService.deletar(id);
            return ResponseEntity.ok("Livro excluído com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao excluir: " + e.getMessage());
        }
    }
}