package br.mackenzie.bibliotecamack.repository;

import org.springframework.data.repository.CrudRepository;
import br.mackenzie.bibliotecamack.model.Autor;

public interface AutorRepository extends CrudRepository<Autor, Long> {
    public Autor findAutorByNome(String nome);
}