
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import model.Address;
import model.Booking;
import model.Movie;
import model.MovieStatus;
import model.MovieType;
import model.Screen;
import model.Theater;
import service.BookingService;
import service.MovieService;
import service.ScreenService;
import service.TheaterService;

public class TheaterDetail{
    
    static List<Theater> theaters = new TheaterService().readAll();
    static List<Screen> screens = new ArrayList<Screen>(); 
    static List<Movie> movies = new ArrayList<Movie>(); 
    static Theater theater;
   
    public static Screen addScreen(){
        Scanner sc = new Scanner(System.in);
            System.out.println("Enter the Screen details");
            System.out.println("Total No Of Seats:");
            int seats = sc.nextInt();
            System.out.println("Enter Movie Detail to Show:");
            Movie movie = addMovie();
            long id = new MovieService().checkMovieOrCreate(movie);
            movie.setMovieId(id);
            movies.add(movie);
            System.out.println("Enter total no of show times:");
            int noOfShowTime = sc.nextInt();
            String[] showTimes = new String[noOfShowTime];
        
            for(int i = 0; i < noOfShowTime; i++){
                System.out.printf("Enter %d Show Time:\n", i+1);
                String showTime = sc.next();
                String amPm = sc.next();
                showTimes[i] = showTime+" "+amPm;
            
            }
            return new Screen(seats, movie, showTimes);
    }
    
    public static void addTheater(){
        Scanner sc = new Scanner(System.in);
            System.out.println("Enter the theater details");
            System.out.println("Theater name:");
            String theaterName = sc.nextLine();
        
            System.out.println("Rating:");
            Float rating = sc.nextFloat();
            Address address = getAddress();
            System.out.println("Total no of Screen:");
            int noOfScreens = sc.nextInt();
            for(int i = 1; i < noOfScreens+1; i++){
                System.out.printf("Screen %d:", i);
                Screen screen = addScreen();
                long id = new ScreenService().add(screen);
                screen.setScreenId(id);
                screens.add(screen);
            }
            theater = new Theater(theaterName, address, screens, movies, rating);
            theater.setTheaterId(new TheaterService().add(theater));
            System.out.println(theater);
        
    }
    
    public static Address getAddress(){
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter theater Addres Details:");
            System.out.print("Street:");
            String street = sc.nextLine();
            System.out.print("City:");
            String city = sc.nextLine();
            System.out.print("State:");
            String state = sc.nextLine();
            System.out.print("Pincode:");
            String pincode = sc.nextLine();
            System.out.print("Landmark:");
            String landmark = sc.nextLine(); 
            return new Address(street, city, state, pincode, landmark);
    }
    
    public static Movie addMovie(){
        Scanner sc = new Scanner(System.in);
            System.out.println("Enter Movie Details:");
            System.out.println("MovieName:");
            String movieName = sc.nextLine();
            System.out.println("MovieType");
            String movieType = sc.nextLine();
            MovieType enumType = MovieType.valueOf(movieType);
            System.out.println("MovieStatus:");
            String movieStatus = sc.nextLine();
            MovieStatus enumStatus = MovieStatus.valueOf(movieStatus);
            return new Movie(movieName, enumType, enumStatus);
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
    public static String[][] bookSeats(String[][] theaterSeats, String[] bookedSeats, long bookingId){
        
        for(int i = 0; i < theaterSeats.length;i++){
            for(int j = 0; j < 10; j++){
                for(int k = 0; k < bookedSeats.length; k++){
                    if(theaterSeats[i][j].equals(bookedSeats[k])){
                        theaterSeats[i][j] = String.valueOf(bookingId);
                        break;
                    }
                }
            }
        }
        return theaterSeats;
    }
    
    public static void timeBook(String[] showTimes, long theaterId, int screenId, String date, Screen screen) throws ParseException{
        Scanner sc = new Scanner(System.in);
            int i = 0;
            for(String showTime : showTimes) {
                System.out.printf("%d.%s\t",++i,showTime);
            }
            System.out.println("\nSelect your time:");
            int timeId = sc.nextInt();
            List<Booking> bookingList = new BookingService().searchBooked(theaterId, screenId, new BookTickets().showDateTime(date, showTimes[timeId - 1]));
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
    
    public static void changeOrCreateScreen(){
        Scanner sc = new Scanner(System.in);
            for (Theater theater: new TheaterService().readAll()){
                System.out.println(theater.getTheaterId()+"\t"+theater.getTheaterName());
            }
            System.out.println("Select TheaterId to add Movie");
            long theaterId = sc.nextLong();
            Theater theater = new TheaterService().read(theaterId);
            System.out.println("\t\tScreens and Movies ");
            int id = 0;
            List<Screen> screenList = theater.getScreen();
            System.out.println(theater.getTheaterId()+","+theater.getTheaterName());
            for (Screen screen: screenList){
                Movie movie = screen.getMovie();
                System.out.println("\tScreen :"+ ++id +"\t"+movie.getMovieName()+"\t\t"+ screen.getSeats()+"\t\t"+Arrays.toString(screen.getShowTimes()));
            }
            System.out.println("Select \n\t1.Change Screen Existing Movie \n\t2.Add Screen or Show Time\n\t3.Add Show Times");
            switch(sc.nextInt()){
                case 1:
                {   
                    System.out.println("Enter ScreenId to change Movie");
                    int screenId = sc.nextInt();
                
                    Screen screen = screenList.get(((int)screenId) - 1);
                    List<Movie> movieList = theater.getMovies();
                    if(movieList.isEmpty()){
                        Movie movie = addMovie();
                        long movieId = new MovieService().checkMovieOrCreate(movie);
                        movie.setMovieId(movieId);
                        movieList.add(movie);
                        screenList.add(screen);
                        screen.setMovie(movie);
                        new ScreenService().update(screen, screen.getScreenId());
                        theater.setScreen(screenList);
                        theater.setMovies(movieList);
                    }else{
                        int j = 1;
                        for (Movie movie: movieList){
                            System.out.println(j++ +"\t"+movie.getMovieName()+"\t"+movie.getMovieType()+"\t"+movie.getMovieStatus());
                        }
                        System.out.printf("Enter MovieId to add Screen %d",screenId);
                        int movieId = sc.nextInt();
                        screen.setMovie(movieList.get(((int)movieId) - 1));
                        screenList.set(((int)screenId) - 1, screen);
                        new ScreenService().update(screen, screen.getScreenId());
                        theater.setScreen(screenList);
                    } 
                    new TheaterService().update(theater, theaterId);
                }
                break;
                case 2: 
                {
                    List<Screen> screens = new ArrayList<Screen>();
                    int noOfScreens = theater.getScreen().size();
                    System.out.println("New Screen Add!! Enter Screen Seats");
                    
                    System.out.printf("Screen %d:",noOfScreens+1);
                    Screen screen = addScreen();
                    List<Movie> movieList = theater.getMovies();
                    movieList.add(screen.getMovie());
                    if(movieList.isEmpty()){
                        Movie movie = screen.getMovie();
                        long movieId = new MovieService().checkMovieOrCreate(movie);
                        movie.setMovieId(movieId);
                        movieList.add(movie);   
                        screen.setMovie(movie);
                        long newScreenId = new ScreenService().add(screen);
                        screen.setScreenId(newScreenId);
                        screenList.add(screen);
                        theater.setScreen(screenList);
                        theater.setMovies(movieList);
                    }else{  
                        int k = 1;
                        for (Movie movie: movieList){
                            System.out.println(k++ +"\t"+movie.getMovieName()+"\t"+movie.getMovieType()+"\t"+movie.getMovieStatus());
                        }   
                        System.out.printf("Enter MovieId to add Screen");
                        int movieId = sc.nextInt(); 
                        screen.setMovie(movieList.get(((int)movieId) - 1));
                        long screenId = new ScreenService().add(screen);
                        screen.setScreenId(screenId);
                    
                        screenList.add(screen);
                        theater.setScreen(screenList);
                    }
                    new TheaterService().update(theater, theaterId);
                }
                break;
                case 3:{
                    System.out.println("Enter ScreenId to add show time");
                    int screenId = sc.nextInt(), k = 1;
                    Screen screen = screenList.get(((int)screenId) - 1);
                    String[] showTimes = screen.getShowTimes(); 
                    for (String showTime: showTimes){
                        System.out.printf("%d.%s\t",++k,showTime);
                    }
                    System.out.println("Enter noOfShowTime:");
                    int noOfShowTime = sc.nextInt();
                    for(int i = showTimes.length; i < noOfShowTime+1; i++){
                        System.out.printf("Enter %d Show Time:\n", i+1);
                        showTimes = Arrays.copyOf(showTimes, i + 1);
                        String showTime = sc.next();    
                        String amPm = sc.next();
                        showTimes[i] = showTime+" "+amPm;   
                    }
                    screen.setShowTimes(showTimes);
                    new ScreenService().update(screen, screen.getScreenId());
                }    
                break;
                default:
                    System.out.println("Enter correct options plzz!!");
                break;
            }
        
    }
    
    public static void adminAccess() throws ParseException{
        Scanner sc = new Scanner(System.in);
        System.out.println("\tBookMyTicket Application");
        boolean loop = true;
        while(loop){
            System.out.println("------*---------*-----------*-------------*---------");
            System.out.println(" 1. Add New Theater\n 2. Add New Movie \n 3. Available Tickets \n 4. Booked Tickets \n 5. Exit");
            int choice = 0;
            choice = sc.nextInt();
            
            switch(choice){
                case 1:
                {
                    addTheater();
                 }
                break;
                case 2:
                {
                    changeOrCreateScreen();   
                }
                break;
                case 3:
                {
                    availableTickets();
                }
                break;
                case 4:
                {
                    System.out.println("Booked Tickets");
                    
                    List<Booking> bookingList = new BookingService().readAll();
                    System.out.println("BookingId\tNoOfBookedSeats\tTheaterId\tMovieId\t   ScreenNo\t   BookedDate\t   BookedTime");
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
}