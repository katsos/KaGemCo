package controllers.admin;

import database.Database;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.User;

public class GetUsersList extends HttpServlet {

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

        ArrayList<User> users;
        users = Database.getUsers();
        
        response.setContentType("text/html;charset=UTF-8");
        out = response.getWriter();
        
        try {
            for ( User user : users ) 
                addUserRow(user);
        } finally {
            out.close();
        }
    }
    
    private void addUserRow( User user ) {

        out.write("<tr>");
            out.write("<td>" + user.getUsername() + "</td>\n");
            out.write("<td>" + user.getRole() + "</td>\n");
            out.write("<td>" + user.getDate() + "</td>\n");
            out.write("<td>" + 
                        "<a href='#' onClick=\"onEditUser(\""+ user.getUsername() +"\"); return false;\"" + '>' +
                                "Επεξεργασία </a> </td>");
            out.write("<td>" + "<a href='#' onClick='onDeleteUser(\""+ user.getUsername() +"\"); return false;' > Διαγραφή </a> </td>");
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