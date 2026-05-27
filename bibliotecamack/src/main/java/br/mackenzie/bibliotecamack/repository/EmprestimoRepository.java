package br.mackenzie.bibliotecamack.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import br.mackenzie.bibliotecamack.model.Emprestimo;
import br.mackenzie.bibliotecamack.model.Leitor;
import java.util.List;

@Repository
public interface EmprestimoRepository extends CrudRepository<Emprestimo, Long> {

    public List<Emprestimo> findByLeitor(Leitor leitor);
    
    public List<Emprestimo> findByStatus(String status);

    @Query("SELECT l.autor.nome, COUNT(e) FROM Emprestimo e " +
           "JOIN e.livros l " +
           "WHERE l.autor IS NOT NULL " +
           "GROUP BY l.autor.nome " +
           "ORDER BY COUNT(e) DESC")
    List<Object[]> autoresMaisLidos();
}