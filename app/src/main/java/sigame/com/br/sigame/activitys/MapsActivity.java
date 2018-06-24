package sigame.com.br.sigame.activitys;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.ufma.lsdi.cddl.CDDL;
import br.ufma.lsdi.cddl.Callback;
import br.ufma.lsdi.cddl.Publisher;
import br.ufma.lsdi.cddl.Subscriber;
import br.ufma.lsdi.cddl.message.CommandRequest;
import br.ufma.lsdi.cddl.message.ContextMessage;
import br.ufma.lsdi.cddl.message.MOUUID;
import br.ufma.lsdi.cddl.message.TechnologyID;
import br.ufma.lsdi.cddl.type.CDDLConfig;
import br.ufma.lsdi.cddl.type.ClientId;
import br.ufma.lsdi.cddl.type.Host;
import br.ufma.lsdi.cddl.type.Topic;
import retrofit2.Call;
import retrofit2.Response;
import sigame.com.br.sigame.R;
import sigame.com.br.sigame.model.TipoUsuario;
import sigame.com.br.sigame.retrofit.RetrofitInicializador;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private TextView txt_msg_robot;
    private Marker marker;
    private String queryId;
    List<String> sensorList;
    private List<TipoUsuario> tipoUsuarios;

    private final CDDL cddl = CDDL.getInstance();

    private final String clientId = "ivan.rodrigues@lsdi.ufma.br";

    private Subscriber sub;

    private Publisher pub;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        txt_msg_robot = findViewById(R.id.txt_msg_robot);
        mapFragment.getMapAsync(this);


        //iniciar CDDL
        iniciarCDDL(this);

        //mostrando sensores internos
        mostrarSensores();


        //publicar dado de contexto
        //publisherContext(new ContextMessage("String","location","São Luis"));

        //subscrever tópico
        subscrever("ivan.rodrigues@lsdi.ufma.br/Location");


        //iniciar sensores
        sensorList = Arrays.asList("BMI160 Accelerometer","Location");
        startSensores(sensorList);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        configMap();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void getTipoUsuario() throws Exception{

        tipoUsuarios = new ArrayList<TipoUsuario>();

        try {
            Call<List<TipoUsuario>> call = new RetrofitInicializador().getTipoUsuario().getTipoUsuario();
            call.enqueue(new retrofit2.Callback<List<TipoUsuario>>() {
                @Override
                public void onResponse(Call<List<TipoUsuario>> call, Response<List<TipoUsuario>> response) {
                    if (response.body() != null){
                        tipoUsuarios.addAll(response.body());
                    }
                }

                @Override
                public void onFailure(Call<List<TipoUsuario>> call, Throwable t) {

                }
            });
        }catch (Exception e){

        }

    }


    public void configMap(){

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //map.getUiSettings().setScrollGesturesEnabled(false);
         LatLng latLng = new LatLng(-2.557505, -44.307923);
        // atualizar posição da camera
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng).zoom(19).tilt(45).build();
        CameraUpdate update = CameraUpdateFactory.newCameraPosition(cameraPosition);
        //move camera
        mMap.moveCamera(update);

        addMarker(latLng,"User","Location",R.drawable.user_location);


    }

    //atualiar posição
    private void updatePosition(LatLng latLng){
        //map.animateCamera(CameraUpdateFactory.newLatLng( latLng ));
        marker.setPosition( latLng );
    }


    public Marker addMarker(LatLng latLng, String title, String snippet, int img) {
        Marker markerr = mMap.addMarker(new MarkerOptions().position(latLng)
                .title(title).snippet(snippet).draggable(false)
                .icon(BitmapDescriptorFactory.fromResource(img)));
        return markerr;
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


    private void mostrarSensores(){
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