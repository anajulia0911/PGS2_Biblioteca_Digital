package br.mackenzie.bibliotecamack.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import br.mackenzie.bibliotecamack.model.Leitor;

@Repository
public interface LeitorRepository extends CrudRepository<Leitor, Long> {

    public Leitor findByRegistroAcademico(String registroAcademico);

    public Leitor findByEmail(String email);
}