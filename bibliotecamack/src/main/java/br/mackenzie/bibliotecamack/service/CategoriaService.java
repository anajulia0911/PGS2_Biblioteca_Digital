package br.mackenzie.bibliotecamack.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.mackenzie.bibliotecamack.model.Categoria;
import br.mackenzie.bibliotecamack.repository.CategoriaRepository;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    // 1. Conecta perfeitamente com o 'categoriaService.create(categoria)' do seu Controller
    public void create(Categoria categoria) {
        if (categoria.getNome() != null && !categoria.getNome().trim().isEmpty()) {
            categoriaRepository.save(categoria);
        } else {
            throw new IllegalArgumentException("O nome da categoria não pode ser vazio.");
        }
    }

    // 2. Conecta perfeitamente com o 'categoriaService.findByNome(nome)' do seu Controller
    public Categoria findByNome(String nome) {
        return categoriaRepository.findByNome(nome);
    }

    // 3. Conecta perfeitamente com o 'categoriaService.getTotalCategorias()' do seu Controller
    public long getTotalCategorias() {
        return categoriaRepository.count();
    }

    public void deletar(Long id) {
    categoriaRepository.deleteById(id);
}
}