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
    private static List<String> sids;
    private static List<String> cp_song_ids;
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
        int rnd;
        switch (key) {
            case 0:
                /*
                * category
                * */
                if (null != param) {
                    interface_url = interface_url + param;
                }
                debug(String.format("interface_url=%s", interface_url));
                get_url_info(key, interface_url);
                break;
            case 1:
                /*
                * category/cid
                * */
                debug(String.format("categories=%s", categories));
                if (categories != null) {
                    Collections.sort(categories);
                    Random random = new Random(System.currentTimeMillis());
                    rnd = Math.abs(random.nextInt()) % (categories.size());
                    debug(String.format("rnd=%s", rnd));
                    interface_url = interface_url + categories.get(rnd);
                    if (null != param) {
                        interface_url = interface_url + param;
                    }
                    debug(String.format("interface_url=%s", interface_url));
                    get_url_info(key, interface_url);
                } else {
                    debug("categories == null.");
                }
                break;
            case 2:
                /*
                * channel/nid
                * */
                debug(String.format("channels=%s", channels));
                if (channels != null) {
                    Collections.sort(channels);
                    Random random = new Random(System.currentTimeMillis());
                    rnd = Math.abs(random.nextInt()) % (channels.size());
                    debug(String.format("rnd=%s", rnd));
                    interface_url = interface_url + channels.get(rnd);
                    if (null != param) {
                        interface_url = interface_url + param;
                    }
                    debug(String.format("interface_url=%s", interface_url));
                    get_url_info(key, interface_url);
                } else {
                    debug("channels == null.");
                }
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
                String c_url;
                String c_name;
                JSONArray json_array;
                status = json.getString(json_tags.TAG_STATUS);
                debug(String.format("status=%s", status));
                msg = json.getString(json_tags.TAG_MSG);
                debug(String.format("msg=%s", msg));
                total = json.getString(json_tags.TAG_TOTAL);
                debug(String.format("total=%s", total));
                json_array = json.getJSONArray(json_tags.TAG_LIST);
                System.out.println(json_array);
                JSONObject tmp;
                String m_url;
                String m_count;
                String m_name;
                String m_nid;
                String m_description;
                String m_cid;
                String m_album_name;
                String m_language;
                String m_file_duration;
                String m_country;
                String m_compose_name;
                String m_artist_name;
                String m_cp_song_id;
                String m_lyricist_name;
                String m_sid;
                String m_copy_type;
                String m_cp_id;
                switch (key) {
                    case 0:
                        categories = new ArrayList<String>();
                        for (Object m_json_array : json_array) {
                            tmp = JSONObject.fromObject(m_json_array);
                            m_description = tmp.getString(json_tags.TAG_DESCRIPTION);
                            m_name = tmp.getString(json_tags.TAG_NAME);
                            m_cid = tmp.getString(json_tags.TAG_CID);
                            debug("###########################################################");
                            debug(String.format("idx=%s", json_array.indexOf(m_json_array)));
                            debug(String.format("m_description=%s", m_description));
                            debug(String.format("m_name=%s", m_name));
                            debug(String.format("m_cid=%s", m_cid));
                            debug("###########################################################");
                            categories.add(m_cid);
                        }
                        break;
                    case 1:
                        channels = new ArrayList<String>();
                        for (Object m_json_array : json_array) {
                            tmp = JSONObject.fromObject(m_json_array);
                            m_url = tmp.getString(json_tags.TAG_URL);
                            m_count = tmp.getString(json_tags.TAG_COUNT);
                            m_name = tmp.getString(json_tags.TAG_NAME);
                            m_nid = tmp.getString(json_tags.TAG_NID);
                            m_description = tmp.getString(json_tags.TAG_DESCRIPTION);
                            debug("###########################################################");
                            debug(String.format("idx=%s", json_array.indexOf(m_json_array)));
                            debug(String.format("m_url=%s", m_url));
                            debug(String.format("m_count=%s", m_count));
                            debug(String.format("m_name=%s", m_name));
                            debug(String.format("m_nid=%s", m_nid));
                            debug(String.format("m_description=%s", m_description));
                            debug("###########################################################");
                            channels.add(m_nid);
                        }
                        break;
                    case 2:
                        c_url = json.getString(json_tags.TAG_URL);
                        debug(String.format("c_url=%s", c_url));
                        c_name = json.getString(json_tags.TAG_NAME);
                        debug(String.format("c_name=%s", c_name));
                        sids = new ArrayList<String>();
                        cp_song_ids = new ArrayList<String>();
                        for (Object m_json_array : json_array) {
                            tmp = JSONObject.fromObject(m_json_array);
                            m_album_name = tmp.getString(json_tags.TAG_ALBUM_NAME);
                            m_name = tmp.getString(json_tags.TAG_NAME);
                            m_language = tmp.getString(json_tags.TAG_LANGUAGE);
                            m_file_duration = tmp.getString(json_tags.TAG_FILE_DURATION);
                            m_country = tmp.getString(json_tags.TAG_COUNTRY);
                            m_compose_name = tmp.getString(json_tags.TAG_COMPOSE_NAME);
                            m_artist_name = tmp.getString(json_tags.TAG_ARTIST_NAME);
                            m_cp_song_id = tmp.getString(json_tags.TAG_CP_SONG_ID);
                            m_lyricist_name = tmp.getString(json_tags.TAG_LYRICIST_NAME);
                            m_sid = tmp.getString(json_tags.TAG_SID);
                            m_copy_type = tmp.getString(json_tags.TAG_COPY_TYPE);
                            m_cp_id = tmp.getString(json_tags.TAG_CP_ID);
                            debug("###########################################################");
                            debug(String.format("idx=%s", json_array.indexOf(m_json_array)));
                            debug(String.format("m_album_name=%s", m_album_name));
                            debug(String.format("m_name=%s", m_name));
                            debug(String.format("m_language=%s", m_language));
                            debug(String.format("m_file_duration=%s", m_file_duration));
                            debug(String.format("m_country=%s", m_country));
                            debug(String.format("m_compose_name=%s", m_compose_name));
                            debug(String.format("m_artist_name=%s", m_artist_name));
                            debug(String.format("m_cp_song_id=%s", m_cp_song_id));
                            debug(String.format("m_lyricist_name=%s", m_lyricist_name));
                            debug(String.format("m_sid=%s", m_sid));
                            debug(String.format("m_copy_type=%s", m_copy_type));
                            debug(String.format("m_cp_id=%s", m_cp_id));
                            debug("###########################################################");
                            sids.add(m_sid);
                            cp_song_ids.add(m_cp_song_id);
                        }
                        break;
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
