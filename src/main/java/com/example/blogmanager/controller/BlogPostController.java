package com.example.blogmanager.controller;

import com.example.blogmanager.model.BlogPost;
import com.example.blogmanager.repository.BlogPostRepository;
import com.example.blogmanager.repository.CategoryRepository;
import com.example.blogmanager.view.BlogPostView;

public class BlogPostController {

	private BlogPostView blogPostView;
	private BlogPostRepository blogPostRepository;
	private CategoryRepository categoryRepository;

	public BlogPostController(BlogPostView blogPostView, BlogPostRepository blogPostRepository,
			CategoryRepository categoryRepository) {

		this.blogPostView = blogPostView;
		this.blogPostRepository = blogPostRepository;
		this.categoryRepository = categoryRepository;
	}

	public BlogPostController(BlogPostView blogPostView, BlogPostRepository blogPostRepository) {
		this.blogPostView = blogPostView;
		this.blogPostRepository = blogPostRepository;
	}

	public void getAllBlogPosts() {
		blogPostView.displayBlogPosts(blogPostRepository.findAll());
	}

	public synchronized void addBlogPost(BlogPost blogPost) {
		BlogPost existingBlogPost = blogPostRepository.findById(blogPost.getId());
		if (existingBlogPost != null) {
			blogPostView.showErrorMessage("Blog post with ID " + blogPost.getId() + " already exists.", blogPost);
			return;
		}

		blogPostRepository.save(blogPost);
		blogPostView.addBlogPost(blogPost);
	}

	public synchronized void deleteBlogPost(BlogPost blogPost) {
		BlogPost existingBlogPost = blogPostRepository.findById(blogPost.getId());
		if (existingBlogPost == null) {
			blogPostView.showErrorMessage("No blog post found with ID " + blogPost.getId(), blogPost);
			return;
		}

		blogPostRepository.delete(blogPost.getId());
		blogPostView.deleteBlogPost(blogPost);
	}

	public synchronized void updateBlogPost(BlogPost blogPost) {
		BlogPost existingBlogPost = blogPostRepository.findById(blogPost.getId());
		if (existingBlogPost == null) {
			blogPostView.showErrorMessage("No blog post found with ID " + blogPost.getId(), blogPost);
			return;
		}

		blogPostRepository.update(blogPost);
		blogPostView.updateBlogPost(blogPost);
	}


}
