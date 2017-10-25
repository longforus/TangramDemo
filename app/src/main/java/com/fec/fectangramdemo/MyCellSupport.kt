package com.fec.fectangramdemo

import android.view.View
import com.tmall.wireless.tangram.structure.BaseCell
import com.tmall.wireless.tangram.support.CellSupport

/**
 * Created by XQ Yang on 2017/8/7  16:19.
 * Description :
 */
class MyCellSupport : CellSupport() {

  override fun isValid(cell: BaseCell<*>?): Boolean {
    println("isValid")
    return cell!=null
  }

  override fun bindView(cell: BaseCell<*>?, view: View?) {
    println("$cell 绑定前")
    super.bindView(cell, view)//绑定数据前时调用，比如可以用来绑定contentDespcription，绑定点击事件；
  }

  override fun postBindView(cell: BaseCell<*>?, view: View?) {
    println("$cell 绑定后")
    super.postBindView(cell, view)//绑定数据后调用
  }

  override fun unBindView(cell: BaseCell<*>?, view: View?) {
    println("$cell 解除绑定")
    super.unBindView(cell, view)//解除绑定时
  }
}