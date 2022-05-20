package com.zoho.booktickets.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.zoho.booktickets.service.BookingService;

public class Booking {
    long bookingId;
    long userId;
    long theaterId;
    long movieId;
    long screenId;
    String[] noOfBookedSeats;
    LocalDateTime showDateTime;
    LocalDateTime bookedDateTime;
    public Booking(){        
        
    }
    
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
    
    public String[] noOfSeatsBooking(String[][] str, String seats){
        
        String[] bookedTic = new String[0];
		int rowStart = 0, rowEnd = 0,colEnd = 0,colStart = 0, colS = 0,colE = 0;
        if(seats.length() > 2){
            String[] arr = extractInt(seats).split(" ");
            
            rowStart = ((int)seats.charAt(0)) - 65;
            rowEnd  =  ((int)seats.charAt(3)) - 65;
            colE = Integer.parseInt(arr[1]);
            colS = Integer.parseInt(arr[0]) - 1;
        }else{
            String[] arr = extractInt(seats).split(" ");
            rowStart = ((int)seats.charAt(0)) - 65;
            rowEnd  =  rowStart ;
            colS = Integer.parseInt(arr[0]);
            colE = Integer.parseInt(arr[0]) + 1;
        }
        int k = 0;
		for(int i = 0, c = 'A'; i < str.length ;i++, c++){
            for(int j = 0,p=1; j < 10; j++, p++){
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
				if(rowStart<= i && rowEnd >= i &&  colStart <= j && colEnd > j ){
                    bookedTic = Arrays.copyOf(bookedTic, bookedTic.length + 1);
					bookedTic[k] = ((char)c) + String.valueOf(p);
                    k++;
				}
            }
        }
        return bookedTic;
    }
        public static String[][] bookSeats(String[][] theaterSeats, String[] bookedSeats, long bookingId){
        
        for(int i = 0; i < theaterSeats.length;i++){
            for(int j = 0; j < 10; j++){
                for(int k = 0; k < bookedSeats.length; k++){
                    if(theaterSeats[i][j].equals(bookedSeats[k])){
                        theaterSeats[i][j] = "-";
                        break;
                    }
                }
            }
        }
        return theaterSeats;
    }
    
    
    
     public static String[][] timeBook(LocalDateTime localDateTime, long theaterId, Screen screen)throws ParseException{
        
            List<Booking> bookingList = new BookingService().searchBooked(theaterId, (int)screen.getScreenId(), localDateTime);
            String[][] seats = new Booking().generateSeats(screen.getSeats());
            for(Booking booking: bookingList){
                seats = bookSeats(seats, booking.getNoOfBookedSeats(), booking.getBookingId());
            }
            return seats;
    }
    
    public LocalDateTime showDateTime(String date, String time) throws ParseException{
        SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm");
       SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");
       Date timeFormat = parseFormat.parse(time);
       String dateTime = date +" "+ displayFormat.format(timeFormat);
       DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
       LocalDateTime localDateTime = LocalDateTime.parse(dateTime, formatter);
       
       return localDateTime;
    }
    
    public String[][] generateSeats(int seats){
        int rowEnd = 0, colEnd = 0;
        if(seats > 10 && seats%10 == 0){
            rowEnd = seats/10;
            colEnd = 10;
        }else if (seats <= 10){
            rowEnd = 1;
            colEnd = seats;
        }else{
            rowEnd = seats/10 + 1;
            colEnd = 10;
        }
        String[][] theaterSeats = new String[rowEnd][10];
        for(int i = 0, c = 'A'; i < rowEnd;i++, c++){
            if(i == rowEnd - 1 && seats%10 != 0){
                colEnd = seats%10;
            }
            for(int j = 0,p=1; j < colEnd; j++, p++){
                theaterSeats[i][j] = ((char)c) + String.valueOf(p);
            }
        }
        return theaterSeats;
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
    
    public static int[] totalSeatsBooked(String[][] seats){
        int bookedTic , avail ;
        bookedTic = avail = 0;
		for(int i = 0; i < seats.length ;i++){
            for(int j = 0; j < 10; j++){
                if(seats[i][j].equals("-")){
                    bookedTic++;
                }else{
                    avail++;
                }
            }
        }
        return new int[]{bookedTic, avail};
    }
    
    public static String[] generateDates(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        String[] days = new String[4];
        for(int i = 0; i < 4;i++){
            days[i] = format.format(calendar.getTime());
            calendar.add(Calendar.DATE  , 1);
        } 
        return days;
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
         return new StringBuilder().append(bookingId).append("\t\t").append(noOfBookedSeats).append(" ").append(Arrays.toString(noOfBookedSeats)).append("\t\t").append(theaterId)
         .append("\t\t").append(movieId).append("\t\t").append(screenId).append("\t")
         .append(showDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a"))).append("\t").append(bookedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a"))).toString();
     } //
}