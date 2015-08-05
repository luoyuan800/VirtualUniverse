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
import java.util.concurrent.ConcurrentHashMap;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class Galaxy extends Base{
    private String name;
    private Universe universe;
    private Random random;
    private Map<Star, ScheduledFuture<?>> stars;
    private Map<Planet, ScheduledFuture<?>> planets;
    private boolean black;
    private ScheduledExecutorService executor;

    public Galaxy(int x, int y, int radii, Universe universe) {
        super(x, y, radii);
        this.universe = universe;
        random = new Random(universe.getSeed());
        stars = new ConcurrentHashMap<Star, ScheduledFuture<?>>();
        planets = new ConcurrentHashMap<Planet, ScheduledFuture<?>>();
        executor = universe.getExecutor();
        stardust = universe.getStardust().getStardustWeigh(x, y);
    }

    public void doRun() {
        if (stardust > 0 && stars.size() != 0) {
            buildPlanet();
            age();
        } else if (stardust <= 0 && stars.size() != 0 && !isBlack()) {
            toBlack();
        } else if (stars.size() == 0 || stardust == 0) {
            die();
        }
    }

    /**
     * 检测这个星系内的行星是否碰撞
     */
    public Planet checkHit(Planet planet){
        for(Planet p : planets.keySet()){
                if(planet!=p && !p.isDie()){
                    if(planet.isTouch(p)){
                        if(planet.hit(p)){
                            planets.get(planet).cancel(true);
                        }
                        if(p.hit(planet)){
                            planets.get(p).cancel(true);
                        }
                        return p;
                    }
                }
            }
        return null;
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
            if(stardust<=this.stardust/10) {
                this.stardust -= stardust;
                int radii = (int) (random.nextInt(this.getRadii()) * random.nextFloat());
                Star star = new Star(this, x, y, radii, stardust);
                for(Star s : stars.keySet()){
                    if(s.isTouch(star)){
                        return;
                    }
                }
                this.stars.put(star, executor.scheduleAtFixedRate(star, 0, 100, TimeUnit.MILLISECONDS));
                LOG(String.format("A new Star born on : %s, %s.", x, y));
                star.start();
            }
            next = random.nextBoolean();
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
        if(base <=0){
            return false;
        }
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
        return String.format("Galaxy(age %s) on %s,%s , have %s stars and %s survival planets and %s death planets.", getAge(), getX(), getY(), stars.size(), getSurvivalPlanets(), getDiePlanets());
    }

    public int getSurvivalPlanets(){
        int num = 0;
        for(Planet planet : planets.keySet()){
            if(!planet.die){
                num ++;
            }
        }
        return num;
    }

    public int getDiePlanets(){
        int num = 0;
        for(Planet planet : planets.keySet()){
            if(planet.die){
                num ++;
            }
        }
        return num;
    }

    public boolean isBlack() {
        return black;
    }



    public Random getRandom() {
        return random;
    }

    public void stop(){
        for(Star star : stars.keySet()){
            star.stop();
        }
        for (Planet planet : planets.keySet()){
            planet.stop();
        }
        super.stop();
    }
}
