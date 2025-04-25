package com.example.blogmanager.repository;

import com.example.blogmanager.model.Category;

import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CategoryRepositoryTest {

	private CategoryRepository categoryRepository;

	@Before
	public void setUp() {
		// Mocking the CategoryRepository interface
		categoryRepository = mock(CategoryRepository.class);
	}

	@Test
	public void testSave() {
		Category category = new Category("1", "Technology");
		categoryRepository.save(category);
		verify(categoryRepository).save(category);
	}

	@Test
	public void testFindAll() {
		List<Category> categories = new ArrayList<>();
		categories.add(new Category("1", "Technology"));
		when(categoryRepository.findAll()).thenReturn(categories);

		List<Category> result = categoryRepository.findAll();
		assertEquals(1, result.size());
		assertEquals("Technology", result.get(0).getName());
	}

	@Test
	public void testFindById() {
		Category category = new Category("1", "Technology");
		when(categoryRepository.findById("1")).thenReturn(category);

		Category result = categoryRepository.findById("1");
		assertNotNull(result);
		assertEquals("Technology", result.getName());
	}

	@Test
	public void testUpdate() {
		Category category = new Category("1", "Technology");
		categoryRepository.update(category);
		verify(categoryRepository).update(category);
	}

	@Test
	public void testDelete() {
		categoryRepository.delete("1");
		verify(categoryRepository).delete("1");
	}

}
