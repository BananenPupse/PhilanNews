package de.kevin.philannews;

import android.annotation.SuppressLint;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

class NewsManager {

    static String username = "";
    static String password = "";
    static String nickname = "";
    static String urlBase = "https://news.bau-pixel.net/philan/";
    //    public static String urlBase = "http://192.168.178.188/neu/news/";
    static boolean validUser = false;

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

    static boolean checkCredentials(String name, String password) {
        if (isDisconnected()) return false;
        try {
            InputStream is = new URL(urlBase + "?validateUser&user=" + name + "&password=" + password).openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            return rd.readLine().equals("1");
        } catch (IOException | NullPointerException e) {
            return false;
        }
    }

    static void logout() {
        if (validUser) {
            username = "";
            password = "";
            validUser = false;
        }
    }

    static boolean isValidUser() {
        if (username.isEmpty() || password.isEmpty())
            return false;
        return checkCredentials(username, password);
    }

    static boolean refreshUser() {
        nickname = fetchNickname();
        return validUser = isValidUser();
    }

    static void createNews(String title, String summary, String content) {
        if (isDisconnected()) return;
        if (!validUser) return;
        try {
            @SuppressLint("SimpleDateFormat") String date = new SimpleDateFormat("dd.MM.yyyy").format(new Date());
            InputStream url = new URL(urlBase + "?createNews&user=" + username + "&password=" + password  + "&title=" + title + "&summary=" + summary + "&content=" + content + "&author=" + nickname + "&at=" + date).openStream();
            url.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void editNews(String id, String title, String summary, String content) {
        if (isDisconnected()) return;
        if (!validUser) return;
        try {
            InputStream url = new URL(urlBase + "?editNews&user=" + username + "&password=" + password + "&id=" + id + "&title=" + title + "&summary=" + summary + "&content=" + content).openStream();
            url.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void deleteNews(String id) {
        if (isDisconnected()) return;
        if (!validUser) return;
        try {
            InputStream url = new URL(urlBase + "?deleteNews&user=" + username + "&password=" + password + "&id=" + id).openStream();
            url.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void changePassword(String oldpassword, String password) {
        if (isDisconnected()) return;
        if (!validUser) return;
        try {
            InputStream url = new URL(urlBase + "?changePassword&user=" + username + "&password=" + oldpassword + "&newpassword=" + password).openStream();
            url.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static boolean isDisconnected() {
        try {
            return Runtime.getRuntime().exec("ping -c 1 8.8.8.8").waitFor() != 0;
        } catch (InterruptedException | IOException e) {
            return true;
        }
    }

    static String fetchNickname() {
        if (isDisconnected()) return "";
        try {
            InputStream url = new URL(urlBase + "?getNickname&user=" + username).openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(url, Charset.forName("UTF-8")));
            String str = rd.readLine();
            url.close();
            return str;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    static void changeNickname(String nick) {
        if (!isValidUser()) return;
        try {
            InputStream url = new URL(urlBase + "?setNickname&user=" + username + "&password=" + password + "&nickname=" + nick).openStream();
            url.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        refreshUser();
    }

}
