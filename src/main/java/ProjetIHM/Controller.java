package ProjetIHM;

import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;

import ProjetIHM.Main;
import org.w3c.dom.css.Rect;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TabPane;
import javafx.scene.image.WritableImage;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Controller {
	@FXML
	private Slider slider;

	@FXML
	private GridPane elements;

	@FXML
	private Canvas color;

	@FXML
	private Canvas gray;

	@FXML
	private MenuItem export;

	@FXML
	private MenuItem about;

	@FXML
	private VBox hotColor;

	@FXML
	private VBox freshColor;

	@FXML
	private TabPane panel;

	private List<Node> node = new ArrayList<>();
	private List<ComboBox> comboBoxs = new ArrayList<>();
	private List<ColorPicker> colorPicker = new ArrayList<>();
	private List<Node> colorPanel = new ArrayList<>();
	private List<Node> fcolor = new ArrayList<>();
	private GraphicsContext gcColor;
	private GraphicsContext gcGray;
	private Paint selectedColor;

	public void initialize() {

		// On récupère le gridpane de droite (c'est à dire la selection avec le slider)
		node = elements.getChildren();
		colorPanel = hotColor.getChildren();
		fcolor = freshColor.getChildren();
		// On récupère les 2 canvas
		gcColor = color.getGraphicsContext2D();
		gcGray = gray.getGraphicsContext2D();
		// GraphicsContext gcGray = gray.getGraphicsContext2D();
		gcColor.getCanvas().setOnMouseClicked((e) -> {
			genererRectangle(gcColor);
		});

		getColorPickerAndComboBoxs();
		for (ComboBox comboBox : comboBoxs) {
			comboBox.getItems().addAll("Rectangle", "Circle");
		}
		// De base on les rend invisibles
		for (int i = 0; i < node.size(); i++) {
			elements.getChildren().get(i).setVisible(false);
		}

		// Mise à jour du slider
		slider.valueProperty().addListener((observable, oldValue, newValue) -> {
			updateSlider(newValue);
		});

		// Quand on click sur un des rectangles de droite on peux recupérer la couleur
		// et la mettre dans les colorPickers
		for (ColorPicker colorPicker2 : colorPicker) {
			colorPicker2.setOnAction((e -> {
				updateCanvas();
			}));

			colorPicker2.setOnMouseClicked((e) -> {
				if (e.isShiftDown()) {
					colorPicker2.setValue((Color) selectedColor);
					colorPicker2.hide();
					updateCanvas();
				}
			});
		}

		// Shift+click pour récup la couleur
		for (Node r : colorPanel) {
			Rectangle rec = (Rectangle) r;
			rec.setOnMousePressed((e) -> {
				Rectangle rct = (Rectangle) e.getSource();
				selectedColor = rct.getFill();
			});
		}
		for (Node r : fcolor) {
			Rectangle rec = (Rectangle) r;
			rec.setOnMousePressed((e) -> {
				Rectangle rct = (Rectangle) e.getSource();
				selectedColor = rct.getFill();
			});
		}
	}

	/**
	 * Méthode pour générer un rectangle
	 * 
	 * @param gc
	 */
	private void genererRectangle(GraphicsContext gc) {
		gc.setFill(Color.ALICEBLUE);
		gc.fillRect(40, 100, 40, 40);
	}

	// On récupère les colorPicker et les comboBox
	private void getColorPickerAndComboBoxs() {
		for (Node nodes : node) {

			if (nodes instanceof ColorPicker) {
				colorPicker.add((ColorPicker) nodes);
			}
			if (nodes instanceof ComboBox) {
				comboBoxs.add((ComboBox) nodes);
			}
		}
	}

	private void updateSlider(Number newValue) {
		Main.nbColor = newValue.doubleValue();
		// System.out.println(Main.nbColor);

		// Affichage des ColorPicker (Si 5 est séléctionné alors on affiche 5 comboBoxs
		// et 5 colorPicker)
		for (int i = 0; i < Main.nbColor * 2; i++) {
			elements.getChildren().get(i).setVisible(true);

		}
		for (int i = (int) (Main.nbColor * 2); i < node.size(); i++) {
			elements.getChildren().get(i).setVisible(false);
		}
		updateCanvas();
	}

	public void updateCanvas() {
		gcColor.clearRect(0, 0, (int) color.getWidth(), (int) color.getHeight());

		gcGray.clearRect(0, 0, (int) color.getWidth(), (int) color.getHeight());
		int rdmX = 0;
		int rdmY = 0;
		for (int i = 0; i < Main.nbColor; i++) {
			// Randomize pos
			/*
			 * Random r = new Random(); int rdmX = r.nextInt((int) color.getWidth() - 40);
			 * int rdmY = r.nextInt((int) color.getHeight() - 60);
			 */

			Color color = colorPicker.get(i).getValue();
			Object shape = comboBoxs.get(i).getValue();

			double redval = color.getRed() * 0.3;
			double greenval = color.getGreen() * 0.59;
			double blueval = color.getBlue() * 0.11;
			Color grayColor = new Color(redval + greenval + blueval, redval + greenval + blueval,
					redval + greenval + blueval, 1);

			gcColor.setFill(color);
			gcGray.setFill(grayColor);
			if (shape == "Rectangle") {
				gcColor.fillRect(rdmX, rdmY, 40, 60);
				gcGray.fillRect(rdmX, rdmY, 40, 60);
			} else if (shape == "Circle") {
				gcColor.fillOval(rdmX, rdmY, 40, 60);
				gcGray.fillOval(rdmX, rdmY, 40, 60);
			}
			rdmX += 7 * Main.nbColor;
			rdmY += 2 * Main.nbColor;
		}
	}

	@FXML
	public void exportEvent() {
		// Sauvegarde du gris
		WritableImage img = gray.snapshot(new SnapshotParameters(), null);

		FileChooser fChooser = new FileChooser();
		fChooser.setTitle("Sauvegarde des niveaux de gris");
		fChooser.setInitialFileName("gray");
		fChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichier PNG", "*.png"));

		File file = fChooser.showSaveDialog(Main.st);
		try {
			if (file != null) {
				ImageIO.write(SwingFXUtils.fromFXImage(img, null), "png", file);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Sauvegarde des couleurs
		WritableImage imgcolor = color.snapshot(new SnapshotParameters(), null);
		fChooser.setTitle("Sauvegarde des niveaux des couleurs");
		fChooser.setInitialFileName("color");
		file = fChooser.showSaveDialog(Main.st);
		try {
			if (file != null) {
				ImageIO.write(SwingFXUtils.fromFXImage(imgcolor, null), "png", file);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void close() {
		Platform.exit();
	}

	@FXML
	public void about() throws Exception {
		VBox root = new VBox();

		root.getChildren().addAll(
				new Label("Bienvenue sur notre projet d'IHM créé par Hardelin Baptiste et Vanhee Paul.\n"
						+ "Cette application permet de transformer des couleurs en leurs niveaux de gris."),
				new Label(
						"Ce projet a été fait pour le module M2105 - N2 - Introduction aux interfaces homme-machines. \n"),
				new Label("Organisé par Mr. Casiez et Mr. Anquetil."));

		root.setSpacing(20);
		Stage stage = new Stage();
		stage.initModality(Modality.WINDOW_MODAL);
		Scene scene = new Scene(root, 600, 150);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.setTitle("Bienvenue");
		stage.show();

	}

	@FXML
	public void aide() throws Exception {
		VBox root = new VBox();

		root.getChildren().addAll(new Label(
				"Bienvenue dans notre application qui permet de transformer des couleurs en leurs niveaux de gris.\n"),
				new Label(
						"Pour choisir le nombre de couleurs que vous voulez, il faut bouger le curseur (en haut à droite).\n"),
				new Label("De plus, vous pouvez choisir la forme que vous voulez rectangle/cercle,\n"),
				new Label("Puis vous pouvez choisir la couleur de chaque forme."),
				new Label(
						"Vous pouvez utiliser notre palette de couleur en faisant : SHIFT + CLICK sur la couleur et sur le selecteur.\n"),
				new Label(
						"Enfin vous pouvez vous rendre dans l'onglet Fichier puis exporter au format PNG vos 2 images."));

		root.setSpacing(20);
		Stage stage = new Stage();
		stage.initModality(Modality.WINDOW_MODAL);
		Scene scene = new Scene(root, 700, 250);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.setTitle("Aide");
		stage.show();
	}

}
