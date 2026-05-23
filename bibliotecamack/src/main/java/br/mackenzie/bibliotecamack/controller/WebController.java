package br.mackenzie.bibliotecamack.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import br.mackenzie.bibliotecamack.model.Leitor;
import br.mackenzie.bibliotecamack.service.LeitorService;

@Controller
public class WebController {

    // Injetamos o serviço para poder buscar os dados do banco
    @Autowired
    private LeitorService leitorService;

    @GetMapping("/")
    public String paginaInicial() {
        return "usuario/index"; 
    }

    @GetMapping("/login")
    public String telaLogin() {
        return "usuario/login"; 
    }

    @GetMapping("/livros")
    public String telaLivros() {
        return "usuario/livros"; 
    }

    @GetMapping("/leitores")
    public String telaLeitores(Model model) {
        // 1. Envia um objeto vazio para o Thymeleaf montar o formulário de cadastro
        model.addAttribute("leitor", new Leitor());
        
        // 2. Busca a lista de leitores no banco e envia para montar a tabela
        model.addAttribute("leitores", leitorService.buscarTodos());
        
        return "usuario/leitores"; 
    }

    @GetMapping("/catalogo")
    public String telaCatalogo() {
        return "usuario/catalogo"; 
    }
}