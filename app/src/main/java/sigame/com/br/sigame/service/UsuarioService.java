package sigame.com.br.sigame.service;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import sigame.com.br.sigame.model.Usuario;

public interface UsuarioService {

    @POST("salvarUsuario")
    Call<Usuario> salvarUsuario(@Body Usuario usuario);

}
