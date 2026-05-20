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
@RequestMapping("/biblioteca/admin")
public class BibliotecaWebController {

    @Autowired
    private LivroService livroService;

    @Autowired
    private LeitorService leitorService;

    // Se o usuário acessar "/biblioteca/admin", ele abre a página index
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
        // Usa a lógica de buscar na API externa Open Library pelo ISBN
        livroService.salvarComOpenLibrary(isbn);
        // CORREÇÃO: O redirect precisa conter o caminho completo da URL mapeada
        return "redirect:/biblioteca/admin/livros";
    }

    // --- TELAS DE LEITORES ---
    @GetMapping("/leitores")
    public String listarLeitores(Model model) {
        // CORREÇÃO EXTRA: Usando o método correto de busca do service se o getLeitorRepository não existir
        model.addAttribute("leitores", leitorService.buscarTodos()); 
        model.addAttribute("leitor", new Leitor());
        return "leitores";
    }

    @PostMapping("/leitores/salvar")
    public String salvarLeitor(@ModelAttribute Leitor leitor) {
        leitorService.create(leitor);
        // CORREÇÃO: O redirect precisa conter o caminho completo da URL mapeada
        return "redirect:/biblioteca/admin/leitores";
    }
}