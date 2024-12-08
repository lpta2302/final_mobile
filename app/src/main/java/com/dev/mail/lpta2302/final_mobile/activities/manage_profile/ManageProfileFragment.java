package com.dev.mail.lpta2302.final_mobile.activities.manage_profile;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dev.mail.lpta2302.final_mobile.MainActivity;
import com.dev.mail.lpta2302.final_mobile.R;
import com.dev.mail.lpta2302.final_mobile.global.AuthUser;
import com.dev.mail.lpta2302.final_mobile.user.Gender;
import com.dev.mail.lpta2302.final_mobile.user.User;
import com.dev.mail.lpta2302.final_mobile.user.UserService;
import com.dev.mail.lpta2302.final_mobile.util.QueryCallback;

import java.time.LocalDate;

public class ManageProfileFragment extends Fragment {

    private EditText emailEt, firstNameEt, lastNameEt, dateOfBirthEt;
    private RadioGroup genderRadioGroup;
    private RadioButton maleRadioButton, femaleRadioButton;
    private Button btnSave, btnLogout;

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

        // Load user data
        loadUserData();

        // Handle save button click
        btnSave.setOnClickListener(v -> updateProfile());

        // Handle logout button click
        btnLogout.setOnClickListener(v -> logout());
    }

    private void loadUserData() {
        User currentUser = AuthUser.getInstance().getUser();

        if (currentUser != null) {
            emailEt.setText(currentUser.getEmail());
            firstNameEt.setText(currentUser.getFirstName());
            lastNameEt.setText(currentUser.getLastName());
            dateOfBirthEt.setText(currentUser.getDateOfBirth().toString());

            if (currentUser.getGender() == Gender.MALE) {
                maleRadioButton.setChecked(true);
            } else if (currentUser.getGender() == Gender.FEMALE) {
                femaleRadioButton.setChecked(true);
            }
        }
    }

    private void updateProfile() {
        String email = emailEt.getText().toString().trim();
        String firstName = firstNameEt.getText().toString().trim();
        String lastName = lastNameEt.getText().toString().trim();
        String dateOfBirth = dateOfBirthEt.getText().toString().trim();
        Boolean gender = maleRadioButton.isChecked();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName)) {
            Toast.makeText(requireContext(), "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        User updatedUser = new User();
        updatedUser.setEmail(email);
        updatedUser.setFirstName(firstName);
        updatedUser.setLastName(lastName);
        updatedUser.setDateOfBirth(LocalDate.parse(dateOfBirth));
        updatedUser.setGender(gender ? Gender.MALE : Gender.FEMALE);

        UserService.getInstance().update(updatedUser, new QueryCallback<Void>() {
            @Override
            public void onSuccess(Void expectation) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Exception exception) {

            }
        });
    }

    private void logout() {
        AuthUser.getInstance().setUser(null);
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
    }
}
