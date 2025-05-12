package com.example.blogmanager.repository.mongo;

import static com.example.blogmanager.repository.mongo.CategoryMongoRepository.CATEGORY_COLLECTION_NAME;
import static com.example.blogmanager.repository.mongo.CategoryMongoRepository.CATEGORY_DB_NAME;
import static org.assertj.core.api.Assertions.assertThat;

import java.net.InetSocketAddress;

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
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;;

public class CategoryRepositoryTest {

	private static MongoServer server;
	private static InetSocketAddress serverAddress;

	private MongoClient client;
	private CategoryMongoRepository categoryMongoRepository;
	private MongoCollection<Document> categoryCollection;

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
		categoryMongoRepository = new CategoryMongoRepository(client);
		MongoDatabase database = client.getDatabase(CATEGORY_DB_NAME);
		database.drop(); // Ensure clean database before tests
		categoryCollection = database.getCollection(CATEGORY_COLLECTION_NAME);
	}

	@After
	public void tearDown() {
		client.close();
	}

	@Test
	public void testFindAllWhenDatabaseIsEmpty() {
		assertThat(categoryMongoRepository.findAll()).isEmpty();
	}

	@Test
	public void testFindAllWhenDatabaseIsNotEmpty() {
		addTestCategoryToDatabase("1", "Tech");
		addTestCategoryToDatabase("2", "News");

		assertThat(categoryMongoRepository.findAll()).containsExactly(new Category("1", "Tech"),
				new Category("2", "News"));
	}

	private void addTestCategoryToDatabase(String id, String name) {
		categoryCollection.insertOne(new Document().append("id", id) // ← Mongo’s real primary‐key field
				.append("name", name));
	}

}
