package com.zoho.booktickets.port;

import com.zoho.booktickets.constant.Constant;
import com.zoho.booktickets.jsonutil.JsonUtil;
import com.zoho.booktickets.service.PortService;
import com.zoho.booktickets.service.BookingService;

public class BookingPort {
    public static void add(long bookingId) {
        try {
            new PortService().post(Constant.PORT_URL+"/booking",
                    JsonUtil.objectToString(new BookingService().read(bookingId)));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void update(long bookingId) {
        try {
            new PortService().update(Constant.PORT_URL+"/booking", JsonUtil.objectToString(new BookingService().read(bookingId)));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void delete(long bookingId) {
        try {
            new PortService().delete(Constant.PORT_URL+"/booking?bookedId="+bookingId);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
