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

        // Nhận các ChipButton
        Chip chipEditProfile = root.findViewById(R.id.chipEditProfile);
        Chip chipChangePassword = root.findViewById(R.id.chipChangePassword);
        Chip chipLogout = root.findViewById(R.id.chipLogout);

        // Sự kiện nhấn chip "Chỉnh sửa hồ sơ"
        chipEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), ManageProfile.class);
                startActivity(intent);
            }
        });

        // Sự kiện nhấn chip "Đổi mật khẩu"
        chipChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), ChangePassword.class);
                startActivity(intent);
            }
        });

//        // Sự kiện nhấn chip "Đăng xuất"
//        chipLogout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Hiển thị AlertDialog
//                new AlertDialog.Builder(requireContext())
//                        .setMessage("Bạn có chắc chắn muốn thoát?")
//                        .setCancelable(false)
//                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                // Chuyển hướng về trang Login
//                                Intent intent = new Intent(requireActivity(), MainActivity.class);
//                                startActivity(intent);
//                                requireActivity().finish(); // Đóng activity hiện tại
//                            }
//                        })
//                        .setNegativeButton("Không", null) // Đóng dialog nếu chọn "Không"
//                        .show();
//            }
//        });

        return root;
    }
}
