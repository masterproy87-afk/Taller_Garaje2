package com.uts.taller1.taller_garaje2.controller;

import com.uts.taller1.taller_garaje2.fecade.VehiculoFacade;
import com.uts.taller1.taller_garaje2.modelo.Vehiculo;
import com.uts.taller1.taller_garaje2.exception.BusinessException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/vehiculos")
public class VehiculoServlet extends HttpServlet {
    private VehiculoFacade facade = new VehiculoFacade();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setAttribute("vehiculos", facade.listar());
            req.getRequestDispatcher("/lista.jsp").forward(req, resp);
        } catch (SQLException e) {
            req.setAttribute("error", "Error técnico: " + e.getMessage());
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Vehiculo v = new Vehiculo();
            v.setPlaca(req.getParameter("placa"));
            v.setMarca(req.getParameter("marca"));
            v.setModelo(req.getParameter("modelo"));
            v.setColor(req.getParameter("color"));
            v.setPropietario(req.getParameter("propietario"));
            facade.agregar(v);
            resp.sendRedirect(req.getContextPath() + "/vehiculos");
        } catch (BusinessException e) {
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
        } catch (SQLException e) {
            req.setAttribute("error", "Error técnico: " + e.getMessage());
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
        }
    }
}