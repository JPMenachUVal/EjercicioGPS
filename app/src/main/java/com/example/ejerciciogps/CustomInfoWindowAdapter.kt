package com.example.ejerciciogps

import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker


class CustomInfoWindowAdapter(private val inflater: LayoutInflater, val posicion: LatLng, val titulo:String) : InfoWindowAdapter {
    override fun getInfoContents(m: Marker): View? {
        //Carga layout personalizado.
        val v: View = inflater.inflate(R.layout.infowindow_layout, null)
        if (titulo!="Mi universidad")
            (v.findViewById(R.id.info_window_imagen) as ImageView).setBackgroundResource(R.drawable.ic_baseline_local_mall_32)
        else
            (v.findViewById(R.id.info_window_imagen) as ImageView).setBackgroundResource(R.drawable.ic_baseline_house_64)
        (v.findViewById(R.id.info_window_nombre) as TextView).text = titulo
        (v.findViewById(R.id.info_window_posi) as TextView).text = "${posicion.latitude}, ${posicion.longitude}"
        return v
    }

    override fun getInfoWindow(m: Marker): View? {
        return null
    }

    companion object {
        private const val TAG = "CustomInfoWindowAdapter"
    }
}