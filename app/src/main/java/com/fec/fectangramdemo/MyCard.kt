package com.fec.fectangramdemo

import com.alibaba.android.vlayout.LayoutHelper
import com.tmall.wireless.tangram.dataparser.concrete.Card
import org.json.JSONObject

/**
 * Created by XQ Yang on 2017/8/7  17:45.
 * Description :
 */
class MyCard: Card() {
  override fun parseStyle(data: JSONObject?) {
    super.parseStyle(data)
  }

  override fun convertLayoutHelper(oldHelper: LayoutHelper?): LayoutHelper? {
    return super.convertLayoutHelper(oldHelper)
  }
}