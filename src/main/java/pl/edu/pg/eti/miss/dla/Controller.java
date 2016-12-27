package pl.edu.pg.eti.miss.dla;

import javafx.animation.AnimationTimer;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import pl.edu.pg.eti.miss.dla.model.Pixel;
import pl.edu.pg.eti.miss.dla.utils.Colors;
import pl.edu.pg.eti.miss.dla.utils.Matrix;

import org.controlsfx.control.*;

import java.util.ArrayList;
import java.util.List;

public class Controller {
    @FXML
    Canvas canvas;
    @FXML
    CheckBox backward;
    @FXML
    Button play;
    @FXML
    Button stop;
    @FXML
    TextField startTextField;
    @FXML
    TextField endTextField;
    @FXML
    Label currentText;
    @FXML
    ChoiceBox speedChoiceBox;


    Colors colors = new Colors();
    private AnimationTimer animationTimer;
    GraphicsContext gc;
    private List<Pixel> diffs;
    private List<int[][]> frames;
    private SimpleIntegerProperty frameNumber;

    private static final int FPS = 30;
    private static final int N = 160;

    @FXML
    public void initialize() {
        initFields();
        int launch = N - 10;
        diffs = new ArrayList<>();
        frames = new ArrayList<>();
        int[][] dla = new int[N][N];
        IntegerProperty particles = new SimpleIntegerProperty(0);

        setSeed(N, dla);

        final BooleanProperty done = new SimpleBooleanProperty(false);
        while (!done.get()) {
            if (done.get()) {
                stop();
                playEnable(true);
            }
            int x = (int) (N * Math.random());
            int y = launch;

            while (x < N - 2 && x > 1 && y < N - 2 && y > 1) {
                double r = Math.random();
                if (r < 0.25) x--;
                else if (r < 0.50) x++;
                else if (r < 0.55) y++;
                else y--;

                if (dla[x - 1][y] + dla[x + 1][y] + dla[x][y - 1] + dla[x][y + 1] +
                        dla[x - 1][y - 1] + dla[x + 1][y + 1] + dla[x - 1][y + 1] + dla[x + 1][y - 1] > 0) {
                    int color = (particles.intValue() / 16) % 360;
                    dla[x][y] = color;
                    particles.setValue(particles.intValue() + 1);
                    diffs.add(new Pixel(x, y, colors.get(dla[x][y])));
                    frames.add(Matrix.deepCopy(dla));
                    if (y > launch) done.setValue(Boolean.TRUE);

                    break;
                }
            }
        }

        frameNumber = new SimpleIntegerProperty(0);
        backward.disableProperty().bind(frameNumber.isEqualTo(0));
        currentText.textProperty().bind(frameNumber.divide(FPS).asString());
        endTextField.textProperty().setValue(String.valueOf(frames.size() / FPS));
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long t) {
                System.out.println(frameNumber.get());
                if (frameNumber.get() >= 0 && frameNumber.get() < diffs.size() - 1) {
                    Pixel pixel = diffs.get(frameNumber.get());
                    if (!backward.isSelected()) {
                        frameNumber.setValue(frameNumber.get() + 1);


                        gc.setFill(pixel.getColor());
                        gc.fillRect(4 * pixel.getX(), 4 * (N - pixel.getY() - 1), 4, 4);


                    } else {
                        if (frameNumber.get() > 0) {
                            frameNumber.setValue(frameNumber.get() - 1);


                            gc.clearRect(4 * pixel.getX(), 4 * (N - pixel.getY() - 1), 4, 4);


                        }
                    }
                }
                if (frameNumber.get() == 0 || frameNumber.get() == diffs.size() - 1) {
                    stop();
                    playEnable(true);
                    backward.setSelected(false);
                }
            }
        };

        playEnable(true);
    }

    private void setSeed(int n, int[][] dla) {
        for (int x = 0; x < n; x++) {
            dla[x][0] = 1;
            colors.setFill(gc, 1);
            gc.fillRect(4 * x, 4 * (n - 1), 4, 4);
        }
    }

    private void initFields() {
        colors = new Colors();
        speedChoiceBox.setItems(FXCollections.observableArrayList(
                "x 0.5", "x 1", "x 2", "x 4", "x 8", "x 16")
        );
        speedChoiceBox.setValue("x 1");
        gc = canvas.getGraphicsContext2D();
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
    }


    @FXML
    public void play() {
        animationTimer.start();
        playEnable(false);
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
        gc.clearRect(0, 0, 640, 640);
    }
    ChangeListener<String> forceNumberListener = (observable, oldValue, newValue) -> {
        if (!newValue.matches("\\d*"))
            ((StringProperty) observable).set(oldValue);
    };

}
