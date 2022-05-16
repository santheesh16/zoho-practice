

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import com.zoho.booktickets.exception.*;
import com.zoho.booktickets.model.*;
import com.zoho.booktickets.service.*;

import model.Booking;
import model.Movie;
import model.MovieStatus;
import model.Screen;
import model.Theater;
import model.User;
import service.BookingService;
import service.EmailService;
import service.SmsService;
import service.TheaterService;
import service.UserService;

public class BookTickets{
    
    static Booking booking;
    static Random random = new Random(); 
    static List<Booking> bookedTickets = new ArrayList<Booking>();
    
    public static void printTheaters(){
        System.out.print("TheaterId\tTheaterName\tTheatertAddress\tTheaterRating\n");
        for(Theater theater : new TheaterService().readAll()){    
            System.out.println(theater.getTheaterId()+"\t"+theater.getTheaterName()+"\t"+theater.getAddress()+"\t"+theater.getRating());
        }
    }
    
    public static void printTheaterMovie(Theater theater){
        int i = 1;
        for (Movie movie: theater.getMovies()){
            System.out.println(i++ +","+movie.getMovieName());
        }
    }
    
    public static void printScreen(List<Screen> userScreens){
        System.out.println("\t\t\tMovieName\tTonoOfSeatsAvailable");                        
        int id =0;
        for (Screen screen: userScreens){
            Movie movie = screen.getMovie();
            System.out.println("\tScreen :"+ ++id +"\t"+movie.getMovieName()+"\t\t"+screen.getSeats());                        
        }
    }
    
    public static String extractInt(String str){
        str = str.replaceAll("[^\\d]", " ");
        str = str.trim();
        str = str.replaceAll(" +", " ");
        if (str.equals(""))
            return "-1";
        return str;
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
    
    public static String[] noOfSeatsBooking(String[][] str, String seats){
        
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
    
    public static LocalDateTime showDateTime(String date, String time) throws ParseException{
        SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm");
       SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");
       Date timeFormat = parseFormat.parse(time);
       String dateTime = date +" "+ displayFormat.format(timeFormat);
       DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
       LocalDateTime localDateTime = LocalDateTime.parse(dateTime, formatter);
       
       return localDateTime;
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
    
    public static String[][] generateSeats(int seats){
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
    
    public static void theaterMovie(long userId) throws ParseException{
        Scanner sc = new Scanner(System.in);
        printTheaters();
        System.out.println("Select Your Theater to book Tickets:");
        int theaterId = sc.nextInt();
        Theater userTheater = new TheaterService().read(theaterId);
        List<Screen> userScreens = userTheater.getScreen();
        printScreen(userScreens);
        System.out.println("Select Screen No of Movie:");
        int userScreenNo = sc.nextInt();
        Screen screen = userScreens.get(userScreenNo-1);
        Movie userMovie = screen.getMovie();
        if(userMovie.getMovieStatus().equals(MovieStatus.Available)){
            int dateId = 1;
            String[] dates =  generateDates();
            for(String date : dates) {
                System.out.printf("%d.%s\t",dateId++,date);
            }
            System.out.println("\nSelect your date:");
            String date = dates[sc.nextInt() - 1];
            int timeId = 0, totalSeats = 0, i = 0;
            String[][] seats = null;
            String[] showTimes = screen.getShowTimes();
            boolean flag = false;
            if(dates[0].equals(date)){
                showTimes = screen.getAfterTime();
                if(showTimes.length != 0){
                    for(String showTime : showTimes) {
                    System.out.printf("%d.%s\t",++i,showTime);
                    }
                    System.out.println("Select your time:");
                    timeId = sc.nextInt();
                    totalSeats = screen.getSeats();
                    System.out.println("Seat Arrangement");
                    seats = generateSeats(totalSeats);
                    int[] beforeBook =  totalSeatsBooked(seats);
                    List<Booking> bookingList = new BookingService().searchBooked(theaterId, userScreenNo, showDateTime(date, showTimes[timeId - 1]));
                    int booked = 0;
                    for(Booking booking: bookingList){
                        seats = bookSeats(seats, booking.getNoOfBookedSeats(), booking.getBookingId());
                        booked += booking.getNoOfBookedSeats().length;
                    }
                    for(String[] s : seats){
                        System.out.println(Arrays.toString(s));
                    }   
                    System.out.printf("Booked: %d\t Availble: %d",booked,beforeBook[1]);
                }else{
                    System.out.println("Today's Shows Over!! in this screen");
                    flag = true;
                }
            }else{
                for(String showTime : showTimes) {
                    System.out.printf("%d.%s\t",++i,showTime);
                }
                System.out.println("Select your time:");
                timeId = sc.nextInt();
                totalSeats = screen.getSeats();
                System.out.println("Seat Arrangement");
                seats = generateSeats(totalSeats);
                int[] beforeBook =  totalSeatsBooked(seats);
                List<Booking> bookingList = new BookingService().searchBooked(theaterId, userScreenNo, showDateTime(date, showTimes[timeId - 1]));
                int booked = 0;
                for(Booking booking: bookingList){
                    seats = bookSeats(seats, booking.getNoOfBookedSeats(), booking.getBookingId());
                    booked += booking.getNoOfBookedSeats().length;
                }
                for(String[] s : seats){
                    System.out.println(Arrays.toString(s));
                }   
                System.out.printf("Booked: %d\t Availble: %d",beforeBook[0],beforeBook[1]);
            }
            if(!flag){
                System.out.println("\nEnter required seats Eg:A1-B10 or A4:");
            String bookedSeats = sc.next();
            
            String[] noOfSeatsBooked = null;
            if(bookedSeats.contains(",")){
                noOfSeatsBooked = bookedSeats.split(",");   
            }else if(bookedSeats.contains("-")){
                noOfSeatsBooked = noOfSeatsBooking(seats, bookedSeats);   
            }else{
                String str = bookedSeats+" ";
                noOfSeatsBooked = new String[]{bookedSeats};
            }
            booking = new Booking(userId, theaterId, userMovie.getMovieId(), userScreenNo, noOfSeatsBooked, showDateTime(date, showTimes[timeId-1]));
            long bookingId = new BookingService().add(booking);
            booking.setBookingId(bookingId);
            String[][] seatsBook  = bookSeats(seats, noOfSeatsBooked, bookingId);
            int[] afterBook = totalSeatsBooked(seatsBook);
            
            System.out.printf("Check Your Booked Seats Your Id %d\n",bookingId);
            for(String[] s :seatsBook){
                System.out.println(Arrays.toString(s));
            }
            System.out.printf("Booked: %d\t Availble: %d",afterBook[0],afterBook[1]);
            System.out.printf("\nYour %d Seats Successfully Booked",noOfSeatsBooked.length);
             
            System.out.println("\nBooked Details");
            System.out.println("BookingId\tNoOfBookedSeats\tTheaterId\tMovieId\t\tScreenNo\tShowDateTime\tBookedDateTime");
            System.out.println(booking);
            new EmailService().generatedPDF(booking.getBookingId());
            new EmailService().sendPDFMail(new UserService().read(userId).getEmailId());
            new SmsService().bookTickets(booking.getBookingId(), new UserService().read(userId).getMobileNo());
            
            System.out.println("Check your mail....");
            }
        }
    }
    
    public static void timeBook(LocalDateTime localDateTime, long theaterId, int screenId, Screen screen)throws ParseException{
        /*Scanner sc = new Scanner(System.in);
            int i = 0;
            for(String showTime : showTimes) {
                System.out.printf("%d.%s\t",++i,showTime);
            }
            System.out.println("\nSelect your time:");
            int timeId = sc.nextInt();*/
            List<Booking> bookingList = new BookingService().searchBooked(theaterId, screenId, localDateTime);
            String[][] seats = new BookTickets().generateSeats(screen.getSeats());
            int bookedSeats = 0;
            for(Booking booking: bookingList){
                seats = bookSeats(seats, booking.getNoOfBookedSeats(), booking.getBookingId());
                bookedSeats += booking.getNoOfBookedSeats().length;
            }
            System.out.printf("\n\t\t------------------------\n");
            for (String[] s: seats){
                System.out.println("\t"+Arrays.toString(s));   
            }            
            int[] availSeats = new BookTickets().totalSeatsBooked(seats);
            System.out.printf("\tBookedSeats: %d\t\t Availble: %d\n",availSeats[0], availSeats[1]);
    }
    
   public static void availableTickets() throws ParseException{
        Scanner sc = new Scanner(System.in);
            System.out.println("\t\t\tMovieName\tToatalNoOfSeats each shows");                        
            int id =0;
            for (Theater theater: new TheaterService().readAll()){
                System.out.println(theater.getTheaterId()+","+theater.getTheaterName());
                for (Screen screen: theater.getScreen()){
                    Movie movie = screen.getMovie();
                    System.out.println("\tScreen :"+ ++id +"\t"+movie.getMovieName()+"\t\t"+ screen.getSeats());
                }
            }
            System.out.println("Enter Theater Id & Screen No to check seats");
            System.out.printf("Theater Id:");
            long theaterId = sc.nextLong();
            Theater theater = new TheaterService().read(theaterId);
            System.out.printf("Screen No: ");
            int screenId = sc.nextInt();
            Screen screen = theater.getScreen().get(screenId - 1);
            int dateId = 1;
            String[] dates =  new BookTickets().generateDates();
            for(String date : dates) {
                System.out.printf("%d.%s\t",dateId++,date);
            }
            System.out.println("\nSelect your date:");
            String date = dates[sc.nextInt() - 1];
        
            if(dates[0].equals(date)){
                String[] showTimes = screen.getAfterTime();
                if(showTimes.length != 0){
                    timeBook(showTimes, theaterId, screenId, date, screen);
                }else{
                    System.out.println("Today's Shows Over!! in this screen");
                }
            }else{
                timeBook(screen.getShowTimes(), theaterId, screenId, date, screen);
            }
    }
     
    public static void cancelTickets(long bookingId){
        Scanner sc = new Scanner(System.in);
            Booking booking = new BookingService().read(bookingId);
        Theater theater = new TheaterService().read(booking.getTheaterId());
        long bookedScreenId = booking.getScreenId();
        
        Screen screenBook = theater.getScreen().get(((int)booking.getScreenId()) - 1);
        String[][] seats = generateSeats(screenBook.getSeats());
        String[] bookedSeats = booking.getNoOfBookedSeats();
        seats = bookSeats(seats, bookedSeats, booking.getBookingId());
        for (String[] s: seats){
                System.out.println(Arrays.toString(s));   
        }
        new EmailService().generatedCancelPDF(bookingId);
        new EmailService().sendPDFMail(new UserService().read(booking.getUserId()).getEmailId());
        new SmsService().cancelTickets(booking.getBookingId(), new UserService().read(booking.getUserId()).getMobileNo());
        System.out.println("Your Tickets Successfully Cancelled...");
        System.out.println("Check your mail...");
        new BookingService().delete(bookingId);
    }
    
    public static void userBook()throws ParseException{
        Scanner sc = new Scanner(System.in);
        System.out.println("\tBookMyTicket Application");
        boolean loop = true;
        while(loop){
            System.out.println("------*---------*-----------*-------------*---------");
            System.out.println(" 1. Book Ticket \n 2. Cancel Ticket \n 3. Available Tickets \n 4. Booked Tickets \n 5. Exit");
            int choice = sc.nextInt();
            switch(choice){
                case 1:
                {
                    System.out.println("Register your details to book tickets");
                    System.out.println("User name:");
                    String name = sc.next();
                    System.out.println("Mobile No:");
                    String mobNo = sc.next();
                    System.out.println("EmailId:");
                    String emailId = sc.next();
                    System.out.println("Sex:");
                    String sex = sc.next();
                    
                    String generatedOTP = String.format("%04d", random.nextInt(10000));
                    new SendEmailService().sendOTPMail(emailId, generatedOTP);
                    SendSMSService.sendSms("BookMyTicket OTP is "+generatedOTP, mobNo);
                    System.out.println("Please Enter Your OTP send to your mailId");
                    System.out.println("OTP: ");
                    String userOTP = sc.next();
                    if(generatedOTP.equals(userOTP)){
                        System.out.println("Successfully Registered!! Now Can book your Tickets"); 
                        theaterMovie(new UserService().checkMovieOrAdd(new User(name, mobNo, emailId, sex)));                 
                    }else{
                        System.out.println("Please!! enter correct OTP");
                    }
                 }
                break;
                case 2:
                {
                    if(new BookingService().readAll().size() > 0){
                        System.out.println("Enter Your BookingId");
                        int bookingId = sc.nextInt();
                        cancelTickets(bookingId);
                    }else{
                        System.out.println("No Tickets Booked to cancel");
                    }
                    
                }
                break;
                case 3:{
                    availableTickets();
                }
                break;
                case 4:
                {
                    System.out.println("Booked Tickets");
                    
                    List<Booking> bookingList = new BookingService().readAll();
                    System.out.println("BookingId\tNoOfBookedSeats\tTheaterId\tMovieId\t\tScreenNo\tShowDateTime\tBookedDateTime");
                    if(bookingList.size() > 0){
                        for(Booking booked: bookingList){
                            System.out.println(booked);
                        }
                    }else{
                        System.out.println("No Tickets Booked");
                    }
                }
                break;
                case 5:
                {
                    loop = false;
                }
                break;
                default:
                break;
            }
        }
    }
    
    public static void main(String[] args)throws ParseException{
        Scanner sc = new Scanner(System.in);
        System.out.println(" 1. Book Ticket \n 2. Admin Login \n");
        switch(sc.nextInt()){
            case 1: 
            {
                userBook();
            }
            break;
            case 2:
            {
                new TheaterDetail().adminAccess();
            }
            break;
            default:
            break;
        }
        
    }
}

/*
    try(Scanner sc = new Scanner(System.in)){
            
        } catch (AppException e) {
			throw e;
		} catch (Exception e) {
			throw new AppException(ErrorCode.ERR01, e);
		}*/