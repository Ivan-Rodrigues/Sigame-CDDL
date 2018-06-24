package sigame.com.br.sigame.retrofit;


import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import sigame.com.br.sigame.service.TipoUsuarioService;
import sigame.com.br.sigame.service.UsuarioService;

public class RetrofitInicializador {
    private Retrofit retrofit;

    public RetrofitInicializador(){



        retrofit = new Retrofit.Builder()
                .baseUrl("http://extranet1.seap.ma.gov.br:8080/sigame/API/")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

    }


    public TipoUsuarioService getTipoUsuario(){
        return retrofit.create(TipoUsuarioService.class);
    }

    public UsuarioService salvarUsuario(){
        return  retrofit.create(UsuarioService.class);
    }

}
