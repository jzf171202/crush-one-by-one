package com.zjrb.sjzsw.model;

import java.io.Serializable;
public class PointModel implements Serializable {
    private static final long serialVersionUID = -611454324450559807L;
    private float x;
    private float y;

    public PointModel(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}

