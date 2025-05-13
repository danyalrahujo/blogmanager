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
import org.testcontainers.containers.GenericContainer;

import com.example.blogmanager.model.Category;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.junit.AfterClass;
import org.junit.BeforeClass;

public class CategoryMongoRepositoryTestcontainersIT {

	@SuppressWarnings("rawtypes")
	@ClassRule
	public static final GenericContainer mongo = new GenericContainer("mongo:4.4.3").withExposedPorts(27017);

	private MongoClient client;
	private CategoryMongoRepository categoryRepository;
	private MongoCollection<Document> categoryCollection;

	@BeforeClass
	public static void setupServer() {
		// Start the MongoDB container
		mongo.start();
	}

	@AfterClass
	public static void shutdownServer() {
		// Stop the MongoDB container
		mongo.stop();
	}

	@Before
	public void setup() {
		client = new MongoClient(new ServerAddress(mongo.getHost(), mongo.getFirstMappedPort()));
		categoryRepository = new CategoryMongoRepository(client);
		MongoDatabase database = client.getDatabase(CategoryMongoRepository.CATEGORY_DB_NAME);

		database.drop();
		categoryCollection = database.getCollection(CategoryMongoRepository.CATEGORY_COLLECTION_NAME);
	}

	@After
	public void tearDown() {
		client.close();
	}

	@Test
	public void testFindAllWhenDatabaseIsEmpty() {
		assertThat(categoryRepository.findAll()).isEmpty();
	}

	@Test
	public void testFindAllWhenDatabaseIsNotEmpty() {
		addTestCategoryToDatabase("1", "Tech");
		addTestCategoryToDatabase("2", "News");

		assertThat(categoryRepository.findAll()).containsExactly(new Category("1", "Tech"), new Category("2", "News"));
	}

	@Test
	public void testFindByIdNotFound() {
		assertThat(categoryRepository.findById("42")).isNull();
	}

	@Test
	public void testFindByIdFound() {
		addTestCategoryToDatabase("1", "Tech");
		addTestCategoryToDatabase("2", "News");

		Category c = categoryRepository.findById("2");
		assertThat(c).isEqualTo(new Category("2", "News"));
	}

	@Test
	public void testSave() {
		Category c = new Category("42", "Life");
		categoryRepository.save(c);
		assertThat(readAllCategories()).containsExactly(c);
	}

	@Test
	public void testDelete() {
		addTestCategoryToDatabase("x", "Old");
		categoryRepository.delete("x");
		assertThat(readAllCategories()).isEmpty();
	}

	@Test
	public void testUpdate() {
		Category orig = new Category("a", "Alpha");
		categoryRepository.save(orig);
		assertThat(readAllCategories()).containsExactly(orig);

		Category upd = new Category("a", "Beta");
		categoryRepository.update(upd);
		assertThat(readAllCategories()).containsExactly(upd);
	}

	private void addTestCategoryToDatabase(String id, String name) {
		categoryCollection.insertOne(new Document().append("id", id).append("name", name));
	}

	private List<Category> readAllCategories() {
		return StreamSupport.stream(categoryCollection.find().spliterator(), false)
				.map(d -> new Category(d.getString("id"), d.getString("name"))).collect(Collectors.toList());
	}

}
