/*
 * Planet.java
 * Date: 7/29/2015
 * Time: 5:22 PM
 * 
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED.
*/

package luo.gavin.virtual;

import static luo.gavin.output.Output.LOG;

public class Planet extends Base implements Runnable {
    private double stardust;
    public Planet(Galaxy galaxy, int x, int y, double radii, double stardust) {
        super(x, y, radii);
        this.stardust = stardust;
        LOG(String.format("A new Planet born on : %s, %s.", x, y));

    }

    @Override
    public void run() {

    }
}
