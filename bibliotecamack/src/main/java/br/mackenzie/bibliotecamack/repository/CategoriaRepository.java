package br.mackenzie.bibliotecamack.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import br.mackenzie.bibliotecamack.model.Categoria;

@Repository
public interface CategoriaRepository extends CrudRepository<Categoria, Long> {
    Categoria findByNome(String nome);
}