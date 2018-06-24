package sigame.com.br.sigame.retrofit;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sigame.com.br.sigame.service.TipoUsuarioService;

public class RetrofitInicializador {
    private Retrofit retrofit;

    public RetrofitInicializador(){
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .create();


        retrofit = new Retrofit.Builder()
                .baseUrl("localhost:8080/sigame/API/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

    }


    public TipoUsuarioService getTipoUsuario(){
        return retrofit.create(TipoUsuarioService.class);
    }


}
