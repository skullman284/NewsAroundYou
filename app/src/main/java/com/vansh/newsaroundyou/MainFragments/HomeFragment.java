package com.vansh.newsaroundyou.MainFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigatorExtrasKt;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Looper;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.vansh.newsaroundyou.HomeRecyclerAdapter;
import com.vansh.newsaroundyou.NewsModel;
import com.vansh.newsaroundyou.NewsModelListViewModel;
import com.vansh.newsaroundyou.R;

import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
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
    private CircularProgressIndicator circularProgressIndicator;
    private SwipeRefreshLayout swipeRefreshLayout;
    private NewsModelListViewModel newsModelListViewModel;

    // static constants
    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    private static String NEWSAPIURL = "https://newsapi.org/v2/top-headlines?language=en&apiKey=68c0ced1057946a48b0ed3257afa2043";

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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //hooks
        recyclerView = view.findViewById(R.id.rv_home);
        circularProgressIndicator = view.findViewById(R.id.circular_progress_home);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_home);

        circularProgressIndicator.show();
        //TODO circular progress indicator loading when changing fragments

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNewsToRV(false);
            }
        });

        getNewsToRV(true);

        return view;
    }

    private void getNewsToRV (boolean init){
        //helper function that fetches the news from a web API, parses the JSON format, and passes it to the recycler view
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        List<NewsModel> newsModelList = new ArrayList<>();

        executor.execute(new Runnable() {
            @Override
            public void run() {
                String inline = "";
                try {
                    URL url = new URL(NEWSAPIURL);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:221.0) Gecko/20100101 Firefox/31.0");
                    conn.setRequestMethod("GET");
                    conn.connect();
                    int responseCode = conn.getResponseCode();
                    if (responseCode != 200){
                        throw new RuntimeException("HttpResponseCode: " + String.valueOf(responseCode));
                    }
                    else{
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
                    for (int i = 0; i < jsonarrArticles.size();i++){
                        org.json.simple.JSONObject jsonObjectArticles = (org.json.simple.JSONObject) jsonarrArticles.get(i);
                        NewsModel newsModel = new NewsModel();
                        newsModel.setAuthor((String) jsonObjectArticles.get("author"));

                        String title = (String) jsonObjectArticles.get("title");
                        String[] titleSplit = splitTitlePublisher(title);
                        newsModel.setTitle(titleSplit[0]);
                        newsModel.setPublisher(titleSplit.length !=1 ? titleSplit[titleSplit.length - 1] : "Unknown");

                        newsModel.setCategory("Headlines");
                        newsModel.setContent((String) jsonObjectArticles.get("content"));

                        Calendar calendar = Calendar.getInstance();
                        Date date = timeFormatter((String) jsonObjectArticles.get("publishedAt"));
                        String timeAgo = (String) DateUtils.getRelativeTimeSpanString(date.getTime(), calendar.getTimeInMillis(), DateUtils.MINUTE_IN_MILLIS);
                        String publishedAt= date.toString();
                        newsModel.setPublishedAt(publishedAt);
                        newsModel.setTimeAgo(timeAgo);

                        newsModel.setUrl((String) jsonObjectArticles.get("url"));
                        newsModel.setUrlToImage((String) jsonObjectArticles.get("urlToImage"));

                        if (newsModel.getContent() != null) {
                            newsModelList.add(newsModel);
                        }
                    }
                    //Disconnect the HttpURLConnection stream
                    conn.disconnect();



                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (init) {
                                adapter = new HomeRecyclerAdapter(getContext(), newsModelList, HomeFragment.this);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                                //set up recycler view
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setAdapter(adapter);
                                circularProgressIndicator.hide();
                            }
                            else {
                                adapter.notifyDataSetChanged();
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        }
                    });

                } catch (Exception e) {
                    Log.d("exception", e.toString());
                }
            }
            });

        //updating the news model list view model
        newsModelListViewModel = new ViewModelProvider(requireActivity()).get(NewsModelListViewModel.class);
        newsModelListViewModel.setNewsModelList(newsModelList);
        }

       private String[] splitTitlePublisher(String title){
            //helper function that helps extract the publisher from the title
            String[] titleSplit = title.split(" - ");
            return titleSplit;
       }

       private Date timeFormatter (String time){
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

        newsModelListViewModel = new ViewModelProvider(requireActivity()).get(NewsModelListViewModel.class);
        newsModelListViewModel.setPosition(position);

        //use a random view to find the navhost
        NavController navController = Navigation.findNavController(recyclerView);

        navController.navigate(R.id.action_main_home_fragment_to_articleFragment);

    }
}