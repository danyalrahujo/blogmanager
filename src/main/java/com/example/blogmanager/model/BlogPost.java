package com.example.blogmanager.model;

import java.util.Objects;

public class BlogPost {
	private String id;
	private String title;
	private String content;
	private String author;
	private String creationDate;
	private Category category;

	public BlogPost() {
	}

	public BlogPost(String id, String title, String content, String author, String creationDate, Category category) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.author = author;
		this.creationDate = creationDate;
		this.category = category;
	}

	// Getters and Setters

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		BlogPost blogPost = (BlogPost) o;

		return Objects.equals(id, blogPost.id) && Objects.equals(title, blogPost.title)
				&& Objects.equals(content, blogPost.content) && Objects.equals(author, blogPost.author)
				&& Objects.equals(creationDate, blogPost.creationDate) && Objects.equals(category, blogPost.category);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, title, content, author, creationDate, category);
	}

	@Override
	public String toString() {
		return id + " - " + title + " - " + author + " - " + creationDate + " - " + content + " - " + category;
	}

}
