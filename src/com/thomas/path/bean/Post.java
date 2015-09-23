package com.thomas.path.bean;

import com.google.gson.Gson;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * 帖子
 * 
 * @author thomas
 * 
 */
public class Post extends BmobObject {
	private BmobFile content;
	private String title;
	private User author;
	private BmobRelation likes;
	private BmobRelation comments;
	private String likes_number;
	
	

	public String getLikes_number() {
		return likes_number;
	}

	public void setLikes_number(String likes_number) {
		this.likes_number = likes_number;
	}

	public BmobFile getContent() {
		return content;
	}

	public void setContent(BmobFile content) {
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public BmobRelation getLikes() {
		return likes;
	}

	public void setLikes(BmobRelation likes) {
		this.likes = likes;
	}

	public BmobRelation getComments() {
		return comments;
	}

	public void setComments(BmobRelation comments) {
		this.comments = comments;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}

}
