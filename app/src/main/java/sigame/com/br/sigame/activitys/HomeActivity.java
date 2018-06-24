package sigame.com.br.sigame.activitys;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.j256.ormlite.stmt.query.In;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import br.ufma.lsdi.cddl.CDDL;
import br.ufma.lsdi.cddl.Callback;
import br.ufma.lsdi.cddl.Publisher;
import br.ufma.lsdi.cddl.Subscriber;
import br.ufma.lsdi.cddl.message.CommandRequest;
import br.ufma.lsdi.cddl.message.ContextMessage;
import br.ufma.lsdi.cddl.message.MOUUID;
import br.ufma.lsdi.cddl.message.SensorData;
import br.ufma.lsdi.cddl.message.TechnologyID;
import br.ufma.lsdi.cddl.type.CDDLConfig;
import br.ufma.lsdi.cddl.type.ClientId;
import br.ufma.lsdi.cddl.type.Host;
import br.ufma.lsdi.cddl.type.Topic;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sigame.com.br.sigame.R;
import sigame.com.br.sigame.model.Localizacao;
import sigame.com.br.sigame.model.TipoUsuario;
import sigame.com.br.sigame.utils.PermissionUtils;


public class HomeActivity extends AppCompatActivity {


    List<String> sensorList;
    private List<TipoUsuario> tipoUsuarios;

    private final CDDL cddl = CDDL.getInstance();

    private final String clientId = "ivan.rodrigues@lsdi.ufma.br";

    private Subscriber sub;

    private Publisher pub;

    private Localizacao localizacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);


        //iniciar CDDL
        iniciarCDDL(this);

        //mostrando sensores internos
        mostrarSensores();


        //publicar dado de contexto
        //publisherContext(new ContextMessage("String","location","São Luis"));

        //subscrever tópico
        subscrever("ivan.rodrigues@lsdi.ufma.br/Location");


        //iniciar sensores
        sensorList = Arrays.asList("BMI160 Accelerometer", "Location");
        startSensores(sensorList);


    }

    @OnClick(R.id.btnTrajeto)
    public void criarTrejeto() {

        Intent intent = new Intent(this, MapsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("localizacao", localizacao);
        intent.putExtras(bundle);
        startActivity(intent);


    }


    //iniciando CDDL
    public void iniciarCDDL(Context context) {

        CDDLConfig config = CDDLConfig.builder()
                .host(Host.of("tcp://lsdi.ufma.br:1883"))
                .clientId(ClientId.of(clientId))
                .build();

        cddl.init(context, config);
        cddl.startScan();

    }


    //subscreve em um topico
    public void subscrever(String topic) {
        sub = Subscriber.of(cddl);

        sub.setCallback(new Callback() {
            @Override
            public void messageArrived(ContextMessage contextMessage) {

                Gson gson = new Gson();

                Log.d("subscriber", gson.toJson(contextMessage));


                String type = contextMessage.getType();
//                if (type.equals(SensorData.class.getSimpleName())) {
                    SensorData sensorData = gson.fromJson(contextMessage.getBody(), SensorData.class);
                    localizacao.setLatitude(sensorData.getSensorValue()[0]);
                    localizacao.setLongitude(sensorData.getSensorValue()[1]);
                    localizacao.setData(new Date());

//                }

            }

            @Override
            public void onConnectSuccess() {
                sub.subscribe(Topic.of(topic));
                Log.d("subscriber", "Conectado com sucesso");
            }

            @Override
            public void onConnectFailure(Throwable exception) {
                Log.d("subscriber", "Falha ao Conectar");
            }

            @Override
            public void onSubscribeSuccess(Topic topic) {
                Log.d("subscriber", "Subscrito com sucesso");
            }

            @Override
            public void onSubscribeFailure(Throwable cause) {
                Log.d("subscriber", "Falha ao Subscrever");
            }
        });
        sub.connect();
    }


    //publica em um tópico
    private void publisherContext(ContextMessage msg) {
        // ContextMessage contextMessage = new ContextMessage("String", "User Action", "Walking");

        pub = Publisher.of(cddl);
        pub.setCallback(new Callback() {
            @Override
            public void onPublishFailure(Throwable cause) {
                // publicado com sucesso
            }

            @Override
            public void onConnectSuccess() {
                pub.publish(msg);
            }

            @Override
            public void onConnectFailure(Throwable exception) {
                //falha na conexão
            }
        });
        pub.connect();
    }


    private void mostrarSensores() {
        List<String> sensors = cddl.getInternalSensorList();

        for (String sen : sensors) {
            Log.d("Sensors", sen);
        }
    }


    //publica em um tópico
    private void startSensores(List<String> sensorList) {

        CommandRequest comandRequest = new CommandRequest(clientId,
                new MOUUID(TechnologyID.INTERNAL.id, "localhost"),
                "start-sensors", sensorList);

        pub = Publisher.of(cddl);
        pub.setCallback(new Callback() {
            @Override
            public void onPublishFailure(Throwable cause) {
                Log.d("PUBLISHER", "Falha ao publicar");
            }

            @Override
            public void onConnectSuccess() {
                pub.publish(comandRequest);
                Log.d("PUBLISHER", "publicado");
            }

            @Override
            public void onConnectFailure(Throwable exception) {
                Log.d("PUBLISHER", "Falha ao conectar");
            }
        });
        pub.connect();
    }


    @Override
    protected void onDestroy() {
        if (cddl != null) cddl.stopScan();
        if (pub != null) pub.disconnect();
        if (sub != null) sub.disconnect();
        super.onDestroy();
    }

}
