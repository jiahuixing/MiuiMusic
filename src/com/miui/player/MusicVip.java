package com.miui.player;

/**
 * Project name : MiuiMusic
 * Package name : com.miui.player
 * Created by jiahuixing
 * Created on 2014-02-25
 */

import net.sf.json.JSON;
import net.sf.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class MusicVip {


    public static void main(String[] args) {

        List<String> list = new ArrayList<String>();
        list.add("first");
        list.add("second");
        JSONArray json = JSONArray.fromObject(list);
        System.out.printf("json=%s%n", json);
        System.out.printf("json=%s%n", json.toString());
        if (json instanceof JSON) {
            debug("11111%n");
        } else {
            debug("22222%n");
        }
    }

    private static void debug(String msg) {
        System.out.printf("msg=%s%n", msg);
    }


}
