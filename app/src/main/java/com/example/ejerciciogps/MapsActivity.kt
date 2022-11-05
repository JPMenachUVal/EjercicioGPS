package com.example.ejerciciogps

import android.graphics.Camera
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.ejerciciogps.Coordenadas.feria16Julio
import com.example.ejerciciogps.Coordenadas.hospitalObrero
import com.example.ejerciciogps.Coordenadas.lapaz
import com.example.ejerciciogps.Coordenadas.monticulo
import com.example.ejerciciogps.Coordenadas.perez
import com.example.ejerciciogps.Coordenadas.plazaAvaroa
import com.example.ejerciciogps.Coordenadas.plazaMurillo
import com.example.ejerciciogps.Coordenadas.plazaTriangular
import com.example.ejerciciogps.Coordenadas.stadium
import com.example.ejerciciogps.Coordenadas.torresMall
import com.example.ejerciciogps.Coordenadas.univalle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.ejerciciogps.databinding.ActivityMapsBinding
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLngBounds
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    //Objeto que contiene al mapa de Google y es una variable global de la clase
    private lateinit var mMap: GoogleMap
    //Binding
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Clave para hacer funcionar la funci+on de pixeles de botones.
        Utils.binding = binding

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


        /**
         * Delimitar el zoom permitido en el mapa
         * */
        mMap.apply {
            setMinZoomPreference(15f)
            setMaxZoomPreference(20f)
        }


        /**
         * Configuración personalizada de cámara
         * */
        /*val camaraPersonalizada = CameraPosition.Builder()
            .target(univalle)
            .zoom(17f)
            .tilt(45f) //Ángulo de inclinación de la cámara.
            .bearing(245f) //Ángulo de orientación respecto al norte geográfico
            .build()

        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(camaraPersonalizada))
        */

        /**
         * Movimiento de la cámara usando corrutinas (similares a hilos o procesos en background)
         */
       /*mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(univalle, 17f))
        //uso de corrutinas......
        lifecycleScope.launch {
            delay(5000)
            //animateCamewra: Transición de movimiento en el mapa
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(stadium,17f))


            delay(5000)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(perez,17f))

            delay(5000)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(torresMall,17f))

            delay(5000)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(feria16Julio,17f))

            delay(5000)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(plazaMurillo,17f))

            delay(5000)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(plazaAvaroa,17f))

            delay(5000)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(monticulo,17f))

            delay(5000)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(plazaTriangular,17f))
            mMap.addMarker(MarkerOptions()
                .title("Plaza triangular")
                .snippet("$plazaTriangular")//Contenido extra
                .position(plazaTriangular)
            )
        }*/

        /**
         * Movimiento de la cámara por pixeles en pantalla
         * */
        /*lifecycleScope.launch{
            delay(5000)
            for(i in 0 .. 50) {
                mMap.animateCamera(CameraUpdateFactory.scrollBy(0f,120f))
                delay(500)
            }
        }*/

        /**
         * Limitación del área de acción BOUNDS del mapa.
         * */
        ///Bounds utiliza una posición suroeste y otra noroeste para definir el área de acción:
        val lapazBounds = LatLngBounds(torresMall, hospitalObrero)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lapaz,12f))
        lifecycleScope.launch{
            delay(3_500)
            //Del área delimitada se puede acceder al punto central con center.
            ///mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lapazBounds.center,18f))
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(lapazBounds,32))
        }
        mMap.setLatLngBoundsForCameraTarget(lapazBounds)

        /**
         * Establecer los controles de UI del mapa y los Gestures.
         */
        mMap.uiSettings.apply {
            isZoomControlsEnabled = true //Botones + - (Zoom IN/OUT)
            isCompassEnabled = true //Brújula
            isMapToolbarEnabled = true //Habilita la opción de ruta o verlo en la aplicación de GoogleMaps
            isRotateGesturesEnabled = false //Deshabilitar la rotación del mapa
            isTiltGesturesEnabled = false //Deshabilitar la opción de rotación de la cámara
            isZoomGesturesEnabled = false //Deshabilitar el zoom con los dedos.
        }

        //Establecer padding al mapa
        mMap.setPadding(0,0,0,Utils.dp(64)) //Densidad de pixeles en pantalla

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