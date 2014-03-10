package com.miui.player;

import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * Project name : MiuiMusic
 * Package name : com.miui.player
 * Created by jiahuixing
 * Created on 2014-03-10
 */
public class url_test {

    private static String url_site = "http://fm.duokanbox.com/";

    private static Map<Integer, String> parts;

    private final String CATEGORY = "category";   //category
    private final String CHANNEL = "category/";   //category+cid = category/cid
    private final String CHANNEL_SONGS = "channel/";  //channel+nid = channel/nid
    private final String SID_INFO = "music/";  //music+sid = music/sid
    private final String CP_ID_INFO = "sid/";  //sid+cp_id = sid/sid
    private final String QUERY = "query/";  //query+info = query/info

    private List<String> categories;
    private static List<String> channels;
    private static String info;

    URL url = null;
    HttpURLConnection conn = null;

    private JSONObject json;

    private void init_map() {
        parts = new HashMap<Integer, String>();
        parts.put(0, CATEGORY);
        parts.put(1, CHANNEL);
        parts.put(2, CHANNEL_SONGS);
        parts.put(3, SID_INFO);
        parts.put(4, CP_ID_INFO);
        parts.put(5, QUERY);
    }

    private static void debug(String msg) {
        System.out.printf("######msg=%s######%n", msg);
    }

    private static void debug(long msg) {
        System.out.printf("######msg=%s######%n", msg);
    }

    private static void debug(JSONObject json) {
        System.out.printf("######json=%s######%n", json);
    }

    private void test_interface(int key) {
        debug(key);
        String interface_url;
        interface_url = url_site + parts.get(key);
        switch (key) {
            case 0:
                /*
                * category
                * */
                debug(interface_url);
                StringBuilder sb = new StringBuilder();
                try {
                    url = new URL(interface_url);
                    conn = (HttpURLConnection) url.openConnection();
                    int r_code = conn.getResponseCode();
                    /*debug(r_code);*/
                    if (r_code == HttpURLConnection.HTTP_OK) {
                        conn.connect();
                        InputStream is = conn.getInputStream();
                        BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                        String line;
                        while ((line = br.readLine()) != null) {
                    /*debug(line);*/
                            sb.append(line);
                        }
                        /*debug(sb.toString());*/
                        json = JSONObject.fromObject(sb.toString());
                        debug(json);
                        String tmp;
                        tmp = json.getString("total");
                        debug(tmp);
                        List list;
                        list = json.getJSONArray("list");
                        System.out.println(list);
                        int list_len = list.size();
                        debug(list_len);
                        JSONObject json_tmp;
                        categories = new ArrayList<String>();
                        for (Object aList : list) {
                            json_tmp = JSONObject.fromObject(aList);
                            debug(json_tmp);
                            String cid;
                            cid = json_tmp.getString("cid");
                            debug(cid);
                            categories.add(cid);
                        }
                        Collections.sort(categories);
                        System.out.println(categories);
                        conn.disconnect();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                /*
                * category/cid
                * */
                break;
            case 2:
                /*
                * channel/nid
                * */
                break;
            case 3:
                /*
                * music/sid
                * */
                break;
            case 4:
                /*
                * sid/sid
                * */
                break;
            case 5:
                /*
                * query/info
                * */
                break;
            default:
        }
    }

    public static void main(String[] args) {
        url_test t = new url_test();
        t.init_map();
/*        for (int i = 0; i<parts.size();i++){
            debug(String.format("key=%d value=%s", i, parts.get(i)));
        }*/
/*        for (int j = 0;j<parts.size();j++){
            t.test_interface(j);
        }*/
        t.test_interface(0);

    }

}
