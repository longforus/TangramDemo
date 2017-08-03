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
import com.tmall.wireless.tangram.eventbus.BusSupport;
import com.tmall.wireless.tangram.eventbus.Event;
import com.tmall.wireless.tangram.eventbus.EventHandlerWrapper;
import com.tmall.wireless.tangram.structure.BaseCell;
import com.tmall.wireless.tangram.structure.view.ITangramViewLifeCycle;
import com.tmall.wireless.tangram.util.LogUtils;

/**
 * Created by villadora on 15/8/24.
 */
public class SimpleTitleView extends FrameLayout implements ITangramViewLifeCycle {
    private TextView textView;
    private BaseCell cell;
    private EventHandlerWrapper mWrapper;

    public SimpleTitleView(Context context) {
        super(context);
        init();
    }

    public SimpleTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SimpleTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.item_title, this);
        textView = (TextView) findViewById(R.id.title);
    }

    @Override
    public void cellInited(BaseCell cell) {

        mWrapper = BusSupport.wrapEventHandler("floatClick", null, this, "onFloat");
        BusSupport service = null;
        if (cell.serviceManager != null) {//注册事件接收
            service = cell.serviceManager.getService(BusSupport.class);
            service.register(mWrapper);
        }

        setOnClickListener(cell);
        this.cell = cell;
    }

    public void onFloat(Event event) {
        LogUtils.i(TAG,this+"接收到总线事件  type:"+event.type);
        textView.setText("总线event");
    }

    private static final String TAG = "SimpleTitleView";
    @Override
    public void postBindView(BaseCell cell) {
        int pos = cell.pos;
        textView.setText(cell.optStringParam("msg"));
        textView.setTextSize(cell.optIntParam("textSize"));
        textView.setTextColor(Color.parseColor(cell.optStringParam("color")));
    }

    @Override
    public void postUnBindView(BaseCell cell) {
        BusSupport service = null;
        if (cell.serviceManager != null) {
            service = cell.serviceManager.getService(BusSupport.class);
        }
        if (service!=null&&mWrapper!=null) {
            service.unregister(mWrapper);
        }
    }
}
