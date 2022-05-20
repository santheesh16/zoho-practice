package com.zoho.booktickets.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;

import javax.net.ssl.HttpsURLConnection;

import com.zoho.booktickets.model.Booking;
import com.zoho.booktickets.model.Movie;
import com.zoho.booktickets.model.Theater;

public class SmsService {
	
	public static void sendSms(String message,String number)
	{
		try
		{
		String apiKey="Ty6RgaIiEDtxEAjWCsYG2LmgY0R0IXo6b5nh8PCwjsNEFvca7SupIoveXGiN";
		String sendId="FSTSMS";
		message=URLEncoder.encode(message, "UTF-8");
		String language="english";
		String route="p";
		String myUrl="https://www.fast2sms.com/dev/bulk?authorization="+apiKey+"&sender_id="+sendId+"&message="+message+"&language="+language+"&route="+route+"&numbers="+number;
		URL url=new URL(myUrl);
		HttpsURLConnection con=(HttpsURLConnection)url.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", "Mozilla/5.0");
		con.setRequestProperty("cache-control", "no-cache");
		System.out.println("Wait..............");
		int code=con.getResponseCode();
		System.out.println("Response code : "+code);
		StringBuffer response=new StringBuffer();
		BufferedReader br=new BufferedReader(new InputStreamReader(con.getInputStream()));
		while(true)
		{
			String line=br.readLine();
			if(line==null)
			{
				break;
			}
			response.append(line);
		}
		System.out.println(response);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    public void  bookTickets(long bookingId, String mobNo){
        Booking booking = new BookingService().read(bookingId);
        Theater theater = new TheaterService().read(booking.getTheaterId());
        Movie movie = new MovieService().read(booking.getMovieId());
        
        String booked = new StringBuilder("Tickets Booked!\n")
        .append("Here is your Booking ID - ").append(String.valueOf(bookingId))
        .append(" for "+movie.getMovieName()).append(" on "+ booking.getShowDateTime())
        .append(" at"+ theater.getTheaterName()+",").append(theater.getAddress()+",")
        .append("Seats: -"+ Arrays.toString(booking.getNoOfBookedSeats()).replace("[","").replace("]",""))
        .append("(SCREEN-"+booking.getScreenId()+")").toString();
         SmsService.sendSms(booked, mobNo);
    }
    
    public static void cancelTickets(long bookingId, String mobNo){
        Booking booking = new BookingService().read(bookingId);
        Theater theater = new TheaterService().read(booking.getTheaterId());
        Movie movie = new MovieService().read(booking.getMovieId());
        
        String booked = new  StringBuilder("Tickets Cancelled!\n")
        .append("Booking ID - ").append(String.valueOf(bookingId))
        .append(" for "+movie.getMovieName()).append(" on "+ booking.getShowDateTime())
        .append(" at"+ theater.getTheaterName()+",").append(theater.getAddress()+",")
        .append("Seats: -"+ Arrays.toString(booking.getNoOfBookedSeats()).replace("[","").replace("]",""))
        .append("(SCREEN-"+booking.getScreenId()+")").toString();
         SmsService.sendSms(booked, mobNo);
    }
    
	public static void main(String[] args) {
		System.out.println("Program started.....");
	}
}
