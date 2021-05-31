package com.example.ontapthith;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManageCustomerActivity extends AppCompatActivity implements SendingData{

    RecyclerView recyclerView;
    CustomerAdapter customerAdapter;
    List<Customer> list = new ArrayList<>();
    EditText edtName;
    Button btnSave;
    Boolean isUpdate = false;
    int idUpdate = 0;

    String url = "https://60ae49ae80a61f0017332eed.mockapi.io/customer";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_customer);

        recyclerView = findViewById(R.id.rcvCustomer);

        getData();

        customerAdapter = new CustomerAdapter(list, this);
        recyclerView.setAdapter(customerAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        edtName = findViewById(R.id.main_edtName);
        btnSave = findViewById(R.id.main_btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String str = edtName.getText().toString();

                if(str.length() > 0) {
                    if (!isUpdate) {
                        StringRequest stringRequest = new StringRequest(
                                Request.Method.POST,
                                url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Toast.makeText(ManageCustomerActivity.this, "Successed", Toast.LENGTH_SHORT).show();
                                        getData();
                                        edtName.setText("");
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(ManageCustomerActivity.this, "Failed", Toast.LENGTH_SHORT).show();

                                    }
                                }
                        ) {
                            protected Map<String, String> getParams() {
                                Map map = new HashMap();

                                map.put("name", str);
                                map.put("address", "new Address");

                                return map;
                            }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(ManageCustomerActivity.this);
                        requestQueue.add(stringRequest);
                    }

                    if(isUpdate) {
                        StringRequest stringRequest = new StringRequest(
                                Request.Method.PUT,
                                url + "/" + idUpdate,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        isUpdate = false;
                                        idUpdate = 0;
                                        getData();
                                        Toast.makeText(ManageCustomerActivity.this, "Update Successed", Toast.LENGTH_SHORT).show();
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(ManageCustomerActivity.this, "Update Failed", Toast.LENGTH_SHORT).show();

                                    }
                                }
                        ){
                            protected Map<String, String> getParams() {
                                Map map = new HashMap();
                                    map.put("name", str);
                                    map.put("address", "updated addr");
                                return map;
                            }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(ManageCustomerActivity.this);
                        requestQueue.add(stringRequest);
                    }


                } else {
                    Toast.makeText(ManageCustomerActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void getData() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        list = new ArrayList<>();
                        for(int i=0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = (JSONObject) response.get(i);
                                Customer c = new Customer(
                                        Integer.parseInt(jsonObject.getString("id")),
                                        jsonObject.getString("name").toString(),
                                        jsonObject.getString("address").toString()
                                );
                                list.add(c);
                                customerAdapter = new CustomerAdapter(list, ManageCustomerActivity.this);
                                recyclerView.setAdapter(customerAdapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }

        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }


    @Override
    public void sendData(Serializable obj) {
        Customer customer = (Customer) obj;
        edtName.setText(customer.getName());
        idUpdate = customer.getId();
        isUpdate = true;
    }
}