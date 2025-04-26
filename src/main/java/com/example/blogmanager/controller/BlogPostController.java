package com.example.blogmanager.controller;

import com.example.blogmanager.repository.CategoryRepository;
import com.example.blogmanager.repository.BlogPostRepository;

import java.util.List;

import com.example.blogmanager.model.BlogPost;
import com.example.blogmanager.model.Category;

public class BlogPostController {

	private BlogPostRepository blogPostRepository;
	private CategoryRepository categoryRepository;

	public BlogPostController(BlogPostRepository blogPostRepository, CategoryRepository categoryRepository) {
		this.blogPostRepository = blogPostRepository;
		this.categoryRepository = categoryRepository;
	}

	public void createBlogPost(BlogPost blogPost, String categoryId) {
		Category category = categoryRepository.findById(categoryId);
		if (category != null) {
			blogPost.setCategory(category);
			blogPostRepository.save(blogPost); 
		} else {
			System.out.println("Category not found!");
		}
	}

	
	public List<BlogPost> getAllPosts() {
		return blogPostRepository.findAll();
	}

	
	public BlogPost getPostById(String id) {
		return blogPostRepository.findById(id);
	}

	
	public void updatePost(BlogPost blogPost) {
		blogPostRepository.update(blogPost);
	}

		public void deletePost(String id) {
		blogPostRepository.delete(id);
	}


	public List<BlogPost> getPostsByCategory(String categoryId) {
		return blogPostRepository.findByCategory(categoryId); 
	}

}
