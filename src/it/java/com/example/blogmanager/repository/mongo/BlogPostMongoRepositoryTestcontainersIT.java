package com.example.blogmanager.repository.mongo;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.MongoDBContainer;

import com.example.blogmanager.model.BlogPost;
import com.example.blogmanager.model.Category;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class BlogPostMongoRepositoryTestcontainersIT {

	@ClassRule
	public static final MongoDBContainer mongo = new MongoDBContainer("mongo:4.4.3");

	private MongoClient client;
	private BlogPostMongoRepository blogPostRepository;
	private CategoryMongoRepository categoryRepository;
	private MongoCollection<Document> blogPostCollection;

	@Before
	public void setup() {
		client = new MongoClient(new ServerAddress(mongo.getHost(), mongo.getFirstMappedPort()));
		blogPostRepository = new BlogPostMongoRepository(client);
		categoryRepository = new CategoryMongoRepository(client);

		MongoDatabase database = client.getDatabase(BlogPostMongoRepository.BLOG_DB_NAME);
		database.drop();

		blogPostCollection = database.getCollection(BlogPostMongoRepository.BLOG_COLLECTION_NAME);
		database.getCollection(CategoryMongoRepository.CATEGORY_COLLECTION_NAME);

		categoryRepository.save(new Category("cat1", "Tech"));
		categoryRepository.save(new Category("cat2", "News"));
	}

	@After
	public void tearDown() {
		client.close();
	}

	@Test
	public void testFindAll() {
		Category cat1 = categoryRepository.findById("cat1");
		Category cat2 = categoryRepository.findById("cat2");

		addTestBlogPostToDatabase("1", "Hello", "First post", "AuthorA", cat1);
		addTestBlogPostToDatabase("2", "Goodbye", "Second post", "AuthorB", cat2);

		assertThat(blogPostRepository.findAll()).containsExactly(
				new BlogPost("1", "Hello", "First post", "AuthorA", null, cat1),
				new BlogPost("2", "Goodbye", "Second post", "AuthorB", null, cat2));
	}

	@Test
	public void testFindById() {
		Category cat2 = categoryRepository.findById("cat2");
		addTestBlogPostToDatabase("2", "Goodbye", "Second post", "AuthorB", cat2);

		assertThat(blogPostRepository.findById("2"))
				.isEqualTo(new BlogPost("2", "Goodbye", "Second post", "AuthorB", null, cat2));
	}

	@Test
	public void testSave() {
		Category cat1 = categoryRepository.findById("cat1");
		BlogPost blogPost = new BlogPost("1", "Hello", "First post", "AuthorA", null, cat1);
		blogPostRepository.save(blogPost);

		assertThat(readAllBlogPostsFromDatabase()).containsExactly(blogPost);
	}

	@Test
	public void testDelete() {
		Category cat1 = categoryRepository.findById("cat1");
		addTestBlogPostToDatabase("1", "Hello", "First post", "AuthorA", cat1);
		blogPostRepository.delete("1");
		assertThat(readAllBlogPostsFromDatabase()).isEmpty();
	}

	@Test
	public void testUpdate() {
		Category cat1 = categoryRepository.findById("cat1");
		BlogPost original = new BlogPost("1", "Hello", "First post", "AuthorA", null, cat1);
		blogPostRepository.save(original);

		BlogPost updated = new BlogPost("1", "Updated", "Updated post", "AuthorA", null, cat1);
		blogPostRepository.update(updated);

		assertThat(readAllBlogPostsFromDatabase()).containsExactly(updated);
	}

	private void addTestBlogPostToDatabase(String id, String title, String content, String author, Category category) {
		Document catDoc = new Document().append("id", category.getId()).append("name", category.getName());
		blogPostCollection.insertOne(new Document().append("id", id).append("title", title).append("content", content)
				.append("author", author).append("category", catDoc));
	}

	private List<BlogPost> readAllBlogPostsFromDatabase() {
		return StreamSupport.stream(blogPostCollection.find().spliterator(), false).map(d -> {
			Document catDoc = d.get("category", Document.class);
			Category category = catDoc != null ? new Category(catDoc.getString("id"), catDoc.getString("name")) : null;
			return new BlogPost(d.getString("id"), d.getString("title"), d.getString("content"), d.getString("author"),
					d.getString("creationDate"), category);
		}).collect(Collectors.toList());
	}
}
