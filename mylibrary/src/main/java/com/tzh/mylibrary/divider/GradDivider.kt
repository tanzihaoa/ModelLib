package com.tzh.mylibrary.divider

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.tzh.mylibrary.R
import com.tzh.mylibrary.util.getColorInt

class GradDivider(val context: Context, val space: Float, val num: Int, private val heightSpace: Float = space) : XBaseDivider(context) {

    private val mSpace1 by lazy {
        if (num < 3) {
            space / 2
        } else {
            space / 3
        }
    }
    private val mSpace2 by lazy {
        if (num < 3) {
            space / 2
        } else {
            space / 3 * 2
        }
    }


    private val mColor by lazy { context.getColorInt(R.color.transparent) }

    private val mSpaceTopLeftDivider by lazy {
        XDividerBuilder()
            .setRightSideLine(true, mColor, mSpace2, 0f, 0f)
            .create()
    }

    private val mSpaceTopCenterDivider by lazy {
        XDividerBuilder()
            .setLeftSideLine(true, mColor, mSpace1, 0f, 0f)
            .setRightSideLine(true, mColor, mSpace1, 0f, 0f)
            .create()
    }

    private val mSpaceTopRightDivider by lazy {
        XDividerBuilder()
            .setLeftSideLine(true, mColor, mSpace2, 0f, 0f)
            .create()
    }

    private val mSpaceOtherLeftDivider by lazy {
        XDividerBuilder()
            .setTopSideLine(true, mColor, heightSpace, 0f, 0f)
            .setRightSideLine(true, mColor, mSpace2, 0f, 0f)
            .create()
    }
    private val mSpaceOtherCenterDivider by lazy {
        XDividerBuilder()
            .setLeftSideLine(true, mColor, mSpace1, 0f, 0f)
            .setTopSideLine(true, mColor, heightSpace, 0f, 0f)
            .setRightSideLine(true, mColor, mSpace1, 0f, 0f)
            .create()
    }

    private val mSpaceOtherRightDivider by lazy {
        XDividerBuilder()
            .setLeftSideLine(true, mColor, mSpace2, 0f, 0f)
            .setTopSideLine(true, mColor, heightSpace, 0f, 0f)
            .create()
    }


    override fun getDivider(adapter: RecyclerView.Adapter<*>?, itemPosition: Int): XDivider {
        adapter ?: return defaultDivider

        if (itemPosition < num) {
            when {
                itemPosition % num == 0 -> {
                    return mSpaceTopLeftDivider
                }
                itemPosition % num < num - 1 -> {
                    return mSpaceTopCenterDivider
                }
                itemPosition % num == num - 1 -> {
                    return mSpaceTopRightDivider
                }
            }
        } else {
            when {
                itemPosition % num == 0 -> {
                    return mSpaceOtherLeftDivider
                }
                itemPosition % num < num - 1 -> {
                    return mSpaceOtherCenterDivider
                }
                itemPosition % num == num - 1 -> {
                    return mSpaceOtherRightDivider
                }
            }
        }

        return defaultDivider
    }
}