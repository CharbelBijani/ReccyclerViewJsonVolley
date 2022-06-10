package com.example.reccyclerviewjsonvolley;

public class
ModelItem {
    /**
     * # Ajout des variables pour la création du constructeur, des getters et des setters
     **/
    private String imageUrl;
    private String creator;
    private int likes;

    public ModelItem(String imageUrl, String creator, int likes) {
        this.imageUrl = imageUrl;
        this.creator = creator;
        this.likes = likes;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getCreator() {
        return creator;
    }

    public int getLikes() {
        return likes;
    }
}