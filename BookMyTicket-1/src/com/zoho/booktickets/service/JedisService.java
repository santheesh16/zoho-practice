package com.zoho.booktickets.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import com.zoho.booktickets.model.ADUser;

import redis.clients.jedis.Jedis;


public class JedisService {
    Jedis jedis;
    String userKey = "user";
    int userCount;
    int MAX_USER_CACHE = 5;
    Queue<ADUser> usersDetail = new LinkedList<ADUser>();

    public JedisService() {
        this.jedis = new Jedis("localhost", 6379);
        jedis.auth("Zoho@2022");
    }

    public void usersAddQueue() {

        userCount = 0;
        for (ADUser adUser : usersDetail) {
            Map<String, String> messageBody = new HashMap<>();
            messageBody.put("userName", adUser.getUserName());
            messageBody.put("password", adUser.getPassword());
            String messageId = null;
            if (userCount < MAX_USER_CACHE) {
                messageId = jedis.hmset(
                        userKey + String.valueOf(++userCount),
                        messageBody);
                System.out.println("User No: " + userCount + "-" + adUser);
            }
        }
    }

    public void setUserCache(ADUser adUser) {

        userCount = getQueueUserCacheLength();
        Map<String, String> messageBody = new HashMap<>();
        messageBody.put("userName", adUser.getUserName());
        messageBody.put("password", adUser.getPassword());
        String messageId = null;
        if (userCount < MAX_USER_CACHE) {
            messageId = jedis.hmset(
                    userKey + String.valueOf(++userCount),
                    messageBody);
            System.out.println("User No: " + userCount + " " + messageId);
        } else {
            usersDetail.poll();
            usersDetail.add(adUser);
            usersAddQueue();
        }

    }

    public Boolean checkUserCacheExists(String userKey) {
        return jedis.exists(userKey);
    }

    public Long getHashLength() {
        return jedis.del(userKey);
    }

    public String checkUserCacheValue(String userName) {
        try {
            for (int i = 1; i <= MAX_USER_CACHE; i++) {
                String userNameCache = jedis.hget(userKey+String.valueOf(i), "userName");
                String userPasswordCache = jedis.hget(userKey+String.valueOf(i), "password");
                if(userNameCache.equals(userName)){
                    return userPasswordCache;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
        return null;
    }

    public Map<String, String> getAllUserCache(String userKey) {
        return jedis.hgetAll(userKey);
    }

    public int getQueueUserCacheLength() {

        for (int i = 1; i <= MAX_USER_CACHE; i++) {
            if (checkUserCacheExists(userKey + String.valueOf(i))) {
                Map<String, String> aduser = getAllUserCache(userKey + String.valueOf(i));
                ADUser adUser = new ADUser(aduser.get("userName"), aduser.get("password"));
                usersDetail.add(adUser);
            } else {
                break;
            }
        }
        return usersDetail.size();
    }

    public Queue<ADUser> getQueueUserCache() {

        for (int i = 1; i <= MAX_USER_CACHE; i++) {
            if (checkUserCacheExists(userKey + String.valueOf(i))) {
                Map<String, String> aduser = getAllUserCache(userKey + String.valueOf(i));
                usersDetail.add(new ADUser(aduser.get("userName"), aduser.get("password")));
            } else {
                break;
            }
        }
        return usersDetail;
    }

    public Long deleteUserCache(String userKey) {
        return jedis.del(userKey);
    }

    public void deleteAllUserCache() {
        userCount = 0;
        while (userCount < MAX_USER_CACHE) {
            System.out.println(jedis.del(userKey + String.valueOf(++userCount)));
        }
    }

    public static void main(String[] args) {
        JedisService jedisService = new JedisService();
        // System.out.println(jedisService.setUserCache(new ADUser("Santheesh1",
        // "Zoho@2022")));
        // System.out.println(jedisService.setUserCache(new ADUser("Santheesh2",
        // "Zoho@2022")));
        // System.out.println(jedisService.setUserCache(new ADUser("Santheesh3",
        // "Zoho@2022")));
        // System.out.println(jedisService.setUserCache(new ADUser("Santheesh4",
        // "Zoho@2022")));
        // System.out.println(jedisService.setUserCache(new ADUser("Santheesh7",
        // "Zoho@2022")));
        // System.out.println(jedisService.usersDetail.poll());
        // System.out.println(jedisService.usersDetail);
        // System.out.println(new JedisService().deleteUserCache("user6"));
        // System.out.println(new JedisService().getAllUserCache("user4"));
        // System.out.println(JedisService.usersDetail.peek());
        // Queue<ADUser> users = new JedisService().getQueueUserCache("user");
        // System.out.println(new JedisService("user").usersDetail);
        // System.out.println(new JedisService().getQueueUserCache("user"));
        //  System.out.println(new JedisService().checkUserCacheValue("Santheesh3"));

        new JedisService().deleteAllUserCache();
        // System.out.println((new JedisService().getQueueUserCache("user")).peek());
    }
}
