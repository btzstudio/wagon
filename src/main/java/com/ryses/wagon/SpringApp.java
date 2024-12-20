package com.ryses.wagon;

import javafx.application.Application;
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

import java.awt.*;
import java.net.URL;
import java.util.Objects;

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
        stage.setHeight(490);
        stage.setIconified(true);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.getIcons().add(new javafx.scene.image.Image(Objects.requireNonNull(getClass().getResourceAsStream("wagon.png"))));
        stage.show();

        buildTaskBarIcon();

        springContext.getBeanFactory().registerSingleton("primaryStage", stage);
    }

    @Override
    public void stop() throws Exception {
        springContext.stop();
    }

    private void buildTaskBarIcon() {
        var defaultToolkit = Toolkit.getDefaultToolkit();
        var imageResource = getClass().getResource("wagon.png");
        var logo = defaultToolkit.getImage(imageResource);

        var taskbar = Taskbar.getTaskbar();

        try {
            taskbar.setIconImage(logo);
        } catch (final UnsupportedOperationException e) {
            System.out.println("The os does not support: 'taskbar.setIconImage'");
        } catch (final SecurityException e) {
            System.out.println("There was a security exception for: 'taskbar.setIconImage'");
        }

        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Wagon");
    }
}