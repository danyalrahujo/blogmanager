package com.example.blogmanager.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.blogmanager.model.Category;
import com.example.blogmanager.repository.CategoryRepository;

public class TestCategoryController {

	@Mock
	private CategoryRepository categoryRepository;

	@InjectMocks
	private CategoryController categoryController;

	private AutoCloseable closeable;

	@Before
	public void setUp() {
		closeable = MockitoAnnotations.openMocks(this);
	}

	@After
	public void tearDown() throws Exception {
		closeable.close();
	}

	@Test
	public void testCreateCategory() {
		Category category = new Category("1", "Technology");

		categoryController.createCategory(category);

		verify(categoryRepository).save(category);
	}

	@Test
	public void testGetAllCategories() {
		Category category = new Category("1", "Technology");
		List<Category> categories = Arrays.asList(category);

		when(categoryRepository.findAll()).thenReturn(categories);

		List<Category> result = categoryController.getAllCategories();

		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals("Technology", result.get(0).getName());
	}

	@Test
	public void testGetCategoryById() {
		Category category = new Category("1", "Technology");

		when(categoryRepository.findById("1")).thenReturn(category);

		Category result = categoryController.getCategoryById("1");

		assertNotNull(result);
		assertEquals("Technology", result.getName());
	}

	@Test
	public void testDeleteCategory() {
		categoryController.deleteCategory("1");

		verify(categoryRepository).delete("1");
	}
}
