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
	final String TITLE = "title";
	final String AUTHOR = "author";
	final String ID = "id";
	final String CREATIONDATE = "creationDate";
	final String CONTENT = " content";
	final String CATEGORY = "category";

	private final MongoCollection<Document> blogPostCollection;

	public BlogPostMongoRepository(MongoClient client, String databaseName, String collectionName) {
		blogPostCollection = client.getDatabase(databaseName).getCollection(collectionName);
	}

	@Override
	public void save(BlogPost bp) {
		Document doc = new Document().append(ID, bp.getId()).append(TITLE, bp.getTitle())
				.append(CONTENT, bp.getContent()).append(AUTHOR, bp.getAuthor())
				.append(CREATIONDATE, bp.getCreationDate());

		if (bp.getCategory() != null) {
			Document catDoc = new Document().append("id", bp.getCategory().getId()).append("name",
					bp.getCategory().getName());
			doc.append(CATEGORY, catDoc);
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
		Document update = new Document().append(TITLE, bp.getTitle()).append(CONTENT, bp.getContent())
				.append(AUTHOR, bp.getAuthor()).append(CREATIONDATE, bp.getCreationDate());

		if (bp.getCategory() != null) {
			Document catDoc = new Document().append("id", bp.getCategory().getId()).append("name",
					bp.getCategory().getName());
			update.append(CATEGORY, catDoc);
		} else {

			update.append(CATEGORY, null);
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
		String id = d.getString(ID);
		String title = d.getString(TITLE);
		String content = d.getString(CONTENT);
		String author = d.getString(AUTHOR);
		String creationDate = d.getString(CREATIONDATE);

		Document catDoc = d.get(CATEGORY, Document.class);
		Category category = (catDoc == null) ? null : new Category(catDoc.getString("id"), catDoc.getString("name"));

		return new BlogPost(id, title, content, author, creationDate, category);
	}
}
