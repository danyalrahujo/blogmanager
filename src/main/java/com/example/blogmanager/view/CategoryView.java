package com.example.blogmanager.view;

import java.util.List;

import com.example.blogmanager.model.Category;

public interface CategoryView {
	void displayCategories(List<Category> categories);

	void addCategory(Category category);

	void deleteCategory(Category category);

	void updateCategory(Category category);

	void showErrorMessage(String message, Category category);
}
