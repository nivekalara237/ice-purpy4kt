package com.nivekaa.icepurpykt.infra.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.nivekaa.icepurpykt.util.Util;


public class FontText extends AppCompatTextView {
    String TAG = getClass().getName();
    private final Context mContext;
    private String ttfName;

    public FontText(Context context) {
        super(context);
        this.mContext = context;
    }

    public FontText(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.mContext = context;

        // Typeface.createFromAsset doesn't work in the layout editor.
        // Skipping...
        if (isInEditMode()) {
            return;
        }

        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            this.ttfName = attrs.getAttributeValue(Util.ATTRIBUTE_SCHEMA,
                    Util.ATTRIBUTE_TTF_KEY);

            if (null != ttfName)
                init();
        }

    }

    public FontText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
    }

    private void init() {
        Typeface font = Util.getFonts(mContext, ttfName);
        if (null != font)
            setTypeface(font);
    }

    @Override
    public void setTypeface(Typeface tf) {

        super.setTypeface(tf);
    }
}
