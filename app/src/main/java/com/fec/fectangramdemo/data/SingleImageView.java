package com.fec.fectangramdemo.data;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.tmall.wireless.tangram.dataparser.concrete.Style;
import com.tmall.wireless.tangram.structure.BaseCell;
import com.tmall.wireless.tangram.structure.view.ITangramViewLifeCycle;
import com.tmall.wireless.tangram.util.ImageUtils;

public class SingleImageView extends LinearLayout implements ITangramViewLifeCycle {

    public ImageView icon;

    public TextView titleTextView;

    private Context context;

    private final int DEFAULT_ICON_SIZE = Style.dp2px(150);

    public SingleImageView(Context context) {
        this(context, null);
    }

    public SingleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public SingleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initUI(context, DEFAULT_ICON_SIZE);
    }

    private void initUI(Context context, int size){
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);
        setBackgroundColor(Color.WHITE);

        icon = new ImageView(context);
        icon.setScaleType(ImageView.ScaleType.CENTER_CROP);

        LayoutParams iconLp = new LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, size);
        addView(icon, iconLp);
/*
        titleTextView = new TextView(context);
        titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
        LayoutParams titleLp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        titleLp.topMargin = Style.dp2px(4.0);
        addView(titleTextView, titleLp);

        Space space = new Space(context);
        LayoutParams spaceLp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(space, spaceLp);*/
    }


    @Override
    public void cellInited(BaseCell cell) {
        setOnClickListener(cell);
    }

    @Override
    public void postBindView(BaseCell cell) {
//        String title = cell.optStringParam("title");
//        if(!isEmpty(title)) {
//            titleTextView.setText(title);
//        }
//        String imgUrl = cell.optStringParam("imgUrl");
//        if (!isEmpty(imgUrl)) {
//            ImageUtils.doLoadImageUrl(this.icon, imgUrl);
//        }
//        String titleColor = cell.optStringParam("titleColor");
//        if (!isEmpty(titleColor)) {
//            int color = parseColor(titleColor, "#555555");
//            titleTextView.setTextColor(color);
//        }

        String imgUrl = cell.optStringParam("imgUrl");
        ImageUtils.doLoadImageUrl(icon, imgUrl);
        icon.setOnClickListener(cell);
    }

    @Override
    public void postUnBindView(BaseCell cell) {
    }

    public static int parseColor(String colorStr, String defaultColor) {
        try {
            return Color.parseColor(colorStr);
        } catch (Exception e) {
            return Color.parseColor(defaultColor);
        }
    }

    public static boolean isEmpty(String string){
        return string == null || string.isEmpty() || "null".equalsIgnoreCase(string);
    }

}