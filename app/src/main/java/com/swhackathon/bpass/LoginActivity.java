package com.swhackathon.bpass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.swhackathon.bpass.network.RequestInterface;
import com.swhackathon.bpass.network.RequestToServer;
import com.swhackathon.bpass.network.data.requestdata.RequestLogin;
import com.swhackathon.bpass.network.data.responsedata.ResponseSignup;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private Button login;
    private Button signup;
    private EditText et_id, et_pwd;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private RequestInterface service = RequestToServer.getRetrofit().create(RequestInterface.class);
    private int who;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        who = getIntent().getIntExtra("Who",0);

        login = (Button) findViewById(R.id.btn_login);
        signup = (Button) findViewById(R.id.btn_signup);
        et_id = (EditText) findViewById(R.id.et_id);
        et_pwd = (EditText) findViewById(R.id.et_pwd);

        sharedPreferences = this.getSharedPreferences("TOKEN", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = et_id.getText().toString();
                String pwd = et_pwd.getText().toString();
                String role;
                if(who == 0)
                    role = "ROLE_STORE";
                else
                    role = "ROLE_USER";
                Login(new RequestLogin(id, pwd, role));
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                intent.putExtra("Who",who);
                startActivity(intent);
            }
        });
    }

    private void Login(RequestLogin requestLogin){
        String header = "Bearer" + " " + sharedPreferences.getString("token", "token");
        service.requestLogin(header,requestLogin).enqueue(new Callback<ResponseSignup>(){

            @Override
            public void onResponse(Call<ResponseSignup> call, Response<ResponseSignup> response) {
                Log.d("통신 >> ", response.toString());
                if(response.isSuccessful()){
                    /*if(who==0)
                        editor.putString("name", response.body().getAccessToken());
                    else
                        editor.putString("name", response.body().getAccessToken());
                    editor.putString("email", response.body().getAccessToken());
                    editor.apply();
                    editor.commit();*/
                    Intent intent = new Intent(getApplicationContext(), ChoiceActivity.class);
                    intent.putExtra("role",who);
                    startActivity(intent);
                    finish();
                }
                else
                    Toast.makeText(LoginActivity.this, response.message(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseSignup> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "서버 연결에 실패했습니다.", Toast.LENGTH_SHORT).show();
                Log.e("서버 연결에 실패했습니다.", t.getMessage());
            }
        });
    }
}