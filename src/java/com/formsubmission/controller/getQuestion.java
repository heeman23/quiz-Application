/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.formsubmission.controller;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author srv
 */
public class getQuestion extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    Connection conn = new dbConnection().getConnectionInstance();
    ResultSet rst = null;
    PreparedStatement pstmt = null;
    Map<Integer, Map<String, String>> outerMap = new LinkedHashMap<Integer, Map<String, String>>();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        System.out.println("getQuestion class");
        try {
            String flag = request.getParameter("flag");
            if (flag.equals("G")) {
                System.out.println("into If");
                String category = request.getParameter("category");
                String getQuestionData = "select question,option1,option2,option3,option4,answer from testdata where questioncategory=? and ROWNUM <= 10 order by Dbms_Random.Random";
                pstmt = conn.prepareStatement(getQuestionData);
                pstmt.setString(1, category);
                System.out.println("getQuestionData-->" + getQuestionData);
                rst = pstmt.executeQuery();
                int i = 1;
                while (rst.next()) {
                    Map<String, String> innermap = new LinkedHashMap<String, String>();
                    innermap.put("question", rst.getString(1));
                    innermap.put("option1", rst.getString(2));
                    innermap.put("option2", rst.getString(3));
                    innermap.put("option3", rst.getString(4));
                    innermap.put("option4", rst.getString(5));
                    innermap.put("answer", rst.getString(6));

                    outerMap.put(i, innermap);
                    i++;

                }
                String finalMap = new Gson().toJson(outerMap);
                System.out.println("finalMap-->" + finalMap);
                out.write(finalMap);
            }
        } catch (Exception e) {

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
