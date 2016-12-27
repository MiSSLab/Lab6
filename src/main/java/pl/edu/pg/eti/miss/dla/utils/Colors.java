package pl.edu.pg.eti.miss.dla.utils;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Colors {

    private Color[] colors;

    public Colors() {
        colors = new Color[360];
        for (int i = 0; i < 360; i++) {
            colors[i] = Color.hsb(i, .8f, .8f);
        }
    }

    public Color get(int i) {
        return colors[i];
    }

    public void setFill(GraphicsContext gc, int i) {
        gc.setFill(colors[i]);
    }
}
