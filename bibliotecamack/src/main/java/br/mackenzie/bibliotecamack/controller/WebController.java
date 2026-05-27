package br.mackenzie.bibliotecamack.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import br.mackenzie.bibliotecamack.model.Autor;
import br.mackenzie.bibliotecamack.model.Categoria;
import br.mackenzie.bibliotecamack.model.Leitor;
import br.mackenzie.bibliotecamack.model.Livro;
import br.mackenzie.bibliotecamack.repository.AutorRepository;
import br.mackenzie.bibliotecamack.repository.CategoriaRepository;
import br.mackenzie.bibliotecamack.service.AutorService;
import br.mackenzie.bibliotecamack.service.CategoriaService;
import br.mackenzie.bibliotecamack.service.LeitorService;
import br.mackenzie.bibliotecamack.service.LivroService;

@Controller
public class WebController {

    @Autowired private LeitorService leitorService;
    @Autowired private LivroService livroService;
    @Autowired private AutorService autorService;
    @Autowired private CategoriaService categoriaService;
    @Autowired private AutorRepository autorRepository;
    @Autowired private CategoriaRepository categoriaRepository;

    @GetMapping("/")
    public String paginaInicial() {
        return "usuario/index";
    }

    @GetMapping("/login")
    public String telaLogin() {
        return "usuario/login";
    }

    @GetMapping("/admin")
    public String telaAdmin(Model model) {
        model.addAttribute("livros", livroService.buscarTodos());
        model.addAttribute("autores", autorRepository.findAll());
        model.addAttribute("categorias", categoriaRepository.findAll());
        model.addAttribute("novoAutor", new Autor());
        model.addAttribute("novaCategoria", new Categoria());
        return "usuario/admin";
    }

    @PostMapping("/livros/buscar")
    public String buscarLivro(@RequestParam String isbn, Model model) {
        model.addAttribute("livros", livroService.buscarTodos());
        model.addAttribute("autores", autorRepository.findAll());
        model.addAttribute("categorias", categoriaRepository.findAll());
        model.addAttribute("novoAutor", new Autor());
        model.addAttribute("novaCategoria", new Categoria());
        try {
            Livro preview = livroService.buscarPreviewPorIsbn(isbn);
            model.addAttribute("preview", preview);
            model.addAttribute("isbnBuscado", isbn);
        } catch (RuntimeException e) {
            if (e.getMessage().equals("DUPLICADO")) {
                model.addAttribute("erro", "Este livro (ISBN: " + isbn + ") já está cadastrado!");
            } else {
                model.addAttribute("erro", "Erro ao buscar na API: " + e.getMessage());
            }
        }
        return "usuario/admin";
    }

    @PostMapping("/livros/salvar")
    public String salvarLivro(@RequestParam String isbn) {
        livroService.salvarComOpenLibrary(isbn);
        return "redirect:/admin?sucesso=livro";
    }

    @PostMapping("/livros/salvar-manual")
    public String salvarLivroManual(
            @RequestParam String titulo,
            @RequestParam(required = false) String isbn,
            @RequestParam(required = false) String editora,
            @RequestParam(required = false) Long autorId,
            @RequestParam(required = false) Long categoriaId) {
        livroService.salvarManual(titulo, isbn, editora, autorId, categoriaId);
        return "redirect:/admin?sucesso=livro";
    }

    @PostMapping("/autores/salvar")
    public String salvarAutor(@ModelAttribute Autor autor) {
        autorService.create(autor);
        return "redirect:/admin?sucesso=autor";
    }

    @PostMapping("/categorias/salvar")
    public String salvarCategoria(@ModelAttribute Categoria categoria) {
        categoriaService.create(categoria);
        return "redirect:/admin?sucesso=categoria";
    }

    @GetMapping("/livros/deletar/{id}")
    public String deletarLivro(@PathVariable Long id) {
        livroService.deletar(id);
        return "redirect:/admin";
    }

    @GetMapping("/livros/editar/{id}")
public String telaEditarLivro(@PathVariable Long id, Model model) {
    Livro livro = livroService.buscarPorId(id);
    model.addAttribute("livroEditar", livro);
    model.addAttribute("autores", autorRepository.findAll());
    model.addAttribute("categorias", categoriaRepository.findAll());
    return "usuario/editar-livro";
}

@PostMapping("/livros/editar/{id}")
public String salvarEdicaoLivro(
        @PathVariable Long id,
        @RequestParam String titulo,
        @RequestParam(required = false) String isbn,
        @RequestParam(required = false) String editora,
        @RequestParam(required = false) Long autorId,
        @RequestParam(required = false) Long categoriaId) {
    livroService.atualizar(id, titulo, isbn, editora, autorId, categoriaId);
    return "redirect:/admin?sucesso=livro";
}

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

    @GetMapping("/catalogo")
    public String telaCatalogo() {
        return "usuario/catalogo";
    }

    @GetMapping("/autores/deletar/{id}")
public String deletarAutor(@PathVariable Long id) {
    autorService.deletar(id);
    return "redirect:/admin?sucesso=autor";
}

@GetMapping("/categorias/deletar/{id}")
public String deletarCategoria(@PathVariable Long id) {
    categoriaService.deletar(id);
    return "redirect:/admin?sucesso=categoria";
}

@GetMapping("/cadastro")
public String telaCadastroAluno(Model model) {
    model.addAttribute("leitor", new Leitor());
    return "usuario/cadastro";
}

@PostMapping("/cadastro/salvar")
public String salvarCadastroAluno(@ModelAttribute Leitor leitor) {
    leitorService.create(leitor);
    return "redirect:/login?cadastrado=true";
}

}