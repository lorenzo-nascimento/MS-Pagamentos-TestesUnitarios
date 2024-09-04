package br.com.fiap.ms_pagamento.controller;

import br.com.fiap.ms_pagamento.dto.PagamentoDTO;
import br.com.fiap.ms_pagamento.service.PagamentoService;
import br.com.fiap.ms_pagamento.service.exception.ResourceNotFoundException;
import br.com.fiap.ms_pagamento.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


import java.util.List;



@WebMvcTest(PagamentoController.class)
public class PagamentoControllerTests {

    @Autowired
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

    @BeforeEach
    void setup() throws Exception{

        // Criando um pagamentoDTO
        pagamentoDTO = Factory.createPagamentoDTO();
        //Listando PagamentoDTO
        List<PagamentoDTO> list = List.of(pagamentoDTO);
        //Simulando comportamento do service - findAll
        when(service.findAll()).thenReturn(list);

        existingId = 1L;
        nonExistingId = 10L;

        // Simulando comportamento do service - findById
        when(service.findById(existingId)).thenReturn(pagamentoDTO);
        // findById - id não existe - lança exception
        when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
        when(service.insert(any())).thenReturn(pagamentoDTO);
        when(service.insert(pagamentoDTO)).thenReturn(pagamentoDTO);


    }

    @Test
    public void findAllShouldReturnListPagamentoDTO() throws Exception {

        // Chamando uma requisição com o método get em /pagamentos
        ResultActions result = mockMvc.perform(get("/pagamentos")
                .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk());
    }

    @Test
    public void  findByIdShouldReturnPagamentoDTOWhenIdExists() throws Exception {

        ResultActions result = mockMvc.perform(get("/pagamentos/{id}", existingId)
                .accept(MediaType.APPLICATION_JSON));
        // Assertions
        result.andExpect(status().isOk());
        // analisa se tem os campos em result
        // $ - acessar o objeto da resposta
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.valor").exists());
        result.andExpect(jsonPath("$.status").exists());

    }

    @Test
    @DisplayName("findById Deve retornar NotFound quando Id não existe - Erro 404")
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {

        ResultActions result = mockMvc.perform(get("/pagamentos/{id}", nonExistingId)
                .accept(MediaType.APPLICATION_JSON));

        //Assertions
        result.andExpect(status().isNotFound());

    }

    @Test
    public void insertShouldReturnProductDTOCreated() throws Exception{
        // POST tem corpo - JSON
        //Bean objectMapper para converter JAVA para JSON

        PagamentoDTO newDTO = Factory.createNewPagamentoDTO();
        String jsonBody = objectMapper.writeValueAsString(newDTO);

        mockMvc.perform(post("/pagamentos")
                    .content(jsonBody)
                    .contentType(MediaType.APPLICATION_JSON) // Requisição
                    .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.valor").exists())
                .andExpect(jsonPath("$.status").exists());

    }

}
