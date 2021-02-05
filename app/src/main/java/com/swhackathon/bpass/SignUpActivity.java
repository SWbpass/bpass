package com.swhackathon.bpass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import com.swhackathon.bpass.network.RequestInterface;
import com.swhackathon.bpass.network.RequestToServer;
import com.swhackathon.bpass.network.data.requestdata.RequestSignup;
import com.swhackathon.bpass.network.data.responsedata.AdressList;
import com.swhackathon.bpass.network.data.responsedata.ResponseAdressData;
import com.swhackathon.bpass.network.data.responsedata.ResponseSignup;

import java.util.HashMap;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageButton btn_adress;
    private Button btn_signup;
    private EditText et_id, et_pwd, et_name, et_store_name, et_phone, et_store_number, et_adress;
    private TextView tv_adress, textView16, textView17, textView18, textView19;
    private EditText et_adress2;
    private RequestInterface naver = RequestToServer.getNaver_map().create(RequestInterface.class);
    private RequestInterface service = RequestToServer.getRetrofit().create(RequestInterface.class);
    private AdressList myData;
    private Double latitude, longitude;
    private int who;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        toolbar = findViewById(R.id.toolbar);
        btn_adress = (ImageButton) findViewById(R.id.btn_adress);
        et_id = (EditText) findViewById(R.id.et_email);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        et_name = (EditText) findViewById(R.id.et_name);
        et_store_name = (EditText) findViewById(R.id.et_store_name);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_store_number = (EditText) findViewById(R.id.et_store_number);
        et_adress = (EditText) findViewById(R.id.et_adress);
        tv_adress = (TextView) findViewById(R.id.tv_adress);
        et_adress2 = (EditText) findViewById(R.id.et_adress2);
        btn_signup = (Button) findViewById(R.id.btn_signup);
        textView16 = findViewById(R.id.textView16);
        textView17 = findViewById(R.id.textView17);
        textView18 = findViewById(R.id.textView18);
        textView19 = findViewById(R.id.textView19);

        who = getIntent().getIntExtra("Who",0);

        if(who == 1){
            et_store_name.setVisibility(View.GONE);
            et_store_number.setVisibility(View.GONE);
            textView16.setVisibility(View.GONE);
            textView17.setVisibility(View.GONE);
            textView18.setVisibility(View.GONE);
            textView19.setVisibility(View.GONE);
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_before);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        sharedPreferences = this.getSharedPreferences("TOKEN", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        btn_adress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> headerMap = new HashMap<String, String>();
                headerMap.put("X-NCP-APIGW-API-KEY-ID", getString(R.string.client_id));
                headerMap.put("X-NCP-APIGW-API-KEY", getString(R.string.client_secret));
                naver.requestAdress(headerMap,et_adress.getText().toString()).enqueue(new Callback<ResponseAdressData>() {
                    @Override
                    public void onResponse(Call<ResponseAdressData> call, Response<ResponseAdressData> response) {
                        if(response.isSuccessful()){
                            ResponseAdressData adressData = response.body();
                            if(adressData.getAddresses().size() == 0)
                                Toast.makeText(SignUpActivity.this, "다시 정확히 입력해 주세요.", Toast.LENGTH_SHORT).show();
                            else {
                                myData = adressData.getAddresses().get(0);
                                tv_adress.setText(myData.getRoadAddress());
                                latitude = myData.getY();
                                longitude = myData.getX();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseAdressData> call, Throwable t) {
                        Toast.makeText(SignUpActivity.this, "서버 연결에 실패했습니다.", Toast.LENGTH_SHORT).show();
                        Log.e("서버 연결에 실패했습니다.", t.getMessage());
                    }
                });
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = et_id.getText().toString();
                String password = et_pwd.getText().toString();
                String name = et_name.getText().toString();
                String phoneNumber = et_phone.getText().toString();
                String address = tv_adress.getText().toString() + " " + et_adress2.getText().toString();
                String role;
                String storePhoneNumber;
                String storeName;
                Double x;
                Double y;
                if(who == 0) {
                    role = "ROLE_STORE";
                    storePhoneNumber = et_store_number.getText().toString();
                    storeName = et_store_name.getText().toString();
                    x = longitude;
                    y = latitude;
                    register(new RequestSignup(id, password, name, phoneNumber, address, role, storePhoneNumber, storeName, y, x));
                }
                else {
                    role = "ROLE_USER";

                    register(new RequestSignup(id, password, name, phoneNumber, address, role));
                }
            }
        });
    }

    private void register(RequestSignup data){
        service.userJoin(data).enqueue(new Callback<ResponseSignup>(){

            @Override
            public void onResponse(Call<ResponseSignup> call, Response<ResponseSignup> response) {
                Log.d("회원가입", response.toString());
                if(response.isSuccessful()){
                    editor.putString("token", response.body().getAccessToken());
                    editor.apply();
                    editor.commit();
                    finish();
                }
                else
                    Toast.makeText(SignUpActivity.this, "회원가입에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseSignup> call, Throwable t) {
                Toast.makeText(SignUpActivity.this, "서버 연결에 실패했습니다.", Toast.LENGTH_SHORT).show();
                Log.e("서버 연결에 실패했습니다.", t.getMessage());
            }
        });
    }
}