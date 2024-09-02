package br.com.fiap.ms_pagamento.service;

import br.com.fiap.ms_pagamento.dto.PagamentoDTO;
import br.com.fiap.ms_pagamento.model.Pagamento;
import br.com.fiap.ms_pagamento.repository.PagamentoRepository;
import br.com.fiap.ms_pagamento.service.exception.ResourceNotFoundException;
import br.com.fiap.ms_pagamento.tests.Factory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
public class PagamentoServiceTests {

    // Mock - Injetando
    @InjectMocks
    private PagamentoService service;

    // Mock do repositório
    @Mock
    private PagamentoRepository repository;

    //preparando os dados
    private Long existingId;
    private Long nonExistingId;

    //Próximos testes
    private Pagamento pagamento;
    private PagamentoDTO pagamentoDTO;

    @BeforeEach
    void setup() throws Exception {
        existingId = 1L;
        nonExistingId = 10L;
        //precisa simular o comportamento do objeto mockado
        //delete - quando id existe
        Mockito.when(repository.existsById(existingId)).thenReturn(true);
        //delete - quando id não existe
        Mockito.when(repository.existsById(nonExistingId)).thenReturn(false);
        //delete - primeiro caso - deleta
        // não faça nada (void) quando ...
        Mockito.doNothing().when(repository).deleteById(existingId);
        //Próximos testes
        pagamento = Factory.createPagamento();
        pagamentoDTO = new PagamentoDTO(pagamento);
        //Simulação do pagamento
        // findById
        Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(pagamento));
        Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
        // insert
        Mockito.when(repository.save(any())).thenReturn(pagamento);
        // Update -primeiro caso - ID existe
        Mockito.when(repository.getReferenceById(existingId)).thenReturn(pagamento);
        // Update -segundo caso - ID inexistente
        Mockito.when(repository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);

    }

    @Test
    @DisplayName("delete Deveria não fazer nada quando Id existe")
    public void deleteShouldDoNothingWhenIdExists() {
        // no service, delete é do tipo void
        Assertions.assertDoesNotThrow(
                () -> {
                    service.delete(existingId);
                }
        );
    }

    @Test
    @DisplayName("delete Deveria não fazer nada quando Id existe")
    public void deleteShouldThrowResourceNotFoundExceptionWhenItDoesNotExist() {
        // no service, delete é do tipo void
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> {
                    service.delete(nonExistingId);
                }
        );
    }

    @Test
    public void findByIdShouldReturnPagamentoDTOWhenIdExists() {
        // no service, delete é do tipo void
        PagamentoDTO result = service.findById(existingId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), existingId);
        Assertions.assertEquals(result.getValor(), pagamento.getValor());
    }

    @Test
    public void findByIdShouldReturnResourceNotFoundExceptionWhenIdDoesNotExists() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(nonExistingId);
        });
    }

    @Test
    public void insertShuldReturnPagamentoDTO() {

        PagamentoDTO result = service.insert(pagamentoDTO);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), pagamento.getId());

    }


}











