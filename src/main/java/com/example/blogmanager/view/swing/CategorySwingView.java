	package com.example.blogmanager.view.swing;
	
	import java.awt.EventQueue;
	
	import javax.swing.JFrame;
	import javax.swing.JPanel;
	import javax.swing.border.EmptyBorder;
	import java.awt.GridLayout;
	import java.awt.GridBagLayout;
	import javax.swing.JLabel;
	import java.awt.GridBagConstraints;
	import javax.swing.JTextField;
	import java.awt.Insets;
	import javax.swing.JScrollPane;
	import javax.swing.JList;
	import javax.swing.ListSelectionModel;
	import javax.swing.JButton;
	
	public class CategorySwingView extends JFrame {
	
		private static final long serialVersionUID = 1L;
		private JPanel contentPane;
		private JTextField textField;
		private JTextField textField_1;
		private JTextField textField_2;
		private JScrollPane scrollPane;
		private JList list;
		private JButton addCatBtn;
		private JButton updateBtn;
		private JButton deleteBtn;
		private JButton clearBtn;
		private JButton blogpostBtn;
		private JLabel errorMsg;
	
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
	
			textField = new JTextField();
			textField.setName("CategoryIdTextBox");
			GridBagConstraints gbc_textField = new GridBagConstraints();
			gbc_textField.gridwidth = 4;
			gbc_textField.fill = GridBagConstraints.HORIZONTAL;
			gbc_textField.insets = new Insets(0, 0, 5, 5);
			gbc_textField.gridx = 1;
			gbc_textField.gridy = 0;
			contentPane.add(textField, gbc_textField);
			textField.setColumns(10);
	
			JLabel lblNewLabel_1 = new JLabel("Name");
			GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
			gbc_lblNewLabel_1.anchor = GridBagConstraints.WEST;
			gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
			gbc_lblNewLabel_1.gridx = 0;
			gbc_lblNewLabel_1.gridy = 1;
			contentPane.add(lblNewLabel_1, gbc_lblNewLabel_1);
	
			textField_1 = new JTextField();
			textField_1.setName("CategoryNameTextBox");
			GridBagConstraints gbc_textField_1 = new GridBagConstraints();
			gbc_textField_1.gridwidth = 4;
			gbc_textField_1.insets = new Insets(0, 0, 5, 5);
			gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
			gbc_textField_1.gridx = 1;
			gbc_textField_1.gridy = 1;
			contentPane.add(textField_1, gbc_textField_1);
			textField_1.setColumns(10);
	
			JLabel lblNewLabel_2 = new JLabel("Category");
			GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
			gbc_lblNewLabel_2.anchor = GridBagConstraints.WEST;
			gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
			gbc_lblNewLabel_2.gridx = 0;
			gbc_lblNewLabel_2.gridy = 2;
			contentPane.add(lblNewLabel_2, gbc_lblNewLabel_2);
	
			textField_2 = new JTextField();
			textField_2.setName("CategoryViewCategoryTextBox");
			GridBagConstraints gbc_textField_2 = new GridBagConstraints();
			gbc_textField_2.gridwidth = 4;
			gbc_textField_2.insets = new Insets(0, 0, 5, 5);
			gbc_textField_2.fill = GridBagConstraints.HORIZONTAL;
			gbc_textField_2.gridx = 1;
			gbc_textField_2.gridy = 2;
			contentPane.add(textField_2, gbc_textField_2);
			textField_2.setColumns(10);
	
			addCatBtn = new JButton("Add Category");
			GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
			gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
			gbc_btnNewButton.gridx = 1;
			gbc_btnNewButton.gridy = 4;
			contentPane.add(addCatBtn, gbc_btnNewButton);
	
			clearBtn = new JButton("Clear");
			GridBagConstraints gbc_btnNewButton_3 = new GridBagConstraints();
			gbc_btnNewButton_3.insets = new Insets(0, 0, 5, 5);
			gbc_btnNewButton_3.gridx = 2;
			gbc_btnNewButton_3.gridy = 4;
			contentPane.add(clearBtn, gbc_btnNewButton_3);
	
			scrollPane = new JScrollPane();
			GridBagConstraints gbc_scrollPane = new GridBagConstraints();
			gbc_scrollPane.gridwidth = 8;
			gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
			gbc_scrollPane.fill = GridBagConstraints.BOTH;
			gbc_scrollPane.gridx = 1;
			gbc_scrollPane.gridy = 5;
			contentPane.add(scrollPane, gbc_scrollPane);
	
			list = new JList();
			list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			list.setName("CategoryList");
			scrollPane.setColumnHeaderView(list);
	
			updateBtn = new JButton("Update");
			GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
			gbc_btnNewButton_1.fill = GridBagConstraints.BOTH;
			gbc_btnNewButton_1.insets = new Insets(0, 0, 5, 5);
			gbc_btnNewButton_1.gridx = 1;
			gbc_btnNewButton_1.gridy = 7;
			contentPane.add(updateBtn, gbc_btnNewButton_1);
	
			deleteBtn = new JButton("Delete");
			GridBagConstraints gbc_btnNewButton_2 = new GridBagConstraints();
			gbc_btnNewButton_2.fill = GridBagConstraints.BOTH;
			gbc_btnNewButton_2.insets = new Insets(0, 0, 5, 5);
			gbc_btnNewButton_2.gridx = 2;
			gbc_btnNewButton_2.gridy = 7;
			contentPane.add(deleteBtn, gbc_btnNewButton_2);
	
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
	
	}
