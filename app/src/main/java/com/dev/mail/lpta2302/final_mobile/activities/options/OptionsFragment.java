package com.dev.mail.lpta2302.final_mobile.activities.options;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dev.mail.lpta2302.final_mobile.ChangePassword;
import com.dev.mail.lpta2302.final_mobile.ManageProfile;
import com.dev.mail.lpta2302.final_mobile.R;
import com.google.android.material.chip.Chip;

public class OptionsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate layout for this fragment
        View root = inflater.inflate(R.layout.fragment_options, container, false);

        return root;
    }
}
