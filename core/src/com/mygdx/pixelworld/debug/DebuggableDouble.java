package com.mygdx.pixelworld.debug;

/**
 * Debuggable value of type double.
 */
public class DebuggableDouble implements Debuggable{
    private final String name;
    private double val;

    public DebuggableDouble(String name){
        this.name = name;
        val = 0;
    }

    public DebuggableDouble(String name, double init) {
        val = init;
        this.name = name;
    }

    public double get(){
        return val;
    }

    public void set(double val){
        this.val = val;
    }

    @Override
    public String getWatch() {
        return String.format("%s -> %f", name, val);
    }
}
