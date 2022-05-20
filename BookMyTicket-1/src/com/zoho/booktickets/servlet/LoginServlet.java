package com.zoho.booktickets.servlet;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.auth.jaas.JAASCallbackHandler;
import com.zoho.booktickets.jsonutil.JsonUtil;
import com.zoho.booktickets.jsonutil.PasswordUtils;
import com.zoho.booktickets.model.ADUser;
import com.zoho.booktickets.model.Movie;
import com.zoho.booktickets.model.User;
import com.zoho.booktickets.service.ADService;
import com.zoho.booktickets.service.EmailService;
import com.zoho.booktickets.service.JedisService;
import com.zoho.booktickets.service.MovieService;
import com.zoho.booktickets.service.SmsService;
import com.zoho.booktickets.service.UserService;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class LoginServlet extends HttpServlet {
    String log4jConfPath = "";
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
            String password = req.getParameter("password");
            user = new User(name, mobNo, emailId, password);
            long userId = new UserService().checkMovieOrAdd(user);
            user.setUserId(userId);
            String userPassCache = new JedisService().checkUserCacheValue(name);
            if (userPassCache != null) {
                Boolean resultPass = password.equals(PasswordUtils.verifyPassword(password , userPassCache));
                System.out.println(resultPass);
                if (resultPass) {
                    if (name.equals("Santheesh")) {
                        session.setAttribute("admin", JsonUtil.objectToString(user));
                        res.sendRedirect("board.jsp");
                    } else {
                        
                        generatedOTP = String.format("%04d", random.nextInt(10000));
                        new EmailService().sendOTPMail(user.getEmailId(), generatedOTP);
                        SmsService.sendSms("BookMyTicket OTP is " + generatedOTP, mobNo);
                        System.out.println(generatedOTP);
                        session.setAttribute("user", JsonUtil.objectToString(user));
                        res.sendRedirect("otpverify.jsp");
                    }
                    System.out.println("Redis login");

                } else {
                    session.setAttribute("error", "Please!! Enter the correct password ");
                    res.sendRedirect("index.jsp");
                }

            } else if (new ADService().authenticate(name, password)) {
                if (name.equals("Santheesh")) {

                    new JedisService().setUserCache(new ADUser(name, PasswordUtils.getHashString(password)));
                    session.setAttribute("admin", JsonUtil.objectToString(user));
                    res.sendRedirect("board.jsp");
                } else {
                    new JedisService().setUserCache(new ADUser(name, PasswordUtils.getHashString(password)));
                    generatedOTP = String.format("%04d", random.nextInt(10000));
                    new EmailService().sendOTPMail(user.getEmailId(), generatedOTP);
                    SmsService.sendSms("BookMyTicket OTP is " + generatedOTP, mobNo);
                    System.out.println(generatedOTP);
                    session.setAttribute("user", JsonUtil.objectToString(user));
                    res.sendRedirect("otpverify.jsp");

                }
                System.out.println("AD Authenticate login");

            } else {
                session.setAttribute("error", "Please!! Enter the correct password ");
                res.sendRedirect("index.jsp");
            }

            // if (new ADService().authenticate(name, password)) {
            // session.setAttribute("user", JsonUtil.objectToString(user));

            // res.sendRedirect("board.jsp");
            // lc = new LoginContext("bookmyticket", new JAASCallbackHandler(emailId,
            // name));
            // lc.login();
            // Subject subject = lc.getSubject();
            // Set<Principal> principals = subject.getPrincipals();
            // List<Principal> list = new ArrayList<Principal>(principals);
            // JAASRolePrincipal role = ((JAASRolePrincipal) list.get(2));
            // System.out.println(role.getName().getClass());

            // LOGGER.info("established new logincontext");
            // if (role.getName().equals("admin")) {

            // } else {
            // session.setAttribute("user", JsonUtil.objectToString(user));
            // generatedOTP = String.format("%04d", random.nextInt(10000));
            // // new EmailService().sendOTPMail(user.getEmailId(), generatedOTP);
            // // SmsService.sendSms("BookMyTicket OTP is "+generatedOTP, mobNo);
            // System.out.println(generatedOTP);
            // res.sendRedirect("otpverify.jsp");
            // }
            // } else {
            // res.sendRedirect("index.jsp");
            // }

        } catch (LoginException e) {
            try {
                res.sendRedirect("index.jsp");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            LOGGER.error("Authentication failed " + e);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}