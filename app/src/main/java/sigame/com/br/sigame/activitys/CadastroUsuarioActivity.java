package sigame.com.br.sigame.activitys;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sigame.com.br.sigame.R;
import sigame.com.br.sigame.model.Pessoa;
import sigame.com.br.sigame.model.TipoUsuario;
import sigame.com.br.sigame.model.Usuario;
import sigame.com.br.sigame.model.enums.TipoUsuarioEnum;
import sigame.com.br.sigame.retrofit.RetrofitInicializador;
import sigame.com.br.sigame.utils.Criptografia;

public class CadastroUsuarioActivity extends AppCompatActivity {

    @BindView(R.id.edtNome)
    public EditText edtNome;
    @BindView(R.id.edtEmail)
    public EditText edtEmail;
    @BindView(R.id.edtTelefone)
    public EditText edtTelefone;
    @BindView(R.id.edtSenha)
    public EditText edtSenha;
    @BindView(R.id.edtConfirmaSenha)
    public EditText edtConfSenha;

    private Usuario usuario;

    public static final String TAG = "LoginActivity";
    private final String PREFS_PRIVATE = "PREFS_PRIVATE";
    private SharedPreferences sharedPreferences;
    private Boolean isLoggedIn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);
        ButterKnife.bind(this);

        preferencies();

    }

    @OnClick(R.id.btnRegistrar)
    public void cadastrarUsuario(){
        Pessoa pessoa = new Pessoa();
        pessoa.setNome(edtNome.getText().toString().toUpperCase().trim());
        pessoa.setTelefone(edtTelefone.getText().toString());
        Usuario usuario = new Usuario();
        usuario.setEmail(edtEmail.getText().toString().trim());
        usuario.setPessoa(pessoa);
        usuario.setTipoUsuario(new TipoUsuario(TipoUsuarioEnum.PEDESTRE.getValue()));
        String confSenha = edtConfSenha.getText().toString();
        String senha = edtSenha.getText().toString();

        if (pessoa.getNome().isEmpty()){
            Toast.makeText(this, "Nome Obrigatório", Toast.LENGTH_SHORT).show();
            edtNome.setError("");
            return;
        }


        if (usuario.getEmail().isEmpty()){
            Toast.makeText(this, "Email Obrigatório", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!senha.isEmpty() && !confSenha.isEmpty() ){
            if(senha.equals(confSenha)){
                usuario.setSenha(Criptografia.md5(senha.toString().trim()));
            }else{
                Toast.makeText(this, "Senha não confere", Toast.LENGTH_SHORT).show();
                return;
            }
        }else{
            Toast.makeText(this, "Senha Obrigatório", Toast.LENGTH_SHORT).show();
            return;
        }


        try{
            Call<Usuario> call = new RetrofitInicializador().salvarUsuario().salvarUsuario(usuario);
            call.enqueue(new Callback<Usuario>() {
                @Override
                public void onResponse(Call<Usuario> call, Response<Usuario> response) {

                        Usuario user = response.body();
                        if (user != null ){
                            if(user.getId() != null){
                                Toast.makeText(getApplicationContext(), "Usuario Salvo com Sucesso", Toast.LENGTH_SHORT).show();
                                ObjectMapper mapper = new ObjectMapper();
                                String objeto = null;
                                try {
                                    objeto = mapper.writeValueAsString(usuario);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);

                            SharedPreferences.Editor preEditor = sharedPreferences.edit();
                            preEditor.putString("usuario", objeto);
                            preEditor.putBoolean("IsLoggedIn", true);
                            preEditor.commit();

                                startActivity(intent);
                                finish();
                            }
                        }

                }

                @Override
                public void onFailure(Call<Usuario> call, Throwable t) {

                }
            });


        }catch (Exception e){
            e.printStackTrace();
        }

    }


    private void preferencies(){
        sharedPreferences = getSharedPreferences(PREFS_PRIVATE, Context.MODE_PRIVATE);
        isLoggedIn = sharedPreferences.getBoolean("IsLoggedIn", false);
        if (isLoggedIn) {
            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
            startActivity(intent);
            finish();
        }
    }




}
