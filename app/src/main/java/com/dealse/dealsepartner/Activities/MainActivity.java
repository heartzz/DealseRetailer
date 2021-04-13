package com.dealse.dealsepartner.Activities;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.dealse.dealsepartner.R;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    public static String TAG = "SignInPartneractivity";
    private ProgressBar progress_bar;
    private FloatingActionButton fab;
    private View parent_view;
    EditText input_mobile;
    String email,password;
    private FirebaseAuth mAuth;
    boolean isAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainscreen_activity);
        parent_view = findViewById(android.R.id.content);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);

        input_mobile = (EditText)findViewById(R.id.input_mobile);


        mAuth = FirebaseAuth.getInstance();


        fab = (FloatingActionButton) findViewById(R.id.fab);

        ((View) findViewById(R.id.sign_up_for_account)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent mainscreenIntent = new Intent(MainActivity.this,Merchant_signup.class);
                startActivity(mainscreenIntent);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                validateForm();
            }
        });
    }



    private void validateForm() {

        String email = input_mobile.getText().toString();
        if (email != null && !email.equalsIgnoreCase("") && email.length() == 10) {
            input_mobile.setError(null);
            fab.setAlpha(0f);
            signIn();
        } else {
            input_mobile.setError("Required.");
            progress_bar.setVisibility(View.VISIBLE);
        }
    }

    private void signIn() {

        Intent mainscreenIntent = new Intent(MainActivity.this, Activity_otpscreen.class);
        mainscreenIntent.putExtra("mobile", input_mobile.getText().toString());
        mainscreenIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainscreenIntent);
    }
        //    LoaderDialog.showLoader(MainActivity.this);

        // [START sign_in_with_email]
        /*mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            progress_bar.setVisibility(View.GONE);
                            fab.setAlpha(1f);
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);

                        }

                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            progress_bar.setVisibility(View.GONE);
                            fab.setAlpha(1f);
                        }
    //                    LoaderDialog.hideLoader();
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }*/




            /*FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference(FirebaseConstants.KEY_USERS);
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    List<Partner> userslist = new ArrayList<Partner>();
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        userslist.add(child.getValue(Partner.class));
                    }

                    for (Partner user : userslist) {

                        if (user.getEmaiId().equalsIgnoreCase(input_email.getText().toString()) && user.getPassword().equalsIgnoreCase(input_password.getText().toString())) {

                            progress_bar.setVisibility(View.GONE);
                            fab.setAlpha(1f);


                            isAvailable = true;

                            DealseApplicationsManager.getInstance().getPref(MainActivity.this).edit().putString(FirebaseConstants.FIELD_ID, user.getId()).apply();
                            DealseApplicationsManager.getInstance().getPref(MainActivity.this).edit().putString(FirebaseConstants.FIELD_EMAIL, user.getEmaiId()).apply();
                            DealseApplicationsManager.getInstance().getPref(MainActivity.this).edit().putString(FirebaseConstants.FIELD_NAME, user.getStoreName()).apply();


                            //                            LoaderDialog.hideLoader();

                            Intent mainscreenIntent = new Intent(MainActivity.this, HomeScreen.class);
                            mainscreenIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mainscreenIntent);

                            break;
                        } else {
                            isAvailable = false;
                            continue;
                        }
                    }
                    if (!isAvailable) {
                        //                        LoaderDialog.hideLoader();
                        Toast.makeText(MainActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        progress_bar.setVisibility(View.GONE);
                        fab.setAlpha(1f);

                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to register user.", error.toException());
                    progress_bar.setVisibility(View.GONE);
                    fab.setAlpha(1f);

                }
            });

        }*/



}
