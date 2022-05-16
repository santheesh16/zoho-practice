package com.zoho.booktickets.port;

import com.zoho.booktickets.constant.Constant;
import com.zoho.booktickets.jsonutil.JsonUtil;
import com.zoho.booktickets.service.PortService;
import com.zoho.booktickets.service.TheaterService;
import com.zoho.booktickets.service.ScreenService;
import com.zoho.booktickets.model.Theater;

public class ScreenPort {
    public static void add(long theaterId) {
        try {
            new PortService().post(Constant.PORT_URL+"/screen",
                    JsonUtil.objectToString(new TheaterService().read(theaterId)));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void update(long screenId) {
        try {
            new PortService().update(Constant.PORT_URL+"/screen",
                    JsonUtil.objectToString(new ScreenService().read(screenId)));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void delete(long screenId) {
        try {
            new PortService().delete(Constant.PORT_URL+"/screen?screenId="+screenId);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
