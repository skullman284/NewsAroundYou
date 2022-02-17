package com.vansh.newsaroundyou;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback;

public class LoginActivity extends AppCompatActivity {

    private MaterialToolbar materialToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        configTransition();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        materialToolbar = findViewById(R.id.login_appbar);
        setSupportActionBar(materialToolbar);
        materialToolbar.setTitle("NewsAroundYou");

    }

    private void configTransition() {
        setEnterSharedElementCallback(new MaterialContainerTransformSharedElementCallback());
    }


}