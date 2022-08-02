package com.vansh.newsaroundyou.Miscellaneous;

import android.content.SearchRecentSuggestionsProvider;

public class NewsSuggestionProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHROITY = "com.vansh.newsaroundyou.Miscellaneous.NewsSuggestionProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public NewsSuggestionProvider() {
        setupSuggestions(AUTHROITY, MODE);
    }
}
