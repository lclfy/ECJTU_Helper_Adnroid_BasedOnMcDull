package com.mcdull.cert.domain;

import com.amap.api.maps.model.LatLng;

/**
 * Created by mcdull on 15/7/31.
 */
public class Location {
    public static int AREA_NAN = 0;
    public static int AREA_BEI = 1;
    public static int TYPE_STAY = 0;
    public static int TYPE_STUDY = 1;
    public static int TYPE_LIFE = 2;
    public static int TYPE_PLAY = 3;
    public static int TYPE_OTHER = 4;
    private int id;
    private String name;
    //    private int area;
//    private int type;
    private LatLng latLng;

    public String getName() {
        return name;
    }

    public LatLng getLatLng() {
        return latLng;
    }

//    public int getArea() {
//        return area;
//    }
//
//    public int getType() {
//        return type;
//    }


    public int getId() {
        return id;
    }

    public Location(int id, String name, LatLng latLng) {
        this.id = id;
        this.name = name;
//        this.area = area;
//        this.type = type;
        this.latLng = latLng;
    }
}
