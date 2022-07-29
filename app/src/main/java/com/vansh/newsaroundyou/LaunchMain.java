package com.vansh.newsaroundyou;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class LaunchMain {
    public Context context;
    public final static String USER = "users";
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
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(USER);
                    ArrayList<String> categories = new ArrayList<>();
                    User guestUser = new User(null);
                    guestUser.setRegion("Singapore");
                    new MaterialAlertDialogBuilder(context)
                            .setTitle("Select your Region")
                            .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    new MaterialAlertDialogBuilder(context)
                                            .setTitle("Select your preferred Categories")
                                            .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    if (categories.isEmpty()){
                                                        categories.add("General");
                                                    }
                                                    guestUser.setCategories(categories);
                                                    databaseReference.child(firebaseUser.getUid()).setValue(guestUser);
                                                    Snackbar.make(bGuest, "Sign in: Success", Snackbar.LENGTH_SHORT).show();
                                                    launch();
                                                }
                                            })
                                            .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.dismiss();
                                                }
                                            })
                                            .setMultiChoiceItems(LoginFragment.CATEGORIES, null, new DialogInterface.OnMultiChoiceClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                                                    if (b){
                                                        categories.add(LoginFragment.CATEGORIES[i]);
                                                    }
                                                    else {
                                                        categories.remove(LoginFragment.CATEGORIES[i]);
                                                    }
                                                }
                                            })
                                            .show();
                                }
                            })
                            .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .setSingleChoiceItems(LoginFragment.REGIONS, 14, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    guestUser.setRegion(LoginFragment.REGIONS[i]);
                                }
                            })
                            .show();

                } else {
                    Log.d("failure", "anonymous", task.getException());
                    Snackbar.make(bGuest, "Sign in: Failure", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }
}
