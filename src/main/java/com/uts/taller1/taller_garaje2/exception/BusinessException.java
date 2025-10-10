package com.uts.taller1.taller_garaje2.exception;

/**
 * Excepción personalizada para errores de negocio en el sistema de garaje.
 * Se utiliza para validar reglas de negocio en la capa de Fachada.
 */
public class BusinessException extends Exception {
    public BusinessException(String message) {
        super(message);
    }
}