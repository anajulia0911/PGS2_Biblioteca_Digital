package br.mackenzie.bibliotecamack.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.mackenzie.bibliotecamack.model.Emprestimo;
import br.mackenzie.bibliotecamack.service.EmprestimoService;

import java.util.List;

@RestController
@RequestMapping("/api/emprestimos")
public class EmprestimoController {

    @Autowired
    private EmprestimoService emprestimoService;

    @PostMapping
    public ResponseEntity<Emprestimo> criarEmprestimo(@RequestBody Emprestimo emprestimo) {
        try {
            Emprestimo novoEmprestimo = emprestimoService.salvar(emprestimo);
            return new ResponseEntity<>(novoEmprestimo, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<Iterable<Emprestimo>> listarTodos() {
        Iterable<Emprestimo> emprestimos = emprestimoService.buscarTodos();
        return new ResponseEntity<>(emprestimos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Emprestimo> buscarPorId(@PathVariable Long id) {
        Emprestimo emprestimo = emprestimoService.buscarPorId(id);
        if (emprestimo != null) {
            return new ResponseEntity<>(emprestimo, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}