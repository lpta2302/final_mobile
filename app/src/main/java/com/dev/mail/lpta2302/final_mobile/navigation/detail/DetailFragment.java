package com.dev.mail.lpta2302.final_mobile.navigation.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.dev.mail.lpta2302.final_mobile.R;

public class DetailFragment extends Fragment {
    private GridLayout gridLayout;
    private ImageView mainImageView; // ImageView chính


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate layout for this fragment
        View root = inflater.inflate(R.layout.fragment_detail, container, false);

        // Lấy GridLayout từ layout
        gridLayout = root.findViewById(R.id.gridLayout);
        mainImageView = root.findViewById(R.id.imageView);
        mainImageView.setImageResource(R.drawable.avatar1);
        // Cấu hình LayoutParams để chỉnh kích thước mainImageView
        ViewGroup.LayoutParams mainImageParams = mainImageView.getLayoutParams();
        mainImageParams.width = 500; // Chiều rộng mới (px)
        mainImageParams.height = 500; // Chiều cao mới (px)
        mainImageView.setLayoutParams(mainImageParams);


        // Thêm 10 ảnh vào GridLayout
        for (int i = 0; i < 29; i++) {
            // Tạo một ImageView mới
            ImageView imageView = new ImageView(getContext());

            // Đặt ảnh cho ImageView
            imageView.setImageResource(R.drawable.avatar1);

            // Cấu hình LayoutParams
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 300; // Chiều rộng cố định (px)
            params.height = 300; // Chiều cao cố định (px)
            params.setMargins(29, 29, 29, 29); // Khoảng cách giữa các ô
            // Đặt Gravity cho ImageView
            params.setGravity(android.view.Gravity.CENTER);
            imageView.setLayoutParams(params);

            // Thêm ImageView vào GridLayout
            gridLayout.addView(imageView);
        }

        return root;
    }
}
