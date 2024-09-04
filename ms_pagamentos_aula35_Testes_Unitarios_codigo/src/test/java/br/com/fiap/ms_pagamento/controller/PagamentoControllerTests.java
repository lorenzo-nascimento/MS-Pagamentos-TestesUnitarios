package br.com.fiap.ms_pagamento.controller;

import br.com.fiap.ms_pagamento.dto.PagamentoDTO;
import br.com.fiap.ms_pagamento.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import br.com.fiap.ms_pagamento.service.PagamentoService;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import javax.xml.transform.Result;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    void setup() throws Exception{

        // Criando um pagamentoDTO
        pagamentoDTO = Factory.createPagamentoDTO();
        //Listando PagamentoDTO
        List<PagamentoDTO> list = List.of(pagamentoDTO);
        //Simulando comportamento do service - findAll
        when(service.findAll()).thenReturn(list);
    }

    @Test
    public void findAllShouldReturnListPagamentoDTO() throws Exception {

        // Chamando uma requisição com o método get em /pagamentos
        ResultActions result = mockMvc.perform(get("/pagamentos")
                .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk());
    }

    


}
