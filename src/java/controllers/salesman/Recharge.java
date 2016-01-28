/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.salesman;

import database.Database;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.json.Json;
import javax.json.JsonWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.Transaction;
import utils.JsonUtils;

/**
 * Recharges the account given the phone number and the recharge amount as URL
 * parameters. Both parameters are necessary for the task to be completed. An
 * error is reported as a response if insufficient parameters are passed.
 *
 * @author pgmank
 */
@WebServlet(name = "Recharge", urlPatterns = {"/Recharge"})
public class Recharge extends HttpServlet {

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

        String phoneNumberParam = request.getParameter("phoneNumber");
        String rechargeAmountParam = request.getParameter("rechargeAmount");
        String errorMessage = "";

        // JSON Output Initialization
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        JsonWriter jsonWriter = Json.createWriter(out);

        
        Long phoneNumber = null;
        if (phoneNumberParam != null) {
            try {
                phoneNumber = Long.valueOf(phoneNumberParam);
            } catch (NumberFormatException e) {
                errorMessage += "Phone number must be an integer number"
                        + JsonUtils.ERR_DLM;
                phoneNumber = -1L;	// dummie value to flag invalid phoneNumber
            }
        }

        
        Double rechargeAmount = null;
        if (rechargeAmountParam != null) {
            try {
                rechargeAmount = Double.valueOf(rechargeAmountParam);
            } catch (NumberFormatException e) {
                errorMessage += "Balance must be a decimal number"
                        + JsonUtils.ERR_DLM;
                rechargeAmount = -1D;	// dummie value to flag invalid rechargeAmount
            }
        }

        // Construct error message describing the undefined parameters
        String undefinedParameters = "";
        if (phoneNumber == null) {
            undefinedParameters += "phoneNumber, ";
        }
        if (rechargeAmount == null) {
            undefinedParameters += "rechargeAmount, ";
        }

        // Check for undefined Parameters
        if (!undefinedParameters.isEmpty()) {
            // Remove extra space and comma from the end of the string
            undefinedParameters = undefinedParameters.substring(0, undefinedParameters.length() - 2);
            errorMessage += "Parameters: " + undefinedParameters + " are undefined";
            JsonUtils.outputJsonError(errorMessage, jsonWriter);
            return;
        } else if (!errorMessage.isEmpty()) {
            // remove the extra delimeter from the end of the string
            errorMessage = errorMessage.substring(0, errorMessage.length() - 2);
            JsonUtils.outputJsonError(errorMessage, jsonWriter);
            return;
        }

        if (rechargeAmount < 10) {
            JsonUtils.outputJsonError("Initial pre-paid amount must be 10 euros or more", jsonWriter);
            return;
        }

        // Check if account to be updated exists
        if (!Database.accountExists(phoneNumber)) {
            JsonUtils.outputJsonError("Phone number: " + phoneNumber + " does not exist", jsonWriter);
            return;
        }

        try {

            if (Database.rechargeAccount(phoneNumber, rechargeAmount)) {

                // TODO: To perfect the task, make recharge and transaction addition one SQL transaction for security purposes.
                Transaction transaction = new Transaction(phoneNumber, rechargeAmount);
                Database.addTransaction(transaction);

                jsonWriter.writeObject(
                        Json.createObjectBuilder()
                            .add("success", "success")
                        .build());

            }

        } catch (SQLException ex) {
            JsonUtils.outputJsonError(ex.getMessage(), jsonWriter);
        }

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
