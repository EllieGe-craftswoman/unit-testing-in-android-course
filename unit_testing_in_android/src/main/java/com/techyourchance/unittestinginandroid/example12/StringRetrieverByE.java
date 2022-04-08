package com.techyourchance.unittestinginandroid.example12;

import android.content.Context;

public class StringRetrieverByE {
    private final Context mContext;

    public StringRetrieverByE(Context context) {
        mContext = context;
    }

    public String getString(int id) {
        return mContext.getString(id);
    }
}
