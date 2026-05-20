package br.mackenzie.bibliotecamack.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import br.mackenzie.bibliotecamack.model.Livro;
import br.mackenzie.bibliotecamack.model.Autor;
import br.mackenzie.bibliotecamack.model.Categoria;

import java.util.List;

@Repository
public interface LivroRepository extends CrudRepository<Livro, Long> {
    
    // Procura livros filtrando pelo Autor
    List<Livro> findByAutor(Autor autor);
    
    // Procura livros filtrando pela Categoria
    List<Livro> findByCategoria(Categoria categoria);
}