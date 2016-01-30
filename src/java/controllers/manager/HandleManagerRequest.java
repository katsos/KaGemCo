package controllers.manager;

import database.Database;
import java.io.IOException;
import java.sql.SQLException;
import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utils.WebUtils;

/**
 * Changes the status of the manager request to either accepted or rejected,
 * given two obligatory URL parameters: requestID and status. If at least one of
 * those parameters are not passed, then an error report is returned as a
 * response, describing the undefined parameters.
 */
@WebServlet(name = "HandleManagerRequest", urlPatterns = {"/HandleManagerRequest"})
public class HandleManagerRequest extends HttpServlet {

    private HttpServletRequest request;
    private String requestIDParam;
    private Long requestID;
    private String status;
    private JsonObject json;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        this.request = request;

        try {

            checkParameters();

            updateDatabase();
            
            buildJson();
            
        } catch (Exception ex) {
            json = Json.createObjectBuilder()
                    .add("error", ex.toString())
                .build();
        } finally {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().print(json);
        }

    }

    private void checkParameters() throws Exception {

        requestIDParam = request.getParameter("requestID");
        status = request.getParameter("status");

        try {
            requestID = Long.valueOf(requestIDParam);

            if (status != null && !status.equals("rejected") && !status.equals("accepted")) {
                throw new Exception("Status format is wrong.");
            }

        } catch (NumberFormatException ex) {
            throw new Exception("RequestID format is wrong.");
        }

    }

    private void updateDatabase() throws Exception {

        String managerUsername = WebUtils.getCookie("username", request).getValue();
        Database.updateManagerRequest(requestID, null, managerUsername, null, null, null, status, null);

    }

    private void buildJson() {

        json = Json.createObjectBuilder()
                .add("error", "")
                .build();

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
