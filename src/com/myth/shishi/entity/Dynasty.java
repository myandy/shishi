package com.myth.shishi.entity;

import java.io.Serializable;

public class Dynasty implements Serializable
{

    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;



    private String intro;
    
    private  String intro2;

    private String dynasty;


    public String getIntro()
    {
        return intro;
    }

    public void setIntro(String intro)
    {
        this.intro = intro;
    }


    public String getIntro2()
    {
        return intro2;
    }

    public void setIntro2(String intro2)
    {
        this.intro2 = intro2;
    }

    public String getDynasty()
    {
        return dynasty;
    }

    public void setDynasty(String dynasty)
    {
        this.dynasty = dynasty;
    }

    
    


}
