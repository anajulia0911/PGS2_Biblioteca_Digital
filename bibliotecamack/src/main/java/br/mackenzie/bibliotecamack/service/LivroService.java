package br.mackenzie.bibliotecamack.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import br.mackenzie.bibliotecamack.repository.LivroRepository;
import br.mackenzie.bibliotecamack.repository.EmprestimoRepository;
import br.mackenzie.bibliotecamack.model.Livro;
import br.mackenzie.bibliotecamack.model.Autor;
import br.mackenzie.bibliotecamack.model.Categoria;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class LivroService {

    @Autowired
    private LivroRepository repository;

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    // Busca na API e salva no banco
    public Livro salvarComOpenLibrary(String isbn) {
        JsonNode dadosLivro = consultarApiOpenLibrary(isbn);
        Livro novoLivro = new Livro();
        novoLivro.setIsbn(isbn);
        preencherDadosDoJson(novoLivro, dadosLivro, isbn);
        return repository.save(novoLivro);
    }

    // Busca na API sem salvar (preview)
    public Livro buscarPreviewPorIsbn(String isbn) {
        if (repository.findByIsbn(isbn).isPresent()) {
            throw new RuntimeException("DUPLICADO");
        }
        JsonNode dadosLivro = consultarApiOpenLibrary(isbn);
        Livro livro = new Livro();
        livro.setIsbn(isbn);
        preencherDadosDoJson(livro, dadosLivro, isbn);
        return livro;
    }

    // ENDPOINT DE CONSULTA AGREGADA:
    // Une dados do banco local com dados em tempo real da API externa
    public List<Map<String, Object>> consultaAgregada() {
        Iterable<Livro> livrosLocais = repository.findAll();
        List<Map<String, Object>> resultado = new ArrayList<>();

        for (Livro livro : livrosLocais) {
            Map<String, Object> item = new HashMap<>();

            // Dados do banco local
            item.put("id", livro.getId());
            item.put("titulo", livro.getTitulo());
            item.put("isbn", livro.getIsbn());
            item.put("editora", livro.getEditora());
            item.put("autor", livro.getAutor() != null ? livro.getAutor().getNome() : "Desconhecido");
            item.put("categoria", livro.getCategoria() != null ? livro.getCategoria().getNome() : "Geral");

            // Dados em tempo real da API externa (se ISBN disponível)
            if (livro.getIsbn() != null && !livro.getIsbn().isBlank()) {
                try {
                    JsonNode dadosApi = consultarApiOpenLibrary(livro.getIsbn());
                    if (dadosApi != null && !dadosApi.isMissingNode()) {
                        item.put("tituloApi", dadosApi.has("title") ? dadosApi.get("title").asText() : "-");
                        item.put("descricaoApi", dadosApi.has("notes") ? dadosApi.get("notes").asText() : "Sem descrição disponível");
                        item.put("numeroPaginas", dadosApi.has("number_of_pages") ? dadosApi.get("number_of_pages").asText() : "-");
                        item.put("acessoApi", "online");
                    } else {
                        item.put("acessoApi", "isbn_nao_encontrado");
                    }
                } catch (Exception e) {
                    // Se a API falhar, usa só os dados locais (modo offline)
                    item.put("acessoApi", "offline");
                    item.put("erroApi", e.getMessage());
                }
            } else {
                item.put("acessoApi", "sem_isbn");
            }

            resultado.add(item);
        }
        return resultado;
    }

    // Método central de chamada à API com tratamento de exceções específicas
    private JsonNode consultarApiOpenLibrary(String isbn) {
        String url = "https://openlibrary.org/api/books?bibkeys=ISBN:" + isbn + "&format=json&jscmd=data";
        RestTemplate restTemplate = new RestTemplate();

        try {
            String jsonResposta = restTemplate.getForObject(url, String.class);

            if (jsonResposta == null || jsonResposta.isBlank()) {
                throw new RuntimeException("API retornou resposta vazia para ISBN: " + isbn);
            }

            ObjectMapper mapper = new ObjectMapper();
            JsonNode raiz = mapper.readTree(jsonResposta);
            return raiz.path("ISBN:" + isbn);

        } catch (ResourceAccessException e) {
            // Falha de conexão / timeout
            throw new RuntimeException("Sem conexão com a Open Library. Verifique sua internet.");
        } catch (HttpClientErrorException e) {
            // Erros 4xx (ex: 404 ISBN inválido)
            throw new RuntimeException("ISBN inválido ou não encontrado na API: " + e.getStatusCode());
        } catch (HttpServerErrorException e) {
            // Erros 5xx (API fora do ar)
            throw new RuntimeException("Serviço Open Library temporariamente indisponível: " + e.getStatusCode());
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            // Payload corrompido / erro de parse JSON
            throw new RuntimeException("Erro ao processar resposta da API (payload inválido): " + e.getMessage());
        }
    }

    // Preenche os campos do livro a partir do JSON da API
    private void preencherDadosDoJson(Livro livro, JsonNode dadosLivro, String isbn) {
        if (dadosLivro != null && !dadosLivro.isMissingNode()) {
            if (dadosLivro.has("title")) livro.setTitulo(dadosLivro.get("title").asText());
            if (dadosLivro.has("publishers") && dadosLivro.get("publishers").isArray()) {
                livro.setEditora(dadosLivro.get("publishers").get(0).get("name").asText());
            }
        } else {
            livro.setTitulo("Título não encontrado na API");
            livro.setEditora("Desconhecida");
        }
    }

    public Livro salvarManual(String titulo, String isbn, String editora, Long autorId, Long categoriaId) {
        Livro livro = new Livro();
        livro.setTitulo(titulo);
        livro.setIsbn(isbn);
        livro.setEditora(editora);
        if (autorId != null) {
            Autor autor = new Autor();
            autor.setId(autorId);
            livro.setAutor(autor);
        }
        if (categoriaId != null) {
            Categoria cat = new Categoria();
            cat.setId(categoriaId);
            livro.setCategoria(cat);
        }
        return repository.save(livro);
    }

    public Iterable<Livro> buscarTodos() {
        return repository.findAll();
    }

    public Livro buscarPorId(Long id) {
    return repository.findById(id).orElseThrow();
}

public Livro atualizar(Long id, String titulo, String isbn, String editora, Long autorId, Long categoriaId) {
    Livro livro = repository.findById(id).orElseThrow();
    livro.setTitulo(titulo);
    livro.setIsbn(isbn);
    livro.setEditora(editora);
    if (autorId != null) {
        Autor autor = new Autor();
        autor.setId(autorId);
        livro.setAutor(autor);
    } else {
        livro.setAutor(null);
    }
    if (categoriaId != null) {
        Categoria cat = new Categoria();
        cat.setId(categoriaId);
        livro.setCategoria(cat);
    } else {
        livro.setCategoria(null);
    }
    return repository.save(livro);
}

    public void deletar(Long id) {
        Livro livro = repository.findById(id).orElseThrow();
        Iterable<br.mackenzie.bibliotecamack.model.Emprestimo> emprestimos = emprestimoRepository.findAll();
        for (br.mackenzie.bibliotecamack.model.Emprestimo e : emprestimos) {
            e.getLivros().remove(livro);
            emprestimoRepository.save(e);
        }
        repository.deleteById(id);
    }


}