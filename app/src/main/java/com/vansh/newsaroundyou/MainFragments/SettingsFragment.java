package com.vansh.newsaroundyou.MainFragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.SearchRecentSuggestions;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vansh.newsaroundyou.HomeRecyclerAdapter;
import com.vansh.newsaroundyou.LaunchMain;
import com.vansh.newsaroundyou.LoginActivity;
import com.vansh.newsaroundyou.LoginFragment;
import com.vansh.newsaroundyou.NewsSuggestionProvider;
import com.vansh.newsaroundyou.R;
import com.vansh.newsaroundyou.SettingsRecyclerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment implements SettingsRecyclerAdapter.ViewHolder.OnNoteListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //initialise views
    private RecyclerView recyclerView;
    private TextView tvSettings;
    private SettingsRecyclerAdapter adapter;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;

    private String region;
    private int regionIndex;
    private boolean[] categoriesChecked = new boolean[LoginFragment.CATEGORIES.length];
    ArrayList<String> categories = new ArrayList<>();

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        //hooks
        recyclerView = view.findViewById(R.id.rv_settings);
        tvSettings = view.findViewById(R.id.tv_settings);
        databaseReference = FirebaseDatabase.getInstance().getReference(LaunchMain.USER);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //setting tv text
        String tvText = firebaseUser.getEmail();
        if (tvText == null || tvText.isEmpty()){
            tvText = "Guest User";
        }
        tvSettings.setText(tvText.toUpperCase(Locale.ROOT));

        //setting up the recycler view
        adapter = new SettingsRecyclerAdapter(requireContext(), this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        databaseReference.child(firebaseUser.getUid()).child("region").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                region = snapshot.getValue(String.class);
                regionIndex = Arrays.asList(LoginFragment.REGIONS).indexOf(region);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("hello", "Failed to get region", error.toException());
            }
        });

        databaseReference.child(firebaseUser.getUid()).child("categories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Arrays.fill(categoriesChecked, false);
                for (DataSnapshot snapshotCategory: snapshot.getChildren()){
                    String category = snapshotCategory.getValue(String.class);
                    categories.add(category);
                    categoriesChecked[Arrays.asList(LoginFragment.CATEGORIES).indexOf(category)] = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("hello", "Failed to get category", error.toException());
            }
        });

        return view;
    }

    @Override
    public void onNoteClick(int position) {
        switch (position){
            case 0:
                new MaterialAlertDialogBuilder(getContext())
                        .setTitle("Select your Region")
                        .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setSingleChoiceItems(LoginFragment.REGIONS, regionIndex, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                databaseReference.child(firebaseUser.getUid()).child("region").setValue(LoginFragment.REGIONS[i]);
                            }
                        })
                        .show();
                break;
            case 1:
                new MaterialAlertDialogBuilder(getContext())
                        .setTitle("Select your preferred Categories")
                        .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (categories.isEmpty()){
                                    categories.add("General");
                                }
                                databaseReference.child(firebaseUser.getUid()).child("categories").setValue(categories);
                                dialogInterface.dismiss();
                            }
                        })
                        .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setMultiChoiceItems(LoginFragment.CATEGORIES, categoriesChecked, new DialogInterface.OnMultiChoiceClickListener() {
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
                break;
            case 2:
                SearchRecentSuggestions searchRecentSuggestions = new SearchRecentSuggestions(requireContext(), NewsSuggestionProvider.AUTHROITY, NewsSuggestionProvider.MODE);
                searchRecentSuggestions.clearHistory();
                break;
            case 3:
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(requireContext(), LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                requireActivity().finish();
                break;
        }
    }
}