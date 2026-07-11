package com.acme.legacy.web;

import com.acme.legacy.infrastructure.LegacyConfig;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SystemStatusServlet extends HttpServlet {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LegacyConfig config = (LegacyConfig) getServletContext().getAttribute(LegacyApplicationListener.CONFIG_KEY);
        PrintWriter writer = response.getWriter();

        response.setContentType("text/html;charset=UTF-8");

        writer.println("<html>");
        writer.println("<head><title>System Status</title></head>");
        writer.println("<body bgcolor=\"#FFFFFF\" text=\"#000000\">");
        writer.println("<font face=\"Courier New, Courier, monospace\" size=\"2\">");
        writer.println("<h3>ACME CUSTOMER LEDGER STATUS PANEL</h3>");
        writer.println("<pre>");
        writer.println("SYSTEM CODE     : " + config.getSystemCode());
        writer.println("CURRENT OPERATOR: " + config.getSystemOperator());
        writer.println("DEFAULT REGION  : " + config.getDefaultRegion());
        writer.println("DATABASE URL    : " + config.getDatabaseUrl());
        writer.println("SESSION ID      : " + request.getSession(true).getId());
        writer.println("REMOTE ADDRESS  : " + request.getRemoteAddr());
        writer.println("SERVER TIME     : " + dateFormat.format(new Date()));
        writer.println("</pre>");
        writer.println("</font>");
        writer.println("</body>");
        writer.println("</html>");
    }
}
