package com.example.blogmanager.view.swing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.swing.DefaultListModel;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JTextComponentFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.blogmanager.controller.CategoryController;
import com.example.blogmanager.model.Category;

@RunWith(GUITestRunner.class)
public class CategorySwingViewTest extends AssertJSwingJUnitTestCase {

	private FrameFixture window;
	private CategorySwingView categorySwingView;

	@Mock
	private CategoryController categoryController;

	private AutoCloseable closeable;

	@Override
	protected void onSetUp() throws Exception {
		closeable = MockitoAnnotations.openMocks(this);
		GuiActionRunner.execute(() -> {
			categorySwingView = new CategorySwingView();
			categorySwingView.setCategoryController(categoryController);
			return categorySwingView;
		});
		window = new FrameFixture(robot(), categorySwingView);
		window.show();
	}

	@Override
	protected void onTearDown() throws Exception {
		closeable.close();
	}

	@Test
	@GUITest
	public void testInitialState() {
		window.textBox("CategoryIdTextBox").requireEmpty();
		window.textBox("CategoryNameTextBox").requireEmpty();
		window.button(JButtonMatcher.withText("Add Category")).requireDisabled();
		window.button(JButtonMatcher.withText("Update")).requireDisabled();
		window.button(JButtonMatcher.withText("Delete")).requireDisabled();
		window.button(JButtonMatcher.withText("Clear")).requireDisabled();

		window.label("errorMsg").requireText("");
	}

	@Test
	@GUITest
	public void testAddCategoryButtonDisabledWhenAnyFieldIsBlank() {
		JTextComponentFixture id = window.textBox("CategoryIdTextBox");
		JTextComponentFixture name = window.textBox("CategoryNameTextBox");

		id.enterText(" ");
		name.enterText("Technology");
		window.button(JButtonMatcher.withText("Add Category")).requireDisabled();

		id.setText("");
		name.setText("");

		id.enterText("1");
		name.enterText(" ");
		window.button(JButtonMatcher.withText("Add Category")).requireDisabled();

		id.setText("");
		name.setText("");

	}

	@Test
	public void testAddCategoryButtonShouldDelegateToController() {
		window.textBox("CategoryIdTextBox").enterText("1");
		window.textBox("CategoryNameTextBox").enterText("Technology");

		window.button(JButtonMatcher.withText("Add Category")).click();

		await().atMost(5, TimeUnit.SECONDS)
				.untilAsserted(() -> verify(categoryController).addCategory(new Category("1", "Technology")));
	}

	@Test
	public void testCategoryAddedShouldAddCategoryToListAndResetErrorLabel() {
		Category category = new Category("1", "Tech");

		categorySwingView.addCategory(category);

		assertThat(window.list("CategoryList").contents()).containsExactly(getDisplayString(category));

		window.label("errorMsg").requireText("");
	}

	@Test
	public void testShowErrorMessageShouldUpdateErrorLabel() {
		Category category = new Category("1", "Technology");

		GuiActionRunner.execute(() -> categorySwingView.showErrorMessage("error message", category));

		window.label("errorMsg").requireText("error message: " + category);
	}

	@Test
	public void testUpdateButtonShouldDelegateToController() {
		Category category = new Category("1", "Old");

		GuiActionRunner.execute(() -> {
			DefaultListModel<Category> model = categorySwingView.getListCategoryModel();
			model.addElement(category);
		});

		window.list("CategoryList").selectItem(0);
		window.textBox("CategoryNameTextBox").setText("New");

		window.button(JButtonMatcher.withText("Update")).click();

		await().atMost(5, TimeUnit.SECONDS)
				.untilAsserted(() -> verify(categoryController).updateCategory(new Category("1", "New")));
	}

	@Test
	@GUITest
	public void testDisplayCategoriesShouldPopulateList() {
		Category category = new Category("1", "Tech");

		GuiActionRunner.execute(() -> categorySwingView.displayCategories(Arrays.asList(category)));
		assertThat(window.list("CategoryList").contents()).containsExactly(getDisplayString(category));
	}

	@Test
	@GUITest
	public void testDisplayCategoriesWithEmptyListShouldClearList() {
		Category category = new Category("1", "Tech");

		GuiActionRunner.execute(() -> {
			categorySwingView.displayCategories(Arrays.asList(category));
			categorySwingView.displayCategories(Arrays.asList());
		});

		window.list("CategoryList").requireItemCount(0);
	}

	@Test
	public void testDeleteButtonShouldDelegateToController() {
		Category category = new Category("1", "Tech");

		GuiActionRunner.execute(() -> categorySwingView.getListCategoryModel().addElement(category));

		window.list("CategoryList").selectItem(0);
		window.button(JButtonMatcher.withText("Delete")).click();

		await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> verify(categoryController).deleteCategory(category));
	}

	private String getDisplayString(Category category) {
		return category.getId() + " - " + category.getName();
	}

	@Test
	@GUITest
	public void testMultipleCategoriesShouldBeDisplayedInList() {
		Category c1 = new Category("1", "Tech");
		Category c2 = new Category("2", "Science");

		GuiActionRunner.execute(() -> {
			categorySwingView.getListCategoryModel().addElement(c1);
			categorySwingView.getListCategoryModel().addElement(c2);
		});

		String[] listContents = window.list().contents();
		assertThat(listContents).containsExactly(getDisplayString(c1), getDisplayString(c2));
	}

	@Test
	@GUITest
	public void testSelectingCategoryFromListShouldPopulateTextFields() {
		Category category = new Category("1", "Tech");

		GuiActionRunner.execute(() -> categorySwingView.getListCategoryModel().addElement(category));

		window.list("CategoryList").selectItem(0);

		window.textBox("CategoryIdTextBox").requireText("1");
		window.textBox("CategoryNameTextBox").requireText("Tech");
	}

	@Test
	@GUITest
	public void testCategoryUpdatedShouldUpdateCategoryInList() {
		Category category = new Category("1", "Old");

		GuiActionRunner.execute(() -> categorySwingView.getListCategoryModel().addElement(category));

		window.list("CategoryList").selectItem(0);
		window.textBox("CategoryNameTextBox").setText("New");

		GuiActionRunner.execute(() -> categorySwingView.updateCategory(new Category("1", "New")));

		window.list("CategoryList").requireSelection("1 - New");
	}

	@Test
	@GUITest
	public void testUpdateButtonShouldBeDisabledWhenNoCategoryIsSelected() {
		window.button(JButtonMatcher.withText("Update")).requireDisabled();
	}

	@Test
	@GUITest
	public void testCategoryDeletedShouldRemoveCategoryFromList() {
		Category category = new Category("1", "Tech");

		GuiActionRunner.execute(() -> categorySwingView.getListCategoryModel().addElement(category));

		window.list("CategoryList").selectItem(0);

		GuiActionRunner.execute(() -> categorySwingView.deleteCategory(category));

		window.list("CategoryList").requireItemCount(0);
	}

	@Test
	@GUITest
	public void testDeleteButtonShouldBeDisabledWhenNoCategoryIsSelected() {
		window.button(JButtonMatcher.withText("Delete")).requireDisabled();
	}

	@Test
	@GUITest
	public void testDeletingLastCategoryShouldClearListAndDisableButtons() {
		Category category = new Category("1", "Tech");

		GuiActionRunner.execute(() -> categorySwingView.getListCategoryModel().addElement(category));

		window.list("CategoryList").selectItem(0);

		GuiActionRunner.execute(() -> categorySwingView.deleteCategory(category));

		window.list("CategoryList").requireItemCount(0);
		window.button(JButtonMatcher.withText("Update")).requireDisabled();
		window.button(JButtonMatcher.withText("Delete")).requireDisabled();
	}

	@Test
	@GUITest
	public void testButtonsShouldBeEnabledWhenCategoryIsSelected() {
		Category category = new Category("1", "Tech");

		GuiActionRunner.execute(() -> categorySwingView.getListCategoryModel().addElement(category));

		window.list("CategoryList").selectItem(0);

		window.button(JButtonMatcher.withText("Update")).requireEnabled();
		window.button(JButtonMatcher.withText("Delete")).requireEnabled();
		window.button(JButtonMatcher.withText("Clear")).requireEnabled();
	}

	@Test
	@GUITest
	public void testButtonsShouldBeDisabledAfterClearButtonIsClicked() {
		Category category = new Category("1", "Tech");

		GuiActionRunner.execute(() -> categorySwingView.getListCategoryModel().addElement(category));

		window.list("CategoryList").selectItem(0);
		window.button(JButtonMatcher.withText("Clear")).click();

		window.button(JButtonMatcher.withText("Update")).requireDisabled();
		window.button(JButtonMatcher.withText("Delete")).requireDisabled();
	}

	@Test
	@GUITest
	public void testClearButtonShouldResetTextFields() {
		window.textBox("CategoryIdTextBox").enterText("1");
		window.textBox("CategoryNameTextBox").enterText("Tech");

		window.button(JButtonMatcher.withText("Clear")).click();

		window.textBox("CategoryIdTextBox").requireEmpty();
		window.textBox("CategoryNameTextBox").requireEmpty();
	}

	@Test
	@GUITest
	public void testUpdateCategoryFailureShouldShowErrorMessage() {
		Category category = new Category("1", "Tech");

		GuiActionRunner.execute(() -> categorySwingView.showErrorMessage("update error", category));

		window.label("errorMsg").requireText("update error: " + category);
	}

	@Test
	@GUITest
	public void testDeleteCategoryFailureShouldShowErrorMessage() {
		Category category = new Category("1", "Tech");

		GuiActionRunner.execute(() -> categorySwingView.showErrorMessage("delete error", category));

		window.label("errorMsg").requireText("delete error: " + category);
	}

	@Test
	@GUITest
	public void testUpdateCategoryWithNoSelectionDoesNothing() {
		Category category = new Category("1", "Tech");

		GuiActionRunner.execute(() -> categorySwingView.updateCategory(category));

		assertThat(window.list("CategoryList").contents()).isEmpty();
	}

	@Test
	@GUITest
	public void testCategoryRendererWithNullValue() {
		GuiActionRunner.execute(() -> {
			categorySwingView.getListCategoryModel().addElement(null);
		});

		String[] contents = window.list("CategoryList").contents();

		assertThat(contents).containsExactly("");
	}

	@Test
	@GUITest
	public void testUpdateCategoryWhenIdNotFoundShouldNotModifyList() {
		Category existing = new Category("1", "Tech");
		Category update = new Category("2", "Life");

		GuiActionRunner.execute(() -> {
			categorySwingView.getListCategoryModel().addElement(existing);
			categorySwingView.updateCategory(update);
		});

		assertThat(window.list("CategoryList").contents()).containsExactly("1 - Tech");
	}

	@Test
	@GUITest
	public void testDeselectingCategoryShouldDisableButtons() {
		Category category = new Category("1", "Tech");

		GuiActionRunner.execute(() -> {
			categorySwingView.getListCategoryModel().addElement(category);
		});

		window.list("CategoryList").selectItem(0);
		window.list("CategoryList").clearSelection();

		window.button(JButtonMatcher.withText("Update")).requireDisabled();
		window.button(JButtonMatcher.withText("Delete")).requireDisabled();
	}

}
