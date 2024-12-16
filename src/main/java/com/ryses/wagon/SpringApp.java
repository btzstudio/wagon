package com.ryses.wagon;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SpringApp extends Application {

    private ConfigurableApplicationContext springContext;
    private Parent root;

    @Override
    public void init() throws Exception {
        springContext = new SpringApplicationBuilder(SpringApp.class)
                .headless(false)
                .web(WebApplicationType.NONE)
                .run()
        ;

        springContext
                .getAutowireCapableBeanFactory()
                .autowireBeanProperties(
                        this,
                        AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE,
                        true
                )
        ;

        var fxmlLoader = new FXMLLoader(getClass().getResource("wagon.fxml"));
        fxmlLoader.setControllerFactory(springContext::getBean);
        root = fxmlLoader.load();
    }

    @Override
    public void start(Stage primaryStage) {
        var stage = new Stage();
        stage.setTitle("Wagon");
        stage.initOwner(primaryStage);
        stage.setWidth(400);
        stage.setHeight(450);
        stage.setIconified(true);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.show();

        springContext.getBeanFactory().registerSingleton("primaryStage", stage);
    }

    @Override
    public void stop() throws Exception {
        springContext.stop();
    }
}