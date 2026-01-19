package com.example.blogmanager.app.swing;

import java.awt.EventQueue;

import com.example.blogmanager.controller.BlogPostController;
import com.example.blogmanager.controller.CategoryController;
import com.example.blogmanager.repository.mongo.BlogPostMongoRepository;
import com.example.blogmanager.repository.mongo.CategoryMongoRepository;
import com.example.blogmanager.view.swing.BlogPostSwingView;
import com.example.blogmanager.view.swing.CategorySwingView;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

public class BlogManagerSwingApp {
	public static void main(String[] args) {

		EventQueue.invokeLater(() -> {
			try {
				String mongoHost = "localhost";
				int mongoPort = 27017;

				if (args.length > 0)
					mongoHost = args[0];
				if (args.length > 1)
					mongoPort = Integer.parseInt(args[1]);

				MongoClient mongoClient = new MongoClient(new ServerAddress(mongoHost, mongoPort));

				CategoryMongoRepository categoryRepository = new CategoryMongoRepository(mongoClient, "blogmanager",
						"category");

				BlogPostMongoRepository blogPostRepository = new BlogPostMongoRepository(mongoClient, "blogmanager",
						"blogpost");

				CategorySwingView categoryView = new CategorySwingView();
				BlogPostSwingView blogPostView = new BlogPostSwingView();

				CategoryController categoryController = new CategoryController(categoryView, categoryRepository);
				BlogPostController blogPostController = new BlogPostController(blogPostView, blogPostRepository,
						categoryRepository);

				categoryView.setCategoryController(categoryController);
				blogPostView.setBlogPostController(blogPostController);

			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}
