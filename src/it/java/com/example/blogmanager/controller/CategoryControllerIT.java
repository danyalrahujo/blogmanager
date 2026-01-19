package com.example.blogmanager.controller;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.blogmanager.model.Category;
import com.example.blogmanager.repository.CategoryRepository;
import com.example.blogmanager.repository.mongo.CategoryMongoRepository;
import com.example.blogmanager.view.CategoryView;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

//docker run -p 27017:27017 --rm mongo:4.4.3

public class CategoryControllerIT {

	@Mock
	private CategoryView categoryView;

	private CategoryRepository categoryRepository;

	private CategoryController categoryController;

	private AutoCloseable closeable;

	private static int mongoPort = Integer.parseInt(System.getProperty("mongo.port", "27017"));

	@Before
	public void setUp() {
		closeable = MockitoAnnotations.openMocks(this);
		categoryRepository = new CategoryMongoRepository(new MongoClient(new ServerAddress("localhost", mongoPort)),"blogmanager", "category");

		for (Category category : categoryRepository.findAll()) {
			categoryRepository.delete(category.getId());
		}
		categoryController = new CategoryController(categoryView, categoryRepository);
	}

	@After
	public void releaseMocks() throws Exception {
		closeable.close();
	}

	@Test
	public void testAllCategories() {
		Category category = new Category("1", "Tech");
		categoryRepository.save(category);
		categoryController.getAllCategories();
		verify(categoryView).displayCategories(asList(category));
	}

	@Test
	public void testNewCategory() {
		Category category = new Category("1", "Tech");
		categoryController.addCategory(category);
		verify(categoryView).addCategory(category);
	}

	@Test
	public void testDeleteCategory() {
		Category categoryToDelete = new Category("1", "Tech");
		categoryRepository.save(categoryToDelete);
		categoryController.deleteCategory(categoryToDelete);
		verify(categoryView).deleteCategory(categoryToDelete);
	}

	@Test
	public void testUpdateCategory() {
		Category originalCategory = new Category("1", "Tech");
		categoryRepository.save(originalCategory);

		Category updatedCategory = new Category("1", "Updated Tech");
		categoryController.updateCategory(updatedCategory);

		verify(categoryView).updateCategory(updatedCategory);
	}

}
