package com.example.blogmanager.repository.mongo;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.Document;

import com.example.blogmanager.model.BlogPost;
import com.example.blogmanager.model.Category;
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
		return StreamSupport.stream(blogPostCollection.find().spliterator(), false).map(d -> {
			// top-level fields
			String id = d.getString("id");
			String title = d.getString("title");
			String content = d.getString("content");
			String author = d.getString("author");
			String creationDate = d.getString("creationDate");

			// embedded Category sub-document
			Document catDoc = d.get("category", Document.class);
			Category category = catDoc == null ? null : new Category(catDoc.getString("id"), catDoc.getString("name"));

			// build your BlogPost
			return new BlogPost(id, title, content, author, creationDate, category);
		}).collect(Collectors.toList());

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
