package de.kevin.philannews;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;

public class NewsManager {

    public static String username = "";
    public static String password = "";
    public static String urlBase = "https://news.bau-pixel.net/philan/";
    //    public static String urlBase = "http://192.168.178.188/neu/news/";
    public static boolean validUser = false;

    /* public static boolean checkCredentials(String name, String unhashedPassword) {
        try {
            InputStream is = new URL(urlBase + "?user=" + name).openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String hashedPassword = NewsActivity.readAll(rd).replace("<p hidden>", "").replace("</p>", "");
            return BCrypt.checkpw(unhashedPassword, hashedPassword);
        } catch (IOException e) {
            return false;
        }
    } */

    public static boolean checkCredentials(String name, String password) {
        if (!isConnected()) return false;
        try {
            InputStream is = new URL(urlBase + "?validateUser&user=" + name + "&password=" + password).openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            return rd.readLine().equals("1");
        } catch (IOException | NullPointerException e) {
            return false;
        }
    }

    public static void logout() {
        if (validUser) {
            username = "";
            password = "";
            validUser = false;
        }
    }

    public static boolean isValidUser() {
        if (username.isEmpty() || password.isEmpty())
            return false;
        return checkCredentials(username, password);
    }

    public static boolean refreshUser() {
        return validUser = isValidUser();
    }

    public static void createNews(String title, String link, String content) {
        if (!isConnected()) return;
        if (!validUser) return;
        try {
            InputStream url = new URL(urlBase + "?createNews&user=" + username + "&password=" + password  + "&title=" + title + "&link=" + link + "&content=" + content).openStream();
            url.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        sendNotifications();
    }

    public static void editNews(String id, String title, String link, String content) {
        if (!isConnected()) return;
        if (!validUser) return;
        try {
            InputStream url = new URL(urlBase + "?editNews&user=" + username + "&password=" + password + "&id=" + id + "&title=" + title + "&link=" + link + "&content=" + content).openStream();
            url.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteNews(String id) {
        if (!isConnected()) return;
        if (!validUser) return;
        try {
            InputStream url = new URL(urlBase + "?deleteNews&user=" + username + "&password=" + password + "&id=" + id).openStream();
            url.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void changePassword(String oldpassword, String password) {
        if (!isConnected()) return;
        if (!validUser) return;
        try {
            InputStream url = new URL(urlBase + "?changePassword&user=" + username + "&password=" + oldpassword + "&newpassword=" + password).openStream();
            url.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isConnected() {
        try {
            return Runtime.getRuntime().exec("ping -c 1 8.8.8.8").waitFor() == 0;
        } catch (InterruptedException | IOException e) {
            return false;
        }
    }

}
