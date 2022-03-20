package com.vansh.newsaroundyou;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class NewsModelListViewModel extends ViewModel {

    private List<NewsModel> newsModelList;
    private int position;

    public List<NewsModel> getNewsModelList() {
        return newsModelList;
    }

    public void setNewsModelList(List<NewsModel> newsModelList) {
        this.newsModelList = newsModelList;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
