package com.fec.fectangramdemo;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.bumptech.glide.Glide;
import com.fec.fectangramdemo.data.ImageTvView;
import com.fec.fectangramdemo.data.SimpleFooterView;
import com.fec.fectangramdemo.data.SimpleTitleView;
import com.fec.fectangramdemo.data.SingleImageView;
import com.tmall.wireless.tangram.TangramBuilder;
import com.tmall.wireless.tangram.TangramEngine;
import com.tmall.wireless.tangram.structure.BaseCell;
import com.tmall.wireless.tangram.support.SimpleClickSupport;
import com.tmall.wireless.tangram.util.IInnerImageSetter;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.json.JSONArray;
import org.json.JSONException;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rv;
    private TangramBuilder.InnerBuilder mBuilder;
    private TangramEngine engine;
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        otherInit();

        TangramBuilder.init(this, new IInnerImageSetter() {
            @Override
            public <IMAGE extends ImageView> void doLoadImageUrl(@NonNull IMAGE view, @Nullable String url) {
                Glide.with(MainActivity.this).load(url).into(view);
            }
        }, ImageView.class);

        mBuilder = TangramBuilder.newInnerBuilder(MainActivity.this);

        mBuilder.registerCell(1, SingleImageView.class);
        mBuilder.registerCell(2, SimpleTitleView.class);
        mBuilder.registerCell(3, SimpleFooterView.class);
        mBuilder.registerCell(4, ImageTvView.class);

        engine = mBuilder.build();
        //engine.getLayoutManager().setFixOffset(0,0,0,48);
        engine.register(SimpleClickSupport.class, new SimpleClickSupport(){
            @Override
            public void defaultClick(View targetView, BaseCell cell, int type) {
                Log.d(TAG, "defaultClick: type = "+type+"cell:"+cell.extras);
            }
        });

        engine.bindView(rv);
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //在 scroll 事件中触发 engine 的 onScroll，内部会触发需要异步加载的卡片去提前加载数据
                engine.onScrolled();
            }
        });


        engine.enableAutoLoadMore(true);

        loadData("dataFec1.json");
    }

    private void loadData(String fileName) {
        String json = new String(getAssertsFile(this, fileName));
        JSONArray data = null;
        if (!TextUtils.isEmpty(json)) {
            try {
                data = new JSONArray(json);
                engine.setData(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void otherInit() {
        rv = (RecyclerView) findViewById(R.id.rv);
        BottomNavigationBar bnb = (BottomNavigationBar) findViewById(R.id.bnb);
        bnb.addItem(new BottomNavigationItem(R.mipmap.ic_launcher, "首页"))
            .addItem(new BottomNavigationItem(R.mipmap.ic_launcher, "分类"))
            .addItem(new BottomNavigationItem(R.mipmap.ic_launcher, "购物车"))
            .addItem(new BottomNavigationItem(R.mipmap.ic_launcher, "我的"))
            .initialise();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Toolbar toolbar = (Toolbar) findViewById(R.id.tb);
            toolbar.setTitle("这是actionBar");
            toolbar.setBackgroundResource(R.color.item_default_color);
            setSupportActionBar(toolbar);
        }
        bnb.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int i) {
                switch (i) {
                    case 0:
                        loadData("dataFec1.json");
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        loadData("data.json");
                        break;
                }
            }

            @Override
            public void onTabUnselected(int i) {

            }

            @Override
            public void onTabReselected(int i) {

            }
        });
    }

    public static byte[] getAssertsFile(Context context, String fileName) {
        InputStream inputStream = null;
        AssetManager assetManager = context.getAssets();
        try {
            inputStream = assetManager.open(fileName);
            if (inputStream == null) {
                return null;
            }

            BufferedInputStream bis = null;
            int length;
            try {
                bis = new BufferedInputStream(inputStream);
                length = bis.available();
                byte[] data = new byte[length];
                bis.read(data);

                return data;
            } catch (IOException e) {

            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (Exception e) {

                    }
                }
            }

            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (engine != null) {
            engine.destroy();
        }
    }
}
