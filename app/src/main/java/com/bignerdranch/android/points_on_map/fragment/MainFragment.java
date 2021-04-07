package com.bignerdranch.android.points_on_map.fragment;

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
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.points_on_map.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainFragment extends Fragment {
    public Button log;
    public TextView register, forgotPassword;
    public EditText etEmail, etPassword;
    public FirebaseAuth mAuth;
    public ProgressBar mProgressBarMain;
    NavController navController;
    Bundle mBundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        log = view.findViewById(R.id.log);
        register = view.findViewById(R.id.tvRegister);
        forgotPassword = view.findViewById(R.id.tvForgot_PasswordMain);
        etEmail = view.findViewById(R.id.editTextEmailAddress);
        etPassword = view.findViewById(R.id.editTextPassword);
        mAuth = FirebaseAuth.getInstance();
        mProgressBarMain = view.findViewById(R.id.progressBarMain);
        mProgressBarMain.setVisibility(View.GONE);

        navController = Navigation.findNavController(view);
        mBundle = new Bundle();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.registerUserFragment);
            }
        });

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                navController.navigate(R.id.forgotPasswordFragment);
            }
        });
    }


    private void userLogin() {

        String UserEmail = etEmail.getText().toString().trim();
        String UserPassword = etPassword.getText().toString().trim();

        if(UserEmail.isEmpty()){
            etEmail.setError(getString(R.string.enter_email));
            etEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(UserEmail).matches()){
            etEmail.setError(getString(R.string.enter_existing_email));
            etEmail.requestFocus();
            return;
        }

        if(UserPassword.isEmpty()){
            etPassword.setError(getString(R.string.enter_pass));
            etPassword.requestFocus();
            return;
        }

        if(UserPassword.length() < 6){
            etPassword.setError(getString(R.string.short_pass));
            etPassword.requestFocus();
            return;
        }

        mProgressBarMain.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(UserEmail, UserPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            if(user.isEmailVerified()) {
                                String email = etEmail.getText().toString();
                                mBundle.putString("TitleArg", email);
                                navController.navigate(R.id.mapScreenFragment2, mBundle);

                            }else {
                                user.sendEmailVerification();
                                Toast.makeText(getContext(),
                                        R.string.verify_account,
                                        Toast.LENGTH_LONG).show();
                            }
                            mProgressBarMain.setVisibility(View.GONE);
                        }else {
                            Toast.makeText(getContext(),
                                    R.string.error_pass_email,
                                    Toast.LENGTH_LONG).show();
                            mProgressBarMain.setVisibility(View.GONE);
                        }
                    }
                });
    }
}