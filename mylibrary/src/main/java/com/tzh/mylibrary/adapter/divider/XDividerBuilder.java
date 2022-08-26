package com.tzh.mylibrary.adapter.divider;

import androidx.annotation.ColorInt;

public class XDividerBuilder {

    private XSideLine leftSideLine;
    private XSideLine topSideLine;
    private XSideLine rightSideLine;
    private XSideLine bottomSideLine;


    public XDividerBuilder setLeftSideLine(boolean isHave, @ColorInt int color, float widthDp, float startPaddingDp, float endPaddingDp) {
        this.leftSideLine = new XSideLine(isHave, color, widthDp, startPaddingDp, endPaddingDp);
        return this;
    }

    public XDividerBuilder setTopSideLine(boolean isHave, @ColorInt int color, float widthDp, float startPaddingDp, float endPaddingDp) {
        this.topSideLine = new XSideLine(isHave, color, widthDp, startPaddingDp, endPaddingDp);
        return this;
    }

    public XDividerBuilder setRightSideLine(boolean isHave, @ColorInt int color, float widthDp, float startPaddingDp, float endPaddingDp) {
        this.rightSideLine = new XSideLine(isHave, color, widthDp, startPaddingDp, endPaddingDp);
        return this;
    }

    public XDividerBuilder setBottomSideLine(boolean isHave, @ColorInt int color, float widthDp, float startPaddingDp, float endPaddingDp) {
        this.bottomSideLine = new XSideLine(isHave, color, widthDp, startPaddingDp, endPaddingDp);
        return this;
    }

    public XDivider create() {
        //提供一个默认不显示的sideline，防止空指针
        XSideLine defaultSideLine = new XSideLine(false, 0xff666666, 0, 0, 0);

        leftSideLine = (leftSideLine != null ? leftSideLine : defaultSideLine);
        topSideLine = (topSideLine != null ? topSideLine : defaultSideLine);
        rightSideLine = (rightSideLine != null ? rightSideLine : defaultSideLine);
        bottomSideLine = (bottomSideLine != null ? bottomSideLine : defaultSideLine);

        return new XDivider(leftSideLine, topSideLine, rightSideLine, bottomSideLine);
    }


}
