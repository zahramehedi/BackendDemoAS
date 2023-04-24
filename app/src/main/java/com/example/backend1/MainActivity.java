package com.example.backend1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://10.0.2.2:3000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set the activity's layout
        setContentView(R.layout.activity_main);

        //create a new Retrofit object with a base URL and Gson converter factory
        retrofit = new Retrofit.Builder()
                //sets the base URL for the API endpoint.
                .baseUrl(BASE_URL)
                //This specifies the converter factory to use for converting the server response
                // to java objects. In this case were using 'GsonConverterFactory' to convert
                //JSON responses to Java objects using the Google Gson library
                .addConverterFactory(GsonConverterFactory.create())
                .build();
//Create an instance of the 'RetrofitInterface'
        retrofitInterface = retrofit.create(RetrofitInterface.class);
        //set a click listener on the login button
        //A click listener is an interface in Android that listens for click events
        //on a button. When an instance of the 'OnclickListener' is attached to a view it will
        //be notifies when the view is clicked and the 'onClick' method will be called
        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleLoginDialog();
            }
        });

        findViewById(R.id.signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleSignupDialog();
            }
        });

    }
// method that handles the login dialog
    private void handleLoginDialog() {
//inflate the login dialog layout
        //takes an XML file that describes a UI layout and turns it into a corresponding
        // set of 'View' objects that can be displayed on the screen
        View view = getLayoutInflater().inflate(R.layout.login_dialogue, null);

        //create an alert dialog builder with the inflated view
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setView(view).show();

        //get the login button, email and password input fields from the inflated view
        Button loginBtn = view.findViewById(R.id.login);
        final EditText emailEdit = view.findViewById(R.id.emailEdit);
        final EditText passwordEdit = view.findViewById(R.id.passwordEdit);

        //set a click listener on the login button
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //create a HashMap with the email and password entered by the user
                HashMap<String, String> map = new HashMap<>();


                map.put("email", emailEdit.getText().toString());
                map.put("password", passwordEdit.getText().toString());

                //send a login request using the Retrofit interface
                Call<LoginResult> call = retrofitInterface.executeLogin(map);

                call.enqueue(new Callback<LoginResult>() {
                    @Override
                    public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {

                        if (response.code() == 200) {

                            //if the server response is 200 extract the response body
                            LoginResult result = response.body();

                            //create an alert dialog with the user's name and email
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                            builder1.setTitle(result.getName());
                            builder1.setMessage(result.getEmail());

                            builder1.show();

                            // if the server response is 404 display toast message indicating that the credentials are wrong
                        } else if (response.code() == 404) {
                            Toast.makeText(MainActivity.this, "Wrong Credentials",
                                    Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<LoginResult> call, Throwable t) {
                        //display a toast message with the error message if the request fails
                        Toast.makeText(MainActivity.this, t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

    }

    //method that handles the signup dialog
    private void handleSignupDialog() {

        //inflate the signup dialog layout
        View view = getLayoutInflater().inflate(R.layout.signup_dialogue, null);

        //create a new AlertDialog.Builder object and set the inflated layout as its view
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view).show();

        //Get references to the EditText and Button views in the inflated layout
        Button signupBtn = view.findViewById(R.id.signup);
        final EditText nameEdit = view.findViewById(R.id.nameEdit);
        final EditText emailEdit = view.findViewById(R.id.emailEdit);
        final EditText passwordEdit = view.findViewById(R.id.passwordEdit);

        //set an onClickListener for the signup button
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //create a new HashMap to store the user's signup information
                HashMap<String, String> map = new HashMap<>();

                //add the user's name, email, and password to the hashmap
                map.put("name", nameEdit.getText().toString());
                map.put("email", emailEdit.getText().toString());
                map.put("password", passwordEdit.getText().toString());

                //create a new retrofit call to execute the signup request with the given information
                Call<Void> call = retrofitInterface.executeSignup(map);

                //Asynchronous execute the retrofit call and handle the response
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

                        //If the response code is 200 show a "Signed up successfully" toast message
                        if (response.code() == 200) {
                            Toast.makeText(MainActivity.this,
                                    "Signed up successfully", Toast.LENGTH_LONG).show();
                            //if the response is 400 show an 'Already registered' toast message
                        } else if (response.code() == 400) {
                            Toast.makeText(MainActivity.this,
                                    "Already registered", Toast.LENGTH_LONG).show();
                        }

                    }
                    // If the request fails, show an error message toast
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(MainActivity.this, t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

    }
}