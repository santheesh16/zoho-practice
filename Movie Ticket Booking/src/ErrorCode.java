package com.zoho.booktickets.exception;

public enum ErrorCode {
	
//  Address Failed Exception
	
    ERR01("Please!! Enter showed id's only", 400),
    ERR02("Please!! Enter corrent inputs values", 400),
    ERR03("Please!! Enter time Eg: 8:00 AM format", 400),
    
    //TheaterService
    ERR04("Theater is not able created", 400),
    ERR05("Theater is not able read", 400),
    ERR06("Theater is not able add", 400),
    ERR07("Theater is not able updated", 400),
    ERR08("Theater is not able deleted", 400),
    
    //MovieService
    ERR09("Movie is not able created", 400),
    ERR10("Movie is not able read", 400),
    ERR11("Movie is not able add", 400),
    ERR12("Movie is not able updated", 400),
    ERR13("Movie is not able deleted", 400),
    
    //AddressService
    ERR14("Address is not able created", 400),
    ERR15("Address is not able read", 400),
    ERR16("Addresses is not able add", 400),
    ERR17("Address is not able updated", 400),
    ERR18("Address is not able delete", 400),

    //ScreenService
    ERR19("Screen is not able created", 400),
    ERR20("Screen is not able read", 400),
    ERR21("Screen is not able add", 400),
    ERR22("Screen is not able updated", 400),
    ERR23("Screen is not able delete", 400),
    //ScreenService
    ERR24("User is not able created", 400),
    ERR25("User is not able read", 400),
    ERR26("User is not able add", 400),
    ERR27("User is not able updated", 400),
    ERR28("User is not able delete", 400),
    //BookingService
    ERR29("Booking is not able created", 400),
    ERR30("Booking is not able read", 400),
    ERR31("User is not able updated", 400),
    ERR32("Booking is not able add", 400),
    ERR33("Booking is not able delete", 400);
	
	String message;
	int statusCode;

	ErrorCode(String message, int statusCode) {
		this.message = message;
		this.statusCode = statusCode;
	}
	
	public String getMessage() {
		return this.message;
	}
	public int getStatusCode() {
		return this.statusCode;
	}
}
