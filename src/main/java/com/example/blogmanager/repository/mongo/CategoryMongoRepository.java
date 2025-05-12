package com.example.blogmanager.repository.mongo;

import static com.mongodb.client.model.Filters.eq;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;

import com.example.blogmanager.model.Category;
import com.example.blogmanager.repository.CategoryRepository;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;

public class CategoryMongoRepository implements CategoryRepository {

	public static final String CATEGORY_COLLECTION_NAME = "category";
	public static final String CATEGORY_DB_NAME = "blogDatabase";

	private final MongoCollection<Document> categoryCollection;

	public CategoryMongoRepository(MongoClient client) {
		categoryCollection = client.getDatabase(CATEGORY_DB_NAME).getCollection(CATEGORY_COLLECTION_NAME);
	}

	@Override
	public void save(Category category) {
		Document doc = new Document().append("id", category.getId()).append("name", category.getName());
		categoryCollection.insertOne(doc);
	}

	@Override
	public List<Category> findAll() {
		return StreamSupport.stream(categoryCollection.find().spliterator(), false).map(this::documentToCategory)
				.collect(Collectors.toList());
	}

	@Override
	public Category findById(String id) {
		Document d = categoryCollection.find(eq("id", id)).first();
		return d == null ? null : documentToCategory(d);
	}

	@Override
	public void update(Category category) {
		Document update = new Document("name", category.getName());
		categoryCollection.updateOne(eq("id", category.getId()), new Document("$set", update));
	}

	@Override
	public void delete(String id) {
		categoryCollection.deleteOne(eq("id", id));
	}

	private Category documentToCategory(Document d) {
		String id = d.getString("id");
		String name = d.getString("name");
		return new Category(id, name);
	}
}
