package com.corbainvest.controller;

import com.corbainvest.model.Solicitacao;
import com.corbainvest.model.TipoSolicitacao;
import com.corbainvest.service.DistribuicaoService;
import com.corbainvest.service.SolicitacaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/solicitacoes")
public class SolicitacaoController {

    private final SolicitacaoService solicitacaoService;
    private final DistribuicaoService distribuicaoService;

    public SolicitacaoController(SolicitacaoService solicitacaoService, DistribuicaoService distribuicaoService) {
        this.solicitacaoService = solicitacaoService;
        this.distribuicaoService = distribuicaoService;
    }

    @PostMapping
    public ResponseEntity<String> criarSolicitacao(@RequestBody Solicitacao solicitacao) {
        solicitacaoService.salvarSolicitacao(solicitacao);
        try {
            distribuicaoService.distribuirSolicitacao(solicitacao);
            return new ResponseEntity<>("Solicitação em andamento", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }

    @GetMapping
    public ResponseEntity<?> buscarTodasSolicitacoes() {
        return new ResponseEntity<>(solicitacaoService.buscarTodasSolicitacoes(), HttpStatus.OK);
    }

    @PostMapping("/liberar/{equipe}")
    public ResponseEntity<String> liberarAgente(@PathVariable String equipe) {
        distribuicaoService.liberarAgente(TipoSolicitacao.valueOf(equipe.toUpperCase()));
        return new ResponseEntity<>("Agente liberado", HttpStatus.OK);
    }
}
