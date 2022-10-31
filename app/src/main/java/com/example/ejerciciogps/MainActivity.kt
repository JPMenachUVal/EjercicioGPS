package com.example.ejerciciogps

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.location.LocationRequest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PackageManagerCompat
import com.example.ejerciciogps.Constantes.INTERVAL_TIME
import com.example.ejerciciogps.databinding.ActivityMainBinding
import com.google.android.gms.location.*
import dalvik.system.PathClassLoader
import java.lang.Exception
import java.security.Permission
import kotlin.math.*

class MainActivity : AppCompatActivity() {

    //OJO: esto no es extremamente necesario para esta variable,
    // es una aplicación de companion object.
    // Companion Object define constantes a un nivel global en su clase
    // constantes quie no se requieran inicializar en cada instancia de la clase
    // Sirve para definir constantes globales de acceso general que se quiera tener en la clase
    companion object {
        val REQUIRED_PERMISSION_GPS = arrayOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_NETWORK_STATE
        )
    }

    private lateinit var binding: ActivityMainBinding
    private var isGpsEnabled = false
    private val PERMISSION_ID = 42
    //Variable que vamos a usar para gestionar el GPS con Google Play Services
    //FusedLocation: fusionar los datos respectivos para GPS en un objeto
    private lateinit var fusedLocation: FusedLocationProviderClient
    private var latitud: Double = 0.0
    private var longitud: Double = 0.0
    private var distance: Double = 0.0
    private var velocity: Double = 0.0
    private var contador = 0.0
    private var contadorDistancia = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.fabGPS.setOnClickListener{
            enableGPSService()
        }
        binding.fabCoordenadas.setOnClickListener{
            manageLocation()
        }
    }

    /**
    * Sección: Tratamiento de localización
    * obtención de coordenadas
    */
    @SuppressLint("MissingPermission")
    private fun manageLocation() {
        if (hasGPSEnabled()) {
            if (allPermissionsGrantedGPS()) {
                //fusedlocation solo funciona si el usuario aceptó los permisos de ubicación
                fusedLocation = LocationServices.getFusedLocationProviderClient(this)
                //Evento que escuche cuando se capture datos del sensor correctamente
                fusedLocation.lastLocation.addOnSuccessListener {
                    location -> requestNewLocationData()
                }
            } else
                requestPermissionLocation()
        } else
            goToEnableGPS()
    }

    //OJOO SOLO USAR SI SE ESTÁ COMPLETAMENTE SEGURO DE QUE ENE ESTE PUNTO YA SE GARANTIZA AL 100%
    // EL ACCESO A LOS PERMISOS DADOS POR EL USUARIO.
    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        //Configurar las caracteristicas de nuestra peticion de localizacion
        //Versión 21 y su nueva manera de configurar un request
        var myLocationRequest = com.google.android.gms.location.LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            INTERVAL_TIME
        ).setMaxUpdates(50).build()

        //VERSIONES 20 PARA ABAJO
        /*var myLocationRequest = com.google.android.gms.location.LocationRequest.create().apply {
            priority = Priority.PRIORITY_HIGH_ACCURACY
            interval = 0
            fastestInterval = 0
            numUpdates = 1
        }*/
        fusedLocation.requestLocationUpdates(myLocationRequest, myLocationCallback, Looper.myLooper())
    }

    private val myLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var myLastLocation: Location? = locationResult.lastLocation
            if (myLastLocation != null) {
                var lastLatitude = myLastLocation.latitude
                var lastLongitude = myLastLocation.longitude
                if (contador > 0) {
                    distance = calculateDistance(lastLatitude,lastLongitude)
                    velocity = calculateVelocity()
                }
                /*distance = calculateDistance(lastLatitude, lastLongitude)
                velocity = calculateVelocity()*/
                contadorDistancia = contadorDistancia + distance
                /*NUEVO 21*/
                /*latitud = myLastLocation.latitude
                longitud = myLastLocation.longitude*/
                binding.apply {
                    /*VERSION 20*//*
                    txtLatitud.text = myLastLocation.latitude.toString()
                    txtLongitud.text = myLastLocation.longitude.toString()*/
                    /*VERSION 21*/
                    txtLatitud.text = latitud.toString()
                    txtLongitud.text = longitud.toString()
                    txtDistancia.text = "$distance mts."
                    txtVelocidad.text = "$velocity Km/h."
                    txtDistaciaAcu.text = "Distancia acumulada: $contadorDistancia mts."
                }
                latitud = myLastLocation.latitude
                longitud = myLastLocation.longitude
                contador++
                getAddressName()
            }
        }
    }

    private fun getAddressName() {
        //Clase Geocoder para acceder a direcciones mapeadas de Google Over The Horizon.
        val geocoder = Geocoder(this)
        try {
            //Las direcciones se obtienen en un array sin imporar que solo haya una dirección
            var direcciones = geocoder.getFromLocation(latitud, longitud, 1)
            binding.txtDireccion.text = direcciones.get(0).getAddressLine(0)
        }catch (e: Exception) {
            binding.txtDireccion.text = "No se pudo obtener la dirección"
        }
    }

    private fun calculateDistance(lastLatitude: Double, lastLongitude: Double): Double {
        //var distance = 0.0
        val earthRadius = 6371 //Kilómetros
        val diffLatitude = Math.toRadians(lastLatitude - latitud)
        val diffLongitude = Math.toRadians(lastLongitude - longitud)
        //val sinLatitude = Math.sin(diffLatitude / 2)
        val sinLatitude = sin(diffLatitude / 2)
        //val sinLongitude = Math.sin(diffLongitude / 2)
        val sinLongitude = sin(diffLongitude / 2)
        val result1 = sinLatitude.pow(2.0) + (sinLongitude.pow(2.0)
                * cos(Math.toRadians(latitud))
                * cos(Math.toRadians(lastLatitude))
                )
        val result2 = 2 * atan2(sqrt(result1), sqrt(1-result1))
        var distance = (earthRadius * result2) * 1000.0
        return distance
    }

    private fun calculateVelocity(): Double = (distance / (INTERVAL_TIME/1000.0)) * 3.6
    /*3.6 es para Kilómetro hora desde Metro sobre segundo*/

    /**
    * Sección evaluar si el GPS está activado
    */

    //Evaluate if GPS is enabled
    private fun enableGPSService() {
        if (!hasGPSEnabled()) {
            AlertDialog.Builder(this)
                .setTitle(R.string.alert_dialog_title)
                .setMessage(R.string.alert_dialog_description)
                .setPositiveButton(R.string.alert_dialog_accept,
                    DialogInterface.OnClickListener{
                            dialog, wich -> goToEnableGPS()
                    })
                .setNegativeButton(R.string.alert_dialog_deny) {
                        dialogm, wich -> isGpsEnabled = false
                }
                .setCancelable(true)
                .show()
        } else {
            Toast.makeText(this, "Ya tienes el GPS habilitado",
                Toast.LENGTH_SHORT).show()
        }
    }

    private fun hasGPSEnabled(): Boolean {
        //Castear tipo de dato
        //LocationManager: Orquesta o gestiona lo referido a localización
        // desde el ámbito del uso de las librerías de geolocalización
        val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun goToEnableGPS() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }

    /**Sección: tratamiento de permisos para el uso de GPS:*/
    private fun allPermissionsGrantedGPS(): Boolean {
        //checkSelfPermission: revisa el valor de los permisos en la app
        //packageManager.PERMISSION_GRANTED contiene el valor considerado como permiso otorgado.
        return REQUIRED_PERMISSION_GPS.all {
            ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    //Este método hace lo mismo que el anterior(allPermissionsGrantedGPS()),
    // pero es más explícito en su forma de desarrollo.
    private fun checkPermissionsGPS(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
        android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    //Solicitando los permisos de app al usuario
    private fun requestPermissionLocation() {
        ActivityCompat.requestPermissions(this, REQUIRED_PERMISSION_GPS, PERMISSION_ID)
    }
}