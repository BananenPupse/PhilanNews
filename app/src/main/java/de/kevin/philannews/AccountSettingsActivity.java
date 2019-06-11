package de.kevin.philannews;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

public class AccountSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        EditText password = findViewById(R.id.newPassword);
        EditText passwordRetyped = findViewById(R.id.newPasswordRetyped);
        Button changePassword = findViewById(R.id.changePasswordButton);
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (password.getText().toString().length() < 8) {
                    password.setError("Das Passwort muss aus mindestens 8 Zeichen bestehen!");
                    changePassword.setEnabled(false);
                    return;
                }
                if (!password.getText().toString().equals(passwordRetyped.getText().toString())) {
                    passwordRetyped.setError("Die Passwörter stimmen nicht überein!");
                    changePassword.setEnabled(false);
                    return;
                }
                changePassword.setEnabled(true);
            }
        };
        password.addTextChangedListener(textWatcher);
        passwordRetyped.addTextChangedListener(textWatcher);

        changePassword.setOnClickListener(v -> {
            if (NewsManager.refreshUser()) {
                String oldpassword = NewsManager.password;
                NewsManager.password = password.getText().toString();
                NewsManager.changePassword(oldpassword, password.getText().toString());
                finish();
            }
        });

        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(v -> {
            NewsManager.logout();
            NewsActivity.getNewsActivity().refreshNews();
            ((FloatingActionButton) NewsActivity.getNewsActivity().findViewById(R.id.createNewsFab)).hide();
            Menu menu = NewsActivity.getNewsActivity().getMenu();
            MenuItem account = menu.findItem(R.id.action_account);
            MenuItem login = menu.findItem(R.id.action_login);
            account.setEnabled(false);
            login.setEnabled(true);
            finish();
        });
    }

}
