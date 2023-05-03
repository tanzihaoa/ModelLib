package com.tzh.myapplication.ui.activity

import android.R.attr
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.tzh.myapplication.R
import com.tzh.myapplication.base.AppBaseActivity
import com.tzh.myapplication.databinding.ActivityCardBinding
import com.tzh.myapplication.ui.adapter.CardAdapter
import com.tzh.myapplication.ui.adapter.NewCardAdapter
import com.tzh.myapplication.ui.dto.CardDto
import com.tzh.mylibrary.util.setOnClickNoDouble
import com.tzh.mylibrary.view.card.*


class CardActivity : AppBaseActivity<ActivityCardBinding>(R.layout.activity_card) {
    companion object {
        @JvmStatic
        fun start(context: Context) {
            context.startActivity(Intent(context, CardActivity::class.java))
        }
    }

    var mReItemTouchHelper: ReItemTouchHelper? = null

    override fun initView() {
        val list: MutableList<CardDto> = initCards()
        val setting = CardSetting()

        setting.setSwipeListener(object : OnSwipeCardListener<CardDto>{
            override fun onSwiping(viewHolder: RecyclerView.ViewHolder?, dx: Float, dy: Float, direction: Int) {
                when (direction) {
                    ReItemTouchHelper.DOWN -> Log.e("aaa===", "swiping direction=down")
                    ReItemTouchHelper.UP -> Log.e("aaa===", "swiping direction=up")
                    ReItemTouchHelper.LEFT -> Log.e("aaa===", "swiping direction=left")
                    ReItemTouchHelper.RIGHT -> Log.e("aaa===", "swiping direction=right")
                }
            }

            override fun onSwipedOut(viewHolder: RecyclerView.ViewHolder?, t: CardDto?, direction: Int) {
                when (direction) {
                    ReItemTouchHelper.DOWN -> Toast.makeText(
                        this@CardActivity,
                        "swipe down out",
                        Toast.LENGTH_SHORT
                    ).show()
                    ReItemTouchHelper.UP -> Toast.makeText(
                        this@CardActivity,
                        "swipe up out ",
                        Toast.LENGTH_SHORT
                    ).show()
                    ReItemTouchHelper.LEFT -> Toast.makeText(
                        this@CardActivity,
                        "swipe left out",
                        Toast.LENGTH_SHORT
                    ).show()
                    ReItemTouchHelper.RIGHT -> Toast.makeText(
                        this@CardActivity,
                        "swipe right out",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onSwipedClear() {
                Toast.makeText(this@CardActivity, "cards are consumed", Toast.LENGTH_SHORT).show();
            }
        })

        val helperCallback = CardTouchHelperCallback(binding.recyclerView, list, setting)
        mReItemTouchHelper = ReItemTouchHelper(helperCallback)
        val layoutManager = CardLayoutManager(mReItemTouchHelper!!, setting)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = NewCardAdapter(list)

        binding.turnLeft.setOnClickNoDouble {
            mReItemTouchHelper?.swipeManually(ReItemTouchHelper.LEFT)
        }

        binding.turnRight.setOnClickNoDouble {
            mReItemTouchHelper?.swipeManually(ReItemTouchHelper.RIGHT)
        }
    }

    override fun initData() {

    }


    val U1 = "http://n.sinaimg.cn/translate/20161024/WRsW-fxwztru6973377.jpg"
    val U2 = "http://img02.tooopen.com/images/20151122/tooopen_sy_149199661189.jpg"
    val U3 = "http://gmimg.geimian.com/pic/2015/04/20150419_213113_920.jpg"
    val U4 = "http://pic.qiantucdn.com/58pic/11/84/20/04s58PICiYA.jpg"
    val U5 = "http://img02.tooopen.com/images/20160122/tooopen_sy_155234647714.jpg"
    val U6 = "http://seopic.699pic.com/photo/50007/5448.jpg_wh1200.jpg"
    val U7 = "https://thumbs.dreamstime.com/b/%E6%8A%BD%E8%B1%A1%E6%B2%B9%E7%94%BB-15920804.jpg"

    private fun initCards(): MutableList<CardDto> {
        val list: MutableList<CardDto> = mutableListOf()
        val cardBean = CardDto(U1,"first card")

        val cardBean1 = CardDto(U2,"second card")

        val cardBean2 = CardDto(U3,"third card")

        val cardBean3 = CardDto(U4,"fourth card")

        val cardBean4 = CardDto(U5,"fifth card")

        val cardBean5 = CardDto(U6,"sixth card")

        val cardBean6 = CardDto(U7,"seventh card")

        list.add(cardBean)
        list.add(cardBean1)
        list.add(cardBean2)
        list.add(cardBean3)
        list.add(cardBean4)
        list.add(cardBean5)
        list.add(cardBean6)
        return list
    }
}