package com.tzh.myapplication.ui.dto;

public class StepDto {
    public int mId; // 记录在sqlite的id
    public long mBeginTime; // 计步开始时间
    public long mEndTime; // 计步结束时间
    public int mMode; // 计步模式: 0:不支持模式, 1:静止, 2:走路, 3:跑步, 11:骑车, 12:交通工具
    public int mSteps; // 总步数

    public StepDto(int id, long beginTime, long endTime, int mode, int steps) {
        this.mId = id;
        this.mBeginTime = beginTime;
        this.mEndTime = endTime;
        this.mMode = mode;
        this.mSteps = steps;
    }
}
