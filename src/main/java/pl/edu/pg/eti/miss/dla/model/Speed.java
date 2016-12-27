package pl.edu.pg.eti.miss.dla.model;

public class Speed {
    private int ratio;

    public Speed(int ratio) {
        this.ratio = ratio;
    }

    public int getRatio() {
        return ratio;
    }

    @Override
    public String toString() {
        return "x " + ratio;
    }
}
