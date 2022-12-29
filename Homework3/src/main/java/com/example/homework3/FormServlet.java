package com.example.homework3;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "FormServlet", value = "/form")
public class FormServlet extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String javaAnswer = request.getParameter("java");
        String pythonAnswer = request.getParameter("python");

        HttpSession session = request.getSession(true);
        session.setAttribute("java", javaAnswer);
        session.setAttribute("python", pythonAnswer);
        session.setAttribute("page", "answer");

        response.sendRedirect("index.jsp");
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String page = request.getParameter("page");
        HttpSession session = request.getSession(true);
        session.setAttribute("page", "form");

        response.sendRedirect("index.jsp");
    }

    public void destroy() {
    }
}