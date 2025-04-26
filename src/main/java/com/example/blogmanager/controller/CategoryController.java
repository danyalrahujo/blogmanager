package com.example.blogmanager.controller;

import com.example.blogmanager.repository.CategoryRepository;
import java.util.List;
import com.example.blogmanager.model.Category;

public class CategoryController {
	private CategoryRepository categoryRepository;

	public CategoryController(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	public void createCategory(Category category) {
		categoryRepository.save(category);
	}

	public List<Category> getAllCategories() {
		return categoryRepository.findAll();
	}

	public Category getCategoryById(String id) {
		return categoryRepository.findById(id);
	}

	public void updateCategory(Category category) {
		categoryRepository.update(category);
	}

	public void deleteCategory(String id) {
		categoryRepository.delete(id);
	}
}
