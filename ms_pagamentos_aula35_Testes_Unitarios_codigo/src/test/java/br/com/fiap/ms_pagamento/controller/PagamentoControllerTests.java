package br.com.fiap.ms_pagamento.controller;

import br.com.fiap.ms_pagamento.dto.PagamentoDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import br.com.fiap.ms_pagamento.service.PagamentoService;

@WebMvcTest(PagamentoController.class)
public class PagamentoControllerTests {

    private MockMvc mockMvc; // Para chamar o Endpoint
    // Controller tem dependência do service
    // dependência mockada
    @MockBean
    private PagamentoService service;
    private PagamentoDTO pagamentoDTO;
    private Long existingId;
    private Long nonExistingId;
    // Converter para JSON o objeto Java e enviar na requisição
    @Autowired
    private ObjectMapper objectMapper;

    

}
