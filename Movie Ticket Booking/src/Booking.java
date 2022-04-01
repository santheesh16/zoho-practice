package com.zoho.booktickets.model;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class Booking {
    long bookingId;
    long userId;
    long theaterId;
    long movieId;
    long screenId;
    String[] noOfBookedSeats;
    LocalDateTime showDateTime;
    LocalDateTime bookedDateTime;
    
    
    public Booking(long bookingId, long userId, long theaterId, long movieId, long screenId, String[] noOfBookedSeats){
        this.bookingId = bookingId;
        this.userId = userId;
        this.theaterId = theaterId;
        this.movieId = movieId;
        this.screenId = screenId;
        this.noOfBookedSeats = noOfBookedSeats;
        this.showDateTime = LocalDateTime.now();
        this.bookedDateTime = LocalDateTime.now();
    }
    
    public Booking(long userId, long theaterId, long movieId, long screenId, String[] noOfBookedSeats, LocalDateTime showDateTime){        
        this.userId = userId;
        this.theaterId = theaterId;
        this.movieId = movieId;
        this.screenId = screenId;
        this.noOfBookedSeats = noOfBookedSeats;
        this.showDateTime = showDateTime;
        this.bookedDateTime = LocalDateTime.now();
    }
    
    public Booking(long bookingId,long userId, long theaterId, long movieId, long screenId, String[] noOfBookedSeats, LocalDateTime showDateTime, LocalDateTime bookedDateTime){        
        this.bookingId = bookingId;
        this.userId = userId;
        this.theaterId = theaterId;
        this.movieId = movieId;
        this.screenId = screenId;
        this.noOfBookedSeats = noOfBookedSeats;
        this.showDateTime = showDateTime;
        this.bookedDateTime = bookedDateTime;
    }
    
    public static String extractInt(String str){
        str = str.replaceAll("[^\\d]", " ");
        str = str.trim();
        str = str.replaceAll(" +", " ");
        if (str.equals(""))
            return "-1";
        return str;
    }
    
    public void printString(String seats[][]){
        for(String[] s : seats){
            System.out.println(Arrays.toString(s));
        }
    }
    
    public static String[][] seatsBooked(String seats, int noOfBookedSeats){
        String[][] str = new String[noOfBookedSeats/5][5];
        int rowStart, rowEnd,colEnd = 0,colStart = 0, colS = 0,colE = 0;
        if(seats.length() > 2){
            String[] arr = extractInt(seats).split(" ");
            rowStart = ((int)seats.charAt(0)) - 65;
            rowEnd  =  ((int)seats.charAt(3)) - 64;
            colE = Integer.parseInt(arr[1]);
            colS = Integer.parseInt(arr[0]) - 1;
        }else{
            String[] arr = extractInt(seats).split(" ");
            rowStart = ((int)seats.charAt(0)) - 65;
            rowEnd  =  rowStart ;
            colS = Integer.parseInt(arr[0]);
            colE = Integer.parseInt(arr[0]) + 1;
        }        
        int k=0 ,colStop = 5;
		for(int i = 0, c = 'A'; i < noOfBookedSeats/5 ;i++, c++){
            if(i == rowEnd - 1){
                colStop = colE;
            }
            for(int j = 0,p=1; j < colStop; j++, p++){
                if(seats.length() > 2){
                    if(rowEnd == i){
                        colEnd = colE;
                    }else{
                        colEnd = 10;
                    }
                    if(i == rowStart){
                        colStart = colS;
                    }else{
                        colStart = 0;
                    }
                }else{
                    colStart = colS;
                    colEnd = colE;
                }
                if(rowStart<= i && rowEnd >= i &&  colStart <= j && colEnd > j && k < noOfBookedSeats){
                    str[i][j] = ((char)c) + String.valueOf(p);
				}
            }
        }
        return str;
    }
    
    public void setBookingId(long bookingId){
        this.bookingId = bookingId;
    
    }
    public void setNoOfBookedSeats(String[] noOfBookedSeats){
        this.noOfBookedSeats = noOfBookedSeats;
    }
    
    public long getBookingId(){
        return bookingId;
    }
    
    public long getUserId(){
        return userId;
    }
    public long getMovieId(){
        return movieId;
    }
    public long getTheaterId(){
        return theaterId;
    }
    
    public long getScreenId(){
        return screenId;
    }
    
    public String[] getNoOfBookedSeats(){
        return noOfBookedSeats;
    }
    
    public LocalDateTime getShowDateTime(){
        return showDateTime;
    }
    
    public LocalDateTime getBookedDateTime(){
        return bookedDateTime;
    }
    public String toString(){         
         return new StringBuilder().append(bookingId).append("\t\t").append(noOfBookedSeats[0]).append(" ").append(noOfBookedSeats[1]).append("\t\t").append(theaterId)
         .append("\t\t").append(movieId).append("\t\t").append(screenId).append("\t")
         .append(showDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a"))).append("\t").append(bookedDateTime.format(DateTimeFormatter.ofPattern("yyyy-mm-dd hh:mm:ss a"))).toString();
     } //
}