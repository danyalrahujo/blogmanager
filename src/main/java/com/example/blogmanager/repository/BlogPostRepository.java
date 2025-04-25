package com.example.blogmanager.repository;

import com.example.blogmanager.model.BlogPost;
import java.util.List;

public interface BlogPostRepository {

	void save(BlogPost blogPost);

	List<BlogPost> findAll();

	BlogPost findById(String id);

	void update(BlogPost blogPost);

	void delete(String id);

	List<BlogPost> findByCategory(String categoryId);
}
