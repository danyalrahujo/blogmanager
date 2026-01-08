package com.example.blogmanager.view.swing;

import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GUITestRunner.class)
public class CategorySwingViewTest extends AssertJSwingJUnitTestCase {

	private FrameFixture window;
	private CategorySwingView categorySwingView;

	@Override
	protected void onSetUp() {
		categorySwingView = GuiActionRunner.execute(() -> new CategorySwingView());
		window = new FrameFixture(robot(), categorySwingView);
		window.show();
	}

	@Test
	public void testInitialState() {
		window.textBox("CategoryIdTextBox").requireEmpty();
		window.textBox("CategoryNameTextBox").requireEmpty();
		window.textBox("CategoryViewCategoryTextBox").requireEmpty();

		window.button(JButtonMatcher.withText("Add Category")).requireDisabled();
		window.button(JButtonMatcher.withText("Update")).requireDisabled();
		window.button(JButtonMatcher.withText("Delete")).requireDisabled();
		window.button(JButtonMatcher.withText("Clear")).requireDisabled();
	}

}
