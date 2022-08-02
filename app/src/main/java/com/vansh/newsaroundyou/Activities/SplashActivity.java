package com.vansh.newsaroundyou.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback;
import com.vansh.newsaroundyou.R;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    Animation topAnim, bottomAnim;
    ImageView imageView;
    TextView textViewName;
    TextView textViewTagline;
    private static int SPLASH_SCREEN = 5000;
    private static int FINISH_DELAY = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        configTransition();
        setTheme(R.style.SplashTheme);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        //Animations
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        //Hooks
        imageView = findViewById(R.id.iv_splash);
        textViewName = findViewById(R.id.tv_splash_name);
        textViewTagline = findViewById(R.id.tv_splash_tagline);

        //setting animations
        imageView.setAnimation(topAnim);
        textViewName.setAnimation(topAnim);
        textViewTagline.setAnimation(bottomAnim);

        Glide.with(this).load(R.drawable.icon).into(imageView);

        new Handler().postDelayed(() -> {
            Pair<View, String> pImage = Pair.create((View) imageView, "splash_to_login");
            Pair<View, String> pText = Pair.create((View) imageView, "splash_to_login_text");
            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this, pImage, pText).toBundle();
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent, bundle);
            new Handler().postDelayed(() -> finish(), FINISH_DELAY);
        }, SPLASH_SCREEN);
    }

    private void configTransition() {
        setExitSharedElementCallback(new MaterialContainerTransformSharedElementCallback());
        getWindow().setSharedElementsUseOverlay(false);
    }
}