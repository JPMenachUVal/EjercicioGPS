package com.example.ejerciciogps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.ejerciciogps.databinding.ActivityMapsBinding

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    //Objeto que contiene al mapa de Google y es una variable global de la clase
    private lateinit var mMap: GoogleMap
    //Binding
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        //FRAGMENTOS: Lo más avanzado de diseño en layouts. || Los fragmentos son módulos de diseño reutilizable.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        //El mapa de Google se carga asíncronamente sin congelar la pantalla (hilo principal)
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    //Si el mapa ya está listo, se devuelve en un parámetro
    // googleMap el mapa y sus
    // características
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        //Las posiciones se manejan en un objeto que conjunciona latitud y longitud
        val petraJordania = LatLng(30.3284959495876, 35.44436751713703)
        //Marcador
        mMap.addMarker(MarkerOptions()
            .title("Marcador en Petra, Jordania")
            .snippet("${petraJordania.latitude}, ${petraJordania.longitude}")//Contenido extra
            .position(petraJordania)
        )
        //Colocar la cámara virtual en la posición requerida en el mapa, se va a centrar según las
        // coordenadas en el centro de la pantalla
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(petraJordania, 17f))
        /*
        El zoom en el mapa va en un rango de 0 a 21, se le puede asignar desde 2 a 20
        a partir de 15 se usa para calles y avenidas
        A partir de 5: continentes
        A partir de 10: ciudades, países
        A partir de 20: Sirve para edificios, casas, parques, domicilios
        */

        //Los mapas tienen eventos, como los botones.
        // Se pueden configurar listeners para eventos.
        //El evento más sencillo de los mapas es el click a cualquier parte del mapa de google.
        mMap.setOnMapClickListener{
            //it es la posición donde se hace click con el dedo
            mMap.addMarker(MarkerOptions()
                .title("Nueva ubicación random")
                .snippet("${it.latitude}, ${it.longitude}")
                .position(it)
                .draggable(true)
            )
        }
        /*// Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))*/
    }
}