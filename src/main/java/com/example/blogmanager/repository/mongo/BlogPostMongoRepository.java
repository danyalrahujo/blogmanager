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
import com.mongodb.client.model.Filters;

public class BlogPostMongoRepository implements BlogPostRepository {

	public static final String BLOG_COLLECTION_NAME = "blogPost";
	public static final String BLOG_DB_NAME = "blogDatabase";

	private final MongoCollection<Document> blogPostCollection;

	public BlogPostMongoRepository(MongoClient client) {
		blogPostCollection = client.getDatabase(BLOG_DB_NAME).getCollection(BLOG_COLLECTION_NAME);
	}

	@Override
	public void save(BlogPost bp) {
		Document doc = new Document().append("id", bp.getId()).append("title", bp.getTitle())
				.append("content", bp.getContent()).append("author", bp.getAuthor())
				.append("creationDate", bp.getCreationDate());

		if (bp.getCategory() != null) {
			Document catDoc = new Document().append("id", bp.getCategory().getId()).append("name",
					bp.getCategory().getName());
			doc.append("category", catDoc);
		}

		blogPostCollection.insertOne(doc);
	}

	@Override
	public List<BlogPost> findAll() {
		return StreamSupport.stream(blogPostCollection.find().spliterator(), false).map(this::documentToBlogPost)
				.collect(Collectors.toList());
	}

	@Override
	public BlogPost findById(String id) {
		Document d = blogPostCollection.find(Filters.eq("id", id)).first();
		return d == null ? null : documentToBlogPost(d);
	}

	@Override
	public void update(BlogPost bp) {
		Document update = new Document().append("title", bp.getTitle()).append("content", bp.getContent())
				.append("author", bp.getAuthor()).append("creationDate", bp.getCreationDate());

		if (bp.getCategory() != null) {
			Document catDoc = new Document().append("id", bp.getCategory().getId()).append("name",
					bp.getCategory().getName());
			update.append("category", catDoc);
		} else {

			update.append("category", null);
		}

		blogPostCollection.updateOne(Filters.eq("id", bp.getId()), new Document("$set", update));
	}

	@Override
	public void delete(String id) {
		blogPostCollection.deleteOne(Filters.eq("id", id));
	}

	@Override
	public List<BlogPost> findByCategory(String categoryId) {
		return StreamSupport.stream(blogPostCollection.find(Filters.eq("category.id", categoryId)).spliterator(), false)
				.map(this::documentToBlogPost).collect(Collectors.toList());
	}

	private BlogPost documentToBlogPost(Document d) {
		String id = d.getString("id");
		String title = d.getString("title");
		String content = d.getString("content");
		String author = d.getString("author");
		String creationDate = d.getString("creationDate");

		Document catDoc = d.get("category", Document.class);
		Category category = (catDoc == null) ? null : new Category(catDoc.getString("id"), catDoc.getString("name"));

		return new BlogPost(id, title, content, author, creationDate, category);
	}
}
