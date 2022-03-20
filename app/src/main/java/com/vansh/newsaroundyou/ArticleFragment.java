package com.vansh.newsaroundyou;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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
import com.google.android.material.transition.MaterialContainerTransform;
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback;

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


    //initialise views
    private TextView tvArticleTitle, tvArticlePublisher, tvArticleAuthor, tvArticlePublishedOn, tvArticleContent;
    private ImageView ivArticle;
    private WebView webView;
    private Button bArticleURL;
    private NewsModelListViewModel newsModelListViewModel;

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

        webView = view.findViewById(R.id.webview_article);
        newsModelListViewModel = new ViewModelProvider(requireActivity()).get(NewsModelListViewModel.class);
        NewsModel newsModel = newsModelListViewModel.getNewsModelList().get(newsModelListViewModel.getPosition());

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

        webView.loadUrl(newsModel.getUrl());
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }
        });

        /*//hooks
        tvArticleTitle = view.findViewById(R.id.tv_article_title);
        tvArticlePublisher = view.findViewById(R.id.tv_article_publisher);
        tvArticleAuthor = view.findViewById(R.id.tv_article_author);
        tvArticlePublishedOn = view.findViewById(R.id.tv_article_published_on);
        tvArticleContent = view.findViewById(R.id.tv_article_content);
        ivArticle = view.findViewById(R.id.iv_article);
        bArticleURL = view.findViewById(R.id.b_article_url);
        webView = view.findViewById(R.id.webview_article);

        newsModelListViewModel = new ViewModelProvider(requireActivity()).get(NewsModelListViewModel.class);
        NewsModel newsModel = newsModelListViewModel.getNewsModelList().get(newsModelListViewModel.getPosition());

        tvArticleTitle.setText(newsModel.getTitle());
        tvArticlePublisher.setText(newsModel.getPublisher());
        tvArticleAuthor.setText("By "+ newsModel.getAuthor());
        tvArticlePublishedOn.setText(newsModel.getPublishedAt());
        tvArticleContent.setText(newsModel.getContent());

        Glide.with(getContext())
                .load(newsModel.getUrlToImage())
                .placeholder(R.drawable.placeholder)
                .into(ivArticle);

        bArticleURL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentURL = new Intent();
                intentURL.setAction(Intent.ACTION_VIEW);
                intentURL.addCategory(Intent.CATEGORY_BROWSABLE);
                intentURL.setData(Uri.parse(newsModel.getUrl()));
                startActivity(intentURL);
            }
        });*/

        return view;
    }

}