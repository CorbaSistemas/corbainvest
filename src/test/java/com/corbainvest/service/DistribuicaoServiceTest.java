package com.corbainvest.service;

import com.corbainvest.exception.SemAgenteDisponivelException;
import com.corbainvest.model.Solicitacao;
import com.corbainvest.model.TipoSolicitacao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DistribuicaoServiceTest {

    private DistribuicaoService distribuicaoService;

    @BeforeEach
    void setUp() {
        distribuicaoService = new DistribuicaoService();
    }

    @Test
    void distribuirSolicitacao_deveAtribuirAgente() {
        Solicitacao solicitacao = new Solicitacao();
        solicitacao.setAssunto("Problemas com cartão");

        assertDoesNotThrow(() -> distribuicaoService.distribuirSolicitacao(solicitacao));
        assertDoesNotThrow(() -> distribuicaoService.liberarAgente(TipoSolicitacao.CARTOES));
    }

    @Test
    void distribuirSolicitacao_deveEnfileirarQuandoSemAgentes() {
        Solicitacao solicitacao = new Solicitacao();
        solicitacao.setAssunto("Problemas com cartão");

        distribuicaoService.distribuirSolicitacao(solicitacao);
        distribuicaoService.distribuirSolicitacao(solicitacao);
        distribuicaoService.distribuirSolicitacao(solicitacao);

        assertThrows(SemAgenteDisponivelException.class, () -> distribuicaoService.distribuirSolicitacao(solicitacao));
    }

    @Test
    void liberarAgente_deveLiberarAgenteEAtribuirProximaSolicitacao() {
        Solicitacao solicitacao = new Solicitacao();
        solicitacao.setAssunto("Problemas com cartão");

        distribuicaoService.distribuirSolicitacao(solicitacao);
        distribuicaoService.distribuirSolicitacao(solicitacao);
        distribuicaoService.distribuirSolicitacao(solicitacao);

        distribuicaoService.liberarAgente(TipoSolicitacao.CARTOES);
        assertDoesNotThrow(() ->distribuicaoService.liberarAgente(TipoSolicitacao.CARTOES));
    }
}
