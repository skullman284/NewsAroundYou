package com.vansh.newsaroundyou.MainFragments;

import static java.util.Map.entry;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Looper;
import android.provider.SearchRecentSuggestions;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.transition.MaterialSharedAxis;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.collection.ImmutableSortedMap;
import com.vansh.newsaroundyou.HomeRecyclerAdapter;
import com.vansh.newsaroundyou.LaunchMain;
import com.vansh.newsaroundyou.MainActivity;
import com.vansh.newsaroundyou.NewsModel;
import com.vansh.newsaroundyou.NewsModelListViewModel;
import com.vansh.newsaroundyou.NewsSuggestionProvider;
import com.vansh.newsaroundyou.R;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class
HomeFragment extends Fragment implements HomeRecyclerAdapter.ViewHolder.OnNoteListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //Initialise views
    private RecyclerView recyclerView;
    private HomeRecyclerAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private CircularProgressIndicator circularProgressIndicator;
    private SwipeRefreshLayout swipeRefreshLayout;
    private NewsModelListViewModel newsModelListViewModel;
    private BroadcastReceiver broadcastReceiver;
    private MainActivity activity;

    private ArrayList<NewsModel> newsModelList = new ArrayList<NewsModel>();
    private boolean init = true;
    private String region = "sg";
    ArrayList<String> categories = new ArrayList<String>(Arrays.asList("business"));
    private Context context;

    // static constants
    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    private static String NEWSAPIURLD;
    private String NEWSAPIURLQ;
    public static Map<String, String> COUNTRYCODES;


    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setExitTransition(new MaterialSharedAxis(MaterialSharedAxis.Z, true));
        setReenterTransition(new MaterialSharedAxis(MaterialSharedAxis.Z, false));
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        //initialising country codes
        COUNTRYCODES = new HashMap<>();
        COUNTRYCODES.put("Australia", "au");
        COUNTRYCODES.put("Belgium", "be");
        COUNTRYCODES.put("Canada", "ca");
        COUNTRYCODES.put("Switzerland", "ch");
        COUNTRYCODES.put("France", "fr");
        COUNTRYCODES.put("United Kingdom", "gb");
        COUNTRYCODES.put("Hong Kong", "hk");
        COUNTRYCODES.put("India", "in");
        COUNTRYCODES.put("Japan", "jp");
        COUNTRYCODES.put("Malaysia", "my");
        COUNTRYCODES.put("New Zealand", "nz");
        COUNTRYCODES.put("Philippines", "ph");
        COUNTRYCODES.put("Russia", "ru");
        COUNTRYCODES.put("South Africa", "sa");
        COUNTRYCODES.put("Singapore", "sg");
        COUNTRYCODES.put("Thailand", "th");
        COUNTRYCODES.put("United States of America", "us");
        COUNTRYCODES.put("Ukraine", "ua");
        COUNTRYCODES.put("Venezuela", "ve");

        //Receiving the intent, verifying action, getting query, put in oncreate to avoid multiple recivers being registered
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //handling the intent received i.e. carrying out the search
                handleIntent(intent);
            }
        };
        LocalBroadcastManager.getInstance(context).registerReceiver(broadcastReceiver, new IntentFilter("android.intent.action.SEARCH"));

    }

    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @SuppressLint("FragmentBackPressedCallback")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //hooks
        recyclerView = view.findViewById(R.id.rv_home);
        circularProgressIndicator = view.findViewById(R.id.circular_progress_home);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_home);
        activity = (MainActivity) getActivity();
        adapter = new HomeRecyclerAdapter(context, newsModelList, HomeFragment.this);
        layoutManager = new LinearLayoutManager(context);
        newsModelListViewModel = new ViewModelProvider(requireActivity()).get(NewsModelListViewModel.class);

        //setting region and categories according to user
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(LaunchMain.USER);

        databaseReference.child(firebaseUser.getUid()).child("region").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                region = COUNTRYCODES.get(snapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("hello", "Failed to get region", error.toException());
            }
        });
        databaseReference.child(firebaseUser.getUid()).child("categories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categories.clear();
                for (DataSnapshot snapshotCategory: snapshot.getChildren()){
                    String category = snapshotCategory.getValue(String.class).toLowerCase();
                    categories.add(category);
                }
                getNewsToRV(null);
                newsModelListViewModel.setNewsModelList(newsModelList);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("hello", "Failed to get category", error.toException());
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNewsToRV(null);
            }
        });


        //handling back presses
        OnBackPressedCallback onBackPressedCallback =  new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                getNewsToRV( null);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);

        //set up recycler view
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void getNewsToRV (@Nullable String query){
        //helper function that fetches the news from a web API, parses the JSON format, and passes it to the recycler view
        //the parameter passed indicates whether it is the first time the news is being generated or not
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        if (!newsModelList.isEmpty()) {
            newsModelList.clear();
        }
        recyclerView.setVisibility(View.INVISIBLE);

        circularProgressIndicator.show();

        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    int size = categories.size();
                    if (query != null){
                        size = 1;
                    }
                    ArrayList<String> newsModelTitleList = new ArrayList<>();
                    for (int j = 0; j < size;j++) {
                        //reset url for diff cat
                        String newsapiurl = "https://newsapi.org/v2/top-headlines?country=" + region + "&category="
                                + categories.get(j) + "&pageSize=" + String.valueOf(30/size) + "&apiKey=68c0ced1057946a48b0ed3257afa2043";
                        if (query != null) {
                            newsapiurl = "https://newsapi.org/v2/everything?q=" + query + "&sortBy=relevancy&apiKey=68c0ced1057946a48b0ed3257afa2043";
                        }
                        String inline = "";
                        URL url = new URL(newsapiurl);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:221.0) Gecko/20100101 Firefox/31.0");
                        conn.setRequestMethod("GET");
                        conn.connect();
                        int responseCode = conn.getResponseCode();
                        if (responseCode != 200) {
                            throw new RuntimeException("HttpResponseCode: " + String.valueOf(responseCode));
                        } else {
                            Scanner sc = new Scanner(conn.getInputStream());
                            while (sc.hasNext()) {
                                inline += sc.nextLine();
                            }
                            sc.close();
                        }
                        //JSONParser reads the data from string object and break each data into key value pairs
                        JSONParser parse = new JSONParser();
                        //Type caste the parsed json data in json object
                        org.json.simple.JSONObject jsonObjectMain = (org.json.simple.JSONObject) parse.parse(inline);
                        //Store the JSON object in JSON array as objects (For level 1 array element i.e Results)
                        JSONArray jsonarrArticles = (JSONArray) jsonObjectMain.get("articles");
                        //Get data from Articles array populate the newsmodel
                        for (int i = 0; i < jsonarrArticles.size(); i++) {
                            org.json.simple.JSONObject jsonObjectArticles = (org.json.simple.JSONObject) jsonarrArticles.get(i);
                            NewsModel newsModel = new NewsModel();
                            newsModel.setAuthor((String) jsonObjectArticles.get("author"));

                            String title = (String) jsonObjectArticles.get("title");
                            String[] titleSplit = splitTitlePublisher(title);
                            newsModel.setTitle(titleSplit[0]);
                            newsModel.setPublisher(titleSplit.length != 1 ? titleSplit[titleSplit.length - 1] : "Unknown");

                            newsModel.setCategory(categories.get(j).substring(0, 1).toUpperCase() + categories.get(j).substring(1));
                            if (query != null){
                                newsModel.setCategory("General");
                            }
                            newsModel.setContent((String) jsonObjectArticles.get("content"));

                            Calendar calendar = Calendar.getInstance();
                            Date date = timeFormatter((String) jsonObjectArticles.get("publishedAt"));
                            String timeAgo = (String) DateUtils.getRelativeTimeSpanString(date.getTime(), calendar.getTimeInMillis(), DateUtils.MINUTE_IN_MILLIS);
                            String publishedAt = date.toString();
                            newsModel.setPublishedAt(publishedAt);
                            newsModel.setTimeAgo(timeAgo);

                            newsModel.setUrl((String) jsonObjectArticles.get("url"));
                            newsModel.setUrlToImage((String) jsonObjectArticles.get("urlToImage"));

                            if (newsModel.getContent() != null && !newsModelTitleList.contains(newsModel.getTitle())
                            ) {
                                newsModelList.add(newsModel);
                                newsModelTitleList.add(title);
                            }
                        }
                        //Disconnect the HttpURLConnection stream
                        conn.disconnect();
                    }
                    //updating the news model list view model
                    Collections.sort(newsModelList);
                    newsModelListViewModel.setNewsModelList(newsModelList);

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                            swipeRefreshLayout.setRefreshing(false);
                            recyclerView.setVisibility(View.VISIBLE);
                            circularProgressIndicator.hide();
                        }
                    }, 0);


                }
                catch (Exception e) {
                    Log.d("exception", e.toString());
                }
            }
            });

        }

       private static String[] splitTitlePublisher(String title){
            //helper function that helps extract the publisher from the title
            String[] titleSplit = title.split(" - ");
            return titleSplit;
       }

       private static Date timeFormatter (String time){
        //helper function that helps convert the time in iso format to time ago

           Date date = new Date();
           try {
               date = simpleDateFormat.parse(time);
           } catch (ParseException e) {
               Log.d("exception", e.toString());
           }
           return date;
       }

    @Override
    public void onNoteClick(int position) {

        newsModelListViewModel = new ViewModelProvider(activity).get(NewsModelListViewModel.class);
        newsModelListViewModel.setPosition(position);

        //Finding the navcontroller using a random view then navigating to article fragment
        NavController navController = Navigation.findNavController(recyclerView);

        navController.navigate(R.id.action_main_home_fragment_to_articleFragment);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_main_appbar_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.b_appbar_search:
                getActivity().onSearchRequested();
                return true;
            default:
                break;
        }
        return false;
    }


    private void searchArticle(String query) {
        getNewsToRV(query);
    }

    private void handleIntent(Intent intent){
        if (Intent.ACTION_SEARCH.equals(intent.getAction())){
            String query = intent.getStringExtra(SearchManager.QUERY);
            //saving query as recent suggestion
            SearchRecentSuggestions searchRecentSuggestions = new SearchRecentSuggestions(context, NewsSuggestionProvider.AUTHROITY, NewsSuggestionProvider.MODE);
            searchRecentSuggestions.saveRecentQuery(query, null);
            //searching newsapi for query
            searchArticle(query);
        }
    }


}