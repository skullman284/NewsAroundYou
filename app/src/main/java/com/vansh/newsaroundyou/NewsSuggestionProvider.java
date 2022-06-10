package com.vansh.newsaroundyou;

import android.content.SearchRecentSuggestionsProvider;

public class NewsSuggestionProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHROITY = "com.vansh.newsaroundyou.NewsSuggestionProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public NewsSuggestionProvider() {
        setupSuggestions(AUTHROITY, MODE);
    }
}
