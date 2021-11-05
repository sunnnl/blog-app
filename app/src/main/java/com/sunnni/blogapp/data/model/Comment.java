package com.sunnni.blogapp.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity(tableName = "comment",
        foreignKeys = {@ForeignKey(entity = Post.class,
        parentColumns = "no",
        childColumns = "postNo",
        onDelete = ForeignKey.CASCADE)
})
public class Comment {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    public int id;

    @ColumnInfo(name="date")
    public String date;
    @ColumnInfo(name="writer")
    public String writer;
    @ColumnInfo(name="content")
    public String content;

    @ColumnInfo(name="postNo")
    public int postNo;

    public Comment(String date, String writer, String content, int postNo){
        this.date = date;
        this.writer = writer;
        this.content = content;
        this.postNo = postNo;
    }
}
