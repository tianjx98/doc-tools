package com.example.application.views.docsplit.doc;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * description
 * </p>
 *
 * @author junxiong.tian@hand-china.com 2021/3/29 22:31
 */
public class Markdown {
    private String title;
    private int titleLevel;
    private List<Markdown> subs = new LinkedList<>();

    public Markdown(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTitleLevel() {
        return titleLevel;
    }

    public void setTitleLevel(int titleLevel) {
        this.titleLevel = titleLevel;
    }

    public List<Markdown> getSubs() {
        return subs;
    }

    public void setSubs(List<Markdown> subs) {
        this.subs = subs;
    }
}
