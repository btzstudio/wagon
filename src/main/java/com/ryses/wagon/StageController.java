package com.ryses.wagon;

import com.ryses.wagon.conversion.I18nFileConversionFacade;
import com.ryses.wagon.conversion.domain.ConversionRequest;
import com.ryses.wagon.version.VersionsDiscoverer;
import com.ryses.wagon.version.domain.Version;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;

@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Controller
public class StageController implements Initializable {

    private final VersionsDiscoverer versionsDiscoverer;
    private final I18nFileConversionFacade conversionFacade;

    @FXML
    private Button browseButton;

    @FXML
    private Label directoryPath;

    @FXML
    private Label versionLabel;

    @FXML
    private ComboBox<Version> versionSelector;

    @FXML
    private StackPane overlay;

    @FXML
    private AnchorPane mainContent;

    @FXML
    private Button convertButton;

    @Autowired
    public StageController(VersionsDiscoverer versionsDiscoverer, I18nFileConversionFacade fileConverter) {
        this.versionsDiscoverer = versionsDiscoverer;
        this.conversionFacade = fileConverter;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        onDirectoryChosen(directory -> {
            var versions = versionsDiscoverer.apply(directory);
            var path = directory.getAbsolutePath();

            directoryPath.setText("..." + path.substring(path.length() - 35));
            versionSelector.getItems().removeAll();
            var items = versionSelector.getItems();

            versions.ifPresentOrElse(
                    files -> {
                        items.addAll(files);
                        items.forEach(version -> {
                            if (version.isSelected()) {
                                versionSelector.getSelectionModel().select(version);
                            }
                        });
                        versionLabel.setDisable(false);
                        versionSelector.setDisable(false);
                    },
                    () -> {
                        items.add(new Version());
                        versionSelector.getSelectionModel().select(0);
                    }
            );

            convertButton.setDisable(false);

            convertButton.setOnMouseClicked(_ -> {
                var progress = new ProgressIndicator();
                var vboxOverlay = new VBox();
                vboxOverlay.setAlignment(Pos.CENTER);
                mainContent.setDisable(true);
                vboxOverlay.getChildren().add(progress);
                overlay.getChildren().add(vboxOverlay);

                var version = versionSelector.getSelectionModel().getSelectedItem();
                var request = new ConversionRequest(directory, version, Locale.FRENCH);
                var result = conversionFacade.convert(request);

                result.thenAccept(details -> {
                    var sb = new StringBuilder("CONVERSION DETAILS\n\n");

                    details.forEach(detail -> {
                        if (detail.isSource()) {
                            sb.append(MessageFormat.format("* {0} (source):\n", detail.getRequest().getSourceLocale().getDisplayLanguage()));
                            sb.append(MessageFormat.format("    - {0} ({1} units)\n\n", detail.getFile().getName(), detail.getUnitCount()));
                        } else {
                            sb.append(MessageFormat.format("* {0}:\n", Locale.of(detail.getComponent().getLocale()).getDisplayLanguage()));
                            sb.append(MessageFormat.format("    - {0} ({1} units)\n", detail.getFile().getName(), detail.getUnitCount()));
                        }
                    });

                    var answer = showAlert(Alert.AlertType.CONFIRMATION, "Conversion was a success!", sb.toString());

                    answer.ifPresent(a -> {
                        mainContent.setDisable(false);
                        overlay.getChildren().remove(vboxOverlay);

                        if (!details.isEmpty()) {
                            try {
                                Desktop.getDesktop().open(details.stream().findFirst().get().getExportDirectory());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                }).exceptionally(ex -> {
                    var answer = showAlert(Alert.AlertType.ERROR, ex.getMessage(), ex.getLocalizedMessage());

                    answer.ifPresent(a -> {
                        mainContent.setDisable(false);
                        overlay.getChildren().remove(vboxOverlay);
                    });

                    return null;
                });
            });
        });
    }

    private Optional<ButtonType> showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);

        TextArea area = new TextArea(content);
        area.setWrapText(true);
        area.setEditable(false);
        alert.getButtonTypes().setAll(ButtonType.OK);

        alert.getDialogPane().setContent(area);
        alert.setResizable(true);

        return alert.showAndWait();
    }

    private void onDirectoryChosen(final Consumer<File> onDirectoryChosen) {
        browseButton.setOnMouseClicked(e -> {
            var dirChooser = new DirectoryChooser();
            dirChooser.setTitle("Select service root directory");
            dirChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            var window = ((Node) e.getSource()).getScene().getWindow();
            var dir = dirChooser.showDialog(window);

            if (dir == null) {
                return;
            }

            onDirectoryChosen.accept(dir);
        });
    }
}
