package com.example.blogmanager.view.swing;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import com.example.blogmanager.controller.CategoryController;
import com.example.blogmanager.model.Category;
import com.example.blogmanager.view.CategoryView;

public class CategorySwingView extends JFrame implements CategoryView {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField categoryId;
	private JTextField categoryName;
	private JScrollPane scrollPane;
	private JList<Category> list;
	private JButton addCatBtn;
	private JButton updateBtn;
	private JButton deleteBtn;
	private JButton clearBtn;
	private JButton blogpostBtn;
	private JLabel errorMsg;

	private CategoryController categoryController;

	private DefaultListModel<Category> listCategoryModel;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CategorySwingView frame = new CategorySwingView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void setCategoryController(CategoryController categoryController) {
		this.categoryController = categoryController;
	}

	DefaultListModel<Category> getListCategoryModel() {
		return listCategoryModel;
	}

	public CategorySwingView() {
		setTitle("Category View");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 536, 425);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 0, 0, 0, 0, 30, 30, 30, 30, 30 };
		gbl_contentPane.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 40, 0 };
		gbl_contentPane.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		JLabel lblNewLabel = new JLabel("Id");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		contentPane.add(lblNewLabel, gbc_lblNewLabel);

		categoryId = new JTextField();
		categoryId.setName("CategoryIdTextBox");
		GridBagConstraints gbc_categoryId = new GridBagConstraints();
		gbc_categoryId.gridwidth = 4;
		gbc_categoryId.fill = GridBagConstraints.HORIZONTAL;
		gbc_categoryId.insets = new Insets(0, 0, 5, 5);
		gbc_categoryId.gridx = 1;
		gbc_categoryId.gridy = 0;
		contentPane.add(categoryId, gbc_categoryId);
		categoryId.setColumns(10);
		categoryId.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent e) {
				updateAddCatButtonState();
			}
		});

		JLabel lblNewLabel_1 = new JLabel("Name");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 1;
		contentPane.add(lblNewLabel_1, gbc_lblNewLabel_1);

		categoryName = new JTextField();
		categoryName.setName("CategoryNameTextBox");
		GridBagConstraints gbc_categoryName = new GridBagConstraints();
		gbc_categoryName.gridwidth = 4;
		gbc_categoryName.insets = new Insets(0, 0, 5, 5);
		gbc_categoryName.fill = GridBagConstraints.HORIZONTAL;
		gbc_categoryName.gridx = 1;
		gbc_categoryName.gridy = 1;
		contentPane.add(categoryName, gbc_categoryName);
		categoryName.setColumns(10);
		categoryName.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyReleased(java.awt.event.KeyEvent e) {
				updateAddCatButtonState();
			}
		});

		addCatBtn = new JButton("Add Category");
		addCatBtn.setEnabled(false);
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton.gridx = 1;
		gbc_btnNewButton.gridy = 4;
		contentPane.add(addCatBtn, gbc_btnNewButton);
		addCatBtn.addActionListener(e -> {
			Category category = new Category(categoryId.getText(), categoryName.getText());
			new Thread(() -> categoryController.addCategory(category)).start();
		});

		clearBtn = new JButton("Clear");
		clearBtn.setEnabled(false);
		GridBagConstraints gbc_btnNewButton_3 = new GridBagConstraints();
		gbc_btnNewButton_3.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_3.gridx = 2;
		gbc_btnNewButton_3.gridy = 4;
		contentPane.add(clearBtn, gbc_btnNewButton_3);
		clearBtn.addActionListener(e -> {
			categoryId.setText("");
			categoryName.setText("");
			resetErrorLabel();

			updateBtn.setEnabled(false);
			deleteBtn.setEnabled(false);

			list.clearSelection();

			categoryId.setEnabled(true);
		});

		scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 8;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 5;
		contentPane.add(scrollPane, gbc_scrollPane);

		listCategoryModel = new DefaultListModel<>();
		list = new JList<>(listCategoryModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setName("CategoryList");
		scrollPane.setColumnHeaderView(list);
		list.addListSelectionListener(e -> {
			boolean selected = list.getSelectedIndex() != -1;
			updateBtn.setEnabled(selected);
			deleteBtn.setEnabled(selected);
			clearBtn.setEnabled(selected);

			if (selected) {
				Category category = list.getSelectedValue();
				categoryId.setText(category.getId());
				categoryName.setText(category.getName());

				categoryId.setEnabled(false);
				addCatBtn.setEnabled(false);
			} else {
				categoryId.setText("");
				categoryName.setText("");
				categoryId.setEnabled(true);
			}
			updateAddCatButtonState();
		});
		list.setCellRenderer(new DefaultListCellRenderer() {

			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value instanceof Category) {
					Category category = (Category) value;
					setText(getDisplayString(category));
				}
				return this;
			}
		});

		updateBtn = new JButton("Update");
		updateBtn.setEnabled(false);
		GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
		gbc_btnNewButton_1.fill = GridBagConstraints.BOTH;
		gbc_btnNewButton_1.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_1.gridx = 1;
		gbc_btnNewButton_1.gridy = 7;
		contentPane.add(updateBtn, gbc_btnNewButton_1);
		updateBtn.addActionListener(e -> {
			Category selected = list.getSelectedValue();
			if (selected == null)
				return;

			Category updated = new Category(selected.getId(), categoryName.getText());
			new Thread(() -> categoryController.updateCategory(updated)).start();
		});

		deleteBtn = new JButton("Delete");
		deleteBtn.setEnabled(false);
		GridBagConstraints gbc_btnNewButton_2 = new GridBagConstraints();
		gbc_btnNewButton_2.fill = GridBagConstraints.BOTH;
		gbc_btnNewButton_2.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_2.gridx = 2;
		gbc_btnNewButton_2.gridy = 7;
		contentPane.add(deleteBtn, gbc_btnNewButton_2);
		deleteBtn.addActionListener(e -> {
			Category selected = list.getSelectedValue();
			if (selected != null) {
				new Thread(() -> categoryController.deleteCategory(selected)).start();
			}
		});

		blogpostBtn = new JButton("BlogPosts");
		GridBagConstraints gbc_btnNewButton_4 = new GridBagConstraints();
		gbc_btnNewButton_4.anchor = GridBagConstraints.WEST;
		gbc_btnNewButton_4.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_4.gridx = 3;
		gbc_btnNewButton_4.gridy = 7;
		contentPane.add(blogpostBtn, gbc_btnNewButton_4);

		errorMsg = new JLabel("");
		errorMsg.setName("errorMsg");
		GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
		gbc_lblNewLabel_3.fill = GridBagConstraints.VERTICAL;
		gbc_lblNewLabel_3.gridwidth = 9;
		gbc_lblNewLabel_3.insets = new Insets(0, 0, 0, 5);
		gbc_lblNewLabel_3.gridx = 0;
		gbc_lblNewLabel_3.gridy = 8;
		contentPane.add(errorMsg, gbc_lblNewLabel_3);

	}

	private void updateAddCatButtonState() {
		boolean enabled = !categoryId.getText().trim().isEmpty() && !categoryName.getText().trim().isEmpty();
		addCatBtn.setEnabled(enabled);
		clearBtn.setEnabled(enabled);
	}

	@Override
	public void showErrorMessage(String message, Category category) {
		SwingUtilities.invokeLater(() -> errorMsg.setText(message + ": " + category));
	}

	private String getDisplayString(Category category) {
		return category.getId() + " - " + category.getName();
	}

	private void resetErrorLabel() {
		SwingUtilities.invokeLater(() -> errorMsg.setText(""));
	}

	@Override
	public void displayCategories(List<Category> categories) {
		SwingUtilities.invokeLater(() -> {
			listCategoryModel.clear();
			categories.forEach(listCategoryModel::addElement);
		});
	}

	@Override
	public void addCategory(Category category) {
		SwingUtilities.invokeLater(() -> {
			listCategoryModel.addElement(category);
			errorMsg.setText("");
		});
	}

	@Override
	public void deleteCategory(Category category) {
		SwingUtilities.invokeLater(() -> {
			listCategoryModel.removeElement(category);
			resetErrorLabel();
		});
	}

	@Override
	public void updateCategory(Category category) {
		SwingUtilities.invokeLater(() -> {
			for (int i = 0; i < listCategoryModel.size(); i++) {
				if (listCategoryModel.get(i).getId().equals(category.getId())) {
					listCategoryModel.set(i, category);
					break;
				}
			}
			resetErrorLabel();
		});
	}

}
