package com.vansh.newsaroundyou;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link forgotPasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class forgotPasswordFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //instantiate views
    Button bsendEmail;
    TextInputLayout etForgotEmail;
    CoordinatorLayout forgotCoordinator;
    FirebaseAuth firebaseAuth;

    public forgotPasswordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment forgotPasswordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static forgotPasswordFragment newInstance(String param1, String param2) {
        forgotPasswordFragment fragment = new forgotPasswordFragment();
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
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        //Hooks
        bsendEmail = view.findViewById(R.id.b_send_email);
        etForgotEmail = view.findViewById(R.id.et_forgot_email);
        forgotCoordinator = view.findViewById(R.id.forgot_coordinator);
        firebaseAuth = FirebaseAuth.getInstance();

        //Snackbar
        Snackbar snackbar = Snackbar.make(forgotCoordinator, "Email sent! Check your inbox", Snackbar.LENGTH_SHORT);
        //bsendemail click listner
        View.OnClickListener sendEmailClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etForgotEmail.getEditText().getText().toString().trim();
                firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            snackbar.show();
                        } else {
                            etForgotEmail.setError("No account associated with given email address");
                        }
                    }
                });
            }
        };

        snackbar.setAction("Resend Email", sendEmailClickListener);
        bsendEmail.setOnClickListener(sendEmailClickListener);

        //etEmail text change listener to enable button
        etForgotEmail.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                etForgotEmail.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().isEmpty()){
                    bsendEmail.setEnabled(true);
                }
                else {
                    bsendEmail.setEnabled(false);
                }
            }
        });

        return view;
    }
}