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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Session;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author srv
 */
public class login extends HttpServlet {

    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rst = null;
    String loginFlag = "";
    Session s = null;
    List<String> list = new ArrayList<String>();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            conn = new dbConnection().getConnectionInstance();
            System.out.println("conn" + conn);
            String flag = request.getParameter("flag");
            if (flag.equals("S")) {
                String username = request.getParameter("username");
                String email = request.getParameter("email");
                String password = request.getParameter("password");
                String DOB = request.getParameter("DOB");
                String contact = request.getParameter("contact");
                String gender = request.getParameter("gender");
                String securityQuestion = request.getParameter("securityQuestion");
                String securityAnswer = request.getParameter("securityAnswer");
                String insertuserData = "insert into userdata_test(BIRTHDAY,CONTACT,FLAG,GENDER,SECURITYANSWER,SECURITYQUESTION,USERMAIL,USERNAME,USERPASSWORD) values(?,?,?,?,?,?,?,?,?)";
                pstmt = conn.prepareStatement(insertuserData);
                pstmt.setString(1, DOB);
                pstmt.setString(2, contact);
                pstmt.setString(3, "L");
                pstmt.setString(4, gender);
                pstmt.setString(5, securityAnswer);
                pstmt.setString(6, securityQuestion);
                pstmt.setString(7, email);
                pstmt.setString(8, username);
                pstmt.setString(9, password);
                System.out.println("insertuserData" + insertuserData);
                pstmt.executeUpdate();
                SendAttachmentInMail.sendMail(email);
                out.write("data saved successfully and check your mail for further varification");

            } else if (flag.equals("L")) {
                String sessionId = "";
                String jsonstring = "";
                HttpSession session = request.getSession();
                String email = request.getParameter("email");
                String password = request.getParameter("password");
                System.out.println("email-->" + email);
                System.out.println("password-->" + password);

                String FetchuserDetails = "select flag from userdata_test where usermail=? and userpassword=?";
                pstmt = conn.prepareStatement(FetchuserDetails);
                pstmt.setString(1, email);
                pstmt.setString(2, password);
                System.out.println("FetchuserDetails" + FetchuserDetails);
                rst = pstmt.executeQuery();

                if (rst.next()) {
                    System.out.println("into rst if");
                    loginFlag = rst.getString(1);

                    session.setAttribute("name", email);
                    //session.setMaxInactiveInterval(60);
                    sessionId = session.getId();
                    System.out.println("session instance" + sessionId);
                    list.add(session.getId());

                    list.add(loginFlag);
                    if (!"".equals(sessionId)) {

                        jsonstring = new Gson().toJson(list);
                        System.out.println("jsonstring--->" + jsonstring);
                        System.out.println("list---->" + list);

                    } else {
                        jsonstring = "";

                    }
                    out.write(jsonstring);
                } else {
                    list.add("");
                    list.add("M");
                    out.write(new Gson().toJson(list));
                }

            }
        } catch (Exception e) {
            e.getMessage();

        } finally {
            conn.close();
            list.clear();
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
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(login.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(login.class.getName()).log(Level.SEVERE, null, ex);
        }
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
