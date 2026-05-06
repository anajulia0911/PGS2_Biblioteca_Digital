package br.mackenzie.bibliotecamack.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.mackenzie.bibliotecamack.model.Livro;
import br.mackenzie.bibliotecamack.repository.LivroRepository;

@Service
public class LivroService {
    @Autowired
    private LivroRepository livroRepository;

    public void create(Livro l) {
        // Validação pelo ISBN conforme a lógica de campos únicos do professor
        if (l.getIsbn() != null && !l.getIsbn().isEmpty()) {
            livroRepository.save(l);
        }
    }

    public Livro findByIsbn(String isbn) {
        return livroRepository.findLivroByIsbn(isbn);
    }

    public Iterable<Livro> findAll() {
        return livroRepository.findAll();
    }
}