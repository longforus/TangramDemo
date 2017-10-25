package com.fec.fectangramdemo.data;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.fec.fectangramdemo.R;
import com.longforus.eatchicken.SecondActivity;
import com.tmall.wireless.tangram.structure.BaseCell;
import com.tmall.wireless.tangram.structure.view.ITangramViewLifeCycle;
import com.tmall.wireless.tangram.util.ImageUtils;

/**
 * Created by XQ Yang on 2017/10/19  11:57.
 * Description :
 */

public class ReplaceView6 extends FrameLayout implements ITangramViewLifeCycle {
    private BaseCell cell;
    private ImageView iv;
    private TextView tv;


    private void findViews() {
        iv = (ImageView)findViewById( R.id.iv );
        tv = (TextView)findViewById( R.id.tv );
    }
    public ReplaceView6(Context context) {
        this(context,null);
    }

    public ReplaceView6(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        inflate(getContext(), R.layout.item_image_tv, this);
        findViews();
        Toast.makeText(getContext(), "大吉大利", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void cellInited(BaseCell cell) {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SecondActivity.class);
                getContext().startActivity(intent);
            }
        });
        this.cell = cell;
    }

    @Override
    public void postBindView(BaseCell cell) {
        int pos = cell.pos;
        ImageUtils.doLoadImageUrl(iv, cell.optStringParam("imgUrl"));
        tv.setText(cell.optStringParam("msg"));
    }

    @Override
    public void postUnBindView(BaseCell cell) {
    }
}
