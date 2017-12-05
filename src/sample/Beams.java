package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class Beams implements Initializable {
    @FXML
    ImageView imageBeams;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File file = new File("test2.jpeg");
        Image image = new Image(file.toURI().toString());
        imageBeams.setImage(image);
        imageBeams.setVisible(true);
    }
}
