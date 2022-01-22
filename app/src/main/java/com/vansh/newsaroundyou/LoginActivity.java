package com.vansh.newsaroundyou;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.material.transition.platform.MaterialContainerTransform;
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback;

public class LoginActivity extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        configTransition();
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_login2);
        //Hooks
        imageView = findViewById(R.id.iv_login);
        Glide.with(this).load(R.drawable.icon).into(imageView);

    }

    private void configTransition() {
        setEnterSharedElementCallback(new MaterialContainerTransformSharedElementCallback());

    }
}