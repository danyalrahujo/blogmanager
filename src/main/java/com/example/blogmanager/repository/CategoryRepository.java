package com.example.blogmanager.repository;

import com.example.blogmanager.model.Category;
import java.util.List;

public interface CategoryRepository {
	void save(Category category);

	List<Category> findAll();

	Category findById(String id);

	void update(Category category);

	void delete(String id);

}
