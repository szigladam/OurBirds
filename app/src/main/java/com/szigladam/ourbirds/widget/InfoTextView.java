package com.szigladam.ourbirds.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.szigladam.ourbirds.R;

public class InfoTextView extends LinearLayout {
    public InfoTextView(Context context) {
        super(context);
        init(context);
    }

    public InfoTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public InfoTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context) {
        View.inflate(context, R.layout.view_info_text, this);
    }
}
