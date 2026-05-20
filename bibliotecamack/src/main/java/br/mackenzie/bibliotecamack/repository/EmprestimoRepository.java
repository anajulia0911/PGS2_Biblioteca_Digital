package br.mackenzie.bibliotecamack.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import br.mackenzie.bibliotecamack.model.Emprestimo;
import br.mackenzie.bibliotecamack.model.Leitor;
import java.util.List;

@Repository
public interface EmprestimoRepository extends CrudRepository<Emprestimo, Long> {

    public List<Emprestimo> findByLeitor(Leitor leitor);
    
    public List<Emprestimo> findByStatus(int status);
}