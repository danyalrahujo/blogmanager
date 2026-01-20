package com.example.blogmanager.view.swing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.swing.launcher.ApplicationLauncher.application;

import javax.swing.JFrame;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.bson.Document;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testcontainers.containers.MongoDBContainer;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

@RunWith(GUITestRunner.class)
public class BlogManagerSwingAppE2E extends AssertJSwingJUnitTestCase {

	@ClassRule
	public static final MongoDBContainer mongo = new MongoDBContainer("mongo:4.4.3");

	private static final String DB_NAME = "blogmanager";
	private static final String BLOGPOST_COLLECTION = "blogpost";
	private static final String CATEGORY_COLLECTION = "category";

	private MongoClient mongoClient;
	private FrameFixture window;

	@Override
	protected void onSetUp() {

		String mongoHost = mongo.getContainerIpAddress();
		Integer mongoPort = mongo.getFirstMappedPort();

		mongoClient = new MongoClient(new ServerAddress(mongoHost, mongoPort));

		mongoClient.getDatabase(DB_NAME).drop();

		addCategory("cat1", "Sci");
		addBlogPost("blog1", "Title", "Hello", "Author", "2025-01-01", "cat", "Sci");

		application("com.example.blogmanager.app.swing.BlogManagerSwingApp")
				.withArgs("--mongo-host=" + mongoHost, "--mongo-port=" + mongoPort, "--db-name=" + DB_NAME,
						"--blogpost-collection=" + BLOGPOST_COLLECTION, "--category-collection=" + CATEGORY_COLLECTION)
				.start();

		windowfinder();
	}

	@Override
	protected void onTearDown() {
		if (window != null) {
			window.cleanUp();
		}
		mongoClient.close();
	}

	@Test
	@GUITest
	public void testOnStartExistingBlogPostsAndCategoryAreShown() {

		assertThat(window.list("BlogPostList").contents()).anySatisfy(e -> assertThat(e).contains("Hello", "Sci"));

		window.button(JButtonMatcher.withText("Category")).click();

		windowfinder();

		assertThat(window.list("CategoryList").contents()).anySatisfy(e -> assertThat(e).contains("Sci"));
	}

	@Test
	@GUITest
	public void testAddCategoryThenAddBlogPost() {

		// Assuming DB is empty and no category is present

		window.button(JButtonMatcher.withText("Category")).click();

		windowfinder();

		window.textBox("CategoryIdTextBox").enterText("1");
		window.textBox("CategoryNameTextBox").enterText("Tech");
		window.button(JButtonMatcher.withText("Add Category")).click();

		assertThat(window.list("CategoryList").contents()).anySatisfy(e -> assertThat(e).contains("Tech"));

		window.button(JButtonMatcher.withText("BlogPosts")).click();

		windowfinder();

		window.textBox("BlogPostIdTextBox").enterText("1");
		window.textBox("BlogPostTitleTextBox").enterText("First post");
		window.textBox("BlogPostAuthorTextBox").enterText("Author");
		window.textBox("BlogPostContentTextBox").enterText("Hello world");
		window.comboBox("BlogPostCategoryComboBox").selectItem("Tech");
		window.textBox("BlogPostCreationDateTextBox").enterText("2025-04-01");

		window.button(JButtonMatcher.withText("Create")).click();

		assertThat(window.list("BlogPostList").contents())
				.anySatisfy(e -> assertThat(e).contains("First post", "Tech"));
	}

	@Test
	@GUITest
	public void testUpdateCategoryAndBlogPostAreReflectedEverywhere() {

		window.button(JButtonMatcher.withText("Category")).click();
		windowfinder();

		window.list("CategoryList").selectItem(0);
		window.textBox("CategoryNameTextBox").setText("Science");
		window.button(JButtonMatcher.withText("Update")).click();

		assertThat(window.list("CategoryList").contents()).anySatisfy(e -> assertThat(e).contains("Science"));

		window.button(JButtonMatcher.withText("BlogPosts")).click();
		windowfinder();

		assertThat(window.comboBox("BlogPostCategoryComboBox").contents()).contains("Science");

		window.list("BlogPostList").selectItem(0);
		window.textBox("BlogPostTitleTextBox").setText("Updated Title");
		window.button(JButtonMatcher.withText("Update")).click();

		assertThat(window.list("BlogPostList").contents()).anySatisfy(e -> assertThat(e).contains("Updated Title"));
	}

	@Test
	@GUITest
	public void testDeleteCategoryAndBlogPostAreReflectedEverywhere() {

		window.button(JButtonMatcher.withText("Category")).click();
		windowfinder();

		window.list("CategoryList").selectItem(0);
		window.button(JButtonMatcher.withText("Delete")).click();

		assertThat(window.list("CategoryList").contents()).noneMatch(e -> e.contains("Sci"));

		window.button(JButtonMatcher.withText("BlogPosts")).click();
		windowfinder();

		assertThat(window.comboBox("BlogPostCategoryComboBox").contents()).noneMatch(e -> e.equals("Sci"));

		window.list("BlogPostList").selectItem(0);
		window.button(JButtonMatcher.withText("Delete")).click();

		assertThat(window.list("BlogPostList").contents()).isEmpty();
	}

	private void addCategory(String id, String name) {
		mongoClient.getDatabase(DB_NAME).getCollection(CATEGORY_COLLECTION)
				.insertOne(new Document().append("id", id).append("name", name));
	}

	private void addBlogPost(String id, String title, String content, String author, String creationDate,
			String categoryId, String categoryName) {

		mongoClient.getDatabase(DB_NAME).getCollection(BLOGPOST_COLLECTION)
				.insertOne(new Document().append("id", id).append("title", title).append("content", content)
						.append("author", author).append("creationDate", creationDate)
						.append("category", new Document().append("id", categoryId).append("name", categoryName)));
	}

	private void windowfinder() {
		window = WindowFinder.findFrame(new GenericTypeMatcher<JFrame>(JFrame.class) {
			@Override
			protected boolean isMatching(JFrame frame) {
				return frame.isShowing();
			}
		}).using(robot());
	}
}
