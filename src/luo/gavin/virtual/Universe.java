/*
 * Universe.java
 * Date: 7/27/2015
 * Time: 10:34 AM
 * 
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED.
*/

package luo.gavin.virtual;

import luo.gavin.output.Output;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Universe implements Runnable {
    private static final int period = 100;

    private String name;
    private long age;
    private long seed = new Random(System.currentTimeMillis()).nextLong();
    private Random random = new Random(seed);
    private boolean pause;
    private int length;
    private int width;
    private ScheduledExecutorService executor;
    private ScheduledFuture myFuture;
    private Map<Galaxy, ScheduledFuture<?>> galaxies;
    private Stardust stardust;
    private int count;
    private Output output;
    private Integer limit;

    public void removeGalaxy(Galaxy galaxy) {
        ScheduledFuture future = galaxies.get(galaxy);
        future.cancel(false);
    }

    public void run() {
        if (myFuture != null) {
            buildGalaxy();
            count++;
            if (count == 100) {
                count = 0;
                age++;
            }
        }

    }

    protected void LOG(String msg){
        this.output.LOG(msg);
    }
    public Universe(String name) {
        output = new Output(this.getClass());
        this.age = 0;
        this.name = name;
        this.length = Math.abs(random.nextInt());
        this.width = Math.abs(random.nextInt());
        this.galaxies = new HashMap<Galaxy, ScheduledFuture<?>>(50);
        this.stardust = new Stardust(this);
        new Thread(new Manager(this)).start();
    }

    public Universe(String name, int length, int width){
        output = new Output(this.getClass());
        this.age = 0;
        this.name = name;
        this.length = length;
        this.width = width;
        this.galaxies = new HashMap<Galaxy, ScheduledFuture<?>>(50);
        this.stardust = new Stardust(this);
        new Thread(new Manager(this)).start();
    }

    public Universe(String name, int limit){
        this(name);
        this.limit = limit;
    }
    public Universe(String name, int length, int width, int limit){
        this(name, length, width);
        this.limit = limit;
    }

    public boolean makeCalculate() {
        return !(limit != null && galaxies.size() >= limit) && Math.abs((random.nextDouble() + random.nextInt()) / random.nextInt()) >= galaxies.size() / 500;
    }

    private void buildGalaxy() {
        if (makeCalculate()) {
            int length = random.nextInt(this.getLength() / 500);
            int width = random.nextInt(this.getWidth() / 500);
            int radii = random.nextInt((length+width)/2)/(limit!=null? limit : 1000);
            if(radii <= 0) return;
            int x = random.nextInt(this.getLength() / 2) * (random.nextBoolean() ? -1 : 1);
            int y = random.nextInt(this.getWidth() / 2) * (random.nextBoolean() ? -1 : 1);
            if (getGalaxyByPoint(x, y) == null) {
                Galaxy galaxy = new Galaxy(x, y, radii, this);
                for(Galaxy g : galaxies.keySet()){
                    if(g.isTouch(galaxy)){
                        galaxy.setRadii(radii/2);
                    }
                    if(g.contains(galaxy) != Base.NOT_CONTAIN){
                        return;
                    }
                }
                LOG(String.format("Build a new Galaxy on x=%s, y=%s.", x, y));
                galaxy.start();
                galaxy.buildStar();
                this.galaxies.put(galaxy, executor.scheduleAtFixedRate(galaxy, 0, period, TimeUnit.MILLISECONDS));

            }
        }
    }

    public boolean start() {
        LOG("Start run the universe: " + name);
        executor = Executors.newScheduledThreadPool(50);
        myFuture = executor.scheduleAtFixedRate(this, 0, period, TimeUnit.MILLISECONDS);
        return true;
    }

    public boolean stop() {
        LOG("Stop Universe: " + name);
        myFuture.cancel(true);
        myFuture = null;
        LOG("Stop Universe Executor");
        executor.shutdown();
        for(Galaxy galaxy : galaxies.keySet()){
            galaxy.stop();
        }
        return true;
    }

    public String getName() {
        return name;
    }

    public long getAge() {
        return age;
    }

    public long getSeed() {
        return seed;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(name);
        sb.append("\n").append("Age: ").append(age);
        sb.append("\n").append("Running Status: ").append(pause ? "Suspend" : "Running");
        sb.append("\n").append("Current have Galaxy: ").append(galaxies.size());
        sb.append("\n").append("There ").append(liveGalaxies().size()).append(" are survival.").
                append(" And ").append(getBlackHoles().size()).append(" are black hold");
        return sb.toString();
    }

    public Set<Galaxy> liveGalaxies() {
        Set<Galaxy> lives = new HashSet<Galaxy>();
        for (Galaxy galaxy : galaxies.keySet()) {
            if (!galaxy.isBlack() && !galaxy.isDie()) {
                lives.add(galaxy);
            }
        }
        return lives;
    }

    public Set<Galaxy> getBlackHoles() {
        Set<Galaxy> bh = new HashSet<Galaxy>();
        for (Galaxy galaxy : galaxies.keySet()) {
            if (galaxy.isBlack()) {
                bh.add(galaxy);
            }
        }
        return bh;
    }

    public Galaxy getGalaxyByPoint(int x, int y) {
        for (Galaxy galaxy : galaxies.keySet()) {
            if (galaxy.isInside(x, y)) {
                return galaxy;
            }
        }
        return null;
    }

    public int getLength() {
        return length;
    }

    public int getWidth() {
        return width;
    }

    public Stardust getStardust() {
        return stardust;
    }

    public ScheduledExecutorService getExecutor() {
        return executor;
    }

    private class Manager implements Runnable {
        private Universe universe;
        private boolean running;
        private boolean pause;

        public Manager(Universe universe) {
            this.universe = universe;
            running = true;
            System.out.println("Prepare for the virtual universe run.");
        }

        public void run() {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            while (running) {
                String command = "";
                try {
                    command = br.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (command == null) {
                    continue;
                }
                if (command.equalsIgnoreCase("help")) {
                    System.out.println("help:");
                    System.out.println("quit: Stop the virtual");
                    System.out.println("pause: Suspend the virtual");
                    System.out.println("resume: Resume the virtual");
                    System.out.println("status: Show current status of  the virtual");
                    System.out.println("start: Begging to run the Virtual");
                    System.out.println("search: Try to search a galaxy on the special pointer. Example: 'search galaxy: 1234567, 1234567'");
                } else if (command.equalsIgnoreCase("quit")) {
                    this.running = false;
                    System.out.println("Begging to stop Virtual");
                    universe.stop();
                    System.out.println("Finished stop the Virtual");
                    LOG("Finished stop the Virtual");
                } else if (command.equalsIgnoreCase("pause")) {
                    pause = true;
                    universe.pause = true;
                    System.out.println("Suspend the Virtual Universe: " + universe.getName());
                } else if (command.equalsIgnoreCase("resume")) {
                    if (pause) {
                        System.out.println("Resume the virtual universe: " + universe.getName());
                        pause = false;
                        universe.pause = false;
                    } else {
                        System.out.println("Universe(" + universe.getName() + ") did not pause before.");
                    }
                } else if (command.equalsIgnoreCase("status")) {
                    System.out.println(universe.toString());
                } else if (command.equalsIgnoreCase("start")) {
                    System.out.println("Start the Virtual");
                    universe.start();
                } else if (command.matches("search galaxy: (-*\\d+), (-*\\d+)")) {
                    Matcher matcher = Pattern.compile("search galaxy: (-*\\d+), (-*\\d+)").matcher(command);
                    if (matcher.find()) {
                        String x = matcher.group(1);
                        String y = matcher.group(2);
                        System.out.println(String.format("Try to find if there a Galaxy on x=%s, y=%s ...", x, y));
                        try {
                            Galaxy galaxy = universe.getGalaxyByPoint(Integer.parseInt(x), Integer.parseInt(y));
                            if (galaxy != null) {
                                System.out.println("There a galaxy has been found: \n" + galaxy);
                            } else {
                                System.out.println("Not galaxy found on x=" + x + ", y=" + y);
                            }
                        }catch (NumberFormatException e){
                            System.out.println("Input is invalid: " + e.getMessage());
                        }
                    }
                }
            }
        }
    }

    public static void main(String... args) {
        new Universe("Test", 20);
    }
}
