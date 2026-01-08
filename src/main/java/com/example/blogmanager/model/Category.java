package com.example.blogmanager.model;

import java.util.List;

public class Category {
	private String id;
	private String name;
	private List<BlogPost> blogPosts;

	public Category() {
	}

	public Category(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<BlogPost> getBlogPosts() {
		return blogPosts;
	}

	public void setBlogPosts(List<BlogPost> blogPosts) {
		this.blogPosts = blogPosts;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Category category = (Category) o;

		return id != null ? id.equals(category.id) : category.id == null;
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}

	@Override
	public String toString() {
		return "Category{" + "id='" + id + '\'' + ", name='" + name + '\'' + '}';
	}
}
