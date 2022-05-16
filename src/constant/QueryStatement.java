package com.zoho.booktickets.constant;


public class QueryStatement {
	
    public static final String CREATE_USER_TABLE = "CREATE TABLE users (user_id serial PRIMARY KEY, role VARCHAR ( 10 ),username VARCHAR ( 50 ) NOT NULL,mob_no VARCHAR ( 20 ) NOT NULL, email_id VARCHAR ( 255 ) NOT NULL,sex  VARCHAR ( 10 ) NOT NULL);";
	
    public static final String CREATE_MOVIE_TABLE = "CREATE TABLE movie (movie_id serial PRIMARY KEY,moviename VARCHAR ( 50 ) NOT NULL, movie_type movie_type_enum NOT NULL, movie_status movie_status_enum NOT NULL);";
    
    public static final String ENUM_MOVIE_STATUS = "CREATE TYPE movie_status_enum AS ENUM ('Available', 'NotAvailable');";
    
    public static final String ENUM_MOVIE_TYPE = "CREATE TYPE movie_type_enum AS ENUM ('Tamil', 'English', 'Hindi');";
  
    public static final String CREATE_ADDRESS_TABLE = "CREATE TABLE address (address_id serial PRIMARY KEY, street VARCHAR ( 100 ) NOT NULL, city VARCHAR ( 100 ) NOT NULL, state VARCHAR ( 100 ) NOT NULL, pincode VARCHAR ( 20 ) NOT NULL, landmark VARCHAR ( 100 ) NOT NULL);";
    
    public static final String CREATE_THEATER_TABLE = "CREATE TABLE theater (theater_id serial PRIMARY KEY, theater_name VARCHAR ( 100 ) NOT NULL, theater_address_id integer,screens_list integer [], movies_list integer [], rating float4 NOT NULL,  constraint fk_theater_address foreign key (theater_address_id) REFERENCES address (address_id));";
  
    public static final String CREATE_SCREEN_TABLE = "CREATE TABLE screens (screen_id serial PRIMARY KEY, seats integer, screen_movie_id integer , show_times text[], constraint fk_screen_movie  foreign key (screen_movie_id)  REFERENCES movie (movie_id));";
    
    public static final String CREATE_BOOKING_TABLE = "CREATE TABLE booking (booking_id serial PRIMARY KEY, book_user_id integer, book_theater_id integer, book_movie_id integer, screen_no integer NOT NULL, booked_seats text[] NOT NULL, show_date_time timestamp without time zone default NULL, booked_date_time timestamp without time zone default NULL, CONSTRAINT fk_book_user FOREIGN KEY(book_user_id) REFERENCES users(user_id), CONSTRAINT fk_book_address FOREIGN KEY(book_theater_id) REFERENCES theater(theater_id), CONSTRAINT fk_book_movie FOREIGN KEY(book_movie_id) REFERENCES movie(movie_id));";
    
    public static final String INSERT_USER = "INSERT INTO users (role, username, mob_no, email_id, sex) VALUES(?,?,?,?,?) RETURNING user_id;";
    
    public static final String INSERT_MOVIE = "INSERT INTO movie (moviename, movie_type, movie_status) VALUES(?,?::movie_type_enum,?::movie_status_enum) RETURNING movie_id;";
    
    public static final String INSERT_ADDRESS = "INSERT INTO address (street, city, state, pincode, landmark) VALUES(?,?,?,?,?) RETURNING address_id;";
    
    public static final String UPDATE_ADDRESS = "UPDATE address SET street = ?, city = ? , state = ? , pincode = ?, landmark = ? WHERE address_id = ?;";
    
    public static final String INSERT_THEATER = "INSERT INTO theater (theater_name, theater_address_id, screens_list, movies_list, rating) VALUES(?,?,?,?,?) RETURNING theater_id;";
    
    public static final String INSERT_SCREEN = "INSERT INTO screens (seats, screen_movie_id, show_times) VALUES(?,?,?) RETURNING screen_id;";
    
    public static final String INSERT_BOOKING = "INSERT INTO booking (book_user_id, book_theater_id,book_movie_id, screen_no, booked_seats, show_date_time, booked_date_time) VALUES(?,?,?,?,?,?,?) RETURNING booking_id;";
    
    public static final String UPADE_USER = "UPDATE users SET role = ?, username = ?, mob_no = ?, email_id = ?, sex = ? WHERE user_id = ?;";

    public static final String UPADE_SCREEN = "UPDATE screens SET seats = ?, screen_movie_id = ?, show_times = ? WHERE screen_id = ?;";
    
    public static final String UPADE_MOVIE = "UPDATE movie SET  moviename = ? , movie_type = ?::movie_type_enum , movie_status = ?::movie_status_enum WHERE movie_id = ?;";
    
    public static final String DELETE_USER = "DELETE FROM users WHERE user_id = ?;";
    
    public static final String DELETE_MOVIE = "DELETE FROM movie WHERE movie_id = ?;";
    
    public static final String UPADE_THEATER = "UPDATE theater SET theater_name = ?, theater_address_id = ?, screens_list = ?, movies_list =?, rating =? WHERE theater_id = ?;";
    
    public static final String UPADE_THEATER_SCREEN = "UPDATE theater SET screens_list = ? WHERE theater_id = ?;";
    
    public static final String DELETE_THEATER = "DELETE FROM theater WHERE theater_id = ?;";
    
    public static final String DELETE_BOOKING = "DELETE FROM booking WHERE booking_id = ?;";

    public static final String SELECT_USER = "SELECT * FROM users WHERE user_id = ?;";
    
    public static final String SELECT_USER_ALL  = "SELECT * FROM users ORDER BY user_id ASC ;";
    
    public static final String GET_USER_ID = "SELECT user_id FROM users WHERE email_id = ?";
    
    public static final String SELECT_MOVIE = "SELECT * FROM movie WHERE movie_id = ?;";
    
    public static final String SELECT_MOVIE_ALL= "SELECT * FROM movie ORDER BY  movie_id ASC ;";
    
    public static final String GET_MOVIE_ID = "SELECT movie_id FROM movie WHERE moviename = ? AND movie_type = ? AND movie_status = ?";
                                              
    public static final String DELETE_UNUSED_MOVIE ="DELETE FROM movie mv WHERE  NOT EXISTS (SELECT FROM screens sc WHERE  sc.screen_movie_id = mv.movie_id);";
    
    public static final String SELECT_ADDRESS = "SELECT * FROM address WHERE address_id = ?;";
    
    public static final String DELETE_ADDRESS = "DELETE FROM address WHERE address_id = ?;";
    
    public static final String SELECT_THEATER_NAME = "SELECT * FROM theater WHERE theater_name = ?;";
    
    public static final String SELECT_SCREEN_MOVIE_ID = "SELECT * FROM screens WHERE screen_movie_id = ?;";
    
    public static final String SELECT_THEATER = "SELECT * FROM theater WHERE theater_id = ?;";
    
    public static final String SELECT_SCREEN  = "SELECT * FROM screens WHERE screen_id = ?;";
    
    public static final String DELETE_SCREEN = "DELETE FROM screens WHERE screen_id = ?;";
    
    public static final String SELECT_THEATER_SCREEN = "SELECT screens_list FROM theater WHERE theater_id = ?;";
    
    public static final String SELECT_THEATER_ALL = "SELECT * FROM theater ORDER BY theater_id ASC ;";
    
    public static final String SELECT_BOOKING = "SELECT booking_id, book_user_id, book_theater_id, book_movie_id, screen_no, booked_seats,show_date_time, booked_date_time FROM booking WHERE booking_id = ?;";
    
    public static final String SELECT_BOOKING_ALL = "SELECT * FROM booking ORDER BY booking_id ASC ;";
    
    public static final String UPADE_BOOKING_SEATS = "UPDATE booking SET booked_seats = ? WHERE booking_id = ?;";
    
    public static final String SEARCH_BOOKED = "SELECT * FROM booking WHERE book_theater_id = ? AND screen_no = ? AND show_date_time = ?;";
    
    public static final String SEARCH_USER_BOOKED = "SELECT * FROM booking WHERE book_user_id = ?;";
    
	}
