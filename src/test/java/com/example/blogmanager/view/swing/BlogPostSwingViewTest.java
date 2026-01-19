package com.example.blogmanager.view.swing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JComboBoxFixture;
import org.assertj.swing.fixture.JTextComponentFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.blogmanager.controller.BlogPostController;
import com.example.blogmanager.model.BlogPost;
import com.example.blogmanager.model.Category;

@RunWith(GUITestRunner.class)
public class BlogPostSwingViewTest extends AssertJSwingJUnitTestCase {

	private FrameFixture window;
	private BlogPostSwingView blogPostSwingView;

	@Mock
	private BlogPostController blogPostController;

	private AutoCloseable closeable;

	private static final String DATE = "2025-01-01";

	@Override
	protected void onSetUp() throws Exception {
		closeable = MockitoAnnotations.openMocks(this);
		GuiActionRunner.execute(() -> {
			blogPostSwingView = new BlogPostSwingView();
			blogPostSwingView.setBlogPostController(blogPostController);
			return blogPostSwingView;
		});
		window = new FrameFixture(robot(), blogPostSwingView);
		window.show();
	}

	@Override
	protected void onTearDown() throws Exception {
		closeable.close();
	}

	@Test
	@GUITest
	public void testInitialState() {
		window.textBox("BlogPostIdTextBox").requireEmpty();
		window.textBox("BlogPostTitleTextBox").requireEmpty();
		window.textBox("BlogPostAuthorTextBox").requireEmpty();
		window.textBox("BlogPostContentTextBox").requireEmpty();
		window.textBox("BlogPostCreationDateTextBox").requireEmpty();

		window.comboBox("BlogPostCategoryComboBox").requireItemCount(0);

		window.button(JButtonMatcher.withText("Create")).requireDisabled();
		window.button(JButtonMatcher.withText("Update")).requireDisabled();
		window.button(JButtonMatcher.withText("Delete")).requireDisabled();
		window.button(JButtonMatcher.withText("Clear")).requireDisabled();

		window.label("errorMsg").requireText("");
	}

	@Test
	@GUITest
	public void testCreateButtonDisabledWhenCategoryComboBoxIsEmpty() {
		window.textBox("BlogPostIdTextBox").enterText("1");
		window.textBox("BlogPostTitleTextBox").enterText("Title");
		window.textBox("BlogPostAuthorTextBox").enterText("Author");
		window.textBox("BlogPostContentTextBox").enterText("Content");
		window.textBox("BlogPostCreationDateTextBox").enterText(DATE);

		window.button(JButtonMatcher.withText("Create")).requireDisabled();
	}

	@Test
	@GUITest
	public void testCreateButtonDisabledWhenAnyFieldIsBlankEvenIfCategoryExists() {
		addCategories();

		JTextComponentFixture title = window.textBox("BlogPostTitleTextBox");
		JTextComponentFixture author = window.textBox("BlogPostAuthorTextBox");
		JTextComponentFixture content = window.textBox("BlogPostContentTextBox");

		window.textBox("BlogPostIdTextBox").enterText("1");
		title.enterText("Title");
		author.enterText(" ");
		content.enterText("Content");
		window.textBox("BlogPostCreationDateTextBox").enterText(DATE);

		window.comboBox("BlogPostCategoryComboBox").selectItem(0);

		window.button(JButtonMatcher.withText("Create")).requireDisabled();
	}

	@Test
	@GUITest
	public void testCreateButtonDisabledWhenCategoryNotSelected() {
		addCategories();

		window.textBox("BlogPostIdTextBox").enterText("1");
		window.textBox("BlogPostTitleTextBox").enterText("Title");
		window.textBox("BlogPostAuthorTextBox").enterText("Author");
		window.textBox("BlogPostContentTextBox").enterText("Content");
		window.textBox("BlogPostCreationDateTextBox").enterText(DATE);

		JComboBoxFixture comboBox = window.comboBox("BlogPostCategoryComboBox");
		comboBox.clearSelection();

		window.button(JButtonMatcher.withText("Create")).requireDisabled();
	}

	@Test
	@GUITest
	public void testCreateButtonEnabledWhenAllFieldsAndCategorySelected() {
		addCategories();

		window.textBox("BlogPostIdTextBox").enterText("1");
		window.textBox("BlogPostTitleTextBox").enterText("Title");
		window.textBox("BlogPostAuthorTextBox").enterText("Author");
		window.textBox("BlogPostContentTextBox").enterText("Content");
		window.textBox("BlogPostCreationDateTextBox").enterText(DATE);

		window.comboBox("BlogPostCategoryComboBox").selectItem(0);

		window.button(JButtonMatcher.withText("Create")).requireEnabled();
	}

	@Test
	public void testCreateButtonShouldDelegateToController() {
		addCategories();

		Category category = (Category) window.comboBox("BlogPostCategoryComboBox").target().getSelectedItem();

		window.textBox("BlogPostIdTextBox").enterText("1");
		window.textBox("BlogPostTitleTextBox").enterText("Title");
		window.textBox("BlogPostAuthorTextBox").enterText("Author");
		window.textBox("BlogPostContentTextBox").enterText("Content");
		window.textBox("BlogPostCreationDateTextBox").enterText(DATE);

		window.comboBox("BlogPostCategoryComboBox").selectItem(0);
		window.button(JButtonMatcher.withText("Create")).click();

		await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> verify(blogPostController)
				.addBlogPost(new BlogPost("1", "Title", "Content", "Author", DATE, category)));
	}

	@Test
	public void testBlogPostAddedShouldAddPostToListAndResetErrorLabel() {
		BlogPost post = new BlogPost("1", "Title", "Content", "Author", DATE, new Category("1", "Tech"));

		blogPostSwingView.addBlogPost(post);

		assertThat(window.list("BlogPostList").contents()).containsExactly(getDisplayString(post));

		window.label("errorMsg").requireText("");
	}

	@Test
	public void testShowErrorMessageShouldUpdateErrorLabel() {
		BlogPost post = new BlogPost("1", "Title", "Content", "Author", DATE, new Category("1", "Tech"));

		GuiActionRunner.execute(() -> blogPostSwingView.showErrorMessage("error message", post));

		window.label("errorMsg").requireText("error message: " + post);
	}

	@Test
	public void testUpdateButtonShouldDelegateToController() {
		Category category = new Category("1", "Tech");

		GuiActionRunner.execute(() -> {
			DefaultListModel<BlogPost> model = blogPostSwingView.getListBlogPostModel();
			model.addElement(new BlogPost("1", "Old", "Old", "Old", DATE, category));
			blogPostSwingView.updateCategories(Arrays.asList(category));
		});

		window.list("BlogPostList").selectItem(0);

		window.textBox("BlogPostTitleTextBox").setText("New");
		window.textBox("BlogPostAuthorTextBox").setText("New");
		window.textBox("BlogPostContentTextBox").setText("New");
		window.textBox("BlogPostCreationDateTextBox").setText(DATE);

		window.comboBox("BlogPostCategoryComboBox").selectItem(0);
		window.button(JButtonMatcher.withText("Update")).click();

		await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> verify(blogPostController)
				.updateBlogPost(new BlogPost("1", "New", "New", "New", DATE, category)));
	}

	@Test
	public void testDeleteButtonShouldDelegateToController() {
		Category category = new Category("1", "Tech");
		BlogPost post = new BlogPost("1", "Title", "Content", "Author", DATE, category);

		GuiActionRunner.execute(() -> blogPostSwingView.getListBlogPostModel().addElement(post));

		window.list("BlogPostList").selectItem(0);
		window.button("deleteBtn").click();

		await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> verify(blogPostController).deleteBlogPost(post));
	}

	private void addCategories() {
		GuiActionRunner.execute(() -> blogPostSwingView
				.updateCategories(Arrays.asList(new Category("1", "Tech"), new Category("2", "Life"))));
	}

	private String getDisplayString(BlogPost blogPost) {
		return blogPost.getId() + " - " + blogPost.getTitle() + " - " + blogPost.getAuthor() + " - "
				+ blogPost.getCreationDate() + " - " + blogPost.getContent() + " - "
				+ (blogPost.getCategory() != null ? blogPost.getCategory().getName() : "");
	}

	@Test
	@GUITest
	public void testMultipleBlogPostsShouldBeDisplayedInList() {
		Category category = new Category("1", "Tech");

		BlogPost p1 = new BlogPost("1", "T1", "C1", "A1", DATE, category);
		BlogPost p2 = new BlogPost("2", "T2", "C2", "A2", DATE, category);

		GuiActionRunner.execute(() -> {
			DefaultListModel<BlogPost> model = blogPostSwingView.getListBlogPostModel();
			model.addElement(p1);
			model.addElement(p2);
		});

		String[] listContents = window.list().contents();
		assertThat(listContents).containsExactly(getDisplayString(p1), getDisplayString(p2));
	}

	@Test
	@GUITest
	public void testSelectingBlogPostFromListShouldPopulateAllTextFields() {
		Category category = new Category("1", "Tech");
		BlogPost post = new BlogPost("1", "Title", "Content", "Author", DATE, category);

		GuiActionRunner.execute(() -> {
			blogPostSwingView.getListBlogPostModel().addElement(post);
			blogPostSwingView.updateCategories(Arrays.asList(category));
		});

		window.list("BlogPostList").selectItem(0);

		window.textBox("BlogPostIdTextBox").requireText("1");
		window.textBox("BlogPostTitleTextBox").requireText("Title");
		window.textBox("BlogPostAuthorTextBox").requireText("Author");
		window.textBox("BlogPostContentTextBox").requireText("Content");
		window.textBox("BlogPostCreationDateTextBox").requireText(DATE);
		window.comboBox("BlogPostCategoryComboBox").requireSelection(category.getName());
	}

	@Test
	@GUITest
	public void testSelectingDifferentBlogPostsShouldUpdateTextFields() {
		Category category = new Category("1", "Tech");

		BlogPost p1 = new BlogPost("1", "T1", "C1", "A1", DATE, category);
		BlogPost p2 = new BlogPost("2", "T2", "C2", "A2", DATE, category);

		GuiActionRunner.execute(() -> {
			DefaultListModel<BlogPost> model = blogPostSwingView.getListBlogPostModel();
			model.addElement(p1);
			model.addElement(p2);
			blogPostSwingView.updateCategories(Arrays.asList(category));
		});

		window.list("BlogPostList").selectItem(0);
		window.list("BlogPostList").selectItem(1);

		window.textBox("BlogPostIdTextBox").requireText("2");
		window.textBox("BlogPostTitleTextBox").requireText("T2");
		window.textBox("BlogPostAuthorTextBox").requireText("A2");
		window.textBox("BlogPostContentTextBox").requireText("C2");
	}

	@Test
	@GUITest
	public void testBlogPostUpdatedShouldUpdatePostInList() {
		Category category = new Category("1", "Tech");

		BlogPost oldPost = new BlogPost("1", "Old", "Old", "Old", DATE, category);
		BlogPost newPost = new BlogPost("1", "New", "New", "New", DATE, category);

		GuiActionRunner.execute(() -> {
			blogPostSwingView.getListBlogPostModel().addElement(oldPost);
			blogPostSwingView.updateCategories(Arrays.asList(category));
		});

		window.list("BlogPostList").selectItem(0);

		GuiActionRunner.execute(() -> blogPostSwingView.updateBlogPost(newPost));

		String[] listContents = window.list().contents();
		assertThat(listContents).containsExactly(getDisplayString(newPost));
	}

	@Test
	@GUITest
	public void testUpdateButtonShouldBeDisabledWhenNoBlogPostIsSelected() {
		window.button(JButtonMatcher.withText("Update")).requireDisabled();
	}

	@Test
	@GUITest
	public void testBlogPostDeletedShouldRemovePostFromList() {
		Category category = new Category("1", "Tech");
		BlogPost post = new BlogPost("1", "Title", "Content", "Author", DATE, category);

		GuiActionRunner.execute(() -> blogPostSwingView.getListBlogPostModel().addElement(post));

		window.list("BlogPostList").selectItem(0);

		GuiActionRunner.execute(() -> blogPostSwingView.deleteBlogPost(post));

		window.list("BlogPostList").requireItemCount(0);
	}

	@Test
	@GUITest
	public void testDeleteButtonShouldBeDisabledWhenNoBlogPostIsSelected() {
		window.button(JButtonMatcher.withText("Delete")).requireDisabled();
	}

	@Test
	@GUITest
	public void testDisplayBlogPostsShouldPopulateList() {
		Category category = new Category("1", "Tech");
		BlogPost post = new BlogPost("1", "Title", "Content", "Author", DATE, category);

		GuiActionRunner.execute(() -> blogPostSwingView.displayBlogPosts(Arrays.asList(post)));

		assertThat(window.list("BlogPostList").contents()).containsExactly(getDisplayString(post));
	}

	@Test
	public void testGetComboCategoriesModelShouldReturnModelWithCategories() {
		Category c1 = new Category("1", "Tech");
		Category c2 = new Category("2", "Life");

		GuiActionRunner.execute(() -> {
			blogPostSwingView.updateCategories(Arrays.asList(c1, c2));
		});

		DefaultComboBoxModel<Category> model = GuiActionRunner
				.execute(() -> blogPostSwingView.getComboCategoriesModel());

		assertThat(model.getSize()).isEqualTo(2);
		assertThat(model.getElementAt(0)).isSameAs(c1);
		assertThat(model.getElementAt(1)).isSameAs(c2);
	}

	@Test
	@GUITest
	public void testClearButtonActionListenerWhenNothingSelected() {
		window.button(JButtonMatcher.withText("Clear")).click();

		window.textBox("BlogPostIdTextBox").requireEmpty();
		window.textBox("BlogPostTitleTextBox").requireEmpty();
	}

	@Test
	public void testGetDisplayStringWhenCategoryIsNull() {
		BlogPost post = new BlogPost("1", "T", "C", "A", DATE, null);

		String display = GuiActionRunner.execute(() -> blogPostSwingView.getDisplayString(post));

		assertThat(display).contains(" - ");
	}

	@Test
	@GUITest
	public void testEnsureCategoryExistsWhenCategoryIsMissing() {
		Category category = new Category("1", "Tech");

		GuiActionRunner.execute(() -> blogPostSwingView.ensureCategoryExists(category));

		assertThat(window.comboBox("BlogPostCategoryComboBox").contents()).containsExactly("Tech");
	}

	@Test
	@GUITest
	public void testEnsureCategoryExistsWhenCategoryAlreadyPresent() {
		Category category = new Category("1", "Tech");

		GuiActionRunner.execute(() -> {
			blogPostSwingView.updateCategories(Arrays.asList(category));
			blogPostSwingView.ensureCategoryExists(category);
		});

		assertThat(window.comboBox("BlogPostCategoryComboBox").contents()).containsExactly("Tech");
	}

	@Test
	@GUITest
	public void testUpdateBlogPostWhenNoSelectionShouldDoNothing() {
		BlogPost post = new BlogPost("1", "T", "C", "A", DATE, null);

		GuiActionRunner.execute(() -> blogPostSwingView.updateBlogPost(post));

		assertThat(window.list("BlogPostList").contents()).isEmpty();
	}

	@Test
	@GUITest
	public void testDisplayBlogPostsWithEmptyListShouldClearList() {
		Category category = new Category("1", "Tech");
		BlogPost post = new BlogPost("1", "Title", "Content", "Author", DATE, category);

		GuiActionRunner.execute(() -> {
			blogPostSwingView.displayBlogPosts(Arrays.asList(post));
			blogPostSwingView.displayBlogPosts(Arrays.asList());
		});

		window.list("BlogPostList").requireItemCount(0);
	}

	@Test
	@GUITest
	public void testUpdateCategoriesShouldPopulateCategoryComboBox() {
		Category c1 = new Category("1", "Tech");
		Category c2 = new Category("2", "Life");

		GuiActionRunner.execute(() -> blogPostSwingView.updateCategories(Arrays.asList(c1, c2)));

		JComboBoxFixture comboBox = window.comboBox("BlogPostCategoryComboBox");

		assertThat(comboBox.contents()).containsExactly("Tech", "Life");
	}

	@Test
	@GUITest
	public void testDeletingLastBlogPostShouldClearListAndDisableButtons() {
		Category category = new Category("1", "Tech");
		BlogPost post = new BlogPost("1", "Title", "Content", "Author", DATE, category);

		GuiActionRunner.execute(() -> blogPostSwingView.getListBlogPostModel().addElement(post));

		window.list("BlogPostList").selectItem(0);

		GuiActionRunner.execute(() -> blogPostSwingView.deleteBlogPost(post));

		window.list("BlogPostList").requireItemCount(0);
		window.button(JButtonMatcher.withText("Update")).requireDisabled();
		window.button(JButtonMatcher.withText("Delete")).requireDisabled();
	}

	@Test
	@GUITest
	public void testButtonsShouldBeEnabledWhenBlogPostIsSelected() {
		Category category = new Category("1", "Tech");
		BlogPost post = new BlogPost("1", "Title", "Content", "Author", DATE, category);

		GuiActionRunner.execute(() -> blogPostSwingView.getListBlogPostModel().addElement(post));

		window.list("BlogPostList").selectItem(0);

		window.button(JButtonMatcher.withText("Update")).requireEnabled();
		window.button(JButtonMatcher.withText("Delete")).requireEnabled();
		window.button(JButtonMatcher.withText("Clear")).requireEnabled();
	}

	@Test
	@GUITest
	public void testButtonsShouldBeDisabledAfterClearButtonIsClicked() {
		Category category = new Category("1", "Tech");
		BlogPost post = new BlogPost("1", "Title", "Content", "Author", DATE, category);

		GuiActionRunner.execute(() -> blogPostSwingView.getListBlogPostModel().addElement(post));

		window.list("BlogPostList").selectItem(0);
		window.button(JButtonMatcher.withText("Clear")).click();

		window.button(JButtonMatcher.withText("Update")).requireDisabled();
		window.button(JButtonMatcher.withText("Delete")).requireDisabled();
	}

	@Test
	@GUITest
	public void testButtonsShouldBeDisabledAfterBlogPostIsDeleted() {
		Category category = new Category("1", "Tech");
		BlogPost post = new BlogPost("1", "Title", "Content", "Author", DATE, category);

		GuiActionRunner.execute(() -> blogPostSwingView.getListBlogPostModel().addElement(post));

		window.list("BlogPostList").selectItem(0);

		GuiActionRunner.execute(() -> blogPostSwingView.deleteBlogPost(post));

		window.button(JButtonMatcher.withText("Update")).requireDisabled();
		window.button(JButtonMatcher.withText("Delete")).requireDisabled();
	}

	@Test
	@GUITest
	public void testClearButtonShouldResetAllTextFields() {
		addCategories();
		window.textBox("BlogPostIdTextBox").enterText("1");
		window.textBox("BlogPostTitleTextBox").enterText("T");
		window.textBox("BlogPostAuthorTextBox").enterText("A");
		window.textBox("BlogPostContentTextBox").enterText("C");
		window.comboBox("BlogPostCategoryComboBox").selectItem(0);
		window.textBox("BlogPostCreationDateTextBox").enterText(DATE);

		window.button(JButtonMatcher.withText("Clear")).click();

		window.textBox("BlogPostIdTextBox").requireEmpty();
		window.textBox("BlogPostTitleTextBox").requireEmpty();
		window.textBox("BlogPostAuthorTextBox").requireEmpty();
		window.textBox("BlogPostContentTextBox").requireEmpty();
		window.textBox("BlogPostCreationDateTextBox").requireEmpty();
	}

	@Test
	@GUITest
	public void testUpdateBlogPostFailureShouldShowErrorMessage() {
		BlogPost post = new BlogPost("1", "T", "C", "A", DATE, null);

		GuiActionRunner.execute(() -> blogPostSwingView.showErrorMessage("update error", post));

		window.label("errorMsg").requireText("update error: " + post);
	}

	@Test
	@GUITest
	public void testDeleteBlogPostFailureShouldShowErrorMessage() {
		BlogPost post = new BlogPost("1", "T", "C", "A", DATE, null);

		GuiActionRunner.execute(() -> blogPostSwingView.showErrorMessage("delete error", post));

		window.label("errorMsg").requireText("delete error: " + post);
	}

	@Test
	@GUITest
	public void testBlogPostListRendererWithNonBlogPostValue() {
		GuiActionRunner.execute(() -> {
			blogPostSwingView.getListBlogPostModel().addElement(null);
		});

		String[] contents = window.list("BlogPostList").contents();

		assertThat(contents).containsExactly("");
	}

	@Test
	@GUITest
	public void testUpdateBlogPostWhenIdNotFoundShouldNotModifyList() {
		Category category = new Category("1", "Tech");

		BlogPost existing = new BlogPost("1", "Old", "Old", "Old", DATE, category);
		BlogPost update = new BlogPost("2", "New", "New", "New", DATE, category);

		GuiActionRunner.execute(() -> {
			blogPostSwingView.getListBlogPostModel().addElement(existing);
			blogPostSwingView.updateBlogPost(update);
		});

		assertThat(window.list("BlogPostList").contents()).containsExactly(getDisplayString(existing));
	}

	@Test
	@GUITest
	public void testDeselectingBlogPostShouldClearTextFields() {
		Category category = new Category("1", "Tech");
		BlogPost post = new BlogPost("1", "T", "C", "A", DATE, category);

		GuiActionRunner.execute(() -> {
			blogPostSwingView.getListBlogPostModel().addElement(post);
		});

		window.list("BlogPostList").selectItem(0);
		window.list("BlogPostList").clearSelection();

		window.textBox("BlogPostIdTextBox").requireEmpty();
		window.textBox("BlogPostTitleTextBox").requireEmpty();
	}

	@Test
	@GUITest
	public void testEnsureCategoryExistsWhenOtherCategoryPresent() {
		Category existing = new Category("1", "Tech");
		Category missing = new Category("2", "Life");

		GuiActionRunner.execute(() -> {
			blogPostSwingView.getComboCategoriesModel().addElement(existing);
		});

		GuiActionRunner.execute(() -> {
			blogPostSwingView.ensureCategoryExists(missing);
		});

		assertThat(window.comboBox("BlogPostCategoryComboBox").contents()).containsExactly("Tech", "Life");
	}

}
