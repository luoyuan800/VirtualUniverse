package luo.gavin.virtual;

import java.util.Random;

public abstract class Base {
    private long age;
    private int count;
    private int x;
    private int y;
    private int radii;

    public abstract Random getRandom();
    public Base(int x, int y, int radii) {
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

    public int getRadii() {
        return radii;
    }

    public void setRadii(int radii) {
        this.radii = radii;
    }

    public int contains(Base other) {
        if (other.isInside(x + radii, y) && other.isInside(x - radii, y) && other.isInside(x, y + radii) && other.isInside(x, y - radii)) {
            return CONTAINED;
        } else if (isInside(other.x + other.radii, y) && isInside(other.x - other.radii, y)
                && isInside(other.x, other.y + other.radii) && isInside(other.x, other.y - other.radii)) {
            return CONTAIN;
        }
        return NOT_CONTAIN;
    }

    public static final int NOT_CONTAIN = 0;
    public static final int CONTAIN = 1;
    public static final int CONTAINED = 2;

    public int[] getRandomPoint(){
        int x = this.getX() + getRandom().nextInt(this.getRadii()) * (getRandom().nextBoolean() ? -1 : 1);
        int y = this.getY() + getRandom().nextInt(this.getRadii()) * (getRandom().nextBoolean() ? -1 : 1);
        return new int[]{x, y};
    }
}
