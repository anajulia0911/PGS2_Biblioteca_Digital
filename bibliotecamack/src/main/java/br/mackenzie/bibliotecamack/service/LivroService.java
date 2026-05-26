package br.mackenzie.bibliotecamack.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import br.mackenzie.bibliotecamack.repository.LivroRepository;
import br.mackenzie.bibliotecamack.model.Livro;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class LivroService {

    @Autowired
    private LivroRepository repository;

    @Autowired
    private br.mackenzie.bibliotecamack.repository.EmprestimoRepository emprestimoRepository;

    public Livro salvarComOpenLibrary(String isbn) {
        // 1. Define a URL da API da Open Library com o ISBN fornecido
        String url = "https://openlibrary.org/api/books?bibkeys=ISBN:" + isbn + "&format=json&jscmd=data";
        
        RestTemplate restTemplate = new RestTemplate();
        
        try {
            // 2. Faz a chamada GET para a API recebendo o resultado como String (JSON bruto)
            String jsonResposta = restTemplate.getForObject(url, String.class);
            
            Livro novoLivro = new Livro();
            novoLivro.setIsbn(isbn);

            if (jsonResposta != null && !jsonResposta.isEmpty()) {
                // 3. Usa o ObjectMapper do Jackson (já vem no Spring) para mapear o JSON complexo
                ObjectMapper mapper = new ObjectMapper();
                JsonNode raiz = mapper.readTree(jsonResposta);
                
                // A Open Library responde com uma chave dinâmica baseada no ISBN, ex: {"ISBN:9788535902778": { ... }}
                JsonNode dadosLivro = raiz.path("ISBN:" + isbn);
                
                if (!dadosLivro.isMissingNode()) {
                    // Extrai o título do JSON
                    if (dadosLivro.has("title")) {
                        novoLivro.setTitulo(dadosLivro.get("title").asText());
                    }
                    
                    // Extrai a editora (no JSON da Open Library, 'publishers' é uma lista)
                    if (dadosLivro.has("publishers") && dadosLivro.get("publishers").isArray()) {
                        String editora = dadosLivro.get("publishers").get(0).get("name").asText();
                        novoLivro.setEditora(editora);
                    }
                    
                    // Opcional: Você também pode extrair autores aqui se quiser alimentar sua tabela Autor!
                } else {
                    // Fallback caso o ISBN não seja encontrado na Open Library
                    novoLivro.setTitulo("Título não encontrado na API");
                    novoLivro.setEditora("Desconhecida");
                }
            }

            // 4. Salva no banco de dados (Supabase/PostgreSQL) através do Repository
            return repository.save(novoLivro);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao conectar ou processar dados da API Open Library: " + e.getMessage());
        }
    }
    public Livro buscarPreviewPorIsbn(String isbn) {
    // Verifica se já está cadastrado
    if (repository.findByIsbn(isbn).isPresent()) {
        throw new RuntimeException("DUPLICADO");
    }

    String url = "https://openlibrary.org/api/books?bibkeys=ISBN:" + isbn + "&format=json&jscmd=data";
    RestTemplate restTemplate = new RestTemplate();

    try {
        String jsonResposta = restTemplate.getForObject(url, String.class);
        Livro livro = new Livro();
        livro.setIsbn(isbn);

        if (jsonResposta != null && !jsonResposta.isEmpty()) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode raiz = mapper.readTree(jsonResposta);
            JsonNode dadosLivro = raiz.path("ISBN:" + isbn);

            if (!dadosLivro.isMissingNode()) {
                if (dadosLivro.has("title")) livro.setTitulo(dadosLivro.get("title").asText());
                if (dadosLivro.has("publishers") && dadosLivro.get("publishers").isArray()) {
                    livro.setEditora(dadosLivro.get("publishers").get(0).get("name").asText());
                }
            } else {
                livro.setTitulo("ISBN não encontrado na Open Library");
                livro.setEditora("Desconhecida");
            }
        }
        return livro; // retorna sem salvar
    } catch (RuntimeException e) {
        throw e;
    } catch (Exception e) {
        throw new RuntimeException("Erro ao consultar API: " + e.getMessage());
    }
}

public Livro salvarManual(String titulo, String isbn, String editora, Long autorId, Long categoriaId) {
    Livro livro = new Livro();
    livro.setTitulo(titulo);
    livro.setIsbn(isbn);
    livro.setEditora(editora);
    if (autorId != null) {
        repository.findById(autorId).ifPresent(l -> {}); // só para não quebrar
        br.mackenzie.bibliotecamack.model.Autor autor = new br.mackenzie.bibliotecamack.model.Autor();
        autor.setId(autorId);
        livro.setAutor(autor);
    }
    if (categoriaId != null) {
        br.mackenzie.bibliotecamack.model.Categoria cat = new br.mackenzie.bibliotecamack.model.Categoria();
        cat.setId(categoriaId);
        livro.setCategoria(cat);
    }
    return repository.save(livro);
}

    public Iterable<Livro> buscarTodos() {
        return repository.findAll();
    }

    public void deletar(Long id) {
    // Remove vínculos de empréstimo antes de excluir
    Livro livro = repository.findById(id).orElseThrow();
    Iterable<br.mackenzie.bibliotecamack.model.Emprestimo> emprestimos = emprestimoRepository.findAll();
    for (br.mackenzie.bibliotecamack.model.Emprestimo e : emprestimos) {
        e.getLivros().remove(livro);
        emprestimoRepository.save(e);
    }
    repository.deleteById(id);
}
}