package pl.edu.pg.eti.miss.dla.model;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import pl.edu.pg.eti.miss.dla.utils.Matrix;

import java.util.ArrayList;
import java.util.List;

public class Simulation extends Service {

    private List<int[][]> frames = new ArrayList<>();
    private int n;
    private final int[][] m;

    @Override
    protected Task createTask() {
        return new Task<Void>() {
            protected Void call() throws InterruptedException {
                int launch = n - 10;
                int particles = 0;
                setSeed(n, m);

                frames.add(Matrix.deepCopy(m));

                int highestParticle = 0;
                updateProgress(highestParticle, launch);
                boolean done = false;
                while (!done) {
                    Thread.sleep(1);
                    int x = (int) (n * Math.random());
                    int y = launch;

                    while (x < n - 2 && x > 1 && y < n - 2 && y > 1) {
                        double r = Math.random();
                        if (r < 0.25) x--;
                        else if (r < 0.50) x++;
                        else if (r < 0.65) y++;
                        else y--;

                        if (m[x - 1][y] + m[x + 1][y] + m[x][y - 1] + m[x][y + 1] +
                                m[x - 1][y - 1] + m[x + 1][y + 1] + m[x - 1][y + 1] + m[x + 1][y - 1] > 0) {
                            if (y > highestParticle) {
                                highestParticle = y;
                                updateProgress(highestParticle, launch);
                            }
                            int color = (particles / 16) % 360;
                            m[x][y] = color;
                            particles++;
                            frames.add(Matrix.deepCopy(m));
                            if (y > launch) {
                                done = true;
                            }

                            break;
                        }
                    }
                }
                return null;
            }
        };
    }

    public Simulation(int n) {
        this.n = n;
        m = new int[n][n];
    }

    private void setSeed(int n, int[][] dla) {
        for (int x = 0; x < n; x++) {
            dla[x][0] = 1;
        }
    }


    public List<int[][]> getFrames() {
        return frames;
    }

    public int getFramesCount() {
        return frames.size();
    }

}
