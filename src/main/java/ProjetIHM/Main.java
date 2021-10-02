package ProjetIHM;

import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

	public static double nbColor;
	public static Stage st;

	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		st = stage;
		URL fxmlFileUrl = getClass().getResource("fxproj.fxml");
		if (fxmlFileUrl == null) {
			System.out.println("Impossible de charger le fichier fxml");
			System.exit(-1);
		}
		loader.setLocation(fxmlFileUrl);
		Parent root = loader.load();

		Scene scene = new Scene(root);
		stage.setMaximized(true);
		stage.setResizable(true);
		stage.setScene(scene);
		stage.setTitle("Projet IHM");
		stage.getIcons().add(new Image("https://upload.wikimedia.org/wikipedia/commons/thumb/f/f8/Color_icon_gray_v2.svg/1200px-Color_icon_gray_v2.svg.png"));
		stage.show();

	}

	public static void main(String[] args) {
		Application.launch(args);
	}

}
