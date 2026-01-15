package com.example.blogmanager.controller;

import com.example.blogmanager.repository.CategoryRepository;
import com.example.blogmanager.view.CategoryView;

import com.example.blogmanager.model.Category;

public class CategoryController {
	private CategoryView categoryView;
	private CategoryRepository categoryRepository;

	public CategoryController(CategoryView categoryView, CategoryRepository categoryRepository) {
		this.categoryView = categoryView;
		this.categoryRepository = categoryRepository;
	}

	public void getAllCategories() {
		categoryView.displayCategories(categoryRepository.findAll());
	}

	public synchronized void addCategory(Category category) {
		Category existingCategory = categoryRepository.findById(category.getId());
		if (existingCategory != null) {
			categoryView.showErrorMessage("Category with ID " + category.getId() + " already exists.", category);
			return;
		}

		categoryRepository.save(category);
		categoryView.addCategory(category);
	}

	public synchronized void deleteCategory(Category category) {
		Category existingCategory = categoryRepository.findById(category.getId());
		if (existingCategory == null) {
			categoryView.showErrorMessage("No category found with ID " + category.getId(), category);
			return;
		}

		categoryRepository.delete(category.getId());
		categoryView.deleteCategory(category);
	}

	public synchronized void updateCategory(Category category) {
		Category existingCategory = categoryRepository.findById(category.getId());
		if (existingCategory == null) {
			categoryView.showErrorMessage("No category found with ID " + category.getId(), category);
			return;
		}

		categoryRepository.update(category);
		categoryView.updateCategory(category);
	}
}
