package com.zoho.booktickets.port;

import com.zoho.booktickets.constant.Constant;
import com.zoho.booktickets.jsonutil.JsonUtil;
import com.zoho.booktickets.service.PortService;
import com.zoho.booktickets.service.UserService;
import com.zoho.booktickets.service.TheaterService;

public class TheaterPort {

    public static void add(long theaterId) {
        try {
            new PortService().post(Constant.PORT_URL+"/theater",
                    JsonUtil.objectToString(new TheaterService().read(theaterId)));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void update(long theaterId) {
        try {
            new PortService().update(Constant.PORT_URL+"/theater",
                    JsonUtil.objectToString(new TheaterService().read(theaterId)));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void delete(long theaterId) {
        try {
            new PortService().delete(Constant.PORT_URL+"/theater?theaterId="+theaterId);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}