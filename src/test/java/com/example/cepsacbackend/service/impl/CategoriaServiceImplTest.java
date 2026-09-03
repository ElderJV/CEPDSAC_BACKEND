package com.example.cepsacbackend.service.impl;

import com.example.cepsacbackend.dto.Categoria.CategoriaResponseDTO;
import com.example.cepsacbackend.exception.ResourceNotFoundException;
import com.example.cepsacbackend.mapper.CategoriaMapper;
import com.example.cepsacbackend.model.Categoria;
import com.example.cepsacbackend.repository.CategoriaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceImplTest {

    @Mock
    private CategoriaRepository repository;

    @Mock
    private CategoriaMapper mapper;

    private CategoriaServiceImpl categoriaService;

    @BeforeEach
    void setUp() {
        categoriaService = new CategoriaServiceImpl(repository, mapper);
    }

    @Test
    void findByEstadoTrueMapeaCategoriasActivas() {
        List<Categoria> categorias = List.of(new Categoria((short) 1, "Tecnologia", "Cursos tech", true));
        List<CategoriaResponseDTO> response = List.of(new CategoriaResponseDTO());
        when(repository.findByEstadoTrue()).thenReturn(categorias);
        when(mapper.toResponseDtoList(categorias)).thenReturn(response);

        List<CategoriaResponseDTO> resultado = categoriaService.findByEstadoTrue();

        assertSame(response, resultado);
    }

    @Test
    void obtenerPorIdLanzaExcepcionCuandoNoExiste() {
        when(repository.findById((short) 5)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> categoriaService.obtenerPorId((short) 5));

        assertEquals("No se encontró la categoría con ID 5. Verifique que el ID sea correcto.", exception.getMessage());
    }

    @Test
    void cambiarEstadoInvierteEstadoActual() {
        Categoria categoria = new Categoria((short) 1, "Gestion", "Cursos de gestion", true);
        when(repository.findById((short) 1)).thenReturn(Optional.of(categoria));

        categoriaService.cambiarEstado((short) 1);

        assertFalse(categoria.getEstado());
        verify(repository).save(categoria);
    }
}
