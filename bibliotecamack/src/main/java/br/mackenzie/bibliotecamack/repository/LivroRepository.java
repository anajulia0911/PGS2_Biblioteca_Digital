package br.mackenzie.bibliotecamack.repository;

import org.springframework.data.repository.CrudRepository;
import br.mackenzie.bibliotecamack.model.Livro;

public interface LivroRepository extends CrudRepository<Livro, Long> {
    public Livro findLivroByIsbn(String isbn);
}