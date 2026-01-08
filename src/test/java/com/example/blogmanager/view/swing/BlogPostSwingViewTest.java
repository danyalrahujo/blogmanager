package com.example.blogmanager.view.swing;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JButtonFixture;
import org.assertj.swing.fixture.JTextComponentFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GUITestRunner.class)
public class BlogPostSwingViewTest extends AssertJSwingJUnitTestCase {

	private FrameFixture window;
	private BlogPostSwingView blogPostSwingView;

	@Override
	protected void onSetUp() {
		blogPostSwingView = GuiActionRunner.execute(() -> new BlogPostSwingView());
		window = new FrameFixture(robot(), blogPostSwingView);
		window.show();
	}

	@Test
	public void testInitialState() {
		window.button(JButtonMatcher.withText("Create")).requireDisabled();
		window.button(JButtonMatcher.withText("Update")).requireDisabled();
		window.button(JButtonMatcher.withText("Delete")).requireDisabled();
		window.button(JButtonMatcher.withText("Clear")).requireDisabled();

		window.textBox("BlogPostIdTextBox").requireEmpty();
		window.textBox("BlogPostTitleTextBox").requireEmpty();
		window.textBox("BlogPostAuthorTextBox").requireEmpty();
		window.textBox("BlogPostContentTextBox").requireEmpty();
		window.textBox("BlogPostCreationDateTextBox").requireEmpty();
	}

	@Test
	public void testCreateButtonShouldRemainDisabledWhenAnyFieldIsEmpty() {
		window.textBox("BlogPostIdTextBox").enterText("1");
		window.textBox("BlogPostTitleTextBox").enterText("Title");
		window.textBox("BlogPostAuthorTextBox").enterText("Author");
		window.textBox("BlogPostContentTextBox").enterText("Content");

		window.button(JButtonMatcher.withText("Create")).requireDisabled();
	}


}
