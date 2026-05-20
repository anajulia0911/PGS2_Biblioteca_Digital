package br.mackenzie.bibliotecamack.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.mackenzie.bibliotecamack.model.Leitor;
import br.mackenzie.bibliotecamack.service.LeitorService;

@RestController
@RequestMapping("/api/leitores")
public class LeitorController {

    @Autowired
    private LeitorService leitorService;

    @PostMapping
    public ResponseEntity<String> criarLeitor(@RequestBody Leitor leitor) {
        try {
            leitorService.create(leitor);
            return new ResponseEntity<>("Leitor cadastrado com sucesso!", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Erro ao cadastrar leitor: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<Iterable<Leitor>> listarTodos() {
    
        Iterable<Leitor> leitores = leitorService.buscarTodos();
        return new ResponseEntity<>(leitores, HttpStatus.OK);
    }

    @GetMapping("/ra/{ra}")
    public ResponseEntity<Leitor> buscarPorRA(@PathVariable String ra) {
        Leitor leitor = leitorService.findByRA(ra);
        if (leitor != null) {
            return new ResponseEntity<>(leitor, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}