package com.vansh.newsaroundyou;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LaunchMain {
    public Context context;

    public LaunchMain(Context context) {
        this.context = context;
    }

    public void launch(){
        Intent i = new Intent(context, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(i);
    }

    public void launchGuest(FirebaseAuth firebaseAuth, Button bGuest, Activity activity){
        firebaseAuth.signInAnonymously().addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                Snackbar.make(bGuest, "Sign in: Success", Snackbar.LENGTH_SHORT).show();
                launch();
                }
        });
    }
}
