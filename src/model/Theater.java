package com.zoho.booktickets.model;

import java.util.List;

public class Theater{
    
    private long theaterId;
    private String theaterName;
    private Address address;
    private List<Screen> screens;
    private List<Movie> movies;
    private float rating;
  
  
    public Theater(long theaterId, String theaterName, Address address, List<Screen> screens, List<Movie> movies, float rating){
        this.theaterId = theaterId;
        this.theaterName = theaterName;
        this.address = address;
        this.screens = screens;
        this.movies = movies;
        this.rating = rating;
    }  
    
    public Theater(String theaterName, Address address, List<Screen> screens, List<Movie> movies, float rating){
        
        this.theaterName = theaterName;
        this.address = address;
        this.screens = screens;
        this.movies = movies;
        this.rating = rating;
    }
    
    public void setTheaterId(long theaterId){
        this.theaterId = theaterId;
    }
    
    public void setTheaterName(String theaterName){
        this.theaterName = theaterName;
    }
    
    public void setAddress(Address address){
        this.address = address;
    }
    
    public void setScreen(List<Screen> screens){
        this.screens = screens;
    }
    
    public void setMovies(List<Movie> movies){
        this.movies = movies;
    }
    
    public void setRating(float rating){
        this.rating = rating;
    }
    
    public long getTheaterId(){
        return theaterId;
    }
    
    public String getTheaterName(){
        return theaterName;
    }
    
    public Address getAddress(){
        return address;
    }
    
    public List<Screen> getScreen(){
        return screens;
    }
    
    public List<Movie> getMovies(){
        return movies;
    }
    
    public float getRating(){
        return rating;
    }
    
    public String toString(){         
         return new StringBuilder().append(theaterId).append(", TheaterName:").append(theaterName).append(",Screen").append(screens)
				.append(",Movies").append(movies).append(",").append(address).append(", Rating = ").append(rating).toString();
     } 

}