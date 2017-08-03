package com.fec.fectangramdemo;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.alibaba.android.vlayout.Range;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.fec.fectangramdemo.data.ImageTvView;
import com.fec.fectangramdemo.data.SimpleFooterView;
import com.fec.fectangramdemo.data.SimpleTitleView;
import com.fec.fectangramdemo.data.SingleImageView;
import com.tmall.wireless.tangram.TangramBuilder;
import com.tmall.wireless.tangram.TangramEngine;
import com.tmall.wireless.tangram.core.adapter.GroupBasicAdapter;
import com.tmall.wireless.tangram.dataparser.concrete.Card;
import com.tmall.wireless.tangram.eventbus.BusSupport;
import com.tmall.wireless.tangram.structure.BaseCell;
import com.tmall.wireless.tangram.support.SimpleClickSupport;
import com.tmall.wireless.tangram.support.async.AsyncLoader;
import com.tmall.wireless.tangram.support.async.AsyncPageLoader;
import com.tmall.wireless.tangram.support.async.CardLoadSupport;
import com.tmall.wireless.tangram.util.IInnerImageSetter;
import com.tmall.wireless.tangram.util.LogUtils;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rv;
    private TangramBuilder.InnerBuilder mBuilder;
    private TangramEngine engine;
    private static final String TAG = "MainActivity";
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        otherInit();

        TangramBuilder.init(this, new IInnerImageSetter() {
            @Override
            public <IMAGE extends ImageView> void doLoadImageUrl(@NonNull IMAGE view, @Nullable String url) {
                if (view.getTag()!=null&&view.getTag().equals("noCache")) {
                    Glide.with(MainActivity.this).load(url).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)).into(view);
                } else {
                    Glide.with(MainActivity.this).load(url).into(view);
                }
            }
        }, ImageView.class);
        TangramBuilder.switchLog(true);
        mHandler = new Handler();
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
                if (cell.extras.optString("msg").equals("浮动布局")) {
                    BusSupport bus = engine.getService(BusSupport.class);//发送event
                    bus.post(BusSupport.obtainEvent("floatClick", cell.id, null, null));
                }
                Toast.makeText(MainActivity.this, "点击了控件 type:"+type+"  msg:"+cell.extras.optString("msg"), Toast.LENGTH_SHORT).show();
            }
        });

        engine.bindView(rv);
        engine.setPreLoadNumber(2);
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //在 scroll 事件中触发 engine 的 onScroll，内部会触发需要异步加载的卡片去提前加载数据
                engine.onScrolled();
            }
        });

        CardLoadSupport loadSupport = new CardLoadSupport(new AsyncLoader() {
            @Override
            public void loadData(final Card card, @NonNull final LoadedCallback callback) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        LogUtils.i(TAG,card.load+"       thread="+Thread.currentThread().getName());
                        List<BaseCell> cells = card.getCells();
                        if (cells != null) {
                            for (final BaseCell cell : cells) {
                                mHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        LogUtils.i(TAG, cell.extras.toString());
                                        try {
                                            cell.extras.put("imgUrl", "http://images.csdn.net/20170731/390.jpg");
                                            cell.notifyDataChange();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, 0);
                                SystemClock.sleep(1000);
                            }
                        }
                        callback.finish();
                    }
                }).start();
            }
        }, new AsyncPageLoader() {
            @Override
            public void loadData(final int page, @NonNull final Card card, @NonNull final LoadedCallback callback) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        LogUtils.i(TAG, card.load + "    thread=" + Thread.currentThread().getName());
                        Toast.makeText(MainActivity.this, "异步加载第"+page, Toast.LENGTH_SHORT).show();
                        JSONArray arr = new JSONArray();
                        for (int i = 0; i < 9; i++) {
                            JSONObject obj = new JSONObject();
                            try {
                                obj.put("type", 4);
                                obj.put("msg", card.load + i);
                                switch (i % 3) {
                                    case 0:
                                        obj.put("imgUrl", "http://pic.xiami.net/images/common/uploadpic/0/1501693288300.jpg");
                                        break;
                                    case 1:
                                        obj.put("imgUrl", "http://images.csdn.net/20170802/1.jpg");
                                        break;
                                    case 2:
                                        obj.put("imgUrl", "http://img.ads.csdn.net/2017/201707281634387683.jpg");
                                        break;
                                }
                                arr.put(obj);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        List<BaseCell> cells = engine.parseComponent(arr);
                        if (page == 1) {
                            GroupBasicAdapter<Card, ?> adapter = engine.getGroupBasicAdapter();
                            card.setCells(cells);
                            adapter.refreshWithoutNotify();
                            Range<Integer> range = adapter.getCardRange(card);
                            adapter.notifyItemRemoved(range.getLower());
                            adapter.notifyItemRangeInserted(range.getLower(), cells.size());
                        } else {
                            card.addCells(cells);
                        }
                        callback.finish(card.page != 6);
                        card.notifyDataChange();//调用立即刷新起效
                    }
                }, 1000);
            }
        });
        CardLoadSupport.setInitialPage(1);
        engine.addCardLoadSupport(loadSupport);

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
                        loadData("dataFec2.json");
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
