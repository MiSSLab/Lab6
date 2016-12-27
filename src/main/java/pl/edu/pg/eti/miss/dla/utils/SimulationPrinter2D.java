package pl.edu.pg.eti.miss.dla.utils;

import javafx.scene.canvas.GraphicsContext;
import pl.edu.pg.eti.miss.dla.model.Simulation;

public class SimulationPrinter2D {
    private static final int PIXEL_SIZE = 4;

    private Colors colors = new Colors();
    private final Simulation simulation;
    private final GraphicsContext gc;
    private final int n;

    public SimulationPrinter2D(Simulation simulation, GraphicsContext gc, int n) {
        this.simulation = simulation;
        this.gc = gc;
        this.n = n;
    }

    public void print(int frame) {
        gc.clearRect(0, 0, PIXEL_SIZE * n, PIXEL_SIZE * n);
        int[][] pixels = simulation.getFrames().get(frame);

        for (int x = 0; x < pixels.length; x++) {
            for (int y = 0; y < pixels[x].length; y++) {
                int i = pixels[x][y];
                if (i > 0) {
                    gc.setFill(colors.get(i));
                    gc.fillRect(PIXEL_SIZE * x, PIXEL_SIZE * (n - y - 1), PIXEL_SIZE, PIXEL_SIZE);
                }
            }
        }

    }
}
