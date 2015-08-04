/*
 * Mover.java
 * Date: 8/3/2015
 * Time: 9:35 AM
 * 
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED.
*/

package luo.gavin.virtual;

import luo.gavin.MathUtils;

public abstract class Mover extends Base {
    private Integer revolutionX;
    private Integer revolutionY;
    private int revolutionSpeed;
    private int rotationSpeed;
    private Integer revolutionRadiiSquare;

    public Mover(int x, int y, int radii) {
        super(x, y, radii);
    }

    public void revolution() {
        if (revolutionRadiiSquare == null && revolutionX != null && revolutionY != null) {
            revolutionRadiiSquare = MathUtils.square(revolutionX - getX()) + MathUtils.square(revolutionY - getY());
        }
        int angel = Math.multiplyExact(revolutionSpeed, count);
        int[] coordinate = MathUtils.circleCoordinate(getX(), getY(), getRadii(), angel);
        this.setX(coordinate[0]);
        this.setY(coordinate[1]);
    }

    public void rotation() {

    }

    public int getRevolutionX() {
        return revolutionX;
    }

    public void setRevolutionX(int revolutionX) {
        this.revolutionX = revolutionX;
    }

    public int getRevolutionY() {
        return revolutionY;
    }

    public void setRevolutionY(int revolutionY) {
        this.revolutionY = revolutionY;
    }

    public int getRevolutionSpeed() {
        return revolutionSpeed;
    }

    public void setRevolutionSpeed(int revolutionSpeed) {
        this.revolutionSpeed = revolutionSpeed;
    }

    public int getRotationSpeed() {
        return rotationSpeed;
    }

    public void setRotationSpeed(int rotationSpeed) {
        if(rotationSpeed>3) rotationSpeed = 3;
        this.rotationSpeed = rotationSpeed;
    }
}
