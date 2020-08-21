package com.example.mymovie.networkconnection;


import com.example.mymovie.Cinema;
import com.example.mymovie.Credential;
import com.example.mymovie.Memoir;
import com.example.mymovie.Person;
import com.example.mymovie.entity.Movie;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NetworkConnection {
    private OkHttpClient client = null;
    private String results, newResults;
    private JSONObject jsonObject;
    private String movieid = "";
    private static final String API_KEY_TMDB = "85a553f60ebf6964a7c6ac8762e05c16";
    private static final String API_KEY_GOOGLE = "AIzaSyAbE_n0txLinbDZkyyjktE-Hmt87ASpXYQ";

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public NetworkConnection() {
        client = new OkHttpClient();
    }

    private static final String BASE_URL = "http://10.0.2.2:8080/Assignment1/webresources/";

    public String findByUsername(String username) {
        final String methodPath = "restws.credential/findByUsername/" + username;
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.build();
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    public String checkUser(String userName, String md5psd) {
        final String methodPath = "restws.credential/checkUsernameANDHashpassword/" + userName + "/" + md5psd;
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.get().build();
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    public String checkCinema(String name, String code) {
        final String methodPath = "restws.cinema/findByNameAndCode/" + name + "/" + code;
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.get().build();
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    public String getMaxID() {
        final String methodPath = "restws.cinema/findMaxCid";
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.get().build();
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    public String addPerson(String[] details) {
        Person person = new Person(Integer.parseInt(details[0]),
                details[1],
                details[2],
                details[3],
                details[4],
                details[5],
                details[6],
                details[7],
                details[8]);

        Gson gson = new Gson();
        String cinemaJson = gson.toJson(person);
        String strResponse = "";

        final String methodPath = "restws.person/";
        RequestBody body = RequestBody.create(cinemaJson, JSON);
        Request request = new Request.Builder().url(BASE_URL + methodPath).post(body).build();
        try {
            Response response = client.newCall(request).execute();
            strResponse = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strResponse;
    }

    public String addCinema(String[] details) {
        Cinema cinema = new Cinema(Integer.parseInt(details[0]), details[1], details[2]);

        Gson gson = new Gson();
        String cinemaJson = gson.toJson(cinema);
        String strResponse = "";

        final String methodPath = "restws.cinema/";
        RequestBody body = RequestBody.create(cinemaJson, JSON);
        Request request = new Request.Builder().url(BASE_URL + methodPath).post(body).build();
        try {
            Response response = client.newCall(request).execute();
            strResponse = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strResponse;
    }

    public String addCredential(String[] details) {
        Credential credential = new Credential(Integer.valueOf(details[0]), details[1], details[2], details[3]);
        credential.setPid(Integer.valueOf(details[4]));

        Gson gson = new Gson();
        String cinemaJson = gson.toJson(credential);
        String strResponse = "";

        final String methodPath = "restws.credential/";
        RequestBody body = RequestBody.create(cinemaJson, JSON);
        Request request = new Request.Builder().url(BASE_URL + methodPath).post(body).build();
        try {
            Response response = client.newCall(request).execute();
            strResponse = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strResponse;
    }

    public List getAllCinema() {
        List list = new ArrayList();
        int a = 0;
        final String methodPath = "restws.cinema/";
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.get().build();
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(results);
            for (a = 0; a < jsonArray.length(); a++) {
                list.add(jsonArray.getJSONObject(a).get("cname").toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public String getCid(String cname) {
        JSONArray jsonArray = new JSONArray();
        String cid = "";
        JSONObject jsonObject = new JSONObject();
        final String methodPath = "restws.cinema/findByCname/" + cname;
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.get().build();
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            jsonArray = new JSONArray(results);
            jsonObject = jsonArray.getJSONObject(0);
            cid = jsonObject.getString("cid");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return cid;
    }

    public String getMaxCreid() {
        final String methodPath = "restws.credential/findMaxCreid";
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.get().build();
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    public String getMaxPid() {
        final String methodPath = "restws.person/findMaxPid";
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.get().build();
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    public String getMaxMid() {
        final String methodPath = "restws.memoir/findMaxMid";
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.get().build();
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    public String getData(String id, String startDate, String endDate) {
        final String methodPath = "restws.memoir/approachA/" + id + "/" + startDate + "/" + endDate;
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.get().build();
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    public String barChart(String id, String year) {
        final String methodPath = "restws.memoir/approachB/" + id + "/" + year;
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.get().build();
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    public JSONObject getUserLocation(String address) {
        JSONObject jsonObject = new JSONObject();
        JSONObject resultJsonObject = new JSONObject();
        JSONArray resultJsonArray = new JSONArray();
        JSONObject geometryJsonObject = new JSONObject();
        JSONObject locationJsonObject = new JSONObject();
        final String methodPath = "https://maps.googleapis.com/maps/api/geocode/json?address=" + address + "&key=" + API_KEY_GOOGLE;
        Request.Builder builder = new Request.Builder();
        builder.url(methodPath);
        Request request = builder.get().build();
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            jsonObject = new JSONObject(results);
            resultJsonArray = jsonObject.getJSONArray("results");
            resultJsonObject = resultJsonArray.getJSONObject(0);
            geometryJsonObject = resultJsonObject.getJSONObject("geometry");
            locationJsonObject = geometryJsonObject.getJSONObject("location");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return locationJsonObject;
    }

    public JSONObject getAddress(int pid) {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        final String methodPath = "restws.person/getRealAddress/" + pid;
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.get().build();
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            jsonArray = new JSONArray(results);
            jsonObject = jsonArray.getJSONObject(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONArray getCinema() {
        JSONArray jsonArray = new JSONArray();
        final String methodPath = "restws.cinema";
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.get().build();
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            jsonArray = new JSONArray(results);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    public String addMemoir(String[] details) {
        Memoir memoir = new Memoir(Integer.parseInt(details[0]), details[1], details[2], details[3], details[4], details[5], Float.parseFloat(details[6]));
        memoir.setPid(Integer.parseInt(details[7]));
        memoir.setCid(Integer.parseInt(details[8]));

        Gson gson = new Gson();
        String memoirJson = gson.toJson(memoir);
        String strResponse = "";

        final String methodPath = "restws.memoir/";
        RequestBody body = RequestBody.create(memoirJson, JSON);
        Request request = new Request.Builder().url(BASE_URL + methodPath).post(body).build();
        try {
            Response response = client.newCall(request).execute();
            strResponse = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strResponse;
    }

    public JSONArray getCinemaLocation(List list) {
        List temp = list;
        JSONArray returnJsonArray = new JSONArray();
        JSONArray resultJsonArray = new JSONArray();
        JSONObject resultJsonObject = new JSONObject();
        JSONObject geometryJsonObject = new JSONObject();
        JSONObject locationJsonObject = new JSONObject();
        String name = "", code = "", address = "";
        Map<Integer, JSONObject> mapB = null;
        JSONObject jsonObject = new JSONObject();
        int i = 0;
        for (i = 0; i < temp.size(); i++) {
            mapB = (Map<Integer, JSONObject>) temp.get(i);
            jsonObject = mapB.get(i);
            try {
                name = jsonObject.get("cname").toString();
                code = jsonObject.get("cpostcode").toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            address = name + "," + code + ", Australia";
            final String methodPath = "https://maps.googleapis.com/maps/api/geocode/json?address=" + address + "&key=" + API_KEY_GOOGLE;
            Request.Builder builder = new Request.Builder();
            builder.url(methodPath);
            Request request = builder.get().build();
            try {
                Response response = client.newCall(request).execute();
                results = response.body().string();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                jsonObject = new JSONObject(results);
                resultJsonArray = jsonObject.getJSONArray("results");
                resultJsonObject = resultJsonArray.getJSONObject(0);
                geometryJsonObject = resultJsonObject.getJSONObject("geometry");
                locationJsonObject = geometryJsonObject.getJSONObject("location");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONObject format_jb = new JSONObject();
            try {
                format_jb.put("name", name);
                format_jb.put("location", locationJsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                returnJsonArray.put(i, format_jb);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return returnJsonArray;
    }

    public JSONObject search(String keyword) {
        String keywordM = keyword;
        keywordM = keywordM.replace(" ", "%20");
        keywordM = keywordM.replace(":", "%3A");
        final String methodPath = "https://api.themoviedb.org/3/search/movie?api_key=" + API_KEY_TMDB + "&query=" + keywordM;
        Request.Builder builder = new Request.Builder();
        builder.url(methodPath);
        Request request = builder.get().build();
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            jsonObject = new JSONObject(results);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject MovieSearch(String id) {
        movieid = id;
        final String methodPath = "https://api.themoviedb.org/3/movie/" + movieid + "?api_key=" + API_KEY_TMDB;
        Request.Builder builder = new Request.Builder();
        builder.url(methodPath);
        Request request = builder.get().build();
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            jsonObject = new JSONObject(results);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject MovieCredits(String id) {
        movieid = id;
        final String methodPath = "https://api.themoviedb.org/3/movie/" + movieid + "/credits?api_key=" + API_KEY_TMDB;
        Request.Builder builder = new Request.Builder();
        builder.url(methodPath);
        Request request = builder.get().build();
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            jsonObject = new JSONObject(results);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public String GetImage(String id) {
        String image_url = "";
        JSONObject url_jsonObject = new JSONObject();
        JSONArray poster_jsonArray = new JSONArray();
        JSONObject poster = new JSONObject();
        final String methodPath = "https://api.themoviedb.org/3/movie/" + id + "/images?api_key=" + API_KEY_TMDB;
        Request.Builder builder = new Request.Builder();
        builder.url(methodPath);
        Request request = builder.get().build();
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            url_jsonObject = new JSONObject(results);
            poster_jsonArray = url_jsonObject.getJSONArray("posters");
            poster = poster_jsonArray.getJSONObject(0);
            image_url = poster.get("file_path").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return image_url;
    }

    public JSONObject getHomeImage() {
        JSONObject postJo = new JSONObject();
        final String methodPath = "https://api.themoviedb.org/3/discover/movie?api_key=85a553f60ebf6964a7c6ac8762e05c16&sort_by=popularity.desc&page=1";
        Request.Builder builder = new Request.Builder();
        builder.url(methodPath);
        Request request = builder.get().build();
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            JSONArray resultJa = new JSONObject(results).getJSONArray("results");
            postJo = resultJa.getJSONObject(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return postJo;
    }

    public JSONArray getTopMovies(String pid){
        JSONArray jsonArray = new JSONArray();
        final String methodPath = "restws.memoir/approachF/" + pid;
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.get().build();
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            jsonArray = new JSONArray(results);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    public JSONArray getUserMemoir(String pid){
        JSONArray jsonArray = new JSONArray();
        final String methodPath = "restws.memoir/findByPID/" + pid;
        Request.Builder builder = new Request.Builder();
        builder.url(BASE_URL + methodPath);
        Request request = builder.get().build();
        try {
            Response response = client.newCall(request).execute();
            results = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            jsonArray = new JSONArray(results);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }
}
