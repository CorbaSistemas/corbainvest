package com.corbainvest.service;

import com.corbainvest.exception.SemAgenteDisponivelException;
import com.corbainvest.model.Solicitacao;
import com.corbainvest.model.TipoSolicitacao;
import com.corbainvest.model.StatusSolicitacao;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.LinkedList;

@Service
public class DistribuicaoService {

    private final Map<TipoSolicitacao, Queue<Solicitacao>> filasEquipe = new HashMap<>();
    private final int CAPACIDADE_MAXIMA = 3;
    private final Map<TipoSolicitacao, Integer> agentesDisponiveis = new HashMap<>();

    public DistribuicaoService() {
        for (TipoSolicitacao tipo : TipoSolicitacao.values()) {
            filasEquipe.put(tipo, new LinkedList<>());
            agentesDisponiveis.put(tipo, 0);
        }
    }

    public void distribuirSolicitacao(Solicitacao solicitacao) throws SemAgenteDisponivelException {
        TipoSolicitacao tipo = determinarTipoEquipe(solicitacao.getAssunto());

        if (agentesDisponiveis.get(tipo) < CAPACIDADE_MAXIMA) {
            agentesDisponiveis.put(tipo, agentesDisponiveis.get(tipo) + 1);
            solicitacao.setStatus(StatusSolicitacao.EM_ANDAMENTO);
        } else {
            filasEquipe.get(tipo).add(solicitacao);
            solicitacao.setStatus(StatusSolicitacao.PENDENTE);
            throw new SemAgenteDisponivelException("Nenhum agente disponível. Solicitação enfileirada.");
        }
    }

    private TipoSolicitacao determinarTipoEquipe(String assunto) {
        if (assunto.equalsIgnoreCase("Problemas com cartão")) {
            return TipoSolicitacao.CARTOES;
        } else if (assunto.equalsIgnoreCase("Contratação de empréstimo")) {
            return TipoSolicitacao.EMPRESTIMOS;
        } else {
            return TipoSolicitacao.OUTROS_ASSUNTOS;
        }
    }

    public void liberarAgente(TipoSolicitacao tipo) {
        if (agentesDisponiveis.get(tipo) > 0) {
            agentesDisponiveis.put(tipo, agentesDisponiveis.get(tipo) - 1);
            if (!filasEquipe.get(tipo).isEmpty()) {
                Solicitacao proximaSolicitacao = filasEquipe.get(tipo).poll();
                distribuirSolicitacao(proximaSolicitacao);
            }
        }
    }
}
