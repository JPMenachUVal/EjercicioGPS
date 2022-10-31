package com.example.ejerciciogps

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SharedMemory
import com.example.ejerciciogps.Constantes.KEY_NAME
import com.example.ejerciciogps.Constantes.KEY_USER
import com.example.ejerciciogps.databinding.ActivityPersistenciaBinding

class PersistenciaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPersistenciaBinding

    //Atributos para trabajar con persistencia estilo Android
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersistenciaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeSharedPreference()
        loadData()
        binding.btnGuardar.setOnClickListener{
            saveData()
        }
    }

    //Configurar lo necesario para crear el archivo persistente.
    //Primero busca el archivo, si no existe crea un nuevo archivo, pero si
    // existe va a editar ese archivo.
    private fun initializeSharedPreference() {
        //MODE_PRIVATE: Solo esta app puede usar ese archivo
        sharedPreferences = getSharedPreferences("datos", MODE_PRIVATE)
        //Abriendo el archivo con permisos de escritura
        editor = sharedPreferences.edit()
    }

    private fun saveData() {
        val nombreCompleto = binding.etNombreCompleto.text.toString()
        editor.apply{
            //Cada dato se guarda en un registro de formato clave valor
            putString(KEY_NAME,nombreCompleto)
            putBoolean(KEY_USER,true)
        }.apply()
        //.apply() es un guardado asíncrono
        //.commit() es un guardado síncrono
    }

    private fun loadData() {
        val nombre = sharedPreferences.getString(KEY_NAME, "vacío")
        binding.txtPersistencia.text = nombre
    }
}