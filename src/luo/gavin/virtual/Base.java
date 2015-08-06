package luo.gavin.virtual;

import luo.gavin.MathUtils;
import luo.gavin.output.Output;

import java.util.Random;

public abstract class Base implements  Runnable{
    protected double stardust;
    private long age;
    protected int count;
    private int x;
    private int y;
    private int radii;
    protected Output output;
    protected boolean die;

    protected void LOG(String msg) {
        output.LOG(msg);
    }

    public abstract Random getRandom();

    public Base(int x, int y, int radii) {
        this.x = x;
        this.y = y;
        this.radii = radii;
        this.output = new Output(this.getClass().getSimpleName() + x + "_" + y);
    }

    public void start() {
        LOG(String.format("Point: x=%s, y=%s; Radii: %s; Stardust: %s", x, y, radii, stardust));
        LOG("------");

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
        return Math.hypot(x-getX(), y-getY()) <= getRadii();
//        return ((this.getX() + this.getRadii() >= x && this.getX() - getRadii() <= x) && (this.getY() + this.getRadii() >= y && this.getY() - this.getRadii() <= y));
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

    public int[] getRandomPoint() {
        int x = this.getX() + getRandom().nextInt(this.getRadii()) * (getRandom().nextBoolean() ? -1 : 1);
        int y = this.getY() + getRandom().nextInt(this.getRadii()) * (getRandom().nextBoolean() ? -1 : 1);
        return new int[]{x, y};
    }

    protected int getRandomRadii() {
        int radii = (int) (getRandom().nextInt(this.radii) * getRandom().nextFloat());
        if (radii <= 0) return 1;
        return radii;
    }

    public String toString() {
        return String.format("%s x=%s, y=%s, stardust=%s", this.getClass().getSimpleName(), x, y, stardust);
    }

    public void stop() {
        die = true;
        LOG("Finished stop " + toString());
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean hit(Base other) {
        boolean hit = false;
        if (this.isTouch(other)) {
            if (this.radii > 2 * other.getRadii()) {
                this.stardust += other.stardust;
                LOG("A " + other.getClass().getSimpleName() + " hit this " + getClass().getSimpleName());
                LOG("Hitter is " + other);
            } else if(other.getRadii() > 2 * this.radii){
                LOG("This " + getClass().getSimpleName() + " hit a " + other.getClass().getSimpleName());
                LOG("Target is " + other);
                hit = true;
                this.stop();
            } else{
                LOG("Die for the hit a " + other.getClass().getSimpleName());
                LOG("The target is " + other);
                hit = true;
                this.stop();
            }
        }
        return hit;
    }

    public boolean isDie() {
        return die;
    }

    public void run(){
        try{
            doRun();
        }catch (Exception e){
            LOG(e.toString());
            e.printStackTrace();
        }
    }

    protected abstract void doRun();
}
