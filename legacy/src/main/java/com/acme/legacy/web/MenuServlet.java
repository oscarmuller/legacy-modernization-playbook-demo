package com.acme.legacy.web;

import com.acme.legacy.infrastructure.LegacyConfig;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MenuServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LegacyConfig config = (LegacyConfig) getServletContext().getAttribute(LegacyApplicationListener.CONFIG_KEY);
        PrintWriter writer = response.getWriter();

        response.setContentType("text/html;charset=UTF-8");

        writer.println("<html>");
        writer.println("<head><title>ACME Customer Ledger Menu</title></head>");
        writer.println("<body bgcolor=\"#003366\" text=\"#FFFFFF\" link=\"#FFFF99\" vlink=\"#FFFF99\" alink=\"#FFFF99\">");
        writer.println("<font face=\"Courier New, Courier, monospace\" size=\"2\">");
        writer.println("<b>" + config.getReportBanner() + "</b>");
        writer.println("&nbsp;&nbsp;&nbsp;");
        writer.println("<a target=\"mainFrame\" href=\"customers\">CUSTOMERS</a>");
        writer.println("&nbsp;|&nbsp;");
        writer.println("<a target=\"mainFrame\" href=\"customers-export.xml\">EXPORT XML</a>");
        writer.println("&nbsp;|&nbsp;");
        writer.println("<a target=\"mainFrame\" href=\"status\">SYSTEM STATUS</a>");
        writer.println("</font>");
        writer.println("</body>");
        writer.println("</html>");
    }
}
