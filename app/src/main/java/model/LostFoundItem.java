package com.example.lostandfound.model;

public class LostFoundItem {
    private int id;
    private String postType;
    private String name;
    private String phone;
    private String description;
    private String date;
    private String location;
    private String category;
    private String imageUri;
    private String timestamp;

    public LostFoundItem() {}

    public LostFoundItem(String postType, String name, String phone, String description,
                         String date, String location, String category,
                         String imageUri, String timestamp) {
        this.postType = postType;
        this.name = name;
        this.phone = phone;
        this.description = description;
        this.date = date;
        this.location = location;
        this.category = category;
        this.imageUri = imageUri;
        this.timestamp = timestamp;
    }

    public int getId()             { return id; }
    public String getPostType()    { return postType; }
    public String getName()        { return name; }
    public String getPhone()       { return phone; }
    public String getDescription() { return description; }
    public String getDate()        { return date; }
    public String getLocation()    { return location; }
    public String getCategory()    { return category; }
    public String getImageUri()    { return imageUri; }
    public String getTimestamp()   { return timestamp; }

    public void setId(int id)                 { this.id = id; }
    public void setPostType(String postType)   { this.postType = postType; }
    public void setName(String name)           { this.name = name; }
    public void setPhone(String phone)         { this.phone = phone; }
    public void setDescription(String d)       { this.description = d; }
    public void setDate(String date)           { this.date = date; }
    public void setLocation(String location)   { this.location = location; }
    public void setCategory(String category)   { this.category = category; }
    public void setImageUri(String imageUri)   { this.imageUri = imageUri; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}