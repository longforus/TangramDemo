/*
 * MIT License
 *
 * Copyright (c) 2017 Alibaba Group
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.fec.fectangramdemo.data;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.fec.fectangramdemo.R;
import com.tmall.wireless.tangram.structure.BaseCell;
import com.tmall.wireless.tangram.structure.view.ITangramViewLifeCycle;
import com.tmall.wireless.tangram.util.ImageUtils;

/**
 * Created by villadora on 15/8/24.
 */
public class ImageTvView extends FrameLayout implements ITangramViewLifeCycle {
    private BaseCell cell;
    private ImageView iv;
    private TextView tv;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2017-08-02 18:55:40 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        iv = (ImageView)findViewById( R.id.iv );
        tv = (TextView)findViewById( R.id.tv );
    }

    public ImageTvView(Context context) {
        super(context);
        init();
    }

    public ImageTvView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ImageTvView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        inflate(getContext(), R.layout.item_image_tv, this);
        findViews();
    }

    @Override
    public void cellInited(BaseCell cell) {
        setOnClickListener(cell);
        this.cell = cell;
    }

    @Override
    public void postBindView(BaseCell cell) {
        int pos = cell.pos;
        if (cell.optBoolParam("nocache")) {
            iv.setTag("noCache");
        }
        if (cell.style!=null&&cell.extras.optString("msg").equals("浮动布局")) {
            ViewGroup.LayoutParams params = iv.getLayoutParams();
            params.height = cell.style.height;
            params.width = cell.style.width;
            iv.setLayoutParams(params);
            tv.setLayoutParams(params);
            setLayoutParams(params);
        }

        ImageUtils.doLoadImageUrl(iv, cell.optStringParam("imgUrl"));
        tv.setText(cell.optStringParam("msg"));
    }

    @Override
    public void postUnBindView(BaseCell cell) {
    }
}
