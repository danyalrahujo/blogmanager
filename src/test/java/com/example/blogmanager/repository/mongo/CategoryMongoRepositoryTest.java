package com.example.blogmanager.repository.mongo;

import static com.example.blogmanager.repository.mongo.CategoryMongoRepository.CATEGORY_COLLECTION_NAME;
import static com.example.blogmanager.repository.mongo.CategoryMongoRepository.CATEGORY_DB_NAME;
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

import com.example.blogmanager.model.Category;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;

public class CategoryMongoRepositoryTest {

	private static MongoServer server;
	private static InetSocketAddress serverAddress;

	private MongoClient client;
	private CategoryMongoRepository repo;
	private MongoCollection<Document> collection;

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
		repo = new CategoryMongoRepository(client);
		MongoDatabase db = client.getDatabase(CATEGORY_DB_NAME);
		db.drop();
		collection = db.getCollection(CATEGORY_COLLECTION_NAME);
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
		addTestCategory("1", "Tech");
		addTestCategory("2", "News");

		List<Category> all = repo.findAll();
		assertThat(all).containsExactly(new Category("1", "Tech"), new Category("2", "News"));
	}

	@Test
	public void testFindByIdNotFound() {
		assertThat(repo.findById("nope")).isNull();
	}

	@Test
	public void testFindByIdFound() {
		addTestCategory("1", "Tech");
		addTestCategory("2", "News");

		Category c = repo.findById("2");
		assertThat(c).isEqualTo(new Category("2", "News"));
	}

	@Test
	public void testSave() {
		Category c = new Category("42", "Life");
		repo.save(c);
		assertThat(readAllCategories()).containsExactly(c);
	}

	@Test
	public void testDelete() {
		addTestCategory("x", "Old");
		repo.delete("x");
		assertThat(readAllCategories()).isEmpty();
	}

	@Test
	public void testUpdate() {
		Category orig = new Category("a", "Alpha");
		repo.save(orig);
		assertThat(readAllCategories()).containsExactly(orig);

		Category upd = new Category("a", "Beta");
		repo.update(upd);
		assertThat(readAllCategories()).containsExactly(upd);
	}

	private void addTestCategory(String id, String name) {
		collection.insertOne(new Document().append("id", id).append("name", name));
	}

	private List<Category> readAllCategories() {
		return StreamSupport.stream(collection.find().spliterator(), false)
				.map(d -> new Category(d.getString("id"), d.getString("name"))).collect(Collectors.toList());
	}
}
