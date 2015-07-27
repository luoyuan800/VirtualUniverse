/*
 * Galaxy.java
 * Date: 7/27/2015
 * Time: 2:07 PM
 * 
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED.
*/

package luo.gavin.virtual;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static luo.gavin.output.Output.LOG;

public class Galaxy implements Runnable {
    private String name;
    private Universe universe;
    private int x, y;
    private double radii;
    private double stardust;
    private Random random;
    private List<Star> stars;
    private List planets;
    private boolean black;

    public Galaxy(int x, int y, double radii, Universe universe) {
        this.x = x;
        this.y = y;
        this.universe = universe;
        this.radii = radii;
        stardust = universe.getStardust().getStardustWeigh(x, y);
        random = new Random(universe.getSeed());
        stars = new ArrayList<Star>();
        planets = new ArrayList();
        buildStar();
        LOG(String.format("A new Galaxy born on : %s, %s.", x, y));
    }

    public void run() {
        if (stardust != 0 && stars.size() != 0) {
            buildPlanet();
        }else
        if (stardust <= 0 && stars.size() != 0 && !black) {
            black = true;
            LOG(String.format("A Galaxy die and become black hold on : %s, %s.", x, y));
        } else if (stars.size() == 0 || stardust == 0) {
            LOG(String.format("A Galaxy on : %s, %s die!", x, y));
            universe.removeGalaxy(this);
        }
    }

    private void buildStar() {
        boolean next = true;
        while (this.stardust > 0 && next) {
            int x = this.x + random.nextInt((int) Math.floor(this.radii)) * (random.nextBoolean() ? -1 : 1);
            int y = this.y + random.nextInt((int) Math.floor(this.radii)) * (random.nextBoolean() ? -1 : 1);
            double stardust = this.stardust * random.nextDouble() * random.nextDouble() * random.nextDouble();
            this.stardust -= stardust;
            double radii = random.nextInt((int) Math.floor(this.radii)) * random.nextDouble();
            Star star = new Star(this, x, y, (int) Math.floor(radii), stardust);
            this.stars.add(star);
            next = random.nextBoolean();
        }
    }

    private void buildPlanet() {
        if (makeCalculate()) {

        }
    }

    private boolean makeCalculate() {
        return stardust / stardust * random.nextDouble() >= 1.8;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return String.format("Galaxy on %s,%s , have %s stars and %s planets.", x, y, stars.size(), planets.size());
    }
}
