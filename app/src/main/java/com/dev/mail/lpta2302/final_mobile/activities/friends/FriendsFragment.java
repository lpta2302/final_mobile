package com.dev.mail.lpta2302.final_mobile.activities.friends;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.mail.lpta2302.final_mobile.R;
import com.dev.mail.lpta2302.final_mobile.logic.friend.FriendService;
import com.dev.mail.lpta2302.final_mobile.logic.friend.FriendStatus;
import com.dev.mail.lpta2302.final_mobile.logic.friend.Friendship;
import com.dev.mail.lpta2302.final_mobile.logic.global.AuthUser;
import com.dev.mail.lpta2302.final_mobile.logic.user.User;
import com.dev.mail.lpta2302.final_mobile.logic.user.UserService;
import com.dev.mail.lpta2302.final_mobile.logic.util.QueryCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FriendsFragment extends Fragment {
    private List<Friendship> suggestedUsers;
    private RecyclerView recyclerView;
    private FriendsAdapter friendsAdapter;
    private List<Friendship> friendsList;
    private Button invitationButton, suggestedButton, myFriendButton;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_friends, container, false);

        // Initialize RecyclerView
        recyclerView = root.findViewById(R.id.recyclerView_friends);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize Buttons
        invitationButton = root.findViewById(R.id.invitation);
        suggestedButton = root.findViewById(R.id.suggested);
        myFriendButton = root.findViewById(R.id.myfriend);

        // Setup default state
        friendsList = new ArrayList<>();
        friendsAdapter = new FriendsAdapter(friendsList);
        recyclerView.setAdapter(friendsAdapter);


        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UserService.getInstance().readUsers(new QueryCallback<List<User>>() {
            @Override
            public void onSuccess(List<User> expectation) {
                Log.d("Suggested Users", "Found " + expectation.size() + " suggested users");
                suggestedUsers = expectation.stream().filter(user->
                                !user.getId().equals(AuthUser.getInstance().getUser().getId()) &&
                                !AuthUser.getInstance().getFriends().stream().anyMatch(fr->fr.getUser2().getId().equals(user.getId())))
                        .map(user->Friendship.builder()
                                .user1(AuthUser.getInstance().getUser())
                                .user2(user)
                                .build()
                ).collect(Collectors.toList());

                // Set button click listeners
                setupButtonListeners();
            }

            @Override
            public void onFailure(Exception exception) {
                Log.e("Suggested Users", "Error loading suggested users", exception);
            }
        });

    }

    private void setupButtonListeners() {
        invitationButton.setOnClickListener(v -> {
            setActiveButton(invitationButton);
            loadInvitations();
        });

        suggestedButton.setOnClickListener(v -> {
            setActiveButton(suggestedButton);
            friendsList.clear();
            friendsList.addAll(suggestedUsers);
            friendsAdapter.notifyDataSetChanged();
        });

        myFriendButton.setOnClickListener(v -> {
            setActiveButton(myFriendButton);
            loadMyFriends();
        });
    }

    private void setActiveButton(Button activeButton) {
        // Reset all buttons to default color
        invitationButton.setBackgroundTintList(getContext().getColorStateList(R.color.sub2));
        suggestedButton.setBackgroundTintList(getContext().getColorStateList(R.color.sub2));
        myFriendButton.setBackgroundTintList(getContext().getColorStateList(R.color.sub2));

        // Highlight the active button
        activeButton.setBackgroundTintList(getContext().getColorStateList(R.color.brand));
    }

    private void loadInvitations() {
        FriendService.getInstance().findAll(AuthUser.getInstance().getUser(), new QueryCallback<List<Friendship>>() {
            @Override
            public void onSuccess(List<Friendship> expectation) {
                Log.d("Invitations", "Found " + expectation.size() + " invitations");
                expectation = expectation.stream()
                        .filter(f -> f.getStatus() == FriendStatus.PENDING // Check if status is PENDING
                                && !f.getUser1().getId().equals(AuthUser.getInstance().getUser().getId())) // Check if user1's id is not the current user's id
                        .collect(Collectors.toList());                friendsList.clear();
                friendsList.addAll(expectation);
                friendsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Exception exception) {
                Log.e("Invitations", "Error loading invitations", exception);
                friendsList.clear();
                friendsList.addAll(new ArrayList<>());
                friendsAdapter.notifyDataSetChanged();
            }
        });
    }

    private void loadMyFriends() {

        friendsList.clear();
        friendsList.addAll(
                AuthUser.getInstance().getFriends()
                        .stream().filter(fr->fr.getStatus().equals(FriendStatus.ACCEPTED)).collect(Collectors.toList())
        );
        friendsAdapter.notifyDataSetChanged();
    }
}
