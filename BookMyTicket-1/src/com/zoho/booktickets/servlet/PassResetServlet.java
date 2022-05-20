package com.zoho.booktickets.servlet;

import java.time.ZoneId;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.duosecurity.duoweb.DuoWeb;
import com.zoho.booktickets.constant.Constant;
import com.zoho.booktickets.service.ADService;
import com.zoho.booktickets.service.JwtService;

import io.jsonwebtoken.Claims;

public class PassResetServlet extends HttpServlet {

    public void doPost(HttpServletRequest req, HttpServletResponse res) {
        HttpSession session = req.getSession();
        String userName = session.getAttribute("userName").toString();
        String newPassword = req.getParameter("newPassword");
        String confirmPassword = req.getParameter("confirmPassword");
        System.out.println(newPassword);
        try {
            if (newPassword.equals(confirmPassword)) {
                if (new ADService().updateUserPassword(userName, confirmPassword)) {
                    session.setAttribute("message", "Password successfully updated");
                    res.sendRedirect("reset.jsp");
                } else {
                    session.setAttribute("message", "Password Failed to update");
                    res.sendRedirect("reset.jsp");
                }
            } else {
                session.setAttribute("message", "Please enter new & confirm passwords same");
                res.sendRedirect("reset.jsp");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void doGet(HttpServletRequest req, HttpServletResponse res) {
        HttpSession session = req.getSession();
        String token = req.getParameter("token");
        String sigReq = DuoWeb.signRequest(Constant.I_KEY, Constant.S_KEY, Constant.A_KEY, "santheeesh62");
        System.out.println(sigReq);
        session.setAttribute("sigRequest", sigReq);
        System.out.println(token.isEmpty());
        try {
            if (!token.isEmpty()) {
                Claims claims = new JwtService().decodeJWT(token);
                session.setAttribute("userName", claims.getIssuer());
                System.out.println(new JwtService()
                        .expiryMin(claims.getExpiration().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));
                res.sendRedirect("reset.jsp");
            } else {
                res.sendRedirect("index.jsp");
            }
        } catch (Exception e) {
            try {
                res.sendRedirect("reset.jsp");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }
}