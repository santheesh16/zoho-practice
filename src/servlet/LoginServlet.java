package com.zoho.booktickets.servlet;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.zoho.booktickets.jaas.JAASCallbackHandler;
import com.zoho.booktickets.jaas.JAASRolePrincipal;
import com.zoho.booktickets.jsonutil.JsonUtil;
import com.zoho.booktickets.model.Movie;
import com.zoho.booktickets.model.User;
import com.zoho.booktickets.service.MovieService;
import com.zoho.booktickets.service.UserService;
import com.zoho.booktickets.service.ADService;

public class LoginServlet extends HttpServlet {
    String log4jConfPath = "C:/Program Files/Apache Software Foundation/Tomcat 9.0/webapps/BookMyTicket-1/resources/log4j.properties";

    private static final Logger LOGGER = Logger.getLogger(JAASCallbackHandler.class);
    static Random random = new Random();
    static String generatedOTP;

    public void doPost(HttpServletRequest req, HttpServletResponse res) {
        String OTP = req.getParameter("otp");
        try {
            HttpSession session = req.getSession();
            if (generatedOTP.equals(OTP)) {
                List<Movie> movies = new MovieService().readAll();
                session.setAttribute("movies", movies);
                res.sendRedirect("movies.jsp");
            } else {
                res.getWriter().append("Please enter otp correctly");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res) {
        HttpSession session = req.getSession();
        User user = null;
        PropertyConfigurator.configure(log4jConfPath);
        LoginContext lc = null;
        try {

            String name = req.getParameter("name");
            String mobNo = req.getParameter("mobNo");
            String emailId = req.getParameter("emailId");
            String sex = req.getParameter("sex");
            user = new User(name, mobNo, emailId, sex);
            long userId = new UserService().checkMovieOrAdd(user);
            user.setUserId(userId);
            
            if(new ADService().authenticate(name,"Zoho@2022")){
                lc = new LoginContext("bookmyticket", new JAASCallbackHandler(emailId, name));
                lc.login();
                Subject subject = lc.getSubject();
                Set<Principal> principals = subject.getPrincipals();
                List<Principal> list = new ArrayList<Principal>(principals);
                JAASRolePrincipal role = ((JAASRolePrincipal) list.get(2));
                System.out.println(role.getName().getClass());

                LOGGER.info("established new logincontext");
                if (role.getName().equals("admin")) {
                    session.setAttribute("admin", JsonUtil.objectToString(user));
                    res.sendRedirect("board.jsp");
                } else {
                    session.setAttribute("user", JsonUtil.objectToString(user));
                    generatedOTP = String.format("%04d", random.nextInt(10000));
                    // new EmailService().sendOTPMail(user.getEmailId(), generatedOTP);
                    // SmsService.sendSms("BookMyTicket OTP is "+generatedOTP, mobNo);
                    System.out.println(generatedOTP);
                    res.sendRedirect("otpverify.jsp");
                }
            }else{
                res.sendRedirect("index.jsp");
            }

        } catch (LoginException e) {
            try {
                res.sendRedirect("index.jsp");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            LOGGER.error("Authentication failed " + e);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }
}