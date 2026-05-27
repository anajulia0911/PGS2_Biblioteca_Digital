package br.mackenzie.bibliotecamack.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.mackenzie.bibliotecamack.repository.EmprestimoRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/estatisticas")
public class EstatisticaController {

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    @GetMapping("/autores/mais-lidos")
    public ResponseEntity<?> autoresMaisLidos() {
        List<Object[]> resultado = emprestimoRepository.autoresMaisLidos();

        if (resultado.isEmpty()) {
            return ResponseEntity.ok(Map.of("mensagem", "Nenhum empréstimo registrado ainda."));
        }

        // Primeiro resultado é o mais lido (ORDER BY DESC)
        Object[] maisl = resultado.get(0);

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("autorMaisLido", maisl[0]);
        resposta.put("totalEmprestimos", maisl[1]);

        // Lista completa do ranking
        List<Map<String, Object>> ranking = resultado.stream().map(row -> {
            Map<String, Object> item = new HashMap<>();
            item.put("autor", row[0]);
            item.put("totalEmprestimos", row[1]);
            return item;
        }).toList();

        resposta.put("ranking", ranking);

        return ResponseEntity.ok(resposta);
    }
}