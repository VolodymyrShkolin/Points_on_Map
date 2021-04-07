package com.bignerdranch.android.points_on_map.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bignerdranch.android.points_on_map.R;
import com.bignerdranch.android.points_on_map.user.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterUserFragment extends Fragment {
    private FirebaseAuth mAuth;
    private EditText mEmailReg, mPassReg;
    public Button mRegUser;
    private ProgressBar mProgressBar;
    NavController navController;
    Bundle mBundle;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_register_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mEmailReg = view.findViewById(R.id.editTextRegEmailAddress);
        mPassReg = view.findViewById(R.id.editTextRegPassword);
        mProgressBar = view.findViewById(R.id.progressBarReg);
        mProgressBar.setVisibility(View.GONE);
        mRegUser = view.findViewById(R.id.regUser);

        navController = Navigation.findNavController(view);
        mBundle = new Bundle();

        mRegUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String UserEmail = mEmailReg.getText().toString().trim();
        String UserPassword = mPassReg.getText().toString().trim();

        if(UserEmail.isEmpty()){
            mEmailReg.setError(getString(R.string.enter_email));
            mEmailReg.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(UserEmail).matches()){
            mEmailReg.setError(getString(R.string.enter_existing_email));
            mEmailReg.requestFocus();
            return;
        }

        if(UserPassword.isEmpty()){
            mPassReg.setError(getString(R.string.enter_pass));
            mPassReg.requestFocus();
            return;
        }

        if(UserPassword.length() < 6){
            mPassReg.setError(getString(R.string.short_pass));
            mPassReg.requestFocus();
            return;
        }

        mProgressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(UserEmail, UserPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User(UserEmail, UserPassword);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        String email = mEmailReg.getText().toString();
                                        mBundle.putString("TitleArg", email);
                                        navController.navigate(R.id.mapScreenFragment2, mBundle);

//                                        Intent intent = new Intent(getContext(), MapScreenFragment.class);
//                                        intent.putExtra("email", mEmailReg.getText().toString());
//                                        getContext().startActivity(intent);
                                        Toast.makeText(getContext(), R.string.user_registered,
                                                Toast.LENGTH_LONG).show();
                                        mProgressBar.setVisibility(View.GONE);
                                    }else {
                                        Toast.makeText(getContext(), R.string.not_registered,
                                                Toast.LENGTH_LONG).show();
                                        mProgressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }else {
                            Toast.makeText(getContext(), R.string.not_registered,
                                    Toast.LENGTH_LONG).show();
                            mProgressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}