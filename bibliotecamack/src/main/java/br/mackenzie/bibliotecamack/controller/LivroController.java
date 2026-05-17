package br.mackenzie.bibliotecamack.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import br.mackenzie.bibliotecamack.model.Livro;
import br.mackenzie.bibliotecamack.service.LivroService;

@RestController
@RequestMapping("/api/livros")
public class LivroController {

    @Autowired
    private LivroService service;

    @PostMapping("/{isbn}")
    public Livro cadastrarPorIsbn(@PathVariable String isbn) {
        return service.salvarComOpenLibrary(isbn);
    }

    @GetMapping
    public Iterable<Livro> listarTodos() {
        return service.buscarTodos();
    }
}