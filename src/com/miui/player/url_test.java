package com.miui.player;

import net.sf.json.JSONArray;
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

    private static final String url_site = "http://fm.duokanbox.com/";

    private static final int NO_NEED_PARAM = 0;
    private static final int NEED_PARAM = 1;

    private static final String Q_MASK = "?";
    private static final String AND_MASK = "&";
    private static final String PARAM_PAGE = "pn=";
    private static final String PARAM_SIZE = "size=";

    private static Map<Integer, String> parts;

    private List<String> categories;
    private static List<String> channels;
    private static String info;

    private URL url = null;
    private HttpURLConnection conn = null;

    private void init_map() {
        parts = new HashMap<Integer, String>();
        String CATEGORY = "category";
        parts.put(0, CATEGORY);
        String CHANNEL = "category/";
        parts.put(1, CHANNEL);
        String CHANNEL_SONGS = "channel/";
        parts.put(2, CHANNEL_SONGS);
        String SID_INFO = "music/";
        parts.put(3, SID_INFO);
        String CP_ID_INFO = "sid/";
        parts.put(4, CP_ID_INFO);
        String QUERY = "query/";
        parts.put(5, QUERY);
    }

    private static void debug(String msg) {
        System.out.printf("######%s######%n", msg);
    }

    private void test_interface(int key, int need_param) {
        debug(String.format("key=%s", key));
        String interface_url;
        interface_url = url_site + parts.get(key);
        String param = null;
        if (need_param == NEED_PARAM) {
            param = Q_MASK + PARAM_PAGE + 1 + AND_MASK + PARAM_SIZE + 10;
            debug(String.format("param=%s", param));
        }
        switch (key) {
            case 0:
                /*
                * category
                * */
                if (null != param) {
                    interface_url = interface_url + param;
                }
                debug(String.format("interface_url=%s", interface_url));
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
                        JSONObject json = JSONObject.fromObject(sb.toString());
                        debug(String.format("m_url=%s", json));
                        String tmp;
                        tmp = json.getString("total");
                        debug(String.format("tmp=%s", tmp));
                        List list;
                        list = json.getJSONArray("list");
                        System.out.println(list);
                        int list_len = list.size();
                        debug(String.format("list_len=%s", list_len));
                        JSONObject json_tmp;
                        categories = new ArrayList<String>();
                        for (Object aList : list) {
                            json_tmp = JSONObject.fromObject(aList);
                            debug(String.format("json_tmp=%s", json_tmp));
                            String cid;
                            cid = json_tmp.getString("cid");
                            debug(String.format("cid=%s", cid));
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
                if (categories != null) {
                    Random random = new Random(System.currentTimeMillis());
                    int rnd = Math.abs(random.nextInt()) % (categories.size());
                    debug(String.format("rnd=%s", rnd));
                    interface_url = interface_url + categories.get(rnd);
                }
                if (null != param) {
                    interface_url = interface_url + param;
                }
                debug(String.format("interface_url=%s", interface_url));
                get_url_info(key, interface_url);
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

    private void get_url_info(int key, String input_url) {
        try {
            url = new URL(input_url);
            conn = (HttpURLConnection) url.openConnection();
            int r_code = conn.getResponseCode();
            if (r_code == HttpURLConnection.HTTP_OK) {
                conn.connect();
                InputStream in = conn.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                JSONObject json = JSONObject.fromObject(sb.toString());
                System.out.print(json);
                String status;
                String msg;
                String total;
                JSONArray json_array;
                status = json.getString("status");
                debug(String.format("status=%s", status));
                msg = json.getString("msg");
                debug(String.format("msg=%s", msg));
                total = json.getString("total");
                debug(String.format("total=%s", total));
                json_array = json.getJSONArray("list");
                System.out.println(json_array);
                JSONObject tmp;
                String m_url;
                String m_count;
                String m_name;
                String m_nid;
                String m_description;
                for (Object m_json_array : json_array) {
                    tmp = JSONObject.fromObject(m_json_array);
                    m_url = tmp.getString("url");
                    m_count = tmp.getString("count");
                    m_name = tmp.getString("name");
                    m_nid = tmp.getString("nid");
                    m_description = tmp.getString("description");
                    debug("###########################################################");
                    debug(String.format("m_url=%s", m_url));
                    debug(String.format("m_count=%s", m_count));
                    debug(String.format("m_name=%s", m_name));
                    debug(String.format("m_nid=%s", m_nid));
                    debug(String.format("m_description=%s", m_description));
                    debug("###########################################################");
                }
                conn.disconnect();
            } else {
                debug("ResponseCode not ok.");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
        t.test_interface(0, NO_NEED_PARAM);
        t.test_interface(1, NEED_PARAM);
    }

}
