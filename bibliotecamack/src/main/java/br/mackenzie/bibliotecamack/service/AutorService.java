package br.mackenzie.bibliotecamack.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.mackenzie.bibliotecamack.model.Autor;
import br.mackenzie.bibliotecamack.repository.AutorRepository;

@Service
public class AutorService {
    @Autowired
    private AutorRepository autorRepository;

    public void create(Autor a) {
        if (a.getNome() != null && !a.getNome().equalsIgnoreCase("")) {
            autorRepository.save(a);
        }
    }

    public Autor findByNome(String nome) {
        return autorRepository.findAutorByNome(nome);
    }

    public long getTotalAutores() {
        return autorRepository.count();
    }

    public void deletar(Long id) {
    autorRepository.deleteById(id);
}
}