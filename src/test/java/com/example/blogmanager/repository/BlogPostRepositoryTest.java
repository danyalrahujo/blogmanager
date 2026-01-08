package com.example.blogmanager.repository;

import com.example.blogmanager.model.BlogPost;
import com.example.blogmanager.model.Category;

import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BlogPostRepositoryTest {

	private BlogPostRepository blogPostRepository;

	@Before
	public void setUp() {

		blogPostRepository = mock(BlogPostRepository.class);
	}

	@Test
	public void testSave() {
		Category category = new Category("1", "Technology");
		BlogPost blogPost = new BlogPost("1", "Tech Post", "Content", "Author", "2025-04-01", category);
		blogPostRepository.save(blogPost);
		verify(blogPostRepository).save(blogPost);
	}

	@Test
	public void testFindAll() {
		Category category = new Category("1", "Technology");
		List<BlogPost> blogPosts = new ArrayList<>();
		blogPosts.add(new BlogPost("1", "Tech Post", "Content", "Author", "2025-04-01", category));
		when(blogPostRepository.findAll()).thenReturn(blogPosts);

		List<BlogPost> result = blogPostRepository.findAll();
		assertEquals(1, result.size());
		assertEquals("Tech Post", result.get(0).getTitle());
	}

	@Test
	public void testFindById() {
		Category category = new Category("1", "Technology");
		BlogPost blogPost = new BlogPost("1", "Tech Post", "Content", "Author", "2025-04-01", category);
		when(blogPostRepository.findById("1")).thenReturn(blogPost);

		BlogPost result = blogPostRepository.findById("1");
		assertNotNull(result);
		assertEquals("Tech Post", result.getTitle());
	}

	@Test
	public void testUpdate() {
		Category category = new Category("1", "Technology");
		BlogPost blogPost = new BlogPost("1", "Tech Post", "Content", "Author", "2025-04-01", category);
		blogPostRepository.update(blogPost);
		verify(blogPostRepository).update(blogPost);
	}

	@Test
	public void testDelete() {
		blogPostRepository.delete("1");
		verify(blogPostRepository).delete("1");
	}

	@Test
	public void testFindByCategory() {
		Category category = new Category("1", "Technology");
		BlogPost blogPost = new BlogPost("1", "Tech Post", "Content", "Author", "2025-04-01", category);
		List<BlogPost> blogPosts = new ArrayList<>();
		blogPosts.add(blogPost);

		when(blogPostRepository.findByCategory("1")).thenReturn(blogPosts);

		List<BlogPost> result = blogPostRepository.findByCategory("1");
		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals("Tech Post", result.get(0).getTitle());
	}
}
