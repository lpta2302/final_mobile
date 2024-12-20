package com.dev.mail.lpta2302.final_mobile.activities.manage_profile;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dev.mail.lpta2302.final_mobile.MainActivity;
import com.dev.mail.lpta2302.final_mobile.R;
import com.dev.mail.lpta2302.final_mobile.activities.auth.SignupActivity;
import com.dev.mail.lpta2302.final_mobile.logic.global.AuthUser;
import com.dev.mail.lpta2302.final_mobile.logic.user.Gender;
import com.dev.mail.lpta2302.final_mobile.logic.user.User;
import com.dev.mail.lpta2302.final_mobile.logic.user.UserService;
import com.dev.mail.lpta2302.final_mobile.logic.util.ImageUploadService;
import com.dev.mail.lpta2302.final_mobile.logic.util.QueryCallback;
import com.squareup.picasso.Picasso;

import java.time.LocalDate;
import java.util.Calendar;

public class ManageProfileFragment extends Fragment {

    private EditText emailEt, firstNameEt, lastNameEt, dateOfBirthEt;
    private RadioGroup genderRadioGroup;
    private RadioButton maleRadioButton, femaleRadioButton;
    private Button btnSave, btnLogout;
    private LocalDate dateOfBirth;
    private ImageView addImage;
    private Uri selectedImageUri;
    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    selectedImageUri = uri;
                    addImage.setImageURI(uri);
                }
            });
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
                getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // on below line we are setting date to our text view.
                        dateOfBirth = LocalDate.of(year, monthOfYear, dayOfMonth);
                        dateOfBirthEt.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                },
                year, month, day);
        datePickerDialog.show();


    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_manage_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize UI elements
        emailEt = view.findViewById(R.id.emailEt);
        firstNameEt = view.findViewById(R.id.firstNameEt);
        lastNameEt = view.findViewById(R.id.lastNameEt);
        dateOfBirthEt = view.findViewById(R.id.dateOfBirthEt);
        genderRadioGroup = view.findViewById(R.id.genderRadioGroup);
        maleRadioButton = view.findViewById(R.id.maleRadioButton);
        femaleRadioButton = view.findViewById(R.id.femaleRadioButton);
        btnSave = view.findViewById(R.id.btn_save_password);

        btnLogout = new Button(requireContext()); // Create a logout button dynamically if not in XML
        addImage = view.findViewById(R.id.imgAvatar);
        ImageView calenderBtn = view.findViewById(R.id.calendar);
        // Load user data
        loadUserData();

        calenderBtn.setOnClickListener(pickDate);
        dateOfBirthEt.setOnClickListener(pickDate);
        // Handle save button click
        btnSave.setOnClickListener(v -> updateProfile());
        addImage.setOnClickListener(v-> mGetContent.launch("image/*"));
        // Handle logout button click
        btnLogout.setOnClickListener(v -> logout());
    }

    private void loadUserData() {
        User currentUser = AuthUser.getInstance().getUser();

        if (currentUser != null) {
            emailEt.setText(currentUser.getEmail());
            firstNameEt.setText(currentUser.getFirstName());
            lastNameEt.setText(currentUser.getLastName());
            dateOfBirthEt.setText(currentUser.getDateOfBirth() == null ? "" : currentUser.getDateOfBirth().toString());

            if (currentUser.getGender() == Gender.MALE) {
                maleRadioButton.setChecked(true);
            } else if (currentUser.getGender() == Gender.FEMALE) {
                femaleRadioButton.setChecked(true);
            }

            if (currentUser.getAvatar() != null){
                Picasso.get().load(currentUser.getAvatar()).into(addImage);
            }
        }
    }

    private void updateProfile() {
        String email = emailEt.getText().toString().trim();
        String firstName = firstNameEt.getText().toString().trim();
        String lastName = lastNameEt.getText().toString().trim();
        Boolean gender = maleRadioButton.isChecked();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName)) {
            Toast.makeText(requireContext(), "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        User updatedUser = AuthUser.getInstance().getUser();
        updatedUser.setFirstName(firstName);
        updatedUser.setLastName(lastName);
        updatedUser.setDateOfBirth(dateOfBirth);
        updatedUser.setGender(gender ? Gender.MALE : Gender.FEMALE);

        uploadImage(updatedUser);
    }

    private void uploadImage(User user) {
        ImageUploadService.getInstance().uploadImage(selectedImageUri, new ImageUploadService.OnImageUploadListener() {
            @Override
            public void onSuccess(String imageUrl) {
                user.setAvatar(imageUrl);
                AuthUser.getInstance().getUser().setAvatar(imageUrl);
                UserService.getInstance().update(user, new QueryCallback<Void>() {
                    @Override
                    public void onSuccess(Void expectation) {
                        Toast.makeText(getContext(),"Update successfully",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Exception exception) {
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show()    ;
            }
        });
    }

    private void logout() {
        AuthUser.getInstance().setUser(null);
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
    }
}
