package br.mackenzie.bibliotecamack.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.mackenzie.bibliotecamack.model.Categoria;
import br.mackenzie.bibliotecamack.repository.CategoriaRepository;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    // Criar ou Atualizar Categoria com validação defensiva
    public Categoria save(Categoria categoria) {
        if (categoria.getNome() != null && !categoria.getNome().trim().equalsIgnoreCase("")) {
            return categoriaRepository.save(categoria);
        }
        throw new IllegalArgumentException("O nome da categoria não pode ser vazio.");
    }

    // Listar todas as categorias
    public Iterable<Categoria> findAll() {
        return categoriaRepository.findAll();
    }

    // Buscar por ID
    public Optional<Categoria> findById(Long id) {
        return categoriaRepository.findById(id);
    }

    // Excluir por ID
    public void deleteById(Long id) {
        categoriaRepository.deleteById(id);
    }
}