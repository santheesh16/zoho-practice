import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;

import java.util.Scanner;
import java.util.Calendar;
import java.util.Arrays;
public class StringToLocalDate{
    
    public static void generateDates(){
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Calendar calendar = Calendar.getInstance();
        String[] days = new String[4];
        for(int i = 0; i < 4;i++){
            days[i] = format.format(calendar.getTime());
            calendar.add(Calendar.DATE  , 1);
        } 
        System.out.println(Arrays.toString(days));
    }
    public static void dateTime() throws ParseException{
        Scanner sc = new Scanner(System.in);
        String dateStr = sc.nextLine();
        String time = sc.nextLine();
        
       SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm");
       SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");
       Date timeFormat = parseFormat.parse(time);
       String dateTime = dateStr +" "+ displayFormat.format(timeFormat);
       DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
       LocalDateTime localDateTime = LocalDateTime.parse(dateTime, formatter);
       System.out.println(localDateTime);
       System.out.println(displayFormat.format(timeFormat));
       System.out.println(localDateTime.toLocalTime());
       LocalTime localTime = localDateTime.toLocalTime();
       String localTimeStr = localTime.toString();
       System.out.println(LocalTime.now().isAfter( LocalTime.parse( localTimeStr)));
    }
    
    public static void printAfterTime()  throws ParseException{
        Scanner sc = new Scanner(System.in);
       String time = sc.nextLine();
       System.out.println(LocalTime.now().isAfter(LocalTime.parse(new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("hh:mm a").parse(time)).toString())));
    }
    
    public static void checkCurrentDates(){
        
    }
    
    public static void main(String[] args) throws ParseException{
        
        printAfterTime();   
        
    }
}