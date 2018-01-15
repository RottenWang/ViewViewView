package com.drwang.views.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wang on 2018/1/15.
 */

public class LyricRollView extends View {
    Paint paint;
    private LinearGradient mLinearGradient;
    private String path = "65919.lrc";
    String lyrics;
    List<String> lyricList;

    public LyricRollView(Context context) {
        this(context, null);
    }

    public LyricRollView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LyricRollView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        lyricList = new ArrayList<>();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(getContext().getAssets().open(path)));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            try {
                JSONObject jsonObject = new JSONObject(sb.toString());
                lyrics = jsonObject.getString("lyric");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("wangchen", "lyrics = " + lyrics);
        initLyricList();
    }

    //生成歌词map
    private void initLyricList() {
        if (TextUtils.isEmpty(lyrics)) {
            return;
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
