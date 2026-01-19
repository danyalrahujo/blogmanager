package com.example.blogmanager.repository.mongo;

import static com.example.blogmanager.repository.mongo.BlogPostMongoRepository.BLOG_COLLECTION_NAME;
import static com.example.blogmanager.repository.mongo.BlogPostMongoRepository.BLOG_DB_NAME;
import static org.assertj.core.api.Assertions.assertThat;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.example.blogmanager.model.BlogPost;
import com.example.blogmanager.model.Category;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;

public class BlogPostMongoRepositoryTest {

	private static MongoServer server;
	private static InetSocketAddress serverAddress;

	private MongoClient client;
	private BlogPostMongoRepository repo;
	private MongoCollection<Document> raw;

	@BeforeClass
	public static void setupServer() {
		server = new MongoServer(new MemoryBackend());
		serverAddress = server.bind();
	}

	@AfterClass
	public static void shutdownServer() {
		server.shutdown();
	}

	@Before
	public void setup() {
		client = new MongoClient(new ServerAddress(serverAddress));
		repo = new BlogPostMongoRepository(client,BLOG_DB_NAME, BLOG_COLLECTION_NAME);
		MongoDatabase db = client.getDatabase(BLOG_DB_NAME);
		db.drop();
		raw = db.getCollection(BLOG_COLLECTION_NAME);
	}

	@After
	public void tearDown() {
		client.close();
	}

	@Test
	public void testFindAllWhenDatabaseIsEmpty() {
		assertThat(repo.findAll()).isEmpty();
	}

	@Test
	public void testFindAllWhenDatabaseIsNotEmpty() {
		addRaw("1", "Hello", "First post", "cat1");
		addRaw("2", "Goodbye", "Second post", "cat2");

		assertThat(repo.findAll()).containsExactly(new BlogPost("1", "Hello", "First post", "cat1", null, null),
				new BlogPost("2", "Goodbye", "Second post", "cat2", null, null));
	}

	@Test
	public void testFindByIdWhenNotFound() {
		assertThat(repo.findById("nope")).isNull();
	}

	@Test
	public void testSaveWithCategory() {
		Category category = new Category("c1", "Tech");
		BlogPost post = new BlogPost("10", "With category", "Content", "Author", "01-02-2025", category);

		repo.save(post);

		BlogPost stored = repo.findById("10");

		assertThat(stored.getCategory()).isNotNull();
		assertThat(stored.getCategory().getId()).isEqualTo("c1");
		assertThat(stored.getCategory().getName()).isEqualTo("Tech");
	}

	@Test
	public void testFindByIdWhenFound() {
		addRaw("42", "The Answer", "All about 42", "douglas");
		addRaw("99", "Ninety-Nine", "Just test", "tester");

		BlogPost b = repo.findById("99");
		assertThat(b).isEqualTo(new BlogPost("99", "Ninety-Nine", "Just test", "tester", null, null));
	}

	@Test
	public void testUpdateWithCategory() {
		repo.save(new BlogPost("20", "Old", "Old", "A", null, null));

		Category category = new Category("c2", "Life");
		BlogPost updated = new BlogPost("20", "New", "New", "A", null, category);

		repo.update(updated);

		BlogPost stored = repo.findById("20");

		assertThat(stored.getCategory()).isNotNull();
		assertThat(stored.getCategory().getName()).isEqualTo("Life");
	}

	@Test
	public void testFindByCategoryWhenMatchesExist() {
		Category tech = new Category("c1", "Tech");
		Category life = new Category("c2", "Life");

		BlogPost p1 = new BlogPost("1", "Post1", "Content1", "A", null, tech);
		BlogPost p2 = new BlogPost("2", "Post2", "Content2", "B", null, tech);
		BlogPost p3 = new BlogPost("3", "Post3", "Content3", "C", null, life);

		repo.save(p1);
		repo.save(p2);
		repo.save(p3);

		List<BlogPost> result = repo.findByCategory("c1");

		assertThat(result).containsExactlyInAnyOrder(p1, p2);
	}

	@Test
	public void testFindByCategoryWhenNoMatches() {
		Category tech = new Category("c1", "Tech");
		BlogPost p1 = new BlogPost("1", "Post1", "Content1", "A", null, tech);

		repo.save(p1);

		List<BlogPost> result = repo.findByCategory("does-not-exist");

		assertThat(result).isEmpty();
	}

	@Test
	public void testSave() {
		BlogPost p = new BlogPost("7", "Seventh", "Lucky seven", "seven", null, null);
		repo.save(p);

		assertThat(readAll()).containsExactly(p);
	}

	@Test
	public void testDelete() {
		addRaw("x", "ToBeDeleted", "...", "me");
		repo.delete("x");
		assertThat(readAll()).isEmpty();
	}

	@Test
	public void testUpdate() {
		BlogPost orig = new BlogPost("u", "Orig", "Content", "author", null, null);
		repo.save(orig);
		assertThat(readAll()).containsExactly(orig);

		BlogPost upd = new BlogPost("u", "Updated", "New content", "author", null, null);
		repo.update(upd);
		assertThat(readAll()).containsExactly(upd);
	}

	private void addRaw(String id, String title, String content, String author) {
		raw.insertOne(new Document().append("id", id).append("title", title).append("content", content).append("author",
				author));
	}

	private List<BlogPost> readAll() {
		return StreamSupport.stream(raw.find().spliterator(), false).map(d -> {

			String id = d.getString("id");
			String title = d.getString("title");
			String content = d.getString("content");
			String author = d.getString("author");
			String creationDate = d.getString("creationDate");

			Document catDoc = d.get("category", Document.class);
			Category category = catDoc == null ? null : new Category(catDoc.getString("id"), catDoc.getString("name"));
			return new BlogPost(id, title, content, author, creationDate, category);
		}).collect(Collectors.toList());
	}

}
