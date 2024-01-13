package com.example.mypokedex;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mypokedex.api.ApiService;
import com.example.mypokedex.models.Pokemon;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    private Retrofit retrofit;
    private int pokeNumber = 1;
    private ImageButton btnBack, btnForward;
    private TextView txtPokeName;
    private ImageView img;

    private String imageRequest = "https://raw.githubusercontent" +
            ".com/PokeAPI/sprites/master/sprites/pokemon/versions/generation-v/black-white/animated/";


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnBack = findViewById(R.id.btn_back);
        btnForward = findViewById(R.id.btn_forward);
        img = findViewById(R.id.img_pokemon);
        txtPokeName = findViewById(R.id.txt_name);


        btnBack.setOnClickListener(v -> {
            if(pokeNumber == 1) {
                return;
            }
            pokeNumber--;
            getData();
        });

        btnForward.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                pokeNumber++;
                getData();
            }
        });


        retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        getData();

    }

    private void getData() {
        ApiService service = retrofit.create(ApiService.class);
        Call<Pokemon> responseCall = service.getPokemon(pokeNumber);

        responseCall.enqueue(new Callback<Pokemon>() {
            @Override
            public void onResponse(Call<Pokemon> call, Response<Pokemon> response) {
                if(!response.isSuccessful()) {
                    Log.e(TAG, "on Response failed: " + response.code());
                    Log.e(TAG, "on Response failed: " + response.errorBody());
                }
                Pokemon pokemon = response.body();
                Log.d(TAG, "poke:" + pokemon.getName());
                Log.d(TAG, "poke:" + pokemon.getOrder());
                Log.d(TAG, "poke:" + pokemon.getSprites().getOther().getDream_world().getFront_default());

                txtPokeName.setText(pokemon.getName() + " - " + pokemon.getOrder());
                setPokemonImage();

            }

            @Override public void onFailure(Call<Pokemon> call, Throwable t) {
                Log.e(TAG, "onFailure: "+ t.getMessage());
            }
        });
    }

    private void setPokemonImage() {
        Glide.with(this)
            .load(imageRequest + pokeNumber + ".gif")
            .into(img);
    }
}