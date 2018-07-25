package com.global.test.testglobal.Activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.global.test.testglobal.Adapters.RecyclerItemAdapter;
import com.global.test.testglobal.Model.Item;
import com.global.test.testglobal.R;
import com.global.test.testglobal.Services.GlobalApiServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    RecyclerItemAdapter rcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Items");

        recuperarContraseña();
    }

    public void recuperarContraseña() {

        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(getString(R.string.base_url));

        //OkHttpClient client = ClienteRetrofit.getSSLConfig(this);
        OkHttpClient client = new OkHttpClient();
        Retrofit retrofit = builder.client(client).addConverterFactory(GsonConverterFactory.create()).build();
        retrofit.create(GlobalApiServices.class);

        recuperarContraseñaApi(retrofit);


    }

    public void recuperarContraseñaApi(Retrofit retrofit) {

        GlobalApiServices servicio = retrofit.create(GlobalApiServices.class);
        Call<ArrayList<Item>> loginCall = servicio.getItemList();
        loginCall.enqueue(new Callback<ArrayList<Item>>() {
            @Override
            public void onResponse(Call<ArrayList<Item>> call, Response<ArrayList<Item>> response) {

                if (response.isSuccessful()) {
                    gridImg(response.body());


                    //  Intent in = new Intent(MainActivity.this, HomeActividad.class);
                    // startActivity(in);
                    System.out.println("Dispara a la actividad de home");

                } else {
                    System.out.println("Error en la respuesta" + response.message());

                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        System.out.println(jsonObject.getString("Message"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ArrayList<Item>> call, Throwable t) {
                System.out.println("Falla al obtener respuesta: " + t.getMessage());
            }
        });


    }

    public void gridImg(ArrayList<Item> lotesRes) {
        GridLayoutManager lLayout = new GridLayoutManager(MainActivity.this, 1);
        RecyclerView rView = (RecyclerView) findViewById(R.id.recycler_view);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(lLayout);
        rcAdapter = new RecyclerItemAdapter(MainActivity.this,lotesRes,MainActivity.this);
        rView.setAdapter(rcAdapter);
    }
}
