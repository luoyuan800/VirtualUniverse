/*
 * Galaxy.java
 * Date: 7/27/2015
 * Time: 2:07 PM
 * 
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED.
*/

package luo.gavin.virtual;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static luo.gavin.output.Output.LOG;

public class Galaxy extends Base implements Runnable {
    private String name;
    private Universe universe;
    private double stardust;
    private Random random;
    private Map<Star, ScheduledFuture<?>> stars;
    private Map<Planet, ScheduledFuture<?>> planets;
    private boolean black;
    private boolean die;
    private ScheduledExecutorService executor;

    public Galaxy(int x, int y, double radii, Universe universe) {
        super(x, y, radii);
        this.universe = universe;
        stardust = universe.getStardust().getStardustWeigh(x, y);
        random = new Random(universe.getSeed());
        stars = new HashMap<Star, ScheduledFuture<?>>();
        planets = new HashMap<Planet, ScheduledFuture<?>>();
        executor = Executors.newScheduledThreadPool(10);
        buildStar();
        LOG(String.format("A new Galaxy born on : %s, %s.", x, y));
    }

    public void run() {
        if (stardust != 0 && stars.size() != 0) {
            buildPlanet();
            age();
        } else if (stardust <= 0 && stars.size() != 0 && !isBlack()) {
            toBlack();
        } else if (stars.size() == 0 || stardust == 0) {
            die();
        }
    }

    public void die() {
        LOG(String.format("A Galaxy on : %s, %s die!", getX(), getY()));
        universe.removeGalaxy(this);
        die = true;
    }

    public void toBlack() {
        black = true;
        LOG(String.format("A Galaxy die and become black hold on : %s, %s.", getX(), getY()));
    }

    private void buildStar() {
        boolean next = true;
        while (this.stardust > 0 && next) {
            int x = this.getX() + random.nextInt((int) Math.floor(this.getRadii())) * (random.nextBoolean() ? -1 : 1);
            int y = this.getY() + random.nextInt((int) Math.floor(this.getRadii())) * (random.nextBoolean() ? -1 : 1);
            double stardust = this.stardust * random.nextDouble() * random.nextDouble() * random.nextDouble();
            this.stardust -= stardust;
            double radii = random.nextInt((int) Math.floor(this.getRadii())) * random.nextDouble();
            Star star = new Star(this, x, y, (int) Math.floor(radii), stardust);
            next = random.nextBoolean();
            this.stars.put(star, executor.scheduleAtFixedRate(star, 0, 100, TimeUnit.MILLISECONDS));
        }
    }

    private void buildPlanet() {
        if (makeCalculate()) {
            int x = this.getX() + random.nextInt((int) Math.floor(this.getRadii())) * (random.nextBoolean() ? -1 : 1);
            int y = this.getY() + random.nextInt((int) Math.floor(this.getRadii())) * (random.nextBoolean() ? -1 : 1);
            double stardust = this.stardust * random.nextDouble() * random.nextDouble() * random.nextDouble();
            this.stardust -= stardust;
            double radii = random.nextInt((int) Math.floor(this.getRadii())) * random.nextDouble();
            Planet planet = new Planet(this, x, y, radii, stardust);
            this.planets.put(planet, executor.scheduleAtFixedRate(planet, 0, 100, TimeUnit.MILLISECONDS));
        }
    }

    public Star getStar(int x, int y){
        for(Star star : stars.keySet()){
            if(star.isInside(x, y)){
                return star;
            }
        }
        return null;
    }

    public Planet getPlanet(int x, int y){
        for(Planet planet : planets.keySet()){
            if(planet.isInside(x, y)){
                return planet;
            }
        }
        return null;
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
        return String.format("Galaxy(age %s) on %s,%s , have %s stars and %s planets.", getAge(), getX(), getY(), stars.size(), planets.size());
    }

    public boolean isBlack() {
        return black;
    }

    public boolean isDie() {
        return die;
    }

    public Random getRandom() {
        return random;
    }
}
