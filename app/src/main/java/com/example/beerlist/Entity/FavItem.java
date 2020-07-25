package com.example.beerlist.Entity;


/*
 * Class for set or get data from the Favorites
 */
public class FavItem {

    private String item_name;
    private String item_tagline;
    private String item_description;
    private String key_id;
    private String item_image;


    public FavItem() {
    }

    public FavItem(String item_name, String item_tagline, String item_description, String key_id, String item_image) {
        this.item_name = item_name;
        this.item_tagline = item_tagline;
        this.item_description = item_description;
        this.key_id = key_id;
        this.item_image = item_image;
    }


    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_tagline() {
        return item_tagline;
    }

    public void setItem_tagline(String item_tagline) {
        this.item_tagline = item_tagline;
    }

    public String getItem_description() {
        return item_description;
    }

    public void setItem_description(String item_description) {
        this.item_description = item_description;
    }

    public String getKey_id() {
        return key_id;
    }

    public void setKey_id(String key_id) {
        this.key_id = key_id;
    }

    public String getItem_image() {
        return item_image;
    }

    public void setItem_image(String item_image) {
        this.item_image = item_image;
    }
}
