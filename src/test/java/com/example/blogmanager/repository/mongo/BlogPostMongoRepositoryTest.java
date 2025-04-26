package com.example.blogmanager.repository.mongo;

import static com.example.blogmanager.repository.mongo.BlogPostMongoRepository.BLOG_COLLECTION_NAME;
import static com.example.blogmanager.repository.mongo.BlogPostMongoRepository.BLOG_DB_NAME;
import static org.assertj.core.api.Assertions.assertThat;

import java.net.InetSocketAddress;

import org.bson.Document;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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
	private BlogPostMongoRepository blogPostMongoRepository;
	private MongoCollection<Document> blogPostCollection;

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
		blogPostMongoRepository = new BlogPostMongoRepository(client);
		MongoDatabase database = client.getDatabase(BLOG_DB_NAME);
		database.drop(); // Ensure clean database before tests
		blogPostCollection = database.getCollection(BLOG_COLLECTION_NAME);
	}

	@After
	public void tearDown() {
		client.close();
	}

	@Test
	public void testFindAllWhenDatabaseIsEmpty() {
		assertThat(blogPostMongoRepository.findAll()).isEmpty();
	}

}
