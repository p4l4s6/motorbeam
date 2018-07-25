package com.gh0stcr4ck3r.motorbeam;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gh0stcr4ck3r.motorbeam.adapter.PostAdapter;
import com.gh0stcr4ck3r.motorbeam.model.PostModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ProgressDialog progressDialog;

    private static final String Server = "https://www.motorbeam.com/wp-json/wp/v2/posts?tags=597&fields=id,title.rendered,better_featured_image.source_url";

    private RecyclerView recyclerView;
    private PostAdapter adapter;

    private PostModel postModel;
    private List<PostModel> postList;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog=new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait....");
        progressDialog.setCancelable(false);
        progressDialog.show();

        recyclerView = findViewById(R.id.postListRecycler);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        postList = new ArrayList<>();

        syncMethod();
        //startActivity(new Intent(MainActivity.this,PostDetailsActivity.class));

    }

    private void syncMethod() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Server, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.setCancelable(false);
                progressDialog.dismiss();
                try {
                    JSONArray postArray = new JSONArray(response);
                    Log.w("+++++++++++", response);
                    for (int i = 0; i < postArray.length(); i++) {
                        JSONObject postObject = postArray.getJSONObject(i);
                        String id = postObject.get("id").toString();

                        JSONObject titleObj = (JSONObject) postObject.get("title");
                        String title = titleObj.getString("rendered");

                        JSONObject imageObj = (JSONObject) postObject.get("better_featured_image");
                        String thumb = imageObj.getString("source_url");

                        postModel = new PostModel(id, title, thumb);
                        postList.add(postModel);
                    }
                    adapter = new PostAdapter(postList, MainActivity.this);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.w("+++++++++++", error);
            }
        });
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


}
