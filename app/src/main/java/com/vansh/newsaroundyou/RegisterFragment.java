package com.vansh.newsaroundyou;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //Declaring variables
    private FirebaseAuth firebaseAuth;
    private ImageView imageView;
    private TextInputLayout etEmailRegister, etPasswordRegister, etConfirmPasswordRegister;
    private Button bRegisterRegister;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
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
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        //hooks
        firebaseAuth = FirebaseAuth.getInstance();
        imageView = view.findViewById(R.id.iv_register);
        bRegisterRegister = view.findViewById(R.id.b_register_register);
        etEmailRegister = view.findViewById(R.id.et_email_register);
        etPasswordRegister = view.findViewById(R.id.et_password_register);
        etConfirmPasswordRegister = view.findViewById(R.id.et_confirm_password_register);

        //load image
        Glide.with(this).load(R.drawable.icon).into(imageView);

        //create user
        bRegisterRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmailRegister.getEditText().getText().toString().trim();
                String password = etPasswordRegister.getEditText().getText().toString().trim();
                String confirmPassword = etConfirmPasswordRegister.getEditText().getText().toString().trim();
                Log.d("password", "test");
                if (email.isEmpty()){
                    etEmailRegister.setError("Please enter your email address");
                }
                if (etEmailRegister.getError() == null && isSame(password, confirmPassword) && isPasswordValid(password)) {
                    registerUser(email, password);
                }
            }
        });

        //On text edited listener, remove error

        etPasswordRegister.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                etPasswordRegister.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        etConfirmPasswordRegister.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                etConfirmPasswordRegister.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        etEmailRegister.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                etEmailRegister.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        return view;
    }

    private void registerUser(String email,String password){
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //sign in success
                if (task.isSuccessful()) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                } else {
                    // If sign in fails, display a message to the user.
                    try{
                        throw task.getException();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        etEmailRegister.setError("Invalid email");
                        etEmailRegister.requestFocus();
                    }
                    catch (FirebaseAuthUserCollisionException e){
                       etEmailRegister.setError("Account with this email address already exists");
                       etEmailRegister.requestFocus();
                    }
                    catch(Exception e) {
                        Log.e("Exception", e.getMessage());
                    }
                }
            }
        });
    }

    private boolean isSame(String password, String confirmPassword){
        if (!password.equals(confirmPassword)){
            etConfirmPasswordRegister.setError("Passwords do not match");
            return false;
        }
        else {
            return true;
        }
    }

    private boolean isPasswordValid(String password){
        if (password.length() < 8 || password.length() > 32){
            etConfirmPasswordRegister.setError("Password must be 8 - 32 characters");
            return false;
        }
        if (!hasNumberCaps(password)){
            etConfirmPasswordRegister.setError("Password must contain at least one capital letter and one number.");
            return false;
        }
        return true;
    }

    private boolean hasNumberCaps(String password){
        boolean hasNumber = false;
        boolean hasCaps = false;
        for (int i = 0; i < password.length(); i++){
            try {
                Integer.parseInt(String.valueOf(password.charAt(i)));
                hasNumber = true;
            } catch (NumberFormatException e){
            }
            if (Character.isUpperCase(password.charAt(i))){
                hasCaps = true;
            }
        }
        return hasNumber && hasCaps;
    }
}