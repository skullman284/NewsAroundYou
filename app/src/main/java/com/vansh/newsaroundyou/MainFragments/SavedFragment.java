package com.vansh.newsaroundyou.MainFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textview.MaterialTextView;
import com.google.android.material.transition.MaterialSharedAxis;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vansh.newsaroundyou.HomeRecyclerAdapter;
import com.vansh.newsaroundyou.LaunchMain;
import com.vansh.newsaroundyou.NewsModel;
import com.vansh.newsaroundyou.NewsModelListViewModel;
import com.vansh.newsaroundyou.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SavedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SavedFragment extends Fragment implements HomeRecyclerAdapter.ViewHolder.OnNoteListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //initialising views
    private RecyclerView recyclerView;
    private HomeRecyclerAdapter adapter;
    private CircularProgressIndicator circularProgressIndicator;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MaterialTextView tvSaved;

    private List<NewsModel> newsModelList;
    private NewsModelListViewModel newsModelListViewModel;

    public SavedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SavedFragment.
     */
    // TODO: Rename and change types and number of parameters

    public static SavedFragment newInstance(String param1, String param2) {
        SavedFragment fragment = new SavedFragment();
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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_saved, container, false);
        newsModelList = new ArrayList<NewsModel>();

        //hooks
        recyclerView = view.findViewById(R.id.rv_saved);
        circularProgressIndicator = view.findViewById(R.id.circular_progress_saved);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_saved);
        tvSaved = view.findViewById(R.id.tv_saved);

        circularProgressIndicator.show();
        recyclerView.setVisibility(View.INVISIBLE);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setTvSavedVisibility();
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        //getting the news model list from firebase real time database

        //setting up recycler view and linking it to adapter
        adapter = new HomeRecyclerAdapter(getContext(), newsModelList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(View.VISIBLE);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(LaunchMain.USER);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference.child(firebaseUser.getUid()).child("savedArticles").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshotNewsModel: snapshot.getChildren()){
                    NewsModel newsModel = snapshotNewsModel.getValue(NewsModel.class);
                    newsModelList.add(newsModel);
                    adapter.notifyDataSetChanged();
                    setTvSavedVisibility();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("hello", "error saved fragment news model retrieval", error.toException());
            }
        });

        return view;
    }

    @Override
    public void onNoteClick(int position) {
        newsModelListViewModel = new ViewModelProvider(requireActivity()).get(NewsModelListViewModel.class);
        newsModelListViewModel.setNewsModelList(newsModelList);
        newsModelListViewModel.setPosition(position);

        NavController navController = Navigation.findNavController(recyclerView);
        navController.navigate(R.id.action_main_saved_fragment_to_articleFragment);
    }

    public void setTvSavedVisibility (){
        if (newsModelList.isEmpty()){
            tvSaved.setVisibility(View.VISIBLE);
        }
        else {
            tvSaved.setVisibility(View.INVISIBLE);
        }
    }
}