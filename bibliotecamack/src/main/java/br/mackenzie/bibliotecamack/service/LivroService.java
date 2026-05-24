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

    public Iterable<Livro> buscarTodos() {
        return repository.findAll();
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }
}