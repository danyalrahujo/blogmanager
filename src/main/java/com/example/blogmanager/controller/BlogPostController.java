package com.example.blogmanager.controller;

import com.example.blogmanager.view.BlogPostView;
import com.example.blogmanager.repository.BlogPostRepository;

import com.example.blogmanager.model.BlogPost;

public class BlogPostController {

	private BlogPostView blogPostView;
	private BlogPostRepository blogPostRepository;

	public BlogPostController(BlogPostView blogPostView, BlogPostRepository blogPostRepository) {
		this.blogPostView = blogPostView;
		this.blogPostRepository = blogPostRepository;
	}

	public void getAllBlogPosts() {
		blogPostView.displayBlogPosts(blogPostRepository.findAll());
	}

	public void addBlogPost(BlogPost blogPost) {
		BlogPost existingBlogPost = blogPostRepository.findById(blogPost.getId());
		if (existingBlogPost != null) {
			blogPostView.showErrorMessage("Blog post with ID " + blogPost.getId() + " already exists.", blogPost);
			return;
		}

		blogPostRepository.save(blogPost);
		blogPostView.addBlogPost(blogPost);
	}

	public void deleteBlogPost(BlogPost blogPost) {
		BlogPost existingBlogPost = blogPostRepository.findById(blogPost.getId());
		if (existingBlogPost == null) {
			blogPostView.showErrorMessage("No blog post found with ID " + blogPost.getId(), blogPost);
			return;
		}

		blogPostRepository.delete(blogPost.getId());
		blogPostView.deleteBlogPost(blogPost);
	}

	public void updateBlogPost(BlogPost blogPost) {
		BlogPost existingBlogPost = blogPostRepository.findById(blogPost.getId());
		if (existingBlogPost == null) {
			blogPostView.showErrorMessage("No blog post found with ID " + blogPost.getId(), blogPost);
			return;
		}

		blogPostRepository.update(blogPost);
		blogPostView.updateBlogPost(blogPost);
	}

}
