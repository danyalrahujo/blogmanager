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
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class BlogPostMongoRepositoryTestcontainersIT {
	@ClassRule
	public static final MongoDBContainer mongo = new MongoDBContainer("mongo:4.4.3");

	private MongoClient client;
	private BlogPostMongoRepository blogPostRepository;
	private MongoCollection<Document> blogPostCollection;

	@Before
	public void setup() {
		client = new MongoClient(new ServerAddress(mongo.getHost(), mongo.getFirstMappedPort()));
		blogPostRepository = new BlogPostMongoRepository(client);
		MongoDatabase database = client.getDatabase(BlogPostMongoRepository.BLOG_DB_NAME);

		database.drop();
		blogPostCollection = database.getCollection(BlogPostMongoRepository.BLOG_COLLECTION_NAME);
	}

	@After
	public void tearDown() {
		client.close();
	}

	@Test
	public void testFindAll() {
		addTestBlogPostToDatabase("1", "Hello", "First post", "cat1");
		addTestBlogPostToDatabase("2", "Goodbye", "Second post", "cat2");

		assertThat(blogPostRepository.findAll()).containsExactly(
				new BlogPost("1", "Hello", "First post", "cat1", null, null),
				new BlogPost("2", "Goodbye", "Second post", "cat2", null, null));
	}

	@Test
	public void testFindById() {
		addTestBlogPostToDatabase("1", "Hello", "First post", "cat1");
		addTestBlogPostToDatabase("2", "Goodbye", "Second post", "cat2");

		assertThat(blogPostRepository.findById("2"))
				.isEqualTo(new BlogPost("2", "Goodbye", "Second post", "cat2", null, null));
	}

	@Test
	public void testSave() {
		BlogPost blogPost = new BlogPost("1", "Hello", "First post", "cat1", null, null);
		blogPostRepository.save(blogPost);

		assertThat(readAllBlogPostsFromDatabase()).containsExactly(blogPost);
	}

	@Test
	public void testDelete() {
		addTestBlogPostToDatabase("1", "Hello", "First post", "cat1");
		blogPostRepository.delete("1");

		assertThat(readAllBlogPostsFromDatabase()).isEmpty();
	}

	@Test
	public void testUpdate() {
		BlogPost originalBlogPost = new BlogPost("1", "Hello", "First post", "cat1", null, null);
		blogPostRepository.save(originalBlogPost);

		assertThat(readAllBlogPostsFromDatabase()).containsExactly(originalBlogPost);

		BlogPost updatedBlogPost = new BlogPost("1", "Updated", "Updated post", "cat1", null, null);
		blogPostRepository.update(updatedBlogPost);

		assertThat(readAllBlogPostsFromDatabase()).containsExactly(updatedBlogPost);
	}

	// Helpers

	private void addTestBlogPostToDatabase(String id, String title, String content, String author) {
		blogPostCollection.insertOne(new Document().append("id", id).append("title", title).append("content", content)
				.append("author", author)
		// omit creationDate & category so they remain null in the mapped object
		);
	}

	private List<BlogPost> readAllBlogPostsFromDatabase() {
		return StreamSupport.stream(blogPostCollection.find().spliterator(), false)
				.map(d -> new BlogPost("" + d.get("id"), "" + d.get("title"), "" + d.get("content"),
						"" + d.get("author"), (String) d.get("creationDate"), // cast to string
						null // category is omitted in this test, could be populated
				)).collect(Collectors.toList());
	}
}
