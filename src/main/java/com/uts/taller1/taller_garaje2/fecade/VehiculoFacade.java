package com.uts.taller1.taller_garaje2.fecade;

import com.uts.taller1.taller_garaje2.modelo.Vehiculo;
import com.uts.taller1.taller_garaje2.persistence.VehiculoDAO;
import com.uts.taller1.taller_garaje2.exception.BusinessException;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Fachada para operaciones sobre vehículos.
 * Incluye validaciones de reglas de negocio antes de llamar al DAO.
 * Lanza BusinessException si una regla es violada.
 */
public class VehiculoFacade {
    private final VehiculoDAO dao = new VehiculoDAO();

    private static final Set<String> COLORES_PERMITIDOS = new HashSet<>(Arrays.asList("Rojo", "Blanco", "Negro", "Azul", "Gris"));
    private static final int ANO_ACTUAL = 2025;

    /**
     * Lista todos los vehículos.
     * @return Lista de Vehiculo.
     * @throws SQLException si error de BD.
     */
    public List<Vehiculo> listar() throws SQLException {
        return dao.listar();
    }

    /**
     * Busca vehículo por id.
     * @param id ID del vehículo.
     * @return Vehiculo o null.
     * @throws SQLException si error de BD.
     */
    public Vehiculo buscarPorId(int id) throws SQLException {
        return dao.buscarPorId(id);
    }

    /**
     * Agrega un vehículo validando reglas de negocio.
     * Reglas:
     * - No permitir placa duplicada.
     * - No aceptar propietario vacío o con menos de 5 caracteres.
     * - Marca, modelo y placa deben tener al menos 3 caracteres.
     * - Color solo acepta valores de la lista predefinida.
     * - No aceptar vehículos con modelo de más de 20 años de antigüedad.
     * - Simular notificación si marca es "Ferrari".
     * @param v Vehiculo a agregar.
     * @throws BusinessException si regla violada.
     * @throws SQLException si error de BD.
     */
    public void agregar(Vehiculo v) throws BusinessException, SQLException {
        validarCamposComunes(v);
        validarColor(v.getColor());
        validarModeloAno(v.getModelo());

        if (dao.existePlaca(v.getPlaca())) {
            throw new BusinessException("No se permite agregar un vehículo con placa duplicada.");
        }
        dao.agregar(v);
        if ("Ferrari".equalsIgnoreCase(v.getMarca())) {
            System.out.println("Notificación simulada: Nuevo Ferrari agregado.");
        }
    }

    /**
     * Actualiza un vehículo validando reglas.
     * Regla adicional: Solo si el vehículo existe.
     * @param v Vehiculo a actualizar.
     * @throws BusinessException si regla violada.
     * @throws SQLException si error de BD.
     */
    public void actualizar(Vehiculo v) throws BusinessException, SQLException {
        validarCamposComunes(v);
        validarColor(v.getColor());
        validarModeloAno(v.getModelo());

        if (dao.buscarPorId(v.getId()) == null) {
            throw new BusinessException("No se puede actualizar un vehículo que no existe.");
        }
        dao.actualizar(v);
    }

    /**
     * Elimina un vehículo por id.
     * Regla: No si propietario es "Administrador".
     * @param id ID a eliminar.
     * @throws BusinessException si regla violada.
     * @throws SQLException si error de BD.
     */
    public void eliminar(int id) throws BusinessException, SQLException {
        Vehiculo v = dao.buscarPorId(id);
        if (v == null) {
            throw new BusinessException("No se puede eliminar un vehículo que no existe.");
        }
        if ("Administrador".equalsIgnoreCase(v.getPropietario())) {
            throw new BusinessException("No se puede eliminar un vehículo con propietario 'Administrador'.");
        }
        dao.eliminar(id);
    }

    // Métodos privados para validaciones
    private void validarCamposComunes(Vehiculo v) throws BusinessException {
        if (v.getPlaca() == null || v.getPlaca().length() < 3) {
            throw new BusinessException("Placa debe tener al menos 3 caracteres.");
        }
        if (v.getMarca() == null || v.getMarca().length() < 3) {
            throw new BusinessException("Marca debe tener al menos 3 caracteres.");
        }
        if (v.getModelo() == null || v.getModelo().length() < 3) {
            throw new BusinessException("Modelo debe tener al menos 3 caracteres.");
        }
        if (v.getPropietario() == null || v.getPropietario().isEmpty() || v.getPropietario().length() < 5) {
            throw new BusinessException("Propietario no puede estar vacío y debe tener al menos 5 caracteres.");
        }
    }

    private void validarColor(String color) throws BusinessException {
        if (color != null && !COLORES_PERMITIDOS.contains(color)) {
            throw new BusinessException("Color debe ser: Rojo, Blanco, Negro, Azul o Gris.");
        }
    }

    private void validarModeloAno(String modelo) throws BusinessException {
        try {
            int ano = Integer.parseInt(modelo);
            if (ano < ANO_ACTUAL - 20) {
                throw new BusinessException("Modelo no puede tener más de 20 años de antigüedad.");
            }
        } catch (NumberFormatException e) {
            throw new BusinessException("Modelo debe ser un año numérico válido.");
        }
    }
}