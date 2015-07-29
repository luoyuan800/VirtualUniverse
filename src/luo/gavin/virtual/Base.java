package luo.gavin.virtual;

public abstract class Base {
    private long age;
    private int count;
    private int x;
    private int y;
    private double radii;

    public Base(int x, int y, double radii) {
        this.x = x;
        this.y = y;
        this.radii = radii;
    }

    public void age() {
        count++;
        if (count == 100) {
            count = 0;
            age = getAge() + 1;
        }
    }

    public boolean isTouch(Base other) {
        return other.isInside((int) (x + radii), (y)) || other.isInside((int) (x - radii), y) ||
                other.isInside(x, (int) (y + radii)) || other.isInside(x, (int) (y - radii));
    }

    public double gravitation() {
        return 0.0;
    }

    public long getAge() {
        return age;
    }

    public boolean isInside(int x, int y) {
        return ((this.getX() + this.getRadii() >= x && this.getX() - getRadii() <= x) && (this.getY() + this.getRadii() >= y && this.getY() - this.getRadii() <= y));
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getRadii() {
        return radii;
    }

    public void setRadii(double radii) {
        this.radii = radii;
    }

    public int contains(Base other) {
        if (other.isInside((int) (x + radii), y) && other.isInside((int) (x - radii), y) && other.isInside(x, (int) (y + radii)) && other.isInside(x, (int) (y - radii))) {
            return CONTAINED;
        } else if (isInside((int) (other.x + other.radii), y) && isInside((int) (other.x - other.radii), y)
                && isInside(other.x, (int) (other.y + other.radii)) && isInside(other.x, (int) (other.y - other.radii))) {
            return CONTAIN;
        }
        return NOT_CONTAIN;
    }

    public static final int NOT_CONTAIN = 0;
    public static final int CONTAIN = 1;
    public static final int CONTAINED = 2;
}
