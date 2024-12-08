package com.dev.mail.lpta2302.final_mobile.activities.auth;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.dev.mail.lpta2302.final_mobile.MainActivity;
import com.dev.mail.lpta2302.final_mobile.R;
import com.dev.mail.lpta2302.final_mobile.activities.otp.OtpActivity;
import com.dev.mail.lpta2302.final_mobile.logic.global.AuthUser;
import com.dev.mail.lpta2302.final_mobile.logic.user.User;
import com.dev.mail.lpta2302.final_mobile.logic.user.UserService;
import com.dev.mail.lpta2302.final_mobile.logic.util.QueryCallback;

import java.time.LocalDate;
import java.util.Calendar;

public class SignupActivity extends AppCompatActivity {
    private EditText firstNameEditText, lastNameEditText, emailEditText, passwordEditText, confirmPasswordEditText, selectedDateTV;
    private LocalDate dateOfBirth;
    public static final String EMAIL_TAG = "email";
    private View.OnClickListener pickDate = v->{

                // on below line we are getting
                // the instance of our calendar.
                final Calendar c = Calendar.getInstance();

                // on below line we are getting
                // our day, month and year.
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // on below line we are creating a variable for date picker dialog.
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                        SignupActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our text view.
                                dateOfBirth = LocalDate.of(year, monthOfYear, dayOfMonth);
                                selectedDateTV.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        },
                        year, month, day);
                datePickerDialog.show();
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);

        // Initialize the views
        firstNameEditText = findViewById(R.id.firstNameEt);
        lastNameEditText = findViewById(R.id.lastNameEt);
        emailEditText = findViewById(R.id.emailEt);
        passwordEditText = findViewById(R.id.passwordEt);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEt);
        selectedDateTV = findViewById(R.id.dateOfBirthEt);
        ImageView calenderBtn = findViewById(R.id.calender);

        calenderBtn.setOnClickListener(pickDate);
        selectedDateTV.setOnClickListener(pickDate);

        // Set up the signup button
        findViewById(R.id.signUpConfirmButton).setOnClickListener(v -> handleSignup());

        findViewById(R.id.loginLinkButton).setOnClickListener(v -> {
            finish();
        });
    }

    private void handleSignup() {
        // Fetch user inputs
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        // Validate fields
        if (firstName.isEmpty()) {
            firstNameEditText.setError("First Name is required");
            return;
        }

        if (lastName.isEmpty()) {
            lastNameEditText.setError("Last Name is required");
            return;
        }

        if (email.isEmpty()) {
            emailEditText.setError("Email is required");
            return;
        }

        if (dateOfBirth == null) {
            selectedDateTV.setError("Date of birth is required");
            return;
        }

        if (password.isEmpty()) {
            passwordEditText.setError("Password is required");
            return;
        }

        if (confirmPassword.isEmpty()) {
            confirmPasswordEditText.setError("Please confirm your password");
            return;
        }

        if (!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError("Passwords do not match");
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(SignupActivity.this, OtpActivity.class);
        intent.putExtra(EMAIL_TAG, email);
        startForResult.launch(intent);
    }

    private final ActivityResultLauncher<Intent> startForResult =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                Intent data = result.getData();
                if (result.getResultCode() == RESULT_OK && data != null) {
                    boolean isVerified = data.getBooleanExtra(OtpActivity.OTP_RESULT_TAG, false);

                    String firstName = firstNameEditText.getText().toString().trim();
                    String lastName = lastNameEditText.getText().toString().trim();
                    String email = emailEditText.getText().toString().trim();
                    String password = passwordEditText.getText().toString().trim();

                    if (isVerified) signup(firstName, lastName, email, password);
                    else Toast.makeText(this, "OTP không chính xác.", Toast.LENGTH_SHORT).show();
                }
            });

    private void signup(String firstName, String lastName, String email, String password) {
        User user = User
                .builder()
                .id("a")
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .dateOfBirth(dateOfBirth).build();

        Log.d("CHECKKKKKK", "ONSUCCESS");

        UserService.getInstance().create(
                user, password, new QueryCallback<>() {
                    @Override
                    public void onSuccess(String expectation) {
                        toMain(user);
                    }

                    @Override
                    public void onFailure(Exception exception) {
                        Toast.makeText(SignupActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void toMain(User user){
        AuthUser.getInstance().createSession(user);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
