package com.example.cepsacbackend.service.impl;

import com.example.cepsacbackend.exception.ConflictException;
import com.example.cepsacbackend.exception.ResourceNotFoundException;
import com.example.cepsacbackend.model.Pais;
import com.example.cepsacbackend.repository.PaisRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaisServiceImplTest {

    @Mock
    private PaisRepository paisRepository;

    private PaisServiceImpl paisService;

    @BeforeEach
    void setUp() {
        paisService = new PaisServiceImpl(paisRepository);
    }

    @Test
    void getPaisByIdRetornaPaisCuandoExiste() {
        Pais pais = new Pais((short) 1, "Peru", "PE", "+51");
        when(paisRepository.findById((short) 1)).thenReturn(Optional.of(pais));

        Pais resultado = paisService.getPaisById((short) 1);

        assertSame(pais, resultado);
    }

    @Test
    void getPaisByIdLanzaExcepcionCuandoNoExiste() {
        when(paisRepository.findById((short) 9)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> paisService.getPaisById((short) 9));

        assertEquals("No se encontró el país con ID 9. Verifique que el ID sea correcto.", exception.getMessage());
    }

    @Test
    void createPaisGuardaCuandoNombreNoExiste() {
        Pais pais = new Pais(null, "Chile", "CL", "+56");
        Pais guardado = new Pais((short) 2, "Chile", "CL", "+56");
        when(paisRepository.findByNombre("Chile")).thenReturn(Optional.empty());
        when(paisRepository.save(pais)).thenReturn(guardado);

        Pais resultado = paisService.createPais(pais);

        assertSame(guardado, resultado);
    }

    @Test
    void createPaisLanzaConflictoCuandoNombreExiste() {
        Pais pais = new Pais(null, "Peru", "PE", "+51");
        when(paisRepository.findByNombre("Peru")).thenReturn(Optional.of(new Pais((short) 1, "Peru", "PE", "+51")));

        assertThrows(ConflictException.class, () -> paisService.createPais(pais));

        verify(paisRepository, never()).save(any());
    }

    @Test
    void updatePaisActualizaNombreCuandoNoHayConflicto() {
        Pais existente = new Pais((short) 1, "Peru", "PE", "+51");
        Pais datos = new Pais(null, "Argentina", "AR", "+54");
        when(paisRepository.findById((short) 1)).thenReturn(Optional.of(existente));
        when(paisRepository.findByNombre("Argentina")).thenReturn(Optional.empty());
        when(paisRepository.save(existente)).thenReturn(existente);

        Pais resultado = paisService.updatePais((short) 1, datos);

        assertEquals("Argentina", resultado.getNombre());
        verify(paisRepository).save(existente);
    }
}
