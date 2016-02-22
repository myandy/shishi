package com.myth.shishi.entity;

import java.io.Serializable;

public class Former implements Serializable
{

    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;

    
    private int id;
    
    private String name;
    
    private String yun;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getYun()
    {
        return yun;
    }

    public void setYun(String yun)
    {
        this.yun = yun;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }
    
    


}
