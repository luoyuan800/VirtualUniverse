/*
 * Planet.java
 * Date: 7/29/2015
 * Time: 5:22 PM
 * 
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED.
*/

package luo.gavin.virtual;

import java.util.Random;

import static luo.gavin.output.Output.LOG;

public class Planet extends Base implements Runnable {
    private double stardust;
    public Planet(Galaxy galaxy, int x, int y, int radii, double stardust) {
        super(x, y, radii);
        this.stardust = stardust;

    }

    @Override
    public void run() {

    }

    @Override
    public Random getRandom() {
        return null;
    }
}
