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

import com.example.blogmanager.controller.CategoryController;
import com.example.blogmanager.model.Category;
import com.example.blogmanager.repository.mongo.CategoryMongoRepository;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

@RunWith(GUITestRunner.class)
public class CategorySwingViewIT extends AssertJSwingJUnitTestCase {

	@ClassRule
	public static final MongoDBContainer mongo = new MongoDBContainer("mongo:4.4.3");

	private MongoClient mongoClient;

	private FrameFixture window;
	private CategoryMongoRepository categoryRepository;
	private CategoryController categoryController;
	private CategorySwingView categorySwingView;

	@Override
	protected void onSetUp() {

		mongoClient = new MongoClient(new ServerAddress(mongo.getHost(), mongo.getFirstMappedPort()));

		categoryRepository = new CategoryMongoRepository(mongoClient, "blogmanager", "category");

		for (Category category : categoryRepository.findAll()) {
			categoryRepository.delete(category.getId());
		}

		window = new FrameFixture(robot(), GuiActionRunner.execute(() -> {
			categorySwingView = new CategorySwingView();
			categoryController = new CategoryController(categorySwingView, categoryRepository);
			categorySwingView.setCategoryController(categoryController);
			return categorySwingView;
		}));

		window.show();
	}

	@Override
	protected void onTearDown() {
		mongoClient.close();
	}

	@Test
	public void testAddCategoryUsingSwingUI() {
		window.textBox("CategoryIdTextBox").enterText("1");
		window.textBox("CategoryNameTextBox").enterText("Tech");
		window.button(JButtonMatcher.withText("Add Category")).click();

		await().atMost(5, TimeUnit.SECONDS)
				.untilAsserted(() -> assertThat(categoryRepository.findById("1")).isEqualTo(new Category("1", "Tech")));

		await().atMost(5, TimeUnit.SECONDS)
				.untilAsserted(() -> assertThat(window.list("CategoryList").contents()).containsExactly("1 - Tech"));
	}

	@Test
	public void testDeleteCategoryUsingSwingUI() {
		categoryRepository.save(new Category("1", "Tech"));

		GuiActionRunner.execute(() -> categoryController.getAllCategories());

		window.list("CategoryList").selectItem(0);
		window.button(JButtonMatcher.withText("Delete")).click();

		await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> assertThat(categoryRepository.findById("1")).isNull());

		await().atMost(5, TimeUnit.SECONDS)
				.untilAsserted(() -> assertThat(window.list("CategoryList").contents()).isEmpty());
	}

	@Test
	public void testAddCategoryDuplicateIdShowsErrorMessage() {
		categoryRepository.save(new Category("1", "Existing"));

		GuiActionRunner.execute(() -> categoryController.getAllCategories());

		window.textBox("CategoryIdTextBox").enterText("1");
		window.textBox("CategoryNameTextBox").enterText("Tech");
		window.button(JButtonMatcher.withText("Add Category")).click();

		await().atMost(5, TimeUnit.SECONDS).untilAsserted(
				() -> assertThat(window.list("CategoryList").contents()).containsExactly("1 - Existing"));

		await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> window.label("errorMsg")
				.requireText("Category with ID 1 already exists.: " + new Category("1", "Tech")));
	}

	@Test
	public void testUpdateCategoryUsingSwingUI() {
		categoryRepository.save(new Category("1", "Tech"));

		GuiActionRunner.execute(() -> categoryController.getAllCategories());

		window.list("CategoryList").selectItem(0);

		window.textBox("CategoryIdTextBox").requireText("1");
		window.textBox("CategoryIdTextBox").requireDisabled();

		window.textBox("CategoryNameTextBox").setText("Updated Tech");
		window.button(JButtonMatcher.withText("Update")).click();

		await().atMost(5, TimeUnit.SECONDS).untilAsserted(
				() -> assertThat(categoryRepository.findById("1")).isEqualTo(new Category("1", "Updated Tech")));

		await().atMost(5, TimeUnit.SECONDS).untilAsserted(
				() -> assertThat(window.list("CategoryList").contents()).containsExactly("1 - Updated Tech"));
	}

	@Test
	public void testDeleteCategoryNonExistingShowsErrorMessage() {
		Category category = new Category("1", "Ghost");

		GuiActionRunner.execute(() -> categorySwingView.getListCategoryModel().addElement(category));

		window.list("CategoryList").selectItem(0);
		window.button(JButtonMatcher.withText("Delete")).click();

		await().atMost(5, TimeUnit.SECONDS)
				.untilAsserted(() -> assertThat(window.list("CategoryList").contents()).containsExactly("1 - Ghost"));

		await().atMost(5, TimeUnit.SECONDS)
				.untilAsserted(() -> window.label("errorMsg").requireText("No category found with ID 1: " + category));
	}

}
