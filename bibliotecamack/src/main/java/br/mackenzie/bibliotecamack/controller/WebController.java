package br.mackenzie.bibliotecamack.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import br.mackenzie.bibliotecamack.model.Leitor;
import br.mackenzie.bibliotecamack.service.LeitorService;
import br.mackenzie.bibliotecamack.service.LivroService;

@Controller
public class WebController {

    @Autowired
    private LeitorService leitorService;

    @Autowired
    private LivroService livroService;

    // Página inicial
    @GetMapping("/")
    public String paginaInicial() {
        return "usuario/index";
    }

    // Tela de login
    @GetMapping("/login")
    public String telaLogin() {
        return "usuario/login";
    }

    // ✅ TELA DE LIVROS (AGORA FUNCIONANDO COM LISTA)
    @GetMapping("/livros")
    public String telaLivros(Model model) {
        model.addAttribute("livros", livroService.buscarTodos());
        return "usuario/livros";
    }

    // ✅ SALVAR LIVRO VIA ISBN (API)
    @PostMapping("/livros/salvar")
    public String salvarLivro(@RequestParam String isbn) {
        livroService.salvarComOpenLibrary(isbn);
        return "redirect:/livros";
    }

    // Tela de leitores
    @GetMapping("/leitores")
    public String telaLeitores(Model model) {
        model.addAttribute("leitor", new Leitor());
        model.addAttribute("leitores", leitorService.buscarTodos());
        return "usuario/leitores";
    }

    @PostMapping("/leitores/salvar")
    public String salvarLeitor(@ModelAttribute Leitor leitor) {
        leitorService.create(leitor);
        return "redirect:/leitores";
    }

    // Tela catálogo
    @GetMapping("/catalogo")
    public String telaCatalogo() {
        return "usuario/catalogo";
    }
}