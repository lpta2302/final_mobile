package com.dev.mail.lpta2302.final_mobile.activities.friends;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.mail.lpta2302.final_mobile.R;
import com.dev.mail.lpta2302.final_mobile.friend.FriendService;
import com.dev.mail.lpta2302.final_mobile.friend.Friendship;
import com.dev.mail.lpta2302.final_mobile.global.AuthUser;
import com.dev.mail.lpta2302.final_mobile.user.User;
import com.dev.mail.lpta2302.final_mobile.user.UserService;
import com.dev.mail.lpta2302.final_mobile.util.QueryCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FriendsFragment extends Fragment {
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

        // Set button click listeners
        setupButtonListeners();

        // Load default view
        loadInvitations();

        return root;
    }

    private void setupButtonListeners() {
        invitationButton.setOnClickListener(v -> {
            setActiveButton(invitationButton);
            loadInvitations();
        });

        suggestedButton.setOnClickListener(v -> {
            setActiveButton(suggestedButton);
            loadSuggestedUsers();
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
        activeButton.setBackgroundTintList(getContext().getColorStateList(R.color.main));
    }

    private void loadInvitations() {
        FriendService.getInstance().findAll(AuthUser.getInstance().getUser(), new QueryCallback<List<Friendship>>() {
            @Override
            public void onSuccess(List<Friendship> expectation) {
                Log.d("Invitations", "Found " + expectation.size() + " invitations");
                friendsList.clear();
                friendsList.addAll(expectation);
                friendsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Exception exception) {
                Log.e("Invitations", "Error loading invitations", exception);
            }
        });
    }

    private void loadSuggestedUsers() {
        UserService.getInstance().readUsers(new QueryCallback<List<User>>() {
            @Override
            public void onSuccess(List<User> expectation) {
                Log.d("Suggested Users", "Found " + expectation.size() + " suggested users");
                List<Friendship> friendships = expectation.stream().map(user->
                        Friendship.builder()
                                .user1(AuthUser.getInstance().getUser())
                                .user2(user)
                                .build()).collect(Collectors.toList());
                friendsList.clear();
                friendsList.addAll(friendships);
                friendsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Exception exception) {
                Log.e("Suggested Users", "Error loading suggested users", exception);
            }
        });
    }

    private void loadMyFriends() {
        friendsList.clear();
        friendsList.addAll(AuthUser.getInstance().getFriends());
        friendsAdapter.notifyDataSetChanged();
    }
}
