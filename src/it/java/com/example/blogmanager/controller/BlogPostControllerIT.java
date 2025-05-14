package com.example.blogmanager.controller;

import static org.mockito.Mockito.verify;
import static java.util.Arrays.asList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.blogmanager.model.BlogPost;
import com.example.blogmanager.repository.BlogPostRepository;
import com.example.blogmanager.repository.mongo.BlogPostMongoRepository;
import com.example.blogmanager.view.BlogPostView;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

public class BlogPostControllerIT {

	@Mock
	private BlogPostView blogPostView;

	private BlogPostRepository blogPostRepository;

	private BlogPostController blogPostController;

	private AutoCloseable closeable;

	private static int mongoPort = Integer.parseInt(System.getProperty("mongo.port", "27017"));

	@Before
	public void setUp() {
		closeable = MockitoAnnotations.openMocks(this);
		blogPostRepository = new BlogPostMongoRepository(new MongoClient(new ServerAddress("localhost", mongoPort)));
		// explicit empty the database through the repository
		for (BlogPost blogPost : blogPostRepository.findAll()) {
			blogPostRepository.delete(blogPost.getId());
		}
		blogPostController = new BlogPostController(blogPostView, blogPostRepository);
	}

	@After
	public void releaseMocks() throws Exception {
		closeable.close();
	}

	@Test
	public void testAllBlogPosts() {
		BlogPost blogPost = new BlogPost("1", "Hello", "First post", "cat1", "2025-04-01", null);
		blogPostRepository.save(blogPost);
		blogPostController.getAllBlogPosts();
		verify(blogPostView).displayBlogPosts(asList(blogPost));
	}

	@Test
	public void testNewBlogPost() {
		BlogPost blogPost = new BlogPost("1", "Hello", "First post", "cat1", "2025-04-01", null);
		blogPostController.addBlogPost(blogPost);
		verify(blogPostView).addBlogPost(blogPost);
	}

	@Test
	public void testDeleteBlogPost() {
		BlogPost blogPostToDelete = new BlogPost("1", "Hello", "First post", "cat1", "2025-04-01", null);
		blogPostRepository.save(blogPostToDelete);
		blogPostController.deleteBlogPost(blogPostToDelete);
		verify(blogPostView).deleteBlogPost(blogPostToDelete);
	}

	@Test
	public void testUpdateBlogPost() {
		BlogPost originalBlogPost = new BlogPost("1", "Hello", "First post", "cat1", "2025-04-01", null);
		blogPostRepository.save(originalBlogPost);

		BlogPost updatedBlogPost = new BlogPost("1", "Hello", "Updated post", "cat1", "2025-05-01", null);
		blogPostController.updateBlogPost(updatedBlogPost);

		verify(blogPostView).updateBlogPost(updatedBlogPost);
	}

}
