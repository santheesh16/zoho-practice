package com.zoho.booktickets.jsonutil;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class PasswordUtils {

    public static String getHashString(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            BigInteger number = new BigInteger(1, md.digest(password.getBytes(StandardCharsets.UTF_8)));

            StringBuilder hexString = new StringBuilder(number.toString(16));

            while (hexString.length() < 64) {
                hexString.insert(0, '0');
            }

            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean verifyPassword(String password, String hashedPassword) {
        if (getHashString(password).equals(hashedPassword)) {
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
        String myPassword = "Zoho@2022";

        // Protect user's password. The generated value can be stored in DB.
        String mySecurePassword = PasswordUtils.getHashString(myPassword);

        // Print out protected password
        System.out.println("My secure password = " + mySecurePassword);// = yrf0h7O2SZw+SNPxM3T3uA==

        System.out.println("Decrypt password= " + mySecurePassword);
        System.out.println("Decrypt password= " + PasswordUtils.verifyPassword(myPassword, mySecurePassword));
    }
}