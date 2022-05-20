package com.zoho.booktickets.port;

import com.zoho.booktickets.constant.Constant;
import com.zoho.booktickets.jsonutil.JsonUtil;
import com.zoho.booktickets.service.PortService;
import com.zoho.booktickets.service.UserService;

public class UserPort {

    public static void add(long userId) {
        try {
            new PortService().post(Constant.PORT_URL+"/user",
                    JsonUtil.objectToString(new UserService().read(userId)));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void update(long userId) {
        try {
            new PortService().update(Constant.PORT_URL+"/user",
                    JsonUtil.objectToString(new UserService().read(userId)));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void delete(long userId) {

        try {
            new PortService().delete(Constant.PORT_URL+"/user?userId="+userId);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}