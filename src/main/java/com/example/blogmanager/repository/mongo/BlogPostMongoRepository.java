package com.example.blogmanager.repository.mongo;

import java.util.Collections;
import java.util.List;

import org.bson.Document;

import com.example.blogmanager.model.BlogPost;
import com.example.blogmanager.repository.BlogPostRepository;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;

public class BlogPostMongoRepository implements BlogPostRepository {

	public static final String BLOG_COLLECTION_NAME = "blogPost";
	public static final String BLOG_DB_NAME = "blogDatabase";

	private MongoCollection<Document> blogPostCollection;

	public BlogPostMongoRepository(MongoClient client) {
		blogPostCollection = client.getDatabase(BLOG_DB_NAME).getCollection(BLOG_COLLECTION_NAME);
	}

	@Override
	public void save(BlogPost blogPost) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<BlogPost> findAll() {
		// TODO Auto-generated method stub
		return Collections.emptyList();
	}

	@Override
	public BlogPost findById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(BlogPost blogPost) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<BlogPost> findByCategory(String categoryId) {
		// TODO Auto-generated method stub
		return null;
	}

}
