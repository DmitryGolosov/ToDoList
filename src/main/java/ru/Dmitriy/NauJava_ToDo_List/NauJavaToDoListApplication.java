package ru.Dmitriy.NauJava_ToDo_List;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class NauJavaToDoListApplication extends Application{

    private static ConfigurableApplicationContext springContext;
    private Parent rootNode;
    private FXMLLoader fxmlLoader;

	public static void main(String[] args) {
        launch(args);
	}

    @Override
    public void init() throws Exception {
        springContext = SpringApplication.run(NauJavaToDoListApplication.class);
        fxmlLoader = new FXMLLoader();
        fxmlLoader.setControllerFactory(springContext::getBean);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        fxmlLoader.setLocation(getClass().getResource("/view/auth/LoginView.fxml"));
        rootNode = fxmlLoader.load();

        primaryStage.setTitle("NauJava Todo List");
        primaryStage.getIcons().add(new Image("/images/icon.png"));

        Scene scene = new Scene(rootNode, 1440, 1080);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1440);
        primaryStage.setMinHeight(1080);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        springContext.close();
    }

    public static ConfigurableApplicationContext getSpringContext() {
        return springContext;
    }

}
