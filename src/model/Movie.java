package com.zoho.booktickets.model;

public class Movie{
    private long movieId;
    private String movieName;
    private MovieType movieType;
    private MovieStatus movieStatus;
    
    
    public Movie(long movieId,String movieName, MovieType movieType, MovieStatus movieStatus){
        this.movieId = movieId;
        this.movieName = movieName;
        this.movieType = movieType;
        this.movieStatus = movieStatus;
    }
    
    public Movie(String movieName, MovieType movieType, MovieStatus movieStatus){
        this.movieName = movieName;
        this.movieType = movieType;
        this.movieStatus = movieStatus;
    }
    public void setMovieId(long movieId){
        this.movieId = movieId;
    }
    
    public void setMovieName(String movieName){
        this.movieName = movieName;
    }
    
    public void setMovieType(MovieType movieType){
        this.movieType = movieType;
    }
    
    public void setMovieStatus(MovieStatus movieStatus){
        this.movieStatus = movieStatus;
    }
    
    public long getMovieId(){
        return movieId;
    }
    
    public String getMovieName(){
        return movieName;
    }
    
    public MovieType getMovieType(){
        return movieType;
    }
    
    public MovieStatus getMovieStatus(){
        return movieStatus;
    } 
    public String toString(){
         return new StringBuilder("Movie:").append(movieId).append(",").append(movieName).append(",").append(movieType)
				.append(",").append(movieStatus).toString();         
     } 
}