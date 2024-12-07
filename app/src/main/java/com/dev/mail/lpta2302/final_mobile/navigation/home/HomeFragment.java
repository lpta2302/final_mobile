package com.dev.mail.lpta2302.final_mobile.navigation.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.dev.mail.lpta2302.final_mobile.R;

public class HomeFragment extends Fragment {

    private final MutableLiveData<String> mText = new MutableLiveData<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate layout trực tiếp
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // Thiết lập giá trị ban đầu cho LiveData
        mText.setValue("This is home fragment");

        // Ánh xạ View thủ công
        TextView textView = root.findViewById(R.id.text_home);

        // Quan sát LiveData để cập nhật UI
        mText.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                textView.setText(s);
            }
        });

        return root;
    }
}