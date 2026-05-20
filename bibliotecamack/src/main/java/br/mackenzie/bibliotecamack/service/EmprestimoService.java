package br.mackenzie.bibliotecamack.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.mackenzie.bibliotecamack.model.Emprestimo;
import br.mackenzie.bibliotecamack.repository.EmprestimoRepository;

import java.util.Calendar;
import java.util.Date;

@Service
public class EmprestimoService {

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    public Emprestimo salvar(Emprestimo emprestimo) {
        if (emprestimo.getDataEmprestimo() == null) {
            emprestimo.setDataEmprestimo(new Date());
        }

    
        if (emprestimo.getDataDevolucaoPrevista() == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(emprestimo.getDataEmprestimo());
            calendar.add(Calendar.DAY_OF_MONTH, 14); 
            emprestimo.setDataDevolucaoPrevista(calendar.getTime());
        }

        emprestimo.setStatus(0);

        return emprestimoRepository.save(emprestimo);
    }

    public Iterable<Emprestimo> buscarTodos() {
        return emprestimoRepository.findAll();
    }

    public Emprestimo buscarPorId(Long id) {
        return emprestimoRepository.findById(id).orElse(null);
    }
    
    public long getTotalEmprestimos() {
        return emprestimoRepository.count();
    }
}