package com.example.blogmanager.controller.racecondition;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.blogmanager.controller.CategoryController;
import com.example.blogmanager.model.Category;
import com.example.blogmanager.repository.CategoryRepository;
import com.example.blogmanager.view.CategoryView;

public class CategoryControllerRaceConditionTest {

	@Mock
	private CategoryRepository categoryRepository;

	@Mock
	private CategoryView categoryView;

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
	public void testAddCategoryConcurrent() {
		Category category = new Category("1", "Tech");
		List<Category> categories = new ArrayList<Category>();
		when(categoryRepository.findById(anyString()))
				.thenAnswer(invocation -> categories.stream().findFirst().orElse(null));

		doAnswer(invocation -> {
			categories.add(invocation.getArgument(0));
			return null;
		}).when(categoryRepository).save(any(Category.class));

		List<Thread> threads = IntStream.range(0, 10)
				.mapToObj(i -> new Thread(() -> categoryController.addCategory(category))).peek(Thread::start)
				.collect(Collectors.toList());

		await().atMost(10, TimeUnit.SECONDS).until(() -> threads.stream().noneMatch(Thread::isAlive));

		assertThat(categories).containsExactly(category);
	}

	@Test
	public void testUpdateCategoryConcurrent() {
		List<Category> categories = new ArrayList<>();
		Category original = new Category("1", "Old");
		Category updated = new Category("1", "New");

		categories.add(original);

		when(categoryRepository.findById(anyString())).thenAnswer(invocation -> categories.stream()
				.filter(c -> c.getId().equals(invocation.getArgument(0))).findFirst().orElse(null));

		doAnswer(invocation -> {
			Category c = invocation.getArgument(0);
			for (int i = 0; i < categories.size(); i++) {
				if (categories.get(i).getId().equals(c.getId())) {
					categories.set(i, c);
					break;
				}
			}
			return null;
		}).when(categoryRepository).update(any(Category.class));

		List<Thread> threads = IntStream.range(0, 10)
				.mapToObj(i -> new Thread(() -> categoryController.updateCategory(updated))).peek(Thread::start)
				.collect(Collectors.toList());

		await().atMost(10, TimeUnit.SECONDS).until(() -> threads.stream().noneMatch(Thread::isAlive));

		assertThat(categories).containsExactly(updated);
	}

	@Test
	public void testDeleteCategoryConcurrent() {
		List<Category> categories = new ArrayList<>();
		Category category = new Category("1", "Tech");

		categories.add(category);

		when(categoryRepository.findById(anyString())).thenAnswer(invocation -> categories.stream()
				.filter(c -> c.getId().equals(invocation.getArgument(0))).findFirst().orElse(null));

		doAnswer(invocation -> {
			String id = invocation.getArgument(0);
			categories.removeIf(existing -> existing.getId().equals(id));
			return null;
		}).when(categoryRepository).delete(anyString());
		
		List<Thread> threads = IntStream.range(0, 10)
				.mapToObj(i -> new Thread(() -> categoryController.deleteCategory(category))).peek(Thread::start)
				.collect(Collectors.toList());

		await().atMost(10, TimeUnit.SECONDS).until(() -> threads.stream().noneMatch(Thread::isAlive));

		assertThat(categories).isEmpty();
	}

}
