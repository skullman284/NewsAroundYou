package com.vansh.newsaroundyou;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.transition.MaterialContainerTransform;
import com.google.android.material.transition.MaterialSharedAxis;
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback;
import com.google.android.material.transition.platform.MaterialFadeThrough;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ArticleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArticleFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    boolean flag = true;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    //initialise views
    private TextView tvArticleTitle, tvArticlePublisher, tvArticleAuthor, tvArticlePublishedOn, tvArticleContent;
    private ImageView ivArticle;
    private WebView webView;
    private Button bArticleURL;
    private NewsModel newsModel;
    private final static String[] REGIONS = new String[]{"Australia", "Belgium", "Canada", "Switzerland", "France", "United Kingdom",
            "Hong Kong", "India", "Japan", "Malaysia", "New Zealand", "Philippines", "Russia", "South Africa", "Singapore", "Thailand",
            "United States of America", "Ukraine", "Venezuela"};
    private final static String[] CATEGORIES = new String[]{"Business", "Entertainment", "General", "Health", "Science", "Sports", "Technology"};

    public ArticleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ArticleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ArticleFragment newInstance(String param1, String param2) {
        ArticleFragment fragment = new ArticleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setEnterTransition(new MaterialSharedAxis(MaterialSharedAxis.Z, true));
        setReturnTransition(new MaterialSharedAxis(MaterialSharedAxis.Z, false));
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setSharedElementEnterTransition(new MaterialContainerTransform());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_article, container, false);

        //hooks
        webView = view.findViewById(R.id.webview_article);
        databaseReference = FirebaseDatabase.getInstance().getReference(LaunchMain.USER);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //TODO make ciruclar progress indicator in article fragment work
        NewsModelListViewModel newsModelListViewModel = new ViewModelProvider(requireActivity()).get(NewsModelListViewModel.class);
        newsModel = newsModelListViewModel.getNewsModelList().get(newsModelListViewModel.getPosition());

        //handling back presses in webview
        webView.setOnKeyListener(new View.OnKeyListener(){

            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN){
                    WebView webView = (WebView) view;

                    switch (i){
                        case KeyEvent.KEYCODE_BACK:
                            if (webView.canGoBack()){
                                webView.goBack();
                                return true;
                            }
                            break;
                    }
                }
                return false;
            }
        });

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return true;

            }

        });
        webView.loadUrl(newsModel.getUrl());

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_main_appbar_article, menu);
        //making sure bookmark icon UI is appropriate by using query to check if the current article is already saved in the database
        Query savedArticles =  databaseReference.child(firebaseUser.getUid()).child("savedArticles").orderByChild("url").equalTo(newsModel.getUrl());
        savedArticles.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    menu.findItem(R.id.b_appbar_bookmark).setIcon(R.drawable.ic_baseline_bookmark_24);
                    flag = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("hello", "error saved articles", error.toException());
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.b_appbar_share:
                shareArticle(requireContext(), newsModel.getUrl());
                return true;
            case R.id.b_appbar_bookmark:
                if (flag){
                    item.setIcon(R.drawable.ic_baseline_bookmark_24);
                    flag = false;
                    //adding the newsmodel of the current article to the real time dtabase
                    databaseReference.child(firebaseUser.getUid()).child("savedArticles").push().setValue(newsModel);
                }
                else {
                    item.setIcon(R.drawable.ic_outline_bookmark_border_24);
                    flag = true;
                    //query to find the article with the current url in the real time database
                    Query removeArticle = databaseReference.child(firebaseUser.getUid()).child("savedArticles").orderByChild("url").equalTo(newsModel.getUrl());
                    removeArticle.addListenerForSingleValueEvent(new ValueEventListener() {
                        //remove the current url
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot snapshotUrl : snapshot.getChildren()){
                                snapshotUrl.getRef().setValue(null);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.d("hello", "error remove article", error.toException());
                        }
                    });
                }
                return true;

            default:
                break;
        }
        return false;
    }

    public static void shareArticle(Context context, String URL){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Check out this interesting news article I found!");
        sendIntent.putExtra(Intent.EXTRA_TEXT, URL);

        Intent shareIntent = Intent.createChooser(sendIntent, "Share News Article");
        context.startActivity(shareIntent);
    }

}