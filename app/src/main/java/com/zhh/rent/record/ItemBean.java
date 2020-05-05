package com.zhh.rent.record;


import com.zhh.rent.sqlite.Rent;

public class ItemBean {

    public int itemImage;
    public String itemTitle;
    public String itemContent;
    private Rent rent;

    public ItemBean(int itemImage, String itemTitle, String itemContent) {
        this.itemTitle = itemTitle;
        this.itemContent = itemContent;
        this.itemImage = itemImage;
    }

    public ItemBean(int itemImage, String itemTitle, String itemContent,Rent rent) {
        this.itemTitle = itemTitle;
        this.itemContent = itemContent;
        this.itemImage = itemImage;
        this.rent = rent;
    }

    public Rent getRent() {
        return rent;
    }

    @Override
    public String toString() {
        return "ItemBean{" +
                "itemImage=" + itemImage +
                ", itemTitle='" + itemTitle + '\'' +
                ", itemContent='" + itemContent + '\'' +
                ", rent=" + rent +
                '}';
    }
}
