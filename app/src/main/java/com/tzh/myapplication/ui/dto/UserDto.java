package com.tzh.myapplication.ui.dto;

import com.tzh.mylibrary.util.tool.Cn2Spell;

import java.io.Serializable;

public class UserDto implements Serializable, Comparable<UserDto> {
    String name;    //名字
    String content; //内容
    String time; //日期
    String pinyin;  //中文转换为拼音
    String start; //首字母

    public UserDto(String name, String content, String time, int headerid) {
        this.name = name;
        this.content = content;
        this.time = time;
    }

    public UserDto(String name) {
        this.name = name;
        pinyin = Cn2Spell.getPinYin(name);
        start = pinyin.substring(0, 1).toUpperCase();
        if (!start.matches("[A-Z]")) {
            start = "#";
        }
    }


    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getPinyin() {
        return pinyin;
    }

    public String getStart() {
        return start;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public int compareTo(UserDto user) {
        if (start.equals("#") && !user.getStart().equals("#")) {
            return 1;
        } else if (!start.equals("#") && user.getStart().equals("#")) {
            return -1;
        } else {
            return pinyin.compareToIgnoreCase(user.getPinyin());
        }
    }
}
