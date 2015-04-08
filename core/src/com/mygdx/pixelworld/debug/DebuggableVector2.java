package com.mygdx.pixelworld.debug;

import com.badlogic.gdx.math.Vector2;

public class DebuggableVector2 implements Debuggable{
    private Vector2 val;
    private final String name;

    public DebuggableVector2(String name, Vector2 init) {
        val = init;
        this.name = name;
    }

    public Vector2 get(){
            return val;
        }

    public void set(Vector2 val){
            this.val = new Vector2(val);
    }

    @Override
    public String getWatch() {
            return String.format("%s -> X = %.2f\tY = %.2f", name, val.x, val.y);
        }
}