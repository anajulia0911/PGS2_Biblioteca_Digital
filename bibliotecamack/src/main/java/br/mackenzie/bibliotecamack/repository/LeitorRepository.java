package br.mackenzie.bibliotecamack.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import br.mackenzie.bibliotecamack.model.Leitor;
import java.util.Optional;

@Repository
public interface LeitorRepository extends CrudRepository<Leitor, Long> {

    Optional<Leitor> findByRegistroAcademico(String registroAcademico);

    Optional<Leitor> findByEmail(String email);
}
