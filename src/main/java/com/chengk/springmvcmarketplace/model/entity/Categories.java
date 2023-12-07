package com.chengk.springmvcmarketplace.model.entity;

import java.sql.Timestamp;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("Categories")
public class Categories {
    @Id
    private Integer id;
    private String title;
    private Timestamp createdOn;
    private Timestamp lastUpdated;

    public Categories(Integer id, String title, Timestamp createdOn, Timestamp lastUpdated) {
        this.id = id;
        this.title = title;
        this.createdOn = createdOn;
        this.lastUpdated = lastUpdated;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Timestamp getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Timestamp createdOn) {
        this.createdOn = createdOn;
    }

    public Timestamp getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Timestamp lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

}
