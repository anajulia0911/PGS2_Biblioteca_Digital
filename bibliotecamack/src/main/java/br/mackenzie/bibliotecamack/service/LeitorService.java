package br.mackenzie.bibliotecamack.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.mackenzie.bibliotecamack.model.Leitor;
import br.mackenzie.bibliotecamack.repository.LeitorRepository;

@Service
public class LeitorService {

    @Autowired
    private LeitorRepository leitorRepository;

    public void create(Leitor leitor) {
        if (leitor.getRegistroAcademico() != null && !leitor.getRegistroAcademico().equalsIgnoreCase("")) {
            leitorRepository.save(leitor);
        }
    }

    public Optional<Leitor> findByRA(String ra) {
    return leitorRepository.findByRegistroAcademico(ra);
}
    public Iterable<Leitor> buscarTodos() {
        return leitorRepository.findAll();
    }

    public long getTotalLeitores() {
        return leitorRepository.count();
    }
}