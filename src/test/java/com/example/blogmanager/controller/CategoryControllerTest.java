package com.example.blogmanager.controller;

import static org.mockito.Mockito.ignoreStubs;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static java.util.Arrays.asList;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.blogmanager.model.Category;
import com.example.blogmanager.repository.CategoryRepository;
import com.example.blogmanager.view.CategoryView;

public class CategoryControllerTest {

	@Mock
	private CategoryRepository categoryRepository;

	@Mock
	private CategoryView categoryView;

	@InjectMocks
	private CategoryController categoryController;

	private AutoCloseable closeable;

	@Before
	public void setup() {
		closeable = MockitoAnnotations.openMocks(this);
	}

	@After
	public void releaseMocks() throws Exception {
		closeable.close();
	}

	@Test
	public void testAllCategories() {
		List<Category> categories = asList(new Category("1", "Tech"));
		when(categoryRepository.findAll()).thenReturn(categories);
		categoryController.getAllCategories();
		verify(categoryView).displayCategories(categories);
	}

	@Test
	public void testNewCategoryWhenCategoryDoesNotAlreadyExist() {
		Category category = new Category("1", "Tech");
		when(categoryRepository.findById("1")).thenReturn(null);
		categoryController.addCategory(category);

		InOrder inOrder = inOrder(categoryRepository, categoryView);
		inOrder.verify(categoryRepository).save(category);
		inOrder.verify(categoryView).addCategory(category);
	}

	@Test
	public void testNewCategoryWhenCategoryAlreadyExists() {
		Category categoryToAdd = new Category("1", "Tech");
		Category existingCategory = new Category("1", "Tech");
		when(categoryRepository.findById("1")).thenReturn(existingCategory);
		categoryController.addCategory(categoryToAdd);

		verify(categoryView).showErrorMessage("Category with ID 1 already exists.", existingCategory);
		verifyNoMoreInteractions(ignoreStubs(categoryRepository));
	}

	@Test
	public void testUpdateCategoryWhenCategoryExists() {
		Category categoryToUpdate = new Category("1", "Tech");
		when(categoryRepository.findById("1")).thenReturn(categoryToUpdate);

		categoryController.updateCategory(categoryToUpdate);

		InOrder inOrder = inOrder(categoryRepository, categoryView);
		inOrder.verify(categoryRepository).update(categoryToUpdate);
		inOrder.verify(categoryView).updateCategory(categoryToUpdate);
	}

	@Test
	public void testUpdateCategoryWhenCategoryDoesNotExist() {
		Category categoryToUpdate = new Category("1", "Tech");
		when(categoryRepository.findById("1")).thenReturn(null);

		categoryController.updateCategory(categoryToUpdate);

		verify(categoryView).showErrorMessage("No category found with ID 1", categoryToUpdate);
		verifyNoMoreInteractions(ignoreStubs(categoryRepository));
	}

	@Test
	public void testDeleteCategoryWhenCategoryExists() {
		Category categoryToDelete = new Category("1", "Tech");
		when(categoryRepository.findById("1")).thenReturn(categoryToDelete);

		categoryController.deleteCategory(categoryToDelete);

		InOrder inOrder = inOrder(categoryRepository, categoryView);
		inOrder.verify(categoryRepository).delete("1");
		inOrder.verify(categoryView).deleteCategory(categoryToDelete);
	}

	@Test
	public void testDeleteCategoryWhenCategoryDoesNotExist() {
		Category category = new Category("1", "Tech");
		when(categoryRepository.findById("1")).thenReturn(null);

		categoryController.deleteCategory(category);

		verify(categoryView).showErrorMessage("No category found with ID 1", category);
		verifyNoMoreInteractions(ignoreStubs(categoryRepository));
	}


}
