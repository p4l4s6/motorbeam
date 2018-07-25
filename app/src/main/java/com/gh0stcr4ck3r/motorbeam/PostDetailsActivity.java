package com.gh0stcr4ck3r.motorbeam;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gh0stcr4ck3r.motorbeam.adapter.PostAdapter;
import com.gh0stcr4ck3r.motorbeam.model.PostModel;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PostDetailsActivity extends AppCompatActivity {
    private static final String Server="https://www.motorbeam.com/wp-json/wp/v2/posts/";
    TextView title;
    WebView webView;
    ImageView imageView;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        title=findViewById(R.id.postTitle);
        imageView=findViewById(R.id.postImage);



        webView=findViewById(R.id.postDetails);
        webView.getSettings().setJavaScriptEnabled(true);
        //webView.loadUrl("file:///android_asset/test.html");
        //webView.loadDataWithBaseURL(null, "<style>img{display: inline;height: auto;max-width: 100%;}figure{float: left;height: auto;width: 80%;}</style>" + "file:///android_asset/test.html", "text/html", "UTF-8", null);
        //webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);



        Intent intent= getIntent();
        id=intent.getStringExtra("data");

        syncMethod(id);
    }
    private void setupWebView() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                webView.loadUrl("javascript:MyApp.resize(document.body.getBoundingClientRect().height)");
                super.onPageFinished(view, url);
            }
        });
        webView.addJavascriptInterface(this, "MyApp");
    }

    @JavascriptInterface
    public void resize(final float height) {
        PostDetailsActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                webView.setLayoutParams(new LinearLayout.LayoutParams(getResources().getDisplayMetrics().widthPixels, (int) (height * getResources().getDisplayMetrics().density)));
            }
        });
    }

    private void syncMethod(String postid) {
        String URL=Server+postid+"/";
        StringRequest stringRequest= new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                    try {
                        JSONObject postObject= new JSONObject(response);
                        String id=postObject.get("id").toString();
                        JSONObject titleObj=(JSONObject) postObject.get("title");
                        String pTitle=titleObj.getString("rendered");

                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            title.setText(Html.fromHtml(pTitle,Html.FROM_HTML_MODE_LEGACY));
                        } else {
                            title.setText(Html.fromHtml(pTitle));
                        }

                        JSONObject ContentObj=(JSONObject) postObject.get("content");
                        String pDetails=ContentObj.getString("rendered");
                        pDetails=pDetails.replace("style=\"width: 600px\"", "style=\"width: 100%\"");
                        pDetails=pDetails.replace("\"width: 600px\"", "\"width: 100%\"");
                        pDetails=pDetails.replace("width=\"680\"", "width=\"100%\"");
                        Log.w("+++++++++",pDetails);
                        //webView.loadData(pDetails, "text/html", "UTF-8");
                        //setupWebView();

                        webView.loadDataWithBaseURL(null, "<style>img{display: inline;height: auto;max-width: 100%;}figure{display: inline;height: auto;max-width: 100%;margin:0!important;}</style>" + pDetails, "text/html", "UTF-8", null);

                        JSONObject imageObj = (JSONObject) postObject.get("better_featured_image");
                        String thumb = imageObj.getString("source_url");
                        Picasso.get().load(thumb).into(imageView);


                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.w("+++++++++++",error);
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
