package com.project.namecard.connection;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class CardClickViewRequest extends StringRequest {

    final static String URL = "https://rnryehd111.cafe24.com/NameCard/CardClickViewRequest.php";

    private Map<String, String> map;

    public CardClickViewRequest(String CardID, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map =  new HashMap<>();
        map.put("CardID", CardID);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}

