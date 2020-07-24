package com.google.samples.quickstart.canonical

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat.checkSelfPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.common.api.Status

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener

class MapsFragment : Fragment() {

    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private lateinit var placesClient : PlacesClient
    private lateinit var autocompleteLayout : LinearLayout
    private lateinit var targetLatLng : LatLng
    private lateinit var targetName : String
    private lateinit var autocompleteFragment : AutocompleteSupportFragment
    private var currentLatLng : LatLng? = null
    private var targetMarker : Marker? = null


    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    setUpMap()
                } else {
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied.
                }
                return
            }
            else -> {
                // Ignore all other requests.
            }
        }
    }

    private fun initPlaces() {
        context?.let { Places.initialize(it, getString(R.string.google_maps_key)) }
        placesClient = context?.let { Places.createClient(it) }!!
    }

    private fun setPlacesSearchBias() {
        // Search nearby result
        currentLatLng?.let {
            autocompleteFragment.setLocationBias(
                RectangularBounds.newInstance(
                    LatLng(currentLatLng!!.latitude - 1, currentLatLng!!.longitude - 1),
                    LatLng(currentLatLng!!.latitude + 1, currentLatLng!!.longitude + 1)
                ))
        }
    }

    private fun setUpMap() {
        if (checkSelfPermission(context as Activity,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }

        // Add and adjust the position of MyLocation button.
        map.isMyLocationEnabled = true
        map.setPadding(0, (PADDING_RATIO * autocompleteLayout.height).toInt(),0,0)
        
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
            // Got last known location. In some rare situations this can be null.
                location?.let {
                    Log.d(MAP_FRAGMENT_TAG, "Locating Success ${location.latitude}, ${location.longitude}")
                    lastLocation = location
                    currentLatLng = LatLng(location.latitude, location.longitude)
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        currentLatLng,
                        ZOOM_VALUE
                    ))
                    map.addMarker(MarkerOptions()
                                .position(currentLatLng!!)
                                .title(getString(R.string.my_location_title)))
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        currentLatLng,
                        ZOOM_VALUE
                    ))
                    setPlacesSearchBias()
                } ?: run{
                    Log.d(MAP_FRAGMENT_TAG, "Locating Failed")
                    Toast.makeText(context, getString(R.string.cannot_access_location), Toast.LENGTH_SHORT)
                }
            }
            .addOnFailureListener {
                Log.d(MAP_FRAGMENT_TAG, "fusedLocationClient.lastLocation Failed")
            }
    }

    private fun setUpAutocomplete(autocompleteFragment : AutocompleteSupportFragment, mapFragment : SupportMapFragment) {
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG))
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                targetLatLng = place.latLng!!
                targetName = place.name.toString()
                mapFragment.getMapAsync(searchPlacesCallback)
            }

            override fun onError(status: Status) {
                Log.e(MAP_FRAGMENT_TAG, "An error occurred: $status")
            }
        })
    }


    private val mapReadyCallback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        map = googleMap
        map.uiSettings.isZoomControlsEnabled = true
        setUpMap()
    }

    private val searchPlacesCallback = OnMapReadyCallback { map ->
        targetMarker?.remove()
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(
            targetLatLng,
            ZOOM_VALUE
        ))
        targetMarker = map.addMarker(MarkerOptions()
                                .position(targetLatLng)
                                .title(targetName)
                                .draggable(true))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.activity as Activity)
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPlaces()

        val mapFragment = childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        autocompleteFragment = childFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment
        autocompleteLayout = view.findViewById(R.id.autocomplete_linearLayout)
        mapFragment.getMapAsync(mapReadyCallback)

        setUpAutocomplete(autocompleteFragment, mapFragment)
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val ZOOM_VALUE = 14f
        private const val PADDING_RATIO = 1.5
        private const val MAP_FRAGMENT_TAG = "Mapfragment"
    }
}
