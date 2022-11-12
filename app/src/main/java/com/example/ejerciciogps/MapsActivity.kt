package com.example.ejerciciogps

import android.app.WallpaperColors.fromBitmap
import android.graphics.BitmapFactory
import android.graphics.Camera
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.ejerciciogps.Coordenadas.cossmil
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
import com.example.ejerciciogps.databinding.ActivityMapsBinding
import com.google.android.gms.maps.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
    GoogleMap.OnMarkerDragListener {

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
        //Activar el escuchador del conjunto de botones:
        setupToggleButtons()
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


        /**
         * Estilo personalizado de mapa
         */
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.my_map_style))

        /**
         * Configuración y personalización de marcadores
         */
            val univalleMarcador = mMap.addMarker(MarkerOptions()
                .title("Mi universidad")
                .position(univalle)
            )
        univalleMarcador?.run {
            //setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))//Cambiar color marcador con opciones default


            //setIcon(BitmapDescriptorFactory.defaultMarker(161f))//Cambiar color marcador con color custom
            //setIcon(BitmapDescriptorFactory.fromResource(R.drawable.markercar))//Cambiar marcador con diseño personalizado

            //Cambiar marcador por ícono de Bitmap:
            Utils.getBitmapfromVector(this@MapsActivity, R.drawable.ic_baseline_house_64)?.let{
                setIcon(BitmapDescriptorFactory.fromBitmap(it))
            }

            rotation = 30f //Hacer que el marcador tenga rotación
            isFlat = true //Hacer que el marcador rote o no rote con el mapa
            setAnchor(0.5f, 0.5f) //Hacer que rote desde algún punto
            isDraggable = true
        }

        //Eventos en marcadores
        mMap.setOnMarkerClickListener(this)
        //Cuando la interfaz a implementar tiene muchos métodos es mejor con la forma clásica
        mMap.setOnMarkerDragListener(this)

        /**
         * Trazado de líneas, áreas y círculos en mapa
         * Trazar una línea entre dos puntos se llama Polyline
         */
        /*setupPolyline()
        setupSecondPolyline()*/

        /**
         * Dibujo de áreas Polygon
         */
        setupPolygonArea()

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

    private fun setupPolygonArea() {
        val polygon = mMap.addPolygon(PolygonOptions()
            .geodesic(true)
            .clickable(true)
            .fillColor(Color.WHITE) //Color de relleno de área
            .strokeColor(Color.RED) //Color de los límites de la línea
            .add(univalle, stadium, hospitalObrero) //Puntos límite
    )
    }

    private fun setupPolyline() {
        //Las líneas polyline dependen de una lista o array de coordenadas
        val misRutas = mutableListOf(univalle, stadium, hospitalObrero)
        //val misRutas = mutableListOf(univalle, stadium)
        val polyline = mMap.addPolyline(PolylineOptions()
            .color(Color.RED)
            .width(10f)
            .clickable(true)
            .geodesic(true) //Curvatura respecto al largo de la tierra
        )
        //polyline.points = misRutas

        //Trazar rutas en tiempo real simulando movimiento
        //Se pueden usar hilos o corrutinas
        lifecycleScope.launch{
            val misRutasEnTiempoReal = mutableListOf<LatLng>()
            for (punto in misRutas){
                misRutasEnTiempoReal.add(punto)
                polyline.points = misRutasEnTiempoReal
                delay(2_000)
            }
        }
        //Poner información o descripción en la línea
        polyline.tag = "Ruta Univalle a Hospital Obrero"
        //Configuración del estilo de las uniones entre las líneas
        polyline.jointType = JointType.ROUND/*BEVEL*/
        //polyline.width = 100f
        //Como configurar el patrón de la forma de la línea
        // 1) línea contínua
        // 2) línea punteada
        // 3) segmentos de línea
        polyline.pattern = listOf(Dot(), Gap(32f), Dash(64f), Gap(32f))
        /*MI VERSIÓN INEFICIENTE*//*
        lifecycleScope.launch{
            delay(3_500)
            polyline.points = misRutas
            delay(3_500)
            misRutas.add(hospitalObrero)
            polyline.points = misRutas

            delay(3_500)
            misRutas.add(torresMall)
            polyline.points = misRutas
        }*/
    }

    private fun setupSecondPolyline() {
        val miRutaPersonalizada = mutableListOf(cossmil, torresMall)
        val secondPolyline = mMap.addPolyline(PolylineOptions()
            .color(Color.BLUE)
            .width(15f)
            .clickable(true)
            .geodesic(true)
            .jointType(JointType.ROUND)
        )
        secondPolyline.tag = "Mi ruta Cossmil a Torres Mall"
        secondPolyline.points = miRutaPersonalizada
        //Configurar un evento click sobre la línea
        mMap.setOnPolylineClickListener {
            Toast.makeText(this, "${it.tag}", Toast.LENGTH_SHORT).show()
        }

        //Configurar inicio y fin de la línea:
        /*secondPolyline.startCap = RoundCap()
        secondPolyline.endCap = SquareCap()*/
        //Poner íconos de inicio y fin de la línea
        Utils.getBitmapfromVector(this, R.drawable.ic_baseline_military_tech_32)?.let{
            secondPolyline.startCap = CustomCap(BitmapDescriptorFactory.fromBitmap(it))
        }
        Utils.getBitmapfromVector(this, R.drawable.ic_baseline_local_mall_32)?.let{
            secondPolyline.endCap = CustomCap(BitmapDescriptorFactory.fromBitmap(it))
        }
    }

    private fun setupToggleButtons() {
        binding.toggleGroup.addOnButtonCheckedListener {
                group, checkedId, isChecked ->
            if (isChecked) {
                mMap.mapType = when(checkedId) {
                    R.id.btnNormal -> GoogleMap.MAP_TYPE_NORMAL
                    R.id.btnHibrido -> GoogleMap.MAP_TYPE_HYBRID
                    R.id.btnSatelital -> GoogleMap.MAP_TYPE_SATELLITE
                    R.id.btnTerreno -> GoogleMap.MAP_TYPE_TERRAIN
                    else -> GoogleMap.MAP_TYPE_NONE
                }
            }
        }
    }

    //Click al marcador
    override fun onMarkerClick(marker: Marker): Boolean {
        //marker es el marcador al que se está haciendo click
        Toast.makeText(this, "${marker.position.latitude}, ${marker.position.longitude}", Toast.LENGTH_SHORT).show()
        /**LAYOUT INFLATER PARA MARCADOR CON IMAGEN*/
        //mMap.setInfoWindowAdapter(CustomInfoWindowAdapter(layoutInflater, univalle))
        mMap.setInfoWindowAdapter(CustomInfoWindowAdapter(layoutInflater, marker.position, marker.title.toString()))
        return false
    }

    override fun onMarkerDrag(marker: Marker) {
        //Mientras se mueve el marcador
        binding.toggleGroup.visibility = View.INVISIBLE //Botones invisibles
        marker.alpha = 0.4f/*0.5f*/ //Transparencia del marcador
    }

    override fun onMarkerDragEnd(marker: Marker) {
        binding.toggleGroup.visibility = View.VISIBLE
        marker.alpha = 1.0f
        //Vuelve a la normalidad al soltar marcador
        //Los marcadores tienen una ventana de informaciones llamada info windows
        marker.showInfoWindow()
    }

    override fun onMarkerDragStart(marker: Marker) {
        //Ocultar infoWindow()
        marker.hideInfoWindow()
        //Al iniciar arrastre
    }
}