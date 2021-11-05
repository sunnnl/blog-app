package com.sunnni.blogapp.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity(tableName = "post")
public class Post {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="no")
    public int no;

    @ColumnInfo(name="date")
    public String date;

    @ColumnInfo(name="title")
    public String title;

    @ColumnInfo(name="content")
    public String content;

    public Post(String date, String title, String content){
        this.date = date;
        this.title = title;
        this.content = content;
    }
}
