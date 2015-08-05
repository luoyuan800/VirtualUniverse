/*
 * Star.java
 * Date: 7/27/2015
 * Time: 2:17 PM
 * 
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED.
*/

package luo.gavin.virtual;

import java.util.Random;


public class Star extends Mover {
    private Galaxy galaxy;
    private Random random;
    public Star(Galaxy galaxy, int x, int y, int radii, double stardust){
        super(x, y, radii);
        this.stardust = stardust;
        this.galaxy = galaxy;
        this.random = galaxy.getRandom();
    }
    public void doRun() {
        if(makeCalculate()){
            storm();
        }
    }

    public void revolution() {
    //Do nothing
    }

    private boolean makeCalculate() {
        return stardust / stardust * random.nextDouble() >= 1.8;
    }

    private void storm(){
        LOG(String.format("A sun storm happen on (%s, %s)", getX(), getY()));
    }

    @Override
    public Random getRandom() {
        return null;
    }
}
