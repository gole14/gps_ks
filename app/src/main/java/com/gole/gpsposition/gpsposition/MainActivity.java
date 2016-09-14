package com.gole.gpsposition.gpsposition;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.http.HttpResponseCache;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Toast.makeText(this, "GPS activo", Toast.LENGTH_SHORT).show();
        }else{
            mostrarAlertaGPSDesactivadoAlUsuario();
        }

    }

    private void mostrarAlertaGPSDesactivadoAlUsuario() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Es necesario activar el GPS para el correcto funcionamiento de la aplicación. ¿Desea activarlo?")
                .setCancelable(false)
                .setPositiveButton("Ir a configuración",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent callGPSSettingIntent = new Intent(
                                        Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    public void buttonGetCoor(View v){
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(mLastLocation != null){
            Log.d("Latitud: ",String.valueOf(mLastLocation.getLatitude()));
            Log.d("Longitud: ",String.valueOf(mLastLocation.getLongitude()));
            Toast.makeText(getApplicationContext(),"Latitud: "+String.valueOf(mLastLocation.getLatitude()) + '\n'
                    +"Longitud: "+String.valueOf(mLastLocation.getLongitude()), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d("CONECTADO", "Conexión exitosa a la API");
        Toast.makeText(getApplicationContext(),"Conexión exitosa a Google Play Services", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("Failed", connectionResult.toString());
        Toast.makeText(getApplicationContext(),"No se pudo conectar", Toast.LENGTH_LONG).show();
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }
}
