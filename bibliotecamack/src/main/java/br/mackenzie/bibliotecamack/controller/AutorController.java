package br.mackenzie.bibliotecamack.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.mackenzie.bibliotecamack.model.Autor;
import br.mackenzie.bibliotecamack.service.AutorService;

@RestController
@RequestMapping("/api/autores")
public class AutorController {

    @Autowired
    private AutorService autorService;

    // Endpoint para cadastrar um novo autor
    @PostMapping
    public ResponseEntity<String> cadastrarAutor(@RequestBody Autor autor) {
        if (autor.getNome() == null || autor.getNome().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("O nome do autor é obrigatório.");
        }
        autorService.create(autor);
        return ResponseEntity.status(HttpStatus.CREATED).body("Autor cadastrado com sucesso!");
    }

    // Endpoint para buscar um autor pelo nome
    @GetMapping("/buscar")
    public ResponseEntity<Autor> buscarPorNome(@RequestParam String nome) {
        Autor autor = autorService.findByNome(nome);
        if (autor == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(autor);
    }

    // Endpoint para ver o total de autores cadastrados
    @GetMapping("/total")
    public ResponseEntity<Long> obterTotalAutores() {
        long total = autorService.getTotalAutores();
        return ResponseEntity.ok(total);
    }
}