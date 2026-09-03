package com.example.cepsacbackend.exception;

import com.example.cepsacbackend.dto.Error.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/api/paises/1");
    }

    @Test
    void handleResourceNotFoundExceptionRetornaNotFound() {
        ResponseEntity<ErrorResponseDTO> response = handler.handleResourceNotFoundException(
                new ResourceNotFoundException("No encontrado"), request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().getStatus());
        assertEquals("No encontrado", response.getBody().getMessage());
        assertEquals("/api/paises/1", response.getBody().getPath());
    }

    @Test
    void handleAccessDeniedExceptionRetornaForbidden() {
        ResponseEntity<ErrorResponseDTO> response = handler.handleAccessDeniedException(
                new AccessDeniedException("Sin acceso"), request);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(403, response.getBody().getStatus());
        assertEquals("Acceso denegado. No tienes los permisos necesarios para realizar esta acción.", response.getBody().getMessage());
    }
}
