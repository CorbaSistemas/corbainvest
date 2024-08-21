package com.corbainvest.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Solicitacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeCliente;
    private String assunto;

    @Enumerated(EnumType.STRING)
    private TipoSolicitacao tipoSolicitacao;

    @Enumerated(EnumType.STRING)
    private StatusSolicitacao status;

}
