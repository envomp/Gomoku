package ee.taltech.iti0202.gomoku.app.board;

public class WeightedLocation {

    public Double weight;
    public Integer y;
    public Integer x;

    public WeightedLocation(Integer y, Integer x, Double weight) {
        this.weight = weight;
        this.y = y;
        this.x = x;
    }

    public WeightedLocation() {
    }

    public Location getLocation() {
        return new Location(y, x);
    }
}