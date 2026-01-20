package com.example.blogmanager.view.swing;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testcontainers.containers.MongoDBContainer;

import static org.awaitility.Awaitility.await;
import java.util.concurrent.TimeUnit;

import com.example.blogmanager.controller.BlogPostController;
import com.example.blogmanager.model.BlogPost;
import com.example.blogmanager.model.Category;
import com.example.blogmanager.repository.mongo.BlogPostMongoRepository;
import com.example.blogmanager.repository.mongo.CategoryMongoRepository;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

@RunWith(GUITestRunner.class)
public class BlogPostSwingViewIT extends AssertJSwingJUnitTestCase {

	@ClassRule
	public static final MongoDBContainer mongo = new MongoDBContainer("mongo:4.4.3");

	private MongoClient mongoClient;

	private FrameFixture window;
	private BlogPostMongoRepository blogPostRepository;
	private CategoryMongoRepository categoryRepository;
	private BlogPostController blogPostController;
	private BlogPostSwingView blogPostSwingView;

	@Override
	protected void onSetUp() {

		mongoClient = new MongoClient(new ServerAddress(mongo.getHost(), mongo.getFirstMappedPort()));

		blogPostRepository = new BlogPostMongoRepository(mongoClient, "blogmanager", "blogpost");
		categoryRepository = new CategoryMongoRepository(mongoClient, "blogmanager", "category");

		for (BlogPost blogPost : blogPostRepository.findAll()) {
			blogPostRepository.delete(blogPost.getId());
		}
		for (Category category : categoryRepository.findAll()) {
			categoryRepository.delete(category.getId());
		}

		categoryRepository.save(new Category("cat1", "Tech"));

		window = new FrameFixture(robot(), GuiActionRunner.execute(() -> {
			blogPostSwingView = new BlogPostSwingView();
			blogPostController = new BlogPostController(blogPostSwingView, blogPostRepository, categoryRepository);
			blogPostSwingView.updateCategories(categoryRepository.findAll());
			blogPostSwingView.setBlogPostController(blogPostController);
			return blogPostSwingView;
		}));

		window.show();
	}

	@Override
	protected void onTearDown() {
		mongoClient.close();
	}

	@Test
	public void testAddBlogPostUsingSwingUI() {
		window.textBox("BlogPostIdTextBox").enterText("1");
		window.textBox("BlogPostTitleTextBox").enterText("Hello");
		window.textBox("BlogPostAuthorTextBox").enterText("Author");
		window.textBox("BlogPostContentTextBox").enterText("First post");
		window.comboBox("BlogPostCategoryComboBox").selectItem("Tech");
		window.textBox("BlogPostCreationDateTextBox").enterText("2025-04-01");
		window.button(JButtonMatcher.withText("Create")).click();

		await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> assertThat(blogPostRepository.findById("1")).isEqualTo(
				new BlogPost("1", "Hello", "First post", "Author", "2025-04-01", new Category("cat1", "Tech"))));

		await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> assertThat(window.list("BlogPostList").contents())
				.containsExactly("1 - Hello - Author - 2025-04-01 - First post - Tech"));
	}

	@Test
	public void testDeleteBlogPostUsingSwingUI() {
		Category category = categoryRepository.findById("cat1");
		blogPostRepository.save(new BlogPost("1", "Hello", "First post", "Author", "2025-04-01", category));

		GuiActionRunner.execute(() -> blogPostController.getAllBlogPosts());

		window.list("BlogPostList").selectItem(0);
		window.button(JButtonMatcher.withText("Delete")).click();

		await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> assertThat(blogPostRepository.findById("1")).isNull());

		await().atMost(5, TimeUnit.SECONDS)
				.untilAsserted(() -> assertThat(window.list("BlogPostList").contents()).isEmpty());
	}

	@Test
	public void testUpdateBlogPostUsingSwingUI() {
		Category category = categoryRepository.findById("cat1");
		blogPostRepository.save(new BlogPost("1", "Hello", "First post", "Author", "2025-04-01", category));

		GuiActionRunner.execute(() -> blogPostController.getAllBlogPosts());

		window.list("BlogPostList").selectItem(0);

		window.textBox("BlogPostIdTextBox").requireText("1");
		window.textBox("BlogPostIdTextBox").requireDisabled();

		window.textBox("BlogPostTitleTextBox").setText("Updated");
		window.textBox("BlogPostAuthorTextBox").setText("Updated Author");
		window.textBox("BlogPostContentTextBox").setText("Updated post");
		window.textBox("BlogPostCreationDateTextBox").setText("2025-05-01");

		window.button(JButtonMatcher.withText("Update")).click();

		await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> assertThat(blogPostRepository.findById("1"))
				.isEqualTo(new BlogPost("1", "Updated", "Updated post", "Updated Author", "2025-05-01", category)));

		await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> assertThat(window.list("BlogPostList").contents())
				.containsExactly("1 - Updated - Updated Author - 2025-05-01 - Updated post - Tech"));
	}

	@Test
	public void testAddBlogPostDuplicateIdShowsErrorMessage() {
		Category category = categoryRepository.findById("cat1");
		blogPostRepository.save(new BlogPost("1", "Existing", "Post", "Author", "2025-04-01", category));

		GuiActionRunner.execute(() -> blogPostController.getAllBlogPosts());

		window.textBox("BlogPostIdTextBox").enterText("1");
		window.textBox("BlogPostTitleTextBox").enterText("New");
		window.textBox("BlogPostAuthorTextBox").enterText("Author");
		window.textBox("BlogPostContentTextBox").enterText("Content");
		window.comboBox("BlogPostCategoryComboBox").selectItem("Tech");
		window.textBox("BlogPostCreationDateTextBox").enterText("2025-06-01");

		window.button(JButtonMatcher.withText("Create")).click();

		await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> assertThat(window.list("BlogPostList").contents())
				.containsExactly("1 - Existing - Author - 2025-04-01 - Post - Tech"));

		await().atMost(5, TimeUnit.SECONDS)
				.untilAsserted(() -> window.label("errorMsg").requireText("Blog post with ID 1 already exists.: "
						+ new BlogPost("1", "New", "Content", "Author", "2025-06-01", category)));

	}

	@Test
	public void testDeleteBlogPostNonExistingShowsErrorMessage() {
		Category category = categoryRepository.findById("cat1");
		BlogPost blogPost = new BlogPost("1", "Ghost", "Post", "Author", "2025-01-01", category);

		GuiActionRunner.execute(() -> blogPostSwingView.getListBlogPostModel().addElement(blogPost));

		window.list("BlogPostList").selectItem(0);
		window.button(JButtonMatcher.withText("Delete")).click();

		await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> assertThat(window.list("BlogPostList").contents())
				.containsExactly("1 - Ghost - Author - 2025-01-01 - Post - Tech"));

		await().atMost(5, TimeUnit.SECONDS)
				.untilAsserted(() -> window.label("errorMsg").requireText("No blog post found with ID 1: " + blogPost));
	}

}
