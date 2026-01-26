package com.example.blogmanager.app.swing;

import java.awt.EventQueue;
import java.util.concurrent.Callable;

import com.example.blogmanager.controller.BlogPostController;
import com.example.blogmanager.controller.CategoryController;
import com.example.blogmanager.repository.mongo.BlogPostMongoRepository;
import com.example.blogmanager.repository.mongo.CategoryMongoRepository;
import com.example.blogmanager.view.swing.BlogPostSwingView;
import com.example.blogmanager.view.swing.CategorySwingView;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import java.util.logging.Logger;
import java.util.logging.Level;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(mixinStandardHelpOptions = true)
public class BlogManagerSwingApp implements Callable<Void> {

	@Option(names = { "--mongo-host" }, description = "MongoDB host address")
	private String mongoHost = "localhost";

	@Option(names = { "--mongo-port" }, description = "MongoDB host port")
	private int mongoPort = 27017;

	@Option(names = { "--db-name" }, description = "Database name")
	private String databaseName = "blogmanager";

	@Option(names = { "--blogpost-collection" }, description = "BlogPost collection name")
	private String blogPostCollection = "blogpost";

	@Option(names = { "--category-collection" }, description = "Category collection name")
	private String categoryCollection = "category";

	public static void main(String[] args) {
		new CommandLine(new BlogManagerSwingApp()).execute(args);
	}

	@Override
	public Void call() {

		EventQueue.invokeLater(() -> {
			try {
				MongoClient mongoClient = new MongoClient(new ServerAddress(mongoHost, mongoPort));

				CategoryMongoRepository categoryRepository = new CategoryMongoRepository(mongoClient, databaseName,
						categoryCollection);

				BlogPostMongoRepository blogPostRepository = new BlogPostMongoRepository(mongoClient, databaseName,
						blogPostCollection);

				CategorySwingView categoryView = new CategorySwingView();
				BlogPostSwingView blogPostView = new BlogPostSwingView();

				CategoryController categoryController = new CategoryController(categoryView, categoryRepository);
				BlogPostController blogPostController = new BlogPostController(blogPostView, blogPostRepository,
						categoryRepository);

				categoryView.setCategoryController(categoryController);
				blogPostView.setBlogPostController(blogPostController);

				blogPostView.setCategoryView(categoryView);
				categoryView.setBlogPostSwingView(blogPostView);

				categoryView.setVisible(false);
				blogPostView.setVisible(true);

				categoryController.getAllCategories();
				blogPostController.getAllBlogPosts();
				blogPostController.loadCategories();
				

			} catch (Exception e) {
				Logger.getLogger(getClass().getName())
				.log(Level.SEVERE, "Exception", e);
			}
		});

		return null;
	}
}
