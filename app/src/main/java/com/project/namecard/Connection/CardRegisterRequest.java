package com.project.namecard.Connection;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class CardRegisterRequest extends StringRequest {

    final static String URL = "https://rnryehd111.cafe24.com/NameCard/CardRegisterRequest.php";

    private Map<String, String> map;

    public CardRegisterRequest(String CardImage, String ID, String Owner, String Name, String Company, String Depart, String Position,
                               String CompanyNumber, String PhoneNumber, String Email, String FaxNumber,
                               String Address, String Memo, String DBname, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("CardImage", CardImage);
        map.put("ID", ID);
        map.put("Owner", Owner);
        map.put("Name", Name);
        map.put("Company", Company);
        map.put("Depart", Depart);
        map.put("Position", Position);
        map.put("CompanyNumber", CompanyNumber);
        map.put("PhoneNumber", PhoneNumber);
        map.put("Email", Email);
        map.put("FaxNumber", FaxNumber);
        map.put("Address", Address);
        map.put("Memo", Memo);
        map.put("DBname", DBname);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
