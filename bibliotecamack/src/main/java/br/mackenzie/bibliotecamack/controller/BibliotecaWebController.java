package br.mackenzie.bibliotecamack.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import br.mackenzie.bibliotecamack.model.Leitor;
import br.mackenzie.bibliotecamack.model.Livro;
import br.mackenzie.bibliotecamack.service.LeitorService;
import br.mackenzie.bibliotecamack.service.LivroService;

@Controller
@RequestMapping("/biblioteca")
public class BibliotecaWebController {

    @Autowired
    private LivroService livroService;

    @Autowired
    private LeitorService leitorService;

    @GetMapping
    public String index() {
        return "index";
    }

    // --- TELAS DE LIVROS ---
    @GetMapping("/livros")
    public String listarLivros(Model model) {
        model.addAttribute("livros", livroService.buscarTodos());
        model.addAttribute("novoLivro", new Livro());
        return "livros";
    }

    @PostMapping("/livros/salvar")
    public String salvarLivro(@RequestParam String isbn) {
        // Usa a sua lógica de buscar na API externa Open Library pelo ISBN
        livroService.salvarComOpenLibrary(isbn);
        return "redirect:/biblioteca/livros";
    }

    // --- TELAS DE LEITORES ---
    @GetMapping("/leitores")
    public String listarLeitores(Model model) {
        model.addAttribute("leitores", leitorService.getLeitorRepository().findAll()); // ajuste conforme seus métodos
        model.addAttribute("leitor", new Leitor());
        return "leitores";
    }

    @PostMapping("/leitores/salvar")
    public String salvarLeitor(@ModelAttribute Leitor leitor) {
        leitorService.create(leitor);
        return "redirect:/biblioteca/leitores";
    }
}