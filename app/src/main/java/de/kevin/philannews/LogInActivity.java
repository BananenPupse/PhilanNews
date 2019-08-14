package de.kevin.philannews;

import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

public class LogInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        EditText username = findViewById(R.id.usernameField);
        EditText password = findViewById(R.id.passwordField);
        Button loginButton = findViewById(R.id.loginButton);
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (username.getText().toString().length() < 3 || username.getText().toString().contains(" ")) {
                    username.setError("Kein gültiger Nutzername!");
                    loginButton.setEnabled(false);
                    return;
                }
                if (password.getText().toString().length() < 8) {
                    password.setError("Das Passwort muss mindestens 8 Zeichen haben!");
                    loginButton.setEnabled(false);
                    return;
                }
                loginButton.setEnabled(true);
            }
        };
        username.addTextChangedListener(textWatcher);
        password.addTextChangedListener(textWatcher);

        loginButton.setOnClickListener(v -> {
            if (NewsManager.checkCredentials(username.getText().toString(), password.getText().toString())) {
                NewsManager.username = username.getText().toString();
                NewsManager.password = password.getText().toString();
                ((FloatingActionButton) NewsActivity.getNewsActivity().findViewById(R.id.createNewsFab)).show();
                Menu menu = NewsActivity.getNewsActivity().getMenu();
                MenuItem account = menu.findItem(R.id.action_account);
                MenuItem login = menu.findItem(R.id.action_login);
                account.setEnabled(true);
                login.setEnabled(false);
                NewsActivity.getNewsActivity().refreshNews();
                NewsManager.refreshUser();
                finish();
            } else {
                Snackbar.make(v, "Falsche Login-Daten", Snackbar.LENGTH_LONG).setAction("Action", null).show();


            }
        });
    }
}
