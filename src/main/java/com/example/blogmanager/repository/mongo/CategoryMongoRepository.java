package com.example.blogmanager.repository.mongo;

import java.util.List;

import org.bson.Document;

import com.example.blogmanager.model.Category;
import com.example.blogmanager.repository.CategoryRepository;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;

public class CategoryMongoRepository implements CategoryRepository {

	public static final String CATEGORY_COLLECTION_NAME = "category";
	public static final String CATEGORY_DB_NAME = "blogDatabase";

	private MongoCollection<Document> categoryCollection;

	public CategoryMongoRepository(MongoClient client) {
		categoryCollection = client.getDatabase(CATEGORY_DB_NAME).getCollection(CATEGORY_COLLECTION_NAME);
	}

	@Override
	public void save(Category category) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Category> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Category findById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Category category) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(String id) {
		// TODO Auto-generated method stub

	}

}
