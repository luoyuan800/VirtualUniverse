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
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class Galaxy extends Base implements Runnable {
    private String name;
    private Universe universe;
    private Random random;
    private Map<Star, ScheduledFuture<?>> stars;
    private Map<Planet, ScheduledFuture<?>> planets;
    private boolean black;
    private boolean die;
    private ScheduledExecutorService executor;

    public Galaxy(int x, int y, int radii, Universe universe) {
        super(x, y, radii);
        this.universe = universe;
        random = new Random(universe.getSeed());
        stars = new HashMap<Star, ScheduledFuture<?>>();
        planets = new HashMap<Planet, ScheduledFuture<?>>();
        executor = universe.getExecutor();
        stardust = universe.getStardust().getStardustWeigh(x, y);
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

    public void buildStar() {
        boolean next = true;
        while (this.stardust > 0 && next) {
            int[] pointer = getRandomPoint();
            int x = pointer[0];
            int y = pointer[1];
            double stardust = this.stardust * random.nextDouble() * random.nextFloat() * random.nextFloat();
            this.stardust -= stardust;
            int radii = (int)(random.nextInt(this.getRadii()) * random.nextFloat());
            Star star = new Star(this, x, y, radii, stardust);
            next = random.nextBoolean();
            this.stars.put(star, executor.scheduleAtFixedRate(star, 0, 100, TimeUnit.MILLISECONDS));
            LOG(String.format("A new Star born on : %s, %s.", x, y));
            star.start();
        }
    }

    public void buildPlanet() {
        if (makeCalculate()) {
            int[] pointer = getRandomPoint();
            int x = pointer[0];
            int y = pointer[1];
            double stardust = this.stardust * random.nextDouble() * random.nextDouble() * random.nextDouble();
            this.stardust -= stardust;
            int radii = getRadii();
            Planet planet = new Planet(this, x, y, radii, stardust);
            Star star = getStar(x, y);
            if(star!=null){
                return;
            }
            Planet p1 = getPlanet(x, y);
            if(p1!=null){
                return;
            }
            for(Star star1 : stars.keySet()){
                if(star1.isTouch(planet)){
                    return;
                }
            }
            for(Planet p2 : planets.keySet()){
                if(p2.isTouch(planet)){
                    return;
                }
            }
            this.planets.put(planet, executor.scheduleAtFixedRate(planet, 0, 100, TimeUnit.MILLISECONDS));
            LOG(String.format("A new Planet born on : %s, %s.", x, y));
            planet.start();
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
        int base = Math.abs((int)Math.floor(stardust));
        int next = random.nextInt(base);
        return next * (stars.size() + planets.size()) <= base;
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
