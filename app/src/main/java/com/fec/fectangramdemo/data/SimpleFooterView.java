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
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.fec.fectangramdemo.R;
import com.tmall.wireless.tangram.structure.BaseCell;
import com.tmall.wireless.tangram.structure.view.ITangramViewLifeCycle;

/**
 * Created by villadora on 15/8/24.
 */
public class SimpleFooterView extends FrameLayout implements ITangramViewLifeCycle {
    private BaseCell cell;
    private TextView tvLeft;
    private TextView tvRight;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2017-08-02 17:45:44 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        tvLeft = (TextView)findViewById( R.id.tv_left );
        tvRight = (TextView)findViewById( R.id.tv_right );
    }

    public SimpleFooterView(Context context) {
        super(context);
        init();
    }

    public SimpleFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SimpleFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        inflate(getContext(), R.layout.item_footer, this);
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
        tvLeft.setText(cell.optStringParam("msg"));
        tvLeft.setTextSize(cell.optIntParam("textSize"));
        tvLeft.setTextColor(Color.parseColor(cell.optStringParam("color")));
    }

    @Override
    public void postUnBindView(BaseCell cell) {
    }
}
