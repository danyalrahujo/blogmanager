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

import com.example.blogmanager.controller.BlogPostController;
import com.example.blogmanager.model.BlogPost;
import com.example.blogmanager.repository.BlogPostRepository;
import com.example.blogmanager.view.BlogPostView;

public class BlogPostControllerRaceConditionTest {

	@Mock
	private BlogPostRepository blogPostRepository;

	@Mock
	private BlogPostView blogPostView;

	@InjectMocks
	private BlogPostController blogPostController;

	private AutoCloseable closeable;

	private List<BlogPost> database;

	@Before
	public void setUp() {
		closeable = MockitoAnnotations.openMocks(this);
		database = new ArrayList<>();
	}

	@After
	public void tearDown() throws Exception {
		closeable.close();
	}

	@Test
	public void testAddBlogPostConcurrent() {
		BlogPost post = new BlogPost("1", "t", "c", "a", "d", null);

		when(blogPostRepository.findById(anyString())).thenAnswer(inv -> database.stream().findFirst().orElse(null));

		doAnswer(inv -> {
			database.add(inv.getArgument(0));
			return null;
		}).when(blogPostRepository).save(any(BlogPost.class));

		List<Thread> threads = IntStream.range(0, 10)
				.mapToObj(i -> new Thread(() -> blogPostController.addBlogPost(post))).peek(Thread::start)
				.collect(Collectors.toList());

		await().atMost(10, TimeUnit.SECONDS).until(() -> threads.stream().noneMatch(Thread::isAlive));

		assertThat(database).containsExactly(post);
	}

	@Test
	public void testUpdateBlogPostConcurrent() {
		BlogPost original = new BlogPost("1", "old", "old", "old", "d", null);
		BlogPost updated = new BlogPost("1", "new", "new", "new", "d", null);

		database.add(original);

		when(blogPostRepository.findById(anyString())).thenAnswer(
				inv -> database.stream().filter(p -> p.getId().equals(inv.getArgument(0))).findFirst().orElse(null));

		doAnswer(inv -> {
			BlogPost p = inv.getArgument(0);
			database.removeIf(b -> b.getId().equals(p.getId()));
			database.add(p);
			return null;
		}).when(blogPostRepository).update(any(BlogPost.class));

		List<Thread> threads = IntStream.range(0, 10)
				.mapToObj(i -> new Thread(() -> blogPostController.updateBlogPost(updated))).peek(Thread::start)
				.collect(Collectors.toList());

		await().atMost(10, TimeUnit.SECONDS).until(() -> threads.stream().noneMatch(Thread::isAlive));

		assertThat(database).containsExactly(updated);
	}

	@Test
	public void testDeleteBlogPostConcurrent() {
		BlogPost post = new BlogPost("1", "t", "c", "a", "d", null);
		database.add(post);

		when(blogPostRepository.findById(anyString())).thenAnswer(
				inv -> database.stream().filter(p -> p.getId().equals(inv.getArgument(0))).findFirst().orElse(null));

		doAnswer(inv -> {
			database.removeIf(p -> p.getId().equals(inv.getArgument(0)));
			return null;
		}).when(blogPostRepository).delete(anyString());

		List<Thread> threads = IntStream.range(0, 10)
				.mapToObj(i -> new Thread(() -> blogPostController.deleteBlogPost(post))).peek(Thread::start)
				.collect(Collectors.toList());

		await().atMost(10, TimeUnit.SECONDS).until(() -> threads.stream().noneMatch(Thread::isAlive));

		assertThat(database).isEmpty();
	}
}
