package de.kevin.philannews;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.gitonway.lee.niftymodaldialogeffects.lib.ColorUtils;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class NewsActivity extends AppCompatActivity {

    private static NewsActivity newsActivity;
    private Menu menu;

    public Menu getMenu() {
        return menu;
    }

    public static NewsActivity getNewsActivity() {
        return newsActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newsActivity = this;
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.createNewsFab);
        if (!NewsManager.validUser)
            fab.hide();
        else
            fab.show();
        fab.setOnClickListener(v -> {
            if (NewsManager.isValidUser()) startActivity(new Intent(newsActivity, CreateNewsActivity.class));
        });

        FloatingActionButton refreshFab = findViewById(R.id.refresh);
        refreshFab.setOnClickListener(this::refreshNews);
        refreshNews();

        Intent intent = new Intent(this, CreditsActivity.class);
        intent.setAction("loadApp");
        startActivity(intent);

        SharedPreferences preferences = getSharedPreferences("settings", MODE_PRIVATE);
        if (!preferences.contains("notify")) {
            showNotifyDialog(preferences);
        } else {
            updateSubscription(preferences);
        }
    }

    private void refreshNews(View view) {
        refreshNews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_login) {
            startActivity(new Intent(this, LogInActivity.class));
            return true;
        }

        if (id == R.id.action_account) {
            startActivity(new Intent(this, AccountSettingsActivity.class));
            return true;
        }

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        if (id == R.id.action_credits) {
            startActivity(new Intent(this, CreditsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void refreshNews() {
        NewsManager.refreshUser();
        ((Runnable) () -> {
            ListView listView = findViewById(R.id.listView);
            List<NewsAdapter.News> news = new ArrayList<>();
            for (String[] strs : getNews())
                news.add(new NewsAdapter.News(strs[0], strs[1], strs[2], strs[3]));
            listView.setOnItemClickListener((parent, view, position, id) -> {
                Intent openURL = new Intent(android.content.Intent.ACTION_VIEW);
                openURL.setData(Uri.parse(news.get(position).getLink()));
                startActivity(openURL);
            });
            listView.setAdapter(new NewsAdapter(this, news));
        }).run();
    }

    public List<String[]> getNews() {
        List<String[]> news = new ArrayList<>();
        if(!NewsManager.isConnected()) {
            news.add(new String[]{"-1", "Oh nein!", "http://philan.de/news", "Es konnte keine Verbindung zum Server aufgebaut werden."});
            return news;
        }
        try {
            String sURL =  NewsManager.urlBase + "?newsjson";
            JSONObject json = readJsonFromUrl(sURL);
            JSONArray jsonArray = json.getJSONArray("news");
            if (jsonArray.length() == 0) {
                news.add(new String[]{"-1", "Oh nein!", "http://philan.de/news", "Es konnte keine Verbindung zum Server aufgebaut werden."});
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONArray array = jsonArray.getJSONArray(i);
                news.add(new String[]{array.getString(0), array.getString(1), array.getString(2), array.getString(3).replace("%nl%", "\r\n")});
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return news;
    }

    public static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    public void showNotifyDialog(SharedPreferences preferences) {
        NiftyDialogBuilder builder = NiftyDialogBuilder.getInstance(this);
        builder.withTitle("Benachrichtigungen").withMessage("Möchtest du bei neuen News benachrichtigt werden?").withButton1Text("Ja").withButton2Text("Nein");
        builder.withDialogColor(Color.GRAY);
        builder.isCancelable(false);
        builder.setButton1Click(v -> {
            preferences.edit().putBoolean("notify", true).apply();
            builder.dismiss();
            Toast.makeText(this, "Du möchtest bei neuen News benachrichtigt werden :)", Toast.LENGTH_LONG).show();
            updateSubscription(getSharedPreferences("settings", MODE_PRIVATE));
        });
        builder.setButton2Click(v -> {
            preferences.edit().putBoolean("notify", false).apply();
            builder.dismiss();
            Toast.makeText(this, "Du möchtest bei neuen News nicht benachrichtigt werden :(", Toast.LENGTH_LONG).show();
            updateSubscription(getSharedPreferences("settings", MODE_PRIVATE));
        });
        builder.show();
    }

    public void updateSubscription(SharedPreferences preferences) {
        if (preferences.getBoolean("notify", false))
            FirebaseMessaging.getInstance().subscribeToTopic("global");
        else
            FirebaseMessaging.getInstance().unsubscribeFromTopic("global");
    }
}
