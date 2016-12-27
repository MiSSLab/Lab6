package pl.edu.pg.eti.miss.dla;

import javafx.animation.AnimationTimer;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import org.controlsfx.control.RangeSlider;
import pl.edu.pg.eti.miss.dla.model.Simulation;
import pl.edu.pg.eti.miss.dla.model.Speed;
import pl.edu.pg.eti.miss.dla.utils.SimulationPrinter2D;

public class Controller {
    private static final int FPS = 30;
    private static final int N = 160;
    @FXML
    Canvas canvas;
    @FXML
    CheckBox backward;
    @FXML
    Button play;
    @FXML
    Button stop;
    @FXML
    Button clear;
    @FXML
    TextField startTextField;
    @FXML
    TextField endTextField;
    @FXML
    Label currentText;
    @FXML
    Label maxText;
    @FXML
    ChoiceBox<Speed> speedChoiceBox;


    private AnimationTimer animationTimer;
    GraphicsContext gc;
    private SimpleIntegerProperty frameNumber;

    private Simulation simulation;

    private SimulationPrinter2D printer;

    @FXML
    private ProgressBar progressBar;

    @FXML
    public void initialize() {
        initFields();

        frameNumber = new SimpleIntegerProperty(0);
        backward.disableProperty().bind(frameNumber.isEqualTo(0));
        currentText.textProperty().bind(frameNumber.asString());
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long t) {
                int i = frameNumber.get();
                int ratio = speedChoiceBox.getValue().getRatio();
                if (i < ratio) {
                    if (backward.isSelected()) {
                        frameNumber.setValue(0);
                        stop();
                        backward.setSelected(false);
                    } else {
                        frameNumber.setValue(ratio);
                    }
                } else if (i >= simulation.getFramesCount() - ratio) {
                    if (backward.isSelected()) {
                        frameNumber.setValue(frameNumber.subtract(ratio).get());
                    } else {
                        frameNumber.setValue(simulation.getFramesCount() - 1);
                        stop();
                    }
                } else {
                    if (backward.isSelected()) {
                        frameNumber.setValue(frameNumber.subtract(ratio).get());
                    } else {
                        frameNumber.setValue(frameNumber.add(ratio).get());
                    }
                }
                printer.print(i);
            }

            @Override
            public void stop() {
                super.stop();
                playEnable(true);
            }
        };
    }

    private void initFields() {
        playEnable(false);
        stop.setDisable(true);
        clear.setDisable(true);

        simulation = new Simulation(N);
        simulation.setOnSucceeded((x) -> {
            playEnable(true);
            progressBar.setVisible(false);
            endTextField.textProperty().setValue(String.valueOf(simulation.getFramesCount() - 1));
            maxText.textProperty().setValue(String.valueOf(simulation.getFramesCount() - 1));
        });
        progressBar.progressProperty().bind(simulation.progressProperty());
        simulation.start();

        gc = canvas.getGraphicsContext2D();
        printer = new SimulationPrinter2D(simulation, gc, N);
        Speed speed1 = new Speed(1);
        speedChoiceBox.setItems(FXCollections.observableArrayList(
                speed1, new Speed(2), new Speed(4), new Speed(8), new Speed(16))
        );
        speedChoiceBox.setValue(speed1);
        startTextField.textProperty().addListener(forceNumberListener);
        endTextField.textProperty().addListener(forceNumberListener);
        final RangeSlider hSlider = new RangeSlider(0, 100, 10, 90);
        hSlider.setShowTickMarks(true);
        hSlider.setShowTickLabels(true);
        hSlider.setBlockIncrement(10);


    }

    private void playEnable(boolean enable) {
        play.setDisable(!enable);
        stop.setDisable(enable);
        startTextField.setDisable(!enable);
        endTextField.setDisable(!enable);
        speedChoiceBox.setDisable(!enable);
        clear.setDisable(false);
    }


    @FXML
    public void play() {
        if (validateStartAndEnd()) {
            animationTimer.start();
            frameNumber.setValue(Integer.valueOf(startTextField.textProperty().getValue()));
            playEnable(false);
        }
    }

    private boolean validateStartAndEnd() {
        Integer start = Integer.valueOf(startTextField.textProperty().getValue());
        Integer end = Integer.valueOf(endTextField.textProperty().getValue());
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Wrong values");
        if (start > simulation.getFramesCount() || end > simulation.getFramesCount()) {
            alert.setHeaderText("Start/End greater than number of frames");
            alert.showAndWait();
            return false;
        } else if (start < 0 || end < 0) {
            alert.setHeaderText("Start/End lower than 0");
            alert.showAndWait();
            return false;
        } else if (start > end) {
            alert.setHeaderText("Start greater than end");
            alert.showAndWait();
            return false;
        }
        return true;
    }

    @FXML
    public void stop() {
        animationTimer.stop();
        playEnable(true);
    }

    @FXML
    public void clear() {
        animationTimer.stop();
        playEnable(true);
        backward.setSelected(false);
        frameNumber.setValue(0);
        printer.print(0);
    }

    ChangeListener<String> forceNumberListener = (observable, oldValue, newValue) -> {
        if (!newValue.matches("\\d*"))
            ((StringProperty) observable).set(oldValue);
    };
}
