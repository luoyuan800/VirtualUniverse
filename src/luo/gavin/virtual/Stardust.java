/*
 * Stardust.java
 * Date: 7/27/2015
 * Time: 12:43 PM
 * 
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED.
*/

package luo.gavin.virtual;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Stardust {
    private Universe universe;
    private long seed;
    private double density;
    private Random random = new Random(seed);
    public Stardust(Universe universe){
        this.universe = universe;
        this.seed = universe.getSeed()/100;
        this.density = Math.abs(random.nextInt(universe.getWidth())*random.nextInt(universe.getLength())/universe.getLength()*universe.getWidth());
        while(this.density == 0){
            this.density =  Math.abs(random.nextInt(universe.getWidth())*random.nextInt(universe.getLength())/universe.getLength()*universe.getWidth());
        }
    }

    public double getStardustWeigh(int x, int y){
        return Math.abs(Math.floor(x*y*density));
    }
}
