package com.vansh.newsaroundyou;

import android.os.Bundle;

<<<<<<< HEAD
=======
import androidx.annotation.NonNull;
>>>>>>> be13155e465d7b7f0efad0a7149947b21b4b18bd
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
<<<<<<< HEAD
=======
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
>>>>>>> be13155e465d7b7f0efad0a7149947b21b4b18bd

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
<<<<<<< HEAD
    private ImageView imageView;
    private Button bRegister;
=======
    FirebaseAuth firebaseAuth;
    private ImageView imageView;
    private Button bRegister, bLogin;
    private TextInputLayout etEmail, etPassword;
>>>>>>> be13155e465d7b7f0efad0a7149947b21b4b18bd

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
<<<<<<< HEAD
        imageView = view.findViewById(R.id.iv_login);
        bRegister = view.findViewById(R.id.b_register);

=======
        firebaseAuth = FirebaseAuth.getInstance();
        imageView = view.findViewById(R.id.iv_login);
        bRegister = view.findViewById(R.id.b_register);
        bLogin = view.findViewById(R.id.b_login);
        etEmail = view.findViewById(R.id.et_email);
        etPassword = view.findViewById(R.id.et_password);
>>>>>>> be13155e465d7b7f0efad0a7149947b21b4b18bd

        //Load image
        Glide.with(this).load(R.drawable.icon).into(imageView);

<<<<<<< HEAD

        //Button on click listeners
=======
        //Button on click listeners

>>>>>>> be13155e465d7b7f0efad0a7149947b21b4b18bd
        //Register
        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_loginFragment_to_registerFragment);
            }
        });

<<<<<<< HEAD
=======
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
                            //sign in success, move to next activity
                        }
                        else{
                            //sign in fail
                            try {
                                throw task.getException();
                            }
                            catch (){

                            }
                        }
                    }
                });
            }
        });

>>>>>>> be13155e465d7b7f0efad0a7149947b21b4b18bd
        return view;
    }
}