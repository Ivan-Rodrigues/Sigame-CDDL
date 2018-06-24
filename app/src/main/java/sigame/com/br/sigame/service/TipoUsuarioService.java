package sigame.com.br.sigame.service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import sigame.com.br.sigame.model.TipoUsuario;

public interface TipoUsuarioService {


    @GET("listarTipoUsuario")
    Call<List<TipoUsuario>> getTipoUsuario();

}
