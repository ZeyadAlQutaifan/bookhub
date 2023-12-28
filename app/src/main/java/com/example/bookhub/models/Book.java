package com.example.bookhub.models;

import java.util.ArrayList;
import java.util.List;

public class Book {
    private String writerId ;
    private String title ;
    private String university ;
    private String department;
    private List<String> images = new ArrayList<>();
    private String type ;
    private boolean reserved ;

    private long creationTime ;

    public Book() {
    }

    public Book(String writerId, String title, String university, String department, List<String> images, String type, boolean reserved, long creationTime) {
        this.writerId = writerId;
        this.title = title;
        this.university = university;
        this.department = department;
        this.images = images;
        this.type = type;
        this.reserved = reserved;
        this.creationTime = creationTime;
    }

    public String getWriterId() {
        return writerId;
    }

    public void setWriterId(String writerId) {
        this.writerId = writerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String  getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isReserved() {
        return reserved;
    }

    public void setReserved(boolean reserved) {
        this.reserved = reserved;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }
}
