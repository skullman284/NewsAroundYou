package com.vansh.newsaroundyou;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<NewsModel> newsModelList;

    public HomeRecyclerAdapter(Context context, List<NewsModel> newsModelList) {
        this.context = context;
        this.newsModelList = newsModelList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvHomeTitle, tvHomeCategory, tvHomePublisher, tvHomePublishedAt;
        private ImageView ivHomeCard;
        private Button bHomeShare, bHomeBookmark;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //Find views
            tvHomeTitle = itemView.findViewById(R.id.tv_home_title);
            tvHomeCategory = itemView.findViewById(R.id.tv_home_category);
            tvHomePublisher = itemView.findViewById(R.id.tv_home_publisher);
            tvHomePublishedAt = itemView.findViewById(R.id.tv_home_published_at);
            ivHomeCard = itemView.findViewById(R.id.iv_home_card);
            bHomeShare = itemView.findViewById(R.id.b_home_share);
            bHomeBookmark = itemView.findViewById(R.id.b_home_bookmark);

            //button on click listeners
            bHomeShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO share, intent TO BE USED?
                }
            });
            //temporary, later need to store in cloud if bookmarked or not and check from there

            bHomeBookmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bHomeBookmark.setBackgroundResource(R.drawable.ic_baseline_bookmark_24);
                }
            });
        }

        public TextView getTvHomeTitle() {
            return tvHomeTitle;
        }

        public TextView getTvHomeCategory() {
            return tvHomeCategory;
        }

        public TextView getTvHomePublisher() {
            return tvHomePublisher;
        }

        public TextView getTvHomePublishedAt() {
            return tvHomePublishedAt;
        }

        public ImageView getIvHomeCard() {
            return ivHomeCard;
        }

        public Button getbHomeShare() {
            return bHomeShare;
        }

        public Button getbHomeBookmark() {
            return bHomeBookmark;
        }
    }

    @NonNull
    @Override
    public HomeRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_home_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeRecyclerAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }
}
