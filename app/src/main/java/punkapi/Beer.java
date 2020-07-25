package punkapi;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


//Set and Get values API
public class Beer {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("tagline")
    @Expose
    private String tagline;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;

    //Favorite flag
    private String isFavourite = "0";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {return name;}

    public void setName(String name) {
        this.name = name;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getIsFavourite(){return isFavourite;}

    public void setFavourite(String favourite) {
        isFavourite = favourite;
    }
}