package com.facens.grupo04_atdd.dto;

import jakarta.validation.constraints.NotBlank;

public class AlunoRequestDTO {

    @NotBlank(message = "Nome e obrigatorio")
    private String nome;


}