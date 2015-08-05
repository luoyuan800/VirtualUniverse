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


public class Planet extends Mover{
    private Galaxy galaxy;
    public Planet(Galaxy galaxy, int x, int y, int radii, double stardust) {
        super(x, y, radii);
        this.stardust = stardust;
        this.galaxy = galaxy;

    }

    @Override
    public void doRun() {
        revolution();
    }

    public void revolution(){
        super.revolution();
        this.galaxy.checkHit(this);
    }

    @Override
    public Random getRandom() {
        return null;
    }
}
