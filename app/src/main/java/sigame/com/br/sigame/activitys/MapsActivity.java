package sigame.com.br.sigame.activitys;

import android.content.Context;
import android.content.Intent;
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
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;
import sigame.com.br.sigame.R;
import sigame.com.br.sigame.model.Localizacao;
import sigame.com.br.sigame.model.TipoUsuario;
import sigame.com.br.sigame.retrofit.RetrofitInicializador;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;

    @BindView(R.id.txt_msg_robot)
    public TextView txt_msg_robot;

    private Marker marker;
    private String queryId;

    private Localizacao localizacao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        ButterKnife.bind(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Intent intent = new Intent();
        if (getIntent() != null) {
            intent = getIntent();
            Bundle bundle = new Bundle();
            bundle = intent.getExtras();
            localizacao = new Localizacao();
            localizacao = (Localizacao) bundle.getSerializable("localizacao");
            if (localizacao == null) {
                localizacao = new Localizacao();
                localizacao.setLatitude(-2.557505);
                localizacao.setLongitude(-44.307923);

            }

        }
        configMap(localizacao);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    public void configMap(Localizacao localizacao) {

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //map.getUiSettings().setScrollGesturesEnabled(false);
        LatLng latLng = new LatLng(localizacao.getLatitude(), localizacao.getLongitude());
        // atualizar posição da camera
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng).zoom(19).tilt(45).build();
        CameraUpdate update = CameraUpdateFactory.newCameraPosition(cameraPosition);
        //move camera
        mMap.moveCamera(update);

        addMarker(latLng, "User", "Location", R.drawable.user_location);


    }

    //atualiar posição
    private void updatePosition(LatLng latLng) {
        mMap.animateCamera(CameraUpdateFactory.newLatLng( latLng ));
        marker.setPosition(latLng);
    }


    public Marker addMarker(LatLng latLng, String title, String snippet, int img) {
        Marker markerr = mMap.addMarker(new MarkerOptions().position(latLng)
                .title(title).snippet(snippet).draggable(false)
                .icon(BitmapDescriptorFactory.fromResource(img)));
        return markerr;
    }


}