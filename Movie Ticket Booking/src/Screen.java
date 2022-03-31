package com.zoho.booktickets.model;
import java.util.Arrays;
import java.time.LocalTime;
import java.text.ParseException;

import java.text.SimpleDateFormat;

public class Screen{
    private long screenId;
    private int seats;//Available, TotalNoOfSeats
    private Movie movie ;
    private String showTimes[];
    
    public Screen(long screenId, int seats, Movie movie,String showTimes[]){
        this.screenId = screenId;
        this.seats = seats;
        this.movie = movie;
        this.showTimes = showTimes;
    }
    
    public Screen(int seats, Movie movie ,String showTimes[]){
        this.seats = seats;
        this.movie = movie;
        this.showTimes = showTimes;
    }
    
    
    public void setScreenId(long screenId){
        this.screenId = screenId;
    }
    
    public void setSeats(int seats){
        this.seats = seats;
    }
    
    public void setMovie(Movie movie ){
        this.movie = movie ;
    }
    
    public void setShowTimes(String showTimes[]){
        this.showTimes = showTimes;
    }
    
    public long getScreenId(){
        return screenId;
    }
    
    public int getSeats(){
        
        return seats;
    }
    
    
    
    public Movie getMovie(){
        return movie;
    }
    public String[] getShowTimes(){
        return showTimes;
    }
    
    public String[] getAfterTime()  throws ParseException{
        String[] showAfterTimes;
        int i = 0, len = 0;
        for(String showTime : showTimes) {
                if(!LocalTime.now().isAfter(LocalTime.parse(new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("hh:mm a").parse(showTime)).toString()))){
                    len++;
                }
            }
            showAfterTimes = new String[len];
            for(String showTime : showTimes) {
                if(!LocalTime.now().isAfter(LocalTime.parse(new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("hh:mm a").parse(showTime)).toString()))){
                    showAfterTimes[i++] = showTime;
                }
            }
        
        return showAfterTimes;
    }
    public String toString(){
         return new StringBuilder().append(screenId).append(",").append(seats).append(",").append(movie)
				.append(",").append(Arrays.toString(showTimes)).toString();         
     }    
     
     
}