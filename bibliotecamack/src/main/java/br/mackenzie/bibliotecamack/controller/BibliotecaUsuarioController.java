package br.mackenzie.bibliotecamack.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import br.mackenzie.bibliotecamack.model.*;
import br.mackenzie.bibliotecamack.repository.*;

import java.util.Date;
import java.util.Optional;
import java.util.Calendar;

@Controller
@RequestMapping("/biblioteca/usuario")
public class BibliotecaUsuarioController {

    @Autowired private LivroRepository livroRepository;
    @Autowired private LeitorRepository leitorRepository;
    @Autowired private AutorRepository autorRepository;
    @Autowired private CategoriaRepository categoriaRepository;
    @Autowired private EmprestimoRepository emprestimoRepository;

    // 1. TELA DE LOGIN / ACESSO DO ALUNO
    @GetMapping("/login")
    public String telaLogin() {
        return "usuario/login";
    }

    // 2. PAINEL DO LEITOR (CATÁLOGO COM FILTROS)
    @PostMapping("/catalogo")
    public String acessarCatalogo(@RequestParam("registroAcademico") String ra, Model model) {
        // Simula a autenticação pelo RA
        Optional<Leitor> leitorOpt = leitorRepository.findByRegistroAcademico(ra);
        
        if (leitorOpt.isEmpty()) {
            model.addAttribute("erro", "Registro Académico (RA) não encontrado!");
            return "usuario/login";
        }

        Leitor leitorLogado = leitorOpt.get();
        model.addAttribute("leitor", leitorLogado);
        model.addAttribute("livros", livroRepository.findAll());
        model.addAttribute("autores", autorRepository.findAll());
        model.addAttribute("categorias", categoriaRepository.findAll());
        
        return "usuario/catalogo";
    }

    // Rota complementar para quando o usuário aplicar um filtro de busca
    @GetMapping("/catalogo/filtrar")
    public String filtrarCatalogo(
            @RequestParam("leitorId") Long leitorId,
            @RequestParam(value = "autorId", required = false) Long autorId,
            @RequestParam(value = "categoriaId", required = false) Long categoriaId,
            Model model) {
        
        Leitor leitor = leitorRepository.findById(leitorId).orElseThrow();
        Iterable<Livro> livrosFiltrados;

        if (autorId != null) {
            Autor autor = autorRepository.findById(autorId).orElseThrow();
            livrosFiltrados = livroRepository.findByAutor(autor);
        } else if (categoriaId != null) {
            Categoria cat = categoriaRepository.findById(categoriaId).orElseThrow();
            livrosFiltrados = livroRepository.findByCategoria(cat);
        } else {
            livrosFiltrados = livroRepository.findAll();
        }

        model.addAttribute("leitor", leitor);
        model.addAttribute("livros", livrosFiltrados);
        model.addAttribute("autores", autorRepository.findAll());
        model.addAttribute("categorias", categoriaRepository.findAll());
        
        return "usuario/catalogo";
    }

    // 3. CONFIRMAÇÃO AUTOMÁTICA DE EMPRÉSTIMO
    @PostMapping("/solicitar-emprestimo")
    public String realizarEmprestimo(
            @RequestParam("leitorId") Long leitorId,
            @RequestParam("livroId") Long livroId,
            Model model) {

        Leitor leitor = leitorRepository.findById(leitorId).orElseThrow();
        Livro livro = livroRepository.findById(livroId).orElseThrow();

        // Cria o empréstimo em tempo real
        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setLeitor(leitor);
        emprestimo.setLivro(java.util.List.of(livro));
        emprestimo.setDataEmprestimo(new Date());

        // Define prazo automático de devolução para daqui a 7 dias
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 7);
        emprestimo.setDataDevolucaoPrevista(cal.getTime());
        emprestimo.setStatus("Ativo");

        emprestimoRepository.save(emprestimo);

        model.addAttribute("sucesso", "Livro '" + livro.getTitulo() + "' reservado com sucesso! Retire no balcão.");
        model.addAttribute("leitor", leitor);
        model.addAttribute("livros", livroRepository.findAll());
        model.addAttribute("autores", autorRepository.findAll());
        model.addAttribute("categorias", categoriaRepository.findAll());

        return "usuario/catalogo";
    }
}