package com.myth.shishi.entity;

import java.io.Serializable;

public class Poetry implements Serializable
{

    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;

    private String author;

    private String intro;

    private String title;

    private String poetry;

    private int collect;

    private int id;

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public String getIntro()
    {
        return intro;
    }

    public void setIntro(String intro)
    {
        this.intro = intro;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getPoetry()
    {
        return poetry;
    }

    public void setPoetry(String poetry)
    {
        this.poetry = poetry;
    }

    public int getCollect()
    {
        return collect;
    }

    public void setCollect(int collect)
    {
        this.collect = collect;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
