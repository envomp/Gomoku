package ee.taltech.iti0202.gomoku.app.board;

public class WeightedLocationBuilder {
    private Integer x;
    private Integer y;
    private Double weight;

    public WeightedLocationBuilder setY(Integer y) {
        this.y = y;
        return this;
    }

    public WeightedLocationBuilder setX(Integer x) {
        this.x = x;
        return this;
    }

    public WeightedLocationBuilder setWeight(Double weight) {
        this.weight = weight;
        return this;
    }

    public WeightedLocation create() {
        return new WeightedLocation(y, x, weight);
    }
}