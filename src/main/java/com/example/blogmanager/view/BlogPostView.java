package com.example.blogmanager.view;

import java.util.List;

import com.example.blogmanager.model.BlogPost;
import com.example.blogmanager.model.Category;

public interface BlogPostView {
	void displayBlogPosts(List<BlogPost> blogPosts);

	void addBlogPost(BlogPost blogPost);

	void deleteBlogPost(BlogPost blogPost);

	void updateBlogPost(BlogPost blogPost);

	void showErrorMessage(String message, BlogPost blogPost);

	void updateCategories(List<Category> categories);
}
