package com.tzh.mylibrary.divider;

public class XDivider {

    public XSideLine leftSideLine;
    public XSideLine topSideLine;
    public XSideLine rightSideLine;
    public XSideLine bottomSideLine;


    public XDivider(XSideLine leftSideLine, XSideLine topSideLine, XSideLine rightSideLine, XSideLine bottomSideLine) {
        this.leftSideLine = leftSideLine;
        this.topSideLine = topSideLine;
        this.rightSideLine = rightSideLine;
        this.bottomSideLine = bottomSideLine;
    }

    public XSideLine getLeftSideLine() {
        return leftSideLine;
    }

    public void setLeftSideLine(XSideLine leftSideLine) {
        this.leftSideLine = leftSideLine;
    }

    public XSideLine getTopSideLine() {
        return topSideLine;
    }

    public void setTopSideLine(XSideLine topSideLine) {
        this.topSideLine = topSideLine;
    }

    public XSideLine getRightSideLine() {
        return rightSideLine;
    }

    public void setRightSideLine(XSideLine rightSideLine) {
        this.rightSideLine = rightSideLine;
    }

    public XSideLine getBottomSideLine() {
        return bottomSideLine;
    }

    public void setBottomSideLine(XSideLine bottomSideLine) {
        this.bottomSideLine = bottomSideLine;
    }
}
