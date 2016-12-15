package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {

    private PixelWriter pixelWriter;
    private GraphicsContext graphicsContext2D;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) {

        Canvas canvas = new Canvas(800, 800);
        canvas.setWidth(800);
        canvas.setHeight(800);

        GraphicsContext gc = canvas.getGraphicsContext2D();

        graphicsContext2D = canvas.getGraphicsContext2D();
        pixelWriter = graphicsContext2D.getPixelWriter();

        Pane root = new Pane();

        root.getChildren().add(canvas);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Snowflake");


        int N = 160;
        int launch = N - 10;                   // row to launch particles from
        boolean[][] dla = new boolean[N][N];   // is cell (x, y) occupied


        IntegerProperty particles = new SimpleIntegerProperty(0);
        Color[] colors = new Color[360];
        for (int i = 0; i < 360; i++) {
            colors[i] = Color.hsb(i, .8f, .8f);
        }


        // set seed to be bottom row
        for (int x = 0; x < N; x++) dla[x][0] = true;


        // repeat until aggregate hits top
        final BooleanProperty done = new SimpleBooleanProperty(false);

        new AnimationTimer() {
            @Override
            public void handle(long currentNanoTime) {
                if (done.get()) stop();
                int x = (int) (N * Math.random());
                int y = launch;

                // particle takes a 2d random walk
                while (x < N - 2 && x > 1 && y < N - 2 && y > 1) {
                    double r = Math.random();
                    if (r < 0.25) x--;
                    else if (r < 0.50) x++;
                    else if (r < 0.55) y++;
                    else y--;

                    // check if neighboring site is occupied
                    if (dla[x - 1][y] || dla[x + 1][y] || dla[x][y - 1] || dla[x][y + 1] ||
                            dla[x - 1][y - 1] || dla[x + 1][y + 1] || dla[x - 1][y + 1] || dla[x + 1][y - 1]) {
                        dla[x][y] = true;
                        particles.setValue(particles.intValue() + 1);
                        printPixel(x, N - y - 1, colors[(particles.intValue() / 16) % 360]);

                        // aggregate hits top, so set flag to stop outer while loop
                        if (y > launch) done.setValue(Boolean.TRUE);

                        break;
                    }
                }
            }
        }.start();

        stage.show();
    }
    public void printPixel(int x, int y, Color c){
        int pixelSize = 5;
        graphicsContext2D.setFill(c);
        graphicsContext2D.fillRect(pixelSize * x, pixelSize * y, pixelSize, pixelSize);

    }
}