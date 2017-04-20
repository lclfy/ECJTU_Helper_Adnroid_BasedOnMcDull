package com.mcdull.cert.activity;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.mcdull.cert.activity.base.BaseThemeActivity;
import com.mcdull.cert.R;
import com.mcdull.cert.adapter.LibraryAdapter;

public class LibraryActivity extends BaseThemeActivity {

    private TextView tvQueryTitle;
    private ListView lvLibrary;

    @Override
    protected void onTheme(Bundle savedInstanceState) {
        setContentView(R.layout.activity_library);

        initView();

        init();
    }

    private void init() {
        String libraryJson = getIntent().getStringExtra("libraryJson");
        List<String> list = libraryJsonParse(libraryJson);
        tvQueryTitle.setText("图书馆查询");
        if (list==null)
            return;
        if (list.size() != 0) {
            findViewById(R.id.text).setVisibility(View.GONE);
            LibraryAdapter libraryAdapter = new LibraryAdapter(this, list);
            lvLibrary.setAdapter(libraryAdapter);
        }
    }

    private List<String> libraryJsonParse(String libraryJson) {
        try {
            List<String> list = new ArrayList<>();
            JSONObject jsonObject = new JSONObject(libraryJson);
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            for (int i = 0; i < jsonArray.length(); i++) {
                list.add(jsonArray.getJSONObject(i).getString("BZ"));
            }
            return list;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    private void initView() {
        tvQueryTitle = (TextView) findViewById(R.id.tv_title);
        lvLibrary = (ListView) findViewById(R.id.lv_library);
    }
}
