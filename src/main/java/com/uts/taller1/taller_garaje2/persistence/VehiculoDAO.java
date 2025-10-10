
package com.uts.taller1.taller_garaje2.persistence;

import com.uts.taller1.taller_garaje2.modelo.Vehiculo;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la gestión de vehículos en la base de datos.
 * Obtiene el DataSource mediante JNDI y maneja las conexiones.
 */
public class VehiculoDAO {
    private static DataSource dataSource;

    static {
        try {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:comp/env");
            dataSource = (DataSource) envContext.lookup("jdbc/garageDB");
        } catch (NamingException e) {
            throw new RuntimeException("Error al configurar el DataSource en VehiculoDAO", e);
        }
    }

    /**
     * Busca todos los vehículos en la base de datos.
     * @return Lista de Vehiculo o lista vacía.
     * @throws SQLException si hay error de conexión o consulta.
     */
    public List<Vehiculo> listar() throws SQLException {
        List<Vehiculo> lista = new ArrayList<>();
        String sql = "SELECT * FROM vehiculos";
        try (Connection con = dataSource.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Vehiculo v = new Vehiculo(
                    rs.getInt("id"),
                    rs.getString("placa"),
                    rs.getString("marca"),
                    rs.getString("modelo"),
                    rs.getString("color"),
                    rs.getString("propietario")
                );
                lista.add(v);
            }
        } catch (SQLException ex) {
            System.err.println("Error al listar vehículos: " + ex.getMessage());
            throw ex;
        }
        return lista;
    }

    /**
     * Busca un vehículo por ID.
     * @param id ID del vehículo.
     * @return Vehiculo encontrado o null si no existe.
     * @throws SQLException si hay error de conexión o consulta.
     */
    public Vehiculo buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM vehiculos WHERE id = ?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Vehiculo(
                        rs.getInt("id"),
                        rs.getString("placa"),
                        rs.getString("marca"),
                        rs.getString("modelo"),
                        rs.getString("color"),
                        rs.getString("propietario")
                    );
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error al buscar vehículo por id: " + ex.getMessage());
            throw ex;
        }
        return null;
    }

    /**
     * Verifica si ya existe una placa registrada.
     * @param placa placa a buscar
     * @return true si existe, false si no
     * @throws SQLException si hay error de conexión o consulta.
     */
    public boolean existePlaca(String placa) throws SQLException {
        String sql = "SELECT COUNT(*) FROM vehiculos WHERE placa = ?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, placa);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error al verificar placa: " + ex.getMessage());
            throw ex;
        }
        return false;
    }

    /**
     * Agrega un nuevo vehículo si la placa no existe.
     * @param v Vehiculo a agregar
     * @throws SQLException si hay error de conexión o consulta.
     */
    public void agregar(Vehiculo v) throws SQLException {
        String sql = "INSERT INTO vehiculos (placa, marca, modelo, color, propietario) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, v.getPlaca());
            ps.setString(2, v.getMarca());
            ps.setString(3, v.getModelo());
            ps.setString(4, v.getColor());
            ps.setString(5, v.getPropietario());
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Error al agregar vehículo: " + ex.getMessage());
            throw ex;
        }
    }

    /**
     * Actualiza todos los datos de un vehículo existente por id.
     * @param v Vehiculo a actualizar
     * @throws SQLException si hay error de conexión o consulta.
     */
    public void actualizar(Vehiculo v) throws SQLException {
        String sql = "UPDATE vehiculos SET placa = ?, marca = ?, modelo = ?, color = ?, propietario = ? WHERE id = ?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, v.getPlaca());
            ps.setString(2, v.getMarca());
            ps.setString(3, v.getModelo());
            ps.setString(4, v.getColor());
            ps.setString(5, v.getPropietario());
            ps.setInt(6, v.getId());
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Error al actualizar vehículo: " + ex.getMessage());
            throw ex;
        }
    }

    /**
     * Borra un vehículo por id.
     * @param id ID del vehículo a eliminar
     * @throws SQLException si hay error de conexión o consulta.
     */
    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM vehiculos WHERE id = ?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Error al eliminar vehículo: " + ex.getMessage());
            throw ex;
        }
    }
}