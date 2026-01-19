package com.example.blogmanager.controller;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.ignoreStubs;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.blogmanager.model.BlogPost;
import com.example.blogmanager.repository.BlogPostRepository;
import com.example.blogmanager.view.BlogPostView;

public class BlogPostControllerTest {
	@Mock
	private BlogPostRepository blogPostRepository;

	@Mock
	private BlogPostView blogPostView;

	@InjectMocks
	private BlogPostController blogPostController;

	private AutoCloseable closeable;

	@Before
	public void setup() {
		closeable = MockitoAnnotations.openMocks(this);
	}

	@After
	public void releaseMocks() throws Exception {
		closeable.close();
	}

	@Test
	public void testAllBlogPosts() {
		List<BlogPost> blogPosts = asList(new BlogPost("1", "Hello", "First post", "cat1", "2025-04-01", null));
		when(blogPostRepository.findAll()).thenReturn(blogPosts);
		blogPostController.getAllBlogPosts();
		verify(blogPostView).displayBlogPosts(blogPosts);
	}

	@Test
	public void testGetAllBlogPostsWithEmptyList() {
		when(blogPostRepository.findAll()).thenReturn(null); // Mock an empty list

		blogPostController.getAllBlogPosts();

		verify(blogPostView).displayBlogPosts(null);
	}

	@Test
	public void testNewBlogPostWhenBlogPostDoesNotAlreadyExist() {
		BlogPost blogPost = new BlogPost("1", "Hello", "First post", "cat1", "2025-04-01", null);
		when(blogPostRepository.findById("1")).thenReturn(null);
		blogPostController.addBlogPost(blogPost);
		InOrder inOrder = inOrder(blogPostRepository, blogPostView);
		inOrder.verify(blogPostRepository).save(blogPost);
		inOrder.verify(blogPostView).addBlogPost(blogPost);
	}

	@Test
	public void testNewBlogPostWhenBlogPostAlreadyExists() {
		BlogPost blogPostToAdd = new BlogPost("1", "Hello", "First post", "cat1", "2025-04-01", null);
		BlogPost existingBlogPost = new BlogPost("1", "Hello", "First post", "cat1", "2025-04-01", null);
		when(blogPostRepository.findById("1")).thenReturn(existingBlogPost);
		blogPostController.addBlogPost(blogPostToAdd);
		verify(blogPostView).showErrorMessage("Blog post with ID 1 already exists.", existingBlogPost);
		verifyNoMoreInteractions(ignoreStubs(blogPostRepository));
	}

	@Test
	public void testUpdateBlogPostWhenBlogPostExists() {
		BlogPost blogPostToUpdate = new BlogPost("1", "Hello", "First post", "cat1", "2025-04-01", null);
		when(blogPostRepository.findById("1")).thenReturn(blogPostToUpdate);

		blogPostController.updateBlogPost(blogPostToUpdate);

		InOrder inOrder = inOrder(blogPostRepository, blogPostView);
		inOrder.verify(blogPostRepository).update(blogPostToUpdate);
		inOrder.verify(blogPostView).updateBlogPost(blogPostToUpdate);
	}

	@Test
	public void testUpdateBlogPostWhenBlogPostDoesNotExist() {
		BlogPost blogPostToUpdate = new BlogPost("1", "Hello", "First post", "cat1", "2025-04-01", null);
		when(blogPostRepository.findById("1")).thenReturn(null);

		blogPostController.updateBlogPost(blogPostToUpdate);

		verify(blogPostView).showErrorMessage("No blog post found with ID 1", blogPostToUpdate);
		verifyNoMoreInteractions(ignoreStubs(blogPostRepository));
	}

	@Test
	public void testDeleteBlogPostWhenBlogPostExists() {
		BlogPost blogPostToDelete = new BlogPost("1", "Hello", "First post", "cat1", "2025-04-01", null);
		when(blogPostRepository.findById("1")).thenReturn(blogPostToDelete);
		blogPostController.deleteBlogPost(blogPostToDelete);
		InOrder inOrder = inOrder(blogPostRepository, blogPostView);
		inOrder.verify(blogPostRepository).delete("1");
		inOrder.verify(blogPostView).deleteBlogPost(blogPostToDelete);
	}

	@Test
	public void testDeleteBlogPostWhenBlogPostDoesNotExist() {
		BlogPost blogPost = new BlogPost("1", "Hello", "First post", "cat1", "2025-04-01", null);
		when(blogPostRepository.findById("1")).thenReturn(null);
		blogPostController.deleteBlogPost(blogPost);
		verify(blogPostView).showErrorMessage("No blog post found with ID 1", blogPost);
		verifyNoMoreInteractions(ignoreStubs(blogPostRepository));
	}


}
