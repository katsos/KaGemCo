/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.admin;

import database.Database;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.Manager;


public class GetManagersList extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    protected PrintWriter out = null;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ArrayList<Manager> managers;
        managers = Database.getManagers();
        
        response.setContentType("text/html;charset=UTF-8");
        out = response.getWriter();
        
        try {
            for ( Manager manager : managers ) 
                addManagerRow(manager);
        } finally {
            out.close();
        }
    }
    
    private void addManagerRow( Manager manager ) {

        out.write("<tr>");
            out.write("<td>" + manager.getUsername() + "</td>\n");
            out.write("<td>" + manager.getRole() + "</td>\n");
            out.write("<td>" + manager.getDate() + "</td>\n");
            out.write("<td>" + 
                        "<a href='#' onClick=\"onEditUser(\""+ manager.getUsername() +"\"); return false;\"" + '>' +
                                "Επεξεργασία </a> </td>");
            out.write("<td>" + "<a href='#' onClick='onDeleteUser(\""+ manager.getUsername() +"\"); return false;' > Διαγραφή </a> </td>");
        out.write("</tr>");
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
