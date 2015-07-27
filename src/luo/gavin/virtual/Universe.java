/*
 * Universe.java
 * Date: 7/27/2015
 * Time: 10:34 AM
 * 
 * Copyright 2015 luoyuan.
 * ALL RIGHTS RESERVED.
*/

package luo.gavin.virtual;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static luo.gavin.output.Output.LOG;

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

    public void removeGalaxy(Galaxy galaxy){
        ScheduledFuture future = galaxies.get(galaxy);
        future.cancel(false);
        galaxies.remove(galaxy);
    }
    public void run() {
        if(myFuture!=null) {
            buildGalaxy();
        }

    }

    public Universe(String name) {
        this.age = 0;
        this.name = name;
        this.length = Math.abs(random.nextInt());
        this.width = Math.abs(random.nextInt());
        this.galaxies = new HashMap<Galaxy, ScheduledFuture<?>>(50);
        this.stardust = new Stardust(this);
        new Thread(new Manager(this)).start();
    }

    public boolean makeCalculate() {
        return Math.abs((random.nextDouble() + random.nextInt()) / random.nextInt()) >= galaxies.size() / 500;
    }

    private void buildGalaxy() {
        if (makeCalculate()) {
            LOG("Build a new Galaxy");
            int length = random.nextInt(this.getLength() / 500);
            int width = random.nextInt(this.getWidth() / 500);
            double radii = Math.sqrt((long)length * (long)length + (long)width * (long)width);
            int x = random.nextInt(this.getLength() / 2) * (random.nextBoolean() ? -1 : 1);
            int y = random.nextInt(this.getWidth() / 2) * (random.nextBoolean() ? -1 : 1);
            Galaxy galaxy = new Galaxy(x, y, radii, this);
            this.galaxies.put(galaxy, executor.scheduleAtFixedRate(galaxy, 0, period, TimeUnit.MILLISECONDS));
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
        executor.shutdownNow();
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
        return sb.toString();
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
                } else if (command.equalsIgnoreCase("start")){
                    System.out.println("Start the Virtual");
                    universe.start();
                }
            }
        }
    }

    public static void main(String... args) {
        new Universe("Test");
    }
}
