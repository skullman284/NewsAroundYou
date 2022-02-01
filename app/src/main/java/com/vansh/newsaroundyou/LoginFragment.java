package com.vansh.newsaroundyou;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.nio.file.Watchable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //Declaring Variables
    FirebaseAuth firebaseAuth;
    private ImageView imageView;
    private Button bRegister, bLogin, bForgotPassword, bGuest;
    private TextInputLayout etEmail, etPassword;
    private Boolean emailEmpty = true, passwordEmpty = true;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        //hooks
        imageView = view.findViewById(R.id.iv_login);
        bRegister = view.findViewById(R.id.b_register);
        firebaseAuth = FirebaseAuth.getInstance();
        imageView = view.findViewById(R.id.iv_login);
        bRegister = view.findViewById(R.id.b_register);
        bLogin = view.findViewById(R.id.b_login);
        bForgotPassword = view.findViewById(R.id.b_forgot_password);
        bGuest = view.findViewById(R.id.b_guest);
        etEmail = view.findViewById(R.id.et_email);
        etPassword = view.findViewById(R.id.et_password);
        //Load image
        Glide.with(this).load(R.drawable.icon).into(imageView);

        //Check if user is logged in
        firebaseAuth.signOut();
        if (firebaseAuth.getCurrentUser() != null) {
            // User is signed in
            LaunchMain launchMain = new LaunchMain(getContext());
            launchMain.launch();
        } else {
            // User is signed out
            Log.d("TAG", "onAuthStateChanged:signed_out");
        }

        //Button on click listeners

        //Register
        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_loginFragment_to_registerFragment);
            }
        });

        //Login
        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getEditText().getText().toString().trim();
                String password = etPassword.getEditText().getText().toString().trim();
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Snackbar.make(bGuest, "Sign in: Success", Snackbar.LENGTH_SHORT).show();
                            LaunchMain launchMain = new LaunchMain(getContext());
                            launchMain.launch();
                        }
                        else{
                            //sign in fail
                            etEmail.setError(" ");
                            etPassword.setError("The email or password you entered is incorrect");
                        }
                    }
                });
            }
        });

        //Forgot Password
        bForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_loginFragment_to_forgotPasswordFragment);
            }
        });

        //Guest
        bGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LaunchMain launchMain = new LaunchMain(getContext());
                launchMain.launchGuest(firebaseAuth, bGuest, getActivity());
            }
        });

        //Et text changed listeners
        etEmail.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                etEmail.setError(null);
                etPassword.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().isEmpty()){
                    emailEmpty = false;
                }
                else {
                    emailEmpty = true;
                }
                if (!emailEmpty && !passwordEmpty){
                    bLogin.setEnabled(true);
                }
                else{
                    bLogin.setEnabled(false);
                }
            }
        });
        etPassword.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                etPassword.setError(null);
                etEmail.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable2) {
                if (!editable2.toString().isEmpty()){
                    passwordEmpty = false;
                }
                else {
                    passwordEmpty = true;
                }
                if (!emailEmpty && !passwordEmpty){
                    bLogin.setEnabled(true);
                }
                else{
                    bLogin.setEnabled(false);
                }
            }
        });
        return view;
    }

}