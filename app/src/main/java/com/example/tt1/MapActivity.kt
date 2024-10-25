package com.example.tt1

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var placesClient: PlacesClient
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private var selectedLocation: LatLng? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        // Obtiene el fragmento del mapa
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Inicializa Places
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, getString(R.string.google_maps_key))
        }
        placesClient = Places.createClient(this)

        // Configura el botón flotante para enviar la ubicación seleccionada
        findViewById<FloatingActionButton>(R.id.fab_select_location).setOnClickListener {
            if (selectedLocation != null) {
                // Envía la ubicación seleccionada a la pantalla de evento
                val intent = Intent()
                intent.putExtra("latitude", selectedLocation!!.latitude)
                intent.putExtra("longitude", selectedLocation!!.longitude)
                Log.d("MapActivity", "Ubicación seleccionada: ${selectedLocation!!.latitude}, ${selectedLocation!!.longitude}")
                setResult(Activity.RESULT_OK, intent)
                finish() // Cierra MapActivity
            } else {
                Toast.makeText(this, "Por favor, selecciona una ubicación en el mapa.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        // Configura la capa de "Mi Ubicación"
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            map.isMyLocationEnabled = true
        } else {
            checkLocationPermission() // Verifica los permisos
        }

        // Establece la ubicación inicial en el mapa
        val initialLocation = LatLng(-34.0, 151.0)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation, 10f))

        setupAutocomplete() // Configura el autocompletado

        // Configura el listener para seleccionar una ubicación en el mapa
        map.setOnMapClickListener { latLng ->
            // Agrega un marcador en la ubicación seleccionada
            map.clear() // Limpia marcadores previos si es necesario
            map.addMarker(MarkerOptions().position(latLng).title("Ubicación seleccionada"))
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
            selectedLocation = latLng // Guarda la ubicación seleccionada
        }
    }

    // Verifica y solicita los permisos de ubicación
    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    // Configura el fragmento de autocompletado para búsqueda de lugares
    private fun setupAutocomplete() {
        val autocompleteFragment = supportFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                as AutocompleteSupportFragment

        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG))

        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                // Agrega un marcador en la ubicación seleccionada
                val latLng = place.latLng
                if (latLng != null) {
                    map.clear() // Limpia marcadores previos si es necesario
                    map.addMarker(MarkerOptions().position(latLng).title(place.name))
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                    selectedLocation = latLng // Guarda la ubicación seleccionada
                }
            }

            override fun onError(status: Status) {
                Toast.makeText(this@MapActivity, "Error: ${status.statusMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Maneja la respuesta de solicitud de permisos
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    map.isMyLocationEnabled = true
                }
            } else {
                Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
