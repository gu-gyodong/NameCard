package com.project.namecard.Connection;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class MainFragmentCardRequest extends StringRequest {

    final static String URL = "https://rnryehd111.cafe24.com/NameCard/FragmentCardListRequest.php";

    private Map<String, String> map;

    public MainFragmentCardRequest(String DBname, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("DBname", DBname);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
