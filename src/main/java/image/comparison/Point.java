package image.comparison;


public class Point {
    private  int x;
    private  int y;
    private  int value;

    public Point(int x, int y, int value) {
        this.x = x;
        this.y = y;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point point = (Point) o;

        if (x != point.x) return false;
        if (y != point.y) return false;

        double a = (double) value;
        double b = (double) point.value;
        return Math.round(Math.abs((a - b) / (( a + b) / 2)) * 100) <= 10;

    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        result = 31 * result + value;
        return result;
    }

    @Override
    public String toString() {
        return "Point(" + x + "," + y + "," + value + ")";
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
