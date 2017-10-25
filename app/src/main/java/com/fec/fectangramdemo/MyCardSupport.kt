package com.fec.fectangramdemo

import android.view.View
import com.tmall.wireless.tangram.dataparser.concrete.Card
import com.tmall.wireless.tangram.support.CardSupport

/**
 * Created by XQ Yang on 2017/8/7  16:50.
 * Description :
 */
class MyCardSupport: CardSupport() {
  override fun onBindBackgroundView(layoutView: View?, card: Card?) {

  }



  override fun onUnbindBackgroundView(layoutView: View?, card: Card?) {
    super.onUnbindBackgroundView(layoutView, card)

  }
}