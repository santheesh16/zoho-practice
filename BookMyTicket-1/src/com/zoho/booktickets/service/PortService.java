package com.zoho.booktickets.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServlet;

public class PortService extends HttpServlet {
    
	public void post(String completeUrl, String body) throws Exception {
        URL url = new URL (completeUrl);
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        System.out.println(con);
        System.out.println("Tomcat 1");
        con.setRequestMethod("POST");
		con.setDoOutput(true);
		OutputStream os = con.getOutputStream();
		os.write(body.getBytes("UTF-8"));
		os.flush();
		os.close();
        
        int responseCode = con.getResponseCode();
		System.out.println(con);
        System.out.println("POST Response Code :: " + responseCode);

		if (responseCode == HttpURLConnection.HTTP_OK) { //success
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			System.out.println(response.toString());
		} else {
			System.out.println("POST request not worked");
		}
    }
    
    
    public void update(String completeUrl, String body) throws Exception {
        URL url = new URL (completeUrl);
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        System.out.println(con);
        System.out.println("Tomcat Update");
        con.setRequestMethod("PUT");
		con.setDoOutput(true);// For POST only - START
		OutputStream os = con.getOutputStream();
		os.write(body.getBytes("UTF-8"));
		os.flush();
		os.close();
        
        int responseCode = con.getResponseCode();
		System.out.println(con);
        System.out.println("Put Response Code :: " + responseCode);

		if (responseCode == HttpURLConnection.HTTP_OK) { //success
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// print result
			System.out.println(response.toString());
		} else {
			System.out.println("Put request not worked");
		}
    }
    
    public void delete(String completeUrl) throws Exception {
        URL url = new URL (completeUrl);
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        System.out.println(con);
        System.out.println("Tomcat Delete");
        con.setRequestMethod("DELETE");
		con.setDoOutput(true);// For POST only - START

        OutputStream os = con.getOutputStream();
        DataOutputStream dos = new DataOutputStream(os); 
        dos.writeChars("id"); 
        dos.flush(); 
        dos.close();
		os.flush();
		os.close();
        
        int responseCode = con.getResponseCode();
		System.out.println(con);
        System.out.println("DELETE Response Code :: " + responseCode);

		if (responseCode == HttpURLConnection.HTTP_OK) { 
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// print result
			System.out.println(response.toString());
		} else {
			System.out.println("Delete request not worked");
		}
    }
}
