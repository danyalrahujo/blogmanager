package com.example.blogmanager.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.blogmanager.model.BlogPost;
import com.example.blogmanager.model.Category;
import com.example.blogmanager.repository.BlogPostRepository;
import com.example.blogmanager.repository.CategoryRepository;

public class TestBlogPostController {
	@Mock
	private BlogPostRepository blogPostRepository;

	@Mock
	private CategoryRepository categoryRepository;

	@InjectMocks
	private BlogPostController blogPostController;

	private AutoCloseable closeable;

	@Before
	public void setUp() {
		closeable = MockitoAnnotations.openMocks(this);
	}

	@After
	public void tearDown() throws Exception {
		closeable.close();
	}

	@Test
	public void testCreateBlogPost() {
		Category category = new Category("1", "Technology");
		BlogPost blogPost = new BlogPost("1", "Tech Post", "Content", "Author", "2025-04-01", category);

		when(categoryRepository.findById("1")).thenReturn(category);

		blogPostController.createBlogPost(blogPost, "1");

		verify(blogPostRepository).save(blogPost);
	}

	@Test
	public void testGetAllPosts() {
		Category category = new Category("1", "Technology");
		BlogPost blogPost = new BlogPost("1", "Tech Post", "Content", "Author", "2025-04-01", category);
		List<BlogPost> posts = Arrays.asList(blogPost);

		when(blogPostRepository.findAll()).thenReturn(posts);

		List<BlogPost> result = blogPostController.getAllPosts();

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals("Tech Post", result.get(0).getTitle());
	}

	@Test
	public void testGetPostById() {
		Category category = new Category("1", "Technology");
		BlogPost blogPost = new BlogPost("1", "Tech Post", "Content", "Author", "2025-04-01", category);

		when(blogPostRepository.findById("1")).thenReturn(blogPost);

		BlogPost result = blogPostController.getPostById("1");

		assertNotNull(result);
		assertEquals("Tech Post", result.getTitle());
	}

	@Test
	public void testDeletePost() {
		new BlogPost("1", "Tech Post", "Content", "Author", "2025-04-01", null);

		blogPostController.deletePost("1");

		verify(blogPostRepository).delete("1");
	}
}
