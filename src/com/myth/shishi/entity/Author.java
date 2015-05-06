package com.myth.shishi.entity;

import java.io.Serializable;

public class Author implements Serializable
{

    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;


    private String author;
    
    private String intro;
    

    private String dynasty;
    
    private int p_num;
    
    private String en_name;
    
    private int color;



    public String getIntro()
    {
        return intro;
    }

    public void setIntro(String intro)
    {
        this.intro = intro;
    }



    public int getP_num()
    {
        return p_num;
    }

    public void setP_num(int p_num)
    {
        this.p_num = p_num;
    }

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public String getDynasty()
    {
        return dynasty;
    }

    public void setDynasty(String dynasty)
    {
        this.dynasty = dynasty;
    }

    public String getEn_name()
    {
        return en_name;
    }

    public void setEn_name(String en_name)
    {
        this.en_name = en_name;
    }

    public int getColor()
    {
        return color;
    }

    public void setColor(int color)
    {
        this.color = color;
    }

    
    


}
