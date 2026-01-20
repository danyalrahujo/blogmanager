package com.example.blogmanager.controller;

import static org.mockito.Mockito.verify;
import static java.util.Arrays.asList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.blogmanager.model.BlogPost;
import com.example.blogmanager.model.Category;
import com.example.blogmanager.repository.BlogPostRepository;
import com.example.blogmanager.repository.CategoryRepository;
import com.example.blogmanager.repository.mongo.BlogPostMongoRepository;
import com.example.blogmanager.repository.mongo.CategoryMongoRepository;
import com.example.blogmanager.view.BlogPostView;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

public class BlogPostControllerIT {

	@Mock
	private BlogPostView blogPostView;

	private BlogPostRepository blogPostRepository;
	private CategoryRepository categoryRepository;
	private BlogPostController blogPostController;

	private AutoCloseable closeable;

	private static int mongoPort = Integer.parseInt(System.getProperty("mongo.port", "27017"));

	@Before
	public void setUp() {
		closeable = MockitoAnnotations.openMocks(this);

		MongoClient client = new MongoClient(new ServerAddress("localhost", mongoPort));
		blogPostRepository = new BlogPostMongoRepository(client, "blogmanager", "blogpost");
		categoryRepository = new CategoryMongoRepository(client, "blogmanager", "category");

		for (BlogPost blogPost : blogPostRepository.findAll()) {
			blogPostRepository.delete(blogPost.getId());
		}
		for (Category category : categoryRepository.findAll()) {
			categoryRepository.delete(category.getId());
		}

		categoryRepository.save(new Category("cat1", "Tech"));
		blogPostController = new BlogPostController(blogPostView, blogPostRepository,categoryRepository);
	}

	@After
	public void releaseMocks() throws Exception {
		closeable.close();
	}

	@Test
	public void testAllBlogPosts() {
		Category category = categoryRepository.findById("cat1");
		BlogPost blogPost = new BlogPost("1", "Hello", "First post", "Author", "2025-04-01", category);
		blogPostRepository.save(blogPost);
		blogPostController.getAllBlogPosts();
		verify(blogPostView).displayBlogPosts(asList(blogPost));
	}

	@Test
	public void testNewBlogPost() {
		Category category = categoryRepository.findById("cat1");
		BlogPost blogPost = new BlogPost("1", "Hello", "First post", "Author", "2025-04-01", category);
		blogPostController.addBlogPost(blogPost);
		verify(blogPostView).addBlogPost(blogPost);
	}

	@Test
	public void testDeleteBlogPost() {
		Category category = categoryRepository.findById("cat1");
		BlogPost blogPostToDelete = new BlogPost("1", "Hello", "First post", "Author", "2025-04-01", category);
		blogPostRepository.save(blogPostToDelete);
		blogPostController.deleteBlogPost(blogPostToDelete);
		verify(blogPostView).deleteBlogPost(blogPostToDelete);
	}

	@Test
	public void testUpdateBlogPost() {
		Category category = categoryRepository.findById("cat1");
		BlogPost original = new BlogPost("1", "Hello", "First post", "Author", "2025-04-01", category);
		blogPostRepository.save(original);

		BlogPost updated = new BlogPost("1", "Hello", "Updated post", "Author", "2025-05-01", category);
		blogPostController.updateBlogPost(updated);
		verify(blogPostView).updateBlogPost(updated);
	}

}
