package com.example.blogmanager.view.swing;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import com.example.blogmanager.controller.BlogPostController;
import com.example.blogmanager.model.BlogPost;
import com.example.blogmanager.model.Category;
import com.example.blogmanager.view.BlogPostView;

public class BlogPostSwingView extends JFrame implements BlogPostView {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField BlogPostTxtId;
	private JTextField BlogPostTxtTitle;
	private JLabel lblNewLabel_2;
	private JTextField BlogPostTxtAuthor;
	private JLabel lblNewLabel_3;
	private JTextField BlogPostTxtContent;
	private JLabel lblNewLabel_4;
	private JLabel lblNewLabel_5;
	private JTextField BlogPostTxtCreationDate;
	private JButton btnNewButton;
	private JButton createBtn;
	private JButton updateBtn;
	private JButton deleteBtn;
	private JButton clearBtn;
	private JList<BlogPost> list_1;
	private JScrollPane scrollPane;
	private JComboBox<Category> categryBox;
	private JLabel errorMsg;

	CategorySwingView categoryView;

	private DefaultComboBoxModel<Category> comboBoxCategoriesModel;

	private BlogPostController blogPostController;

	private DefaultListModel<BlogPost> listBlogPostModel;

	public void setBlogPostController(BlogPostController blogPostController) {
		this.blogPostController = blogPostController;
	}

	public void setCategoryView(CategorySwingView categoryView) {
		this.categoryView = categoryView;
	}

	DefaultListModel<BlogPost> getListBlogPostModel() {
		return listBlogPostModel;
	}

	DefaultComboBoxModel<Category> getComboCategoriesModel() {
		return comboBoxCategoriesModel;
	}

	public BlogPostSwingView() {

		setTitle("BlogPost View");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 620, 525);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 0, 0, 0, 30, 30, 30, 30, 30, 30, 30, 0, 0, 0, 0 };
		gbl_contentPane.rowHeights = new int[] { 19, 0, 0, 0, 0, 0, 0, 226, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				0.0 };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 1.0 };
		contentPane.setLayout(gbl_contentPane);

		JLabel lblNewLabel = new JLabel("id");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel.fill = GridBagConstraints.VERTICAL;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		contentPane.add(lblNewLabel, gbc_lblNewLabel);

		BlogPostTxtId = new JTextField();
		BlogPostTxtId.setName("BlogPostIdTextBox");
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.fill = GridBagConstraints.BOTH;
		gbc_textField.gridwidth = 3;
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 0;
		contentPane.add(BlogPostTxtId, gbc_textField);
		BlogPostTxtId.setColumns(10);
		BlogPostTxtId.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent e) {
				updateCreateButtonState();
			}
		});

		JLabel lblNewLabel_1 = new JLabel("title");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 1;
		contentPane.add(lblNewLabel_1, gbc_lblNewLabel_1);

		BlogPostTxtTitle = new JTextField();
		BlogPostTxtTitle.setName("BlogPostTitleTextBox");
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.fill = GridBagConstraints.BOTH;
		gbc_textField_1.gridwidth = 3;
		gbc_textField_1.insets = new Insets(0, 0, 5, 5);
		gbc_textField_1.gridx = 1;
		gbc_textField_1.gridy = 1;
		contentPane.add(BlogPostTxtTitle, gbc_textField_1);
		BlogPostTxtTitle.setColumns(10);
		BlogPostTxtTitle.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent e) {
				updateCreateButtonState();
			}
		});

		lblNewLabel_2 = new JLabel("author");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_2.gridx = 0;
		gbc_lblNewLabel_2.gridy = 2;
		contentPane.add(lblNewLabel_2, gbc_lblNewLabel_2);

		BlogPostTxtAuthor = new JTextField();
		BlogPostTxtAuthor.setName("BlogPostAuthorTextBox");
		GridBagConstraints gbc_textField_2 = new GridBagConstraints();
		gbc_textField_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_2.gridwidth = 3;
		gbc_textField_2.insets = new Insets(0, 0, 5, 5);
		gbc_textField_2.gridx = 1;
		gbc_textField_2.gridy = 2;
		contentPane.add(BlogPostTxtAuthor, gbc_textField_2);
		BlogPostTxtAuthor.setColumns(10);
		BlogPostTxtAuthor.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent e) {
				updateCreateButtonState();
			}
		});

		lblNewLabel_3 = new JLabel("content");
		GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
		gbc_lblNewLabel_3.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_3.gridx = 0;
		gbc_lblNewLabel_3.gridy = 3;
		contentPane.add(lblNewLabel_3, gbc_lblNewLabel_3);

		BlogPostTxtContent = new JTextField();
		BlogPostTxtContent.setName("BlogPostContentTextBox");
		GridBagConstraints gbc_textField_3 = new GridBagConstraints();
		gbc_textField_3.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_3.gridwidth = 3;
		gbc_textField_3.insets = new Insets(0, 0, 5, 5);
		gbc_textField_3.gridx = 1;
		gbc_textField_3.gridy = 3;
		contentPane.add(BlogPostTxtContent, gbc_textField_3);
		BlogPostTxtContent.setColumns(10);
		BlogPostTxtContent.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent e) {
				updateCreateButtonState();
			}
		});

		lblNewLabel_4 = new JLabel("category");
		GridBagConstraints gbc_lblNewLabel_4 = new GridBagConstraints();
		gbc_lblNewLabel_4.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_4.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_4.gridx = 0;
		gbc_lblNewLabel_4.gridy = 4;
		contentPane.add(lblNewLabel_4, gbc_lblNewLabel_4);

		comboBoxCategoriesModel = new DefaultComboBoxModel<>();
		categryBox = new JComboBox<Category>(comboBoxCategoriesModel);
		categryBox.setModel(comboBoxCategoriesModel);
		categryBox.setName("BlogPostCategoryComboBox");
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.gridwidth = 3;
		gbc_comboBox.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 1;
		gbc_comboBox.gridy = 4;
		contentPane.add(categryBox, gbc_comboBox);
		categryBox.addActionListener(e -> updateCreateButtonState());
		categryBox.setRenderer(new DefaultListCellRenderer() {

			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value instanceof Category) {
					Category category = (Category) value;
					setText(category.getName());
				}
				return this;
			}
		});

		lblNewLabel_5 = new JLabel("creationDate");
		GridBagConstraints gbc_lblNewLabel_5 = new GridBagConstraints();
		gbc_lblNewLabel_5.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_5.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_5.gridx = 0;
		gbc_lblNewLabel_5.gridy = 5;
		contentPane.add(lblNewLabel_5, gbc_lblNewLabel_5);

		BlogPostTxtCreationDate = new JTextField();
		BlogPostTxtCreationDate.setName("BlogPostCreationDateTextBox");
		GridBagConstraints gbc_textField_5 = new GridBagConstraints();
		gbc_textField_5.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_5.gridwidth = 3;
		gbc_textField_5.insets = new Insets(0, 0, 5, 5);
		gbc_textField_5.gridx = 1;
		gbc_textField_5.gridy = 5;
		contentPane.add(BlogPostTxtCreationDate, gbc_textField_5);
		BlogPostTxtCreationDate.setColumns(10);
		BlogPostTxtCreationDate.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent e) {
				updateCreateButtonState();
			}
		});

		createBtn = new JButton("Create");
		createBtn.setEnabled(false);
		GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
		gbc_btnNewButton_1.anchor = GridBagConstraints.WEST;
		gbc_btnNewButton_1.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_1.gridx = 0;
		gbc_btnNewButton_1.gridy = 6;
		contentPane.add(createBtn, gbc_btnNewButton_1);
		createBtn.addActionListener(e -> {
			BlogPost post = new BlogPost(BlogPostTxtId.getText(), BlogPostTxtTitle.getText(),
					BlogPostTxtContent.getText(), BlogPostTxtAuthor.getText(), BlogPostTxtCreationDate.getText(),
					(Category) categryBox.getSelectedItem());
			new Thread(() -> blogPostController.addBlogPost(post)).start();
		});

		clearBtn = new JButton("Clear");
		clearBtn.setEnabled(false);
		GridBagConstraints gbc_btnNewButton_4 = new GridBagConstraints();
		gbc_btnNewButton_4.anchor = GridBagConstraints.NORTHWEST;
		gbc_btnNewButton_4.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_4.gridx = 1;
		gbc_btnNewButton_4.gridy = 6;
		contentPane.add(clearBtn, gbc_btnNewButton_4);
		clearBtn.addActionListener(e -> {
			BlogPostTxtId.setText("");
			BlogPostTxtTitle.setText("");
			BlogPostTxtAuthor.setText("");
			BlogPostTxtContent.setText("");
			BlogPostTxtCreationDate.setText("");
			categryBox.setSelectedItem(null);

			updateBtn.setEnabled(false);
			deleteBtn.setEnabled(false);

			list_1.clearSelection();
			resetErrorLabel();
			BlogPostTxtId.setEnabled(true);
		});

		scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridwidth = 14;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 7;
		contentPane.add(scrollPane, gbc_scrollPane);

		listBlogPostModel = new DefaultListModel<>();
		list_1 = new JList<>(listBlogPostModel);
		scrollPane.setViewportView(list_1);
		list_1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list_1.setName("BlogPostList");
		list_1.addListSelectionListener(e -> {
			boolean selected = list_1.getSelectedIndex() != -1;
			updateBtn.setEnabled(selected);
			deleteBtn.setEnabled(selected);
			clearBtn.setEnabled(selected);

			if (selected) {
				BlogPost post = list_1.getSelectedValue();
				BlogPostTxtId.setText(post.getId());
				BlogPostTxtTitle.setText(post.getTitle());
				BlogPostTxtAuthor.setText(post.getAuthor());
				BlogPostTxtContent.setText(post.getContent());
				BlogPostTxtCreationDate.setText(post.getCreationDate());

				Category postCategory = post.getCategory();
				ensureCategoryExists(postCategory);
				categryBox.setSelectedItem(postCategory);

				BlogPostTxtId.setEnabled(false);
				createBtn.setEnabled(false);
			} else {
				BlogPostTxtId.setText("");
				BlogPostTxtTitle.setText("");
				BlogPostTxtAuthor.setText("");
				BlogPostTxtContent.setText("");
				BlogPostTxtCreationDate.setText("");

				BlogPostTxtId.setEnabled(true);

			}
		});
		list_1.setCellRenderer(new DefaultListCellRenderer() {

			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value instanceof BlogPost) {
					BlogPost blogPost = (BlogPost) value;
					setText(getDisplayString(blogPost));
				}
				return this;
			}
		});

		deleteBtn = new JButton("Delete");
		deleteBtn.setEnabled(false);
		deleteBtn.setName("deleteBtn");
		GridBagConstraints gbc_btnNewButton_3 = new GridBagConstraints();
		gbc_btnNewButton_3.anchor = GridBagConstraints.WEST;
		gbc_btnNewButton_3.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_3.gridx = 0;
		gbc_btnNewButton_3.gridy = 8;
		contentPane.add(deleteBtn, gbc_btnNewButton_3);
		deleteBtn.addActionListener(e -> {
			BlogPost selected = list_1.getSelectedValue();
			new Thread(() -> blogPostController.deleteBlogPost(selected)).start();
		});

		updateBtn = new JButton("Update");
		updateBtn.setEnabled(false);
		updateBtn.setName("");
		GridBagConstraints gbc_btnNewButton_2 = new GridBagConstraints();
		gbc_btnNewButton_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton_2.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_2.gridx = 1;
		gbc_btnNewButton_2.gridy = 8;
		contentPane.add(updateBtn, gbc_btnNewButton_2);
		updateBtn.addActionListener(e -> {
			BlogPost selected = list_1.getSelectedValue();
			BlogPost updated = new BlogPost(selected.getId(), BlogPostTxtTitle.getText(), BlogPostTxtContent.getText(),
					BlogPostTxtAuthor.getText(), BlogPostTxtCreationDate.getText(),
					(Category) categryBox.getSelectedItem());
			new Thread(() -> blogPostController.updateBlogPost(updated)).start();
		});

		btnNewButton = new JButton("Category");
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton.anchor = GridBagConstraints.ABOVE_BASELINE;
		gbc_btnNewButton.gridx = 2;
		gbc_btnNewButton.gridy = 8;
		contentPane.add(btnNewButton, gbc_btnNewButton);
		btnNewButton.addActionListener(e -> {
			categoryView.setVisible(true);
			this.setVisible(false);
			resetErrorLabel();
		});

		errorMsg = new JLabel("");
		errorMsg.setName("errorMsg");
		GridBagConstraints gbc_lblNewLabel_6 = new GridBagConstraints();
		gbc_lblNewLabel_6.insets = new Insets(0, 0, 0, 5);
		gbc_lblNewLabel_6.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblNewLabel_6.gridwidth = 4;
		gbc_lblNewLabel_6.gridx = 0;
		gbc_lblNewLabel_6.gridy = 9;
		contentPane.add(errorMsg, gbc_lblNewLabel_6);

	}

	private void updateCreateButtonState() {
		boolean enabled = !BlogPostTxtId.getText().trim().isEmpty() && !BlogPostTxtTitle.getText().trim().isEmpty()
				&& !BlogPostTxtAuthor.getText().trim().isEmpty() && !BlogPostTxtContent.getText().trim().isEmpty()
				&& !BlogPostTxtCreationDate.getText().trim().isEmpty() && categryBox.getSelectedItem() != null;
		createBtn.setEnabled(enabled);
		clearBtn.setEnabled(enabled);
	}

	public void showErrorMessage(String message, BlogPost blogPost) {
		SwingUtilities.invokeLater(() -> errorMsg.setText(message + ": " + blogPost));
	}

	public void updateCategories(List<Category> categories) {
		SwingUtilities.invokeLater(() -> {
			comboBoxCategoriesModel.removeAllElements();
			for (Category c : categories) {
				comboBoxCategoriesModel.addElement(c);
			}
			updateCreateButtonState();
		});
	}

	void ensureCategoryExists(Category category) {
		for (int i = 0; i < comboBoxCategoriesModel.getSize(); i++) {
			if (comboBoxCategoriesModel.getElementAt(i).equals(category)) {
				return;
			}
		}
		comboBoxCategoriesModel.addElement(category);
	}

	String getDisplayString(BlogPost blogPost) {
		return blogPost.getId() + " - " + blogPost.getTitle() + " - " + blogPost.getAuthor() + " - "
				+ blogPost.getCreationDate() + " - " + blogPost.getContent() + " - "
				+ (blogPost.getCategory() != null ? blogPost.getCategory().getName() : "");
	}

	private void resetErrorLabel() {
		SwingUtilities.invokeLater(() -> errorMsg.setText(""));
	}

	@Override
	public void displayBlogPosts(List<BlogPost> blogPosts) {
		SwingUtilities.invokeLater(() -> {
			listBlogPostModel.clear();
			blogPosts.forEach(listBlogPostModel::addElement);
		});
	}

	@Override
	public void addBlogPost(BlogPost blogPost) {
		javax.swing.SwingUtilities.invokeLater(() -> {
			listBlogPostModel.addElement(blogPost);
			resetErrorLabel();
		});
	}

	@Override
	public void deleteBlogPost(BlogPost blogPost) {
		SwingUtilities.invokeLater(() -> {
			listBlogPostModel.removeElement(blogPost);
			resetErrorLabel();
		});
	}

	@Override
	public void updateBlogPost(BlogPost newPost) {
		SwingUtilities.invokeLater(() -> {
			for (int i = 0; i < listBlogPostModel.size(); i++) {
				if (listBlogPostModel.get(i).getId().equals(newPost.getId())) {
					listBlogPostModel.set(i, newPost);
					break;
				}
			}
			resetErrorLabel();
		});
	}

	public void refreshCategories() {
		blogPostController.loadCategories();
	}

}
