package com.szigladam.ourbirds.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.szigladam.ourbirds.OurBirds;
import com.szigladam.ourbirds.R;
import com.szigladam.ourbirds.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private EditText registerName, registerUserName, registerEmail, registerPassword;
    private Button registerButton;
    private TextView loginRedirectText;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerName = findViewById(R.id.register_name);
        registerUserName = findViewById(R.id.register_username);
        registerEmail = findViewById(R.id.register_email);
        registerPassword = findViewById(R.id.register_password);

        registerButton = findViewById(R.id.register_button);
        loginRedirectText = findViewById(R.id.login_redirect_text);

        user = new User();

        auth = OurBirds.getFirebaseAuth();

        database = OurBirds.getFirebaseDatabase();
        reference = database.getReference().child("users");

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = registerName.getText().toString().trim();
                String username = registerUserName.getText().toString().trim();
                String email = registerEmail.getText().toString().trim();
                String password = registerPassword.getText().toString().trim();

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(RegisterActivity.this, "Sikertelen regisztráció: Minden mezőt ki kell tölteni", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "Sikertelen regisztráció: A jelszó túl rövid. Legalább 6 karaktert kell megadni. ", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser(name, username, email, password);
                }
            }
        });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }

    private void registerUser(String name, String username, String email, String password) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    String errorMessage = "";
                    try {
                        throw Objects.requireNonNull(task.getException());
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        errorMessage = "Érvénytelen e-mail cím.";
                    } catch (FirebaseAuthUserCollisionException e) {
                        errorMessage = "Az e-mail cím már használatban van.";
                    } catch (Exception e) {
                        errorMessage = "Ismeretlen hiba történt: " + e.getMessage();
                    }

                    Toast.makeText(RegisterActivity.this, "Sikertelen regisztráció: " + errorMessage, Toast.LENGTH_SHORT).show();
                } else {
                    // Ha a regisztráció sikeres, ellenőrizzük a felhasználónév elérhetőségét
                    DatabaseReference usersReference = database.getReference().child("users");

                    usersReference.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // A felhasználónév már foglalt
                                Toast.makeText(RegisterActivity.this, "Sikertelen regisztráció: A felhasználónév már foglalt. ", Toast.LENGTH_SHORT).show();
                            } else {
                                // A felhasználónév még nem foglalt, regisztráció folytatása
                                FirebaseUser user = auth.getCurrentUser();
                                if (user != null) {
                                    // DisplayName beállítása
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(username)  // Itt használjuk a registerUserName-et
                                            .build();

                                    user.updateProfile(profileUpdates)
                                            .addOnCompleteListener(profileUpdateTask -> {
                                                if (profileUpdateTask.isSuccessful()) {
                                                    //Toast.makeText(RegisterActivity.this, "DisplayName sikeresen beállítva!", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                                saveUserData(name, username, email /*, password*/);

                                //loginActivity onStart() miatt
                                auth.signOut();

                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                Toast.makeText(RegisterActivity.this, "Sikeres regisztráció!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(RegisterActivity.this, "Adatbázishiba: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void saveUserData(String name, String username, String email) {

        LocalDateTime timeNow = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd. HH:mm:ss");
        String regDate = timeNow.format(formatter);

        user.setName(name);
        user.setUsername(username);
        user.setEmail(email);
        user.setRegDate(regDate);

        reference.push().setValue(user);
    }

}