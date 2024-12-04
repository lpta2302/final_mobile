package com.dev.mail.lpta2302.final_mobile;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class UserRepository {
    public UserRepository() {
        db = FirebaseFirestore.getInstance();
    }
    private final FirebaseFirestore db;
    public void isEmailExisting(String email, Callback onResult) {
        try {
            db.collection("users")
                    .whereEqualTo("email", email)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                QuerySnapshot querySnapshot = task.getResult();
                                if (!querySnapshot.isEmpty()) {

                                    onResult.onResultOrError(true, null);
                                } else {

                                    onResult.onResultOrError(false, null);
                                }
                            } else {

                                onResult.onResultOrError(false, null);
                            }
                        }
                    });
        }
        catch (Exception e) {
            onResult.onResultOrError(null, e);
        }
    }

    public void isUserNameExisting(String userName, Callback onResult) {

    }
}
