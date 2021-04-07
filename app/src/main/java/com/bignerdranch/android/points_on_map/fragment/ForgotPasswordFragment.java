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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class ForgotPasswordFragment extends Fragment {
    EditText etResetEmail;
    Button btnResetEmail;
    ProgressBar resetPB;
    FirebaseAuth mAuth;
    NavController mNavController;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_forgot_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etResetEmail = view.findViewById(R.id.resetEmail);
        resetPB = view.findViewById(R.id.progressBarReset);
        resetPB.setVisibility(View.GONE);
        btnResetEmail = view.findViewById(R.id.btnResetEmail);
        mNavController = Navigation.findNavController(view);

        btnResetEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });

        mAuth = FirebaseAuth.getInstance();

    }

    private void resetPassword() {
        String UserEmail = etResetEmail.getText().toString().trim();

        if(UserEmail.isEmpty()){
            etResetEmail.setError(getString(R.string.enter_email));
            etResetEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(UserEmail).matches()){
            etResetEmail.setError(getString(R.string.enter_existing_email));
            etResetEmail.requestFocus();
            return;
        }

        resetPB.setVisibility(View.VISIBLE);
        mAuth.sendPasswordResetEmail(UserEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getContext(),
                            R.string.check_email_change_pass, Toast.LENGTH_LONG).show();
                    mNavController.navigate(R.id.mainFragment);
                }else {
                    Toast.makeText(getContext(),
                            R.string.try_again_error, Toast.LENGTH_LONG).show();
                    resetPB.setVisibility(View.GONE);
                }
            }
        });
    }
}