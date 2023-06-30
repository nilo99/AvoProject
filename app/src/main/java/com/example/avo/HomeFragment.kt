package com.example.avo

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.avo.Adapter.MyAdapter
import com.example.avo.Models.UserViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

private lateinit var viewModel: UserViewModel

private lateinit var userRecyclerView: RecyclerView
private lateinit var textid: TextInputLayout

lateinit var adapter: MyAdapter



class HomeFragment : Fragment(), OnMapReadyCallback {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private var customMarker: Marker? = null
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 123

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    private lateinit var placesClient: PlacesClient
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        mapView = view.findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        Places.initialize(requireContext(), "AIzaSyAwSJ1DUUy8M7_62HxgtVo2IzZYWNFZkgs")
        placesClient = Places.createClient(requireContext())

        val autocompleteFragment = AutocompleteSupportFragment.newInstance()

        autocompleteFragment.setPlaceFields(listOf(com.google.android.libraries.places.api.model.Place.Field.LAT_LNG))

        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                val latLng = place.latLng
                if (latLng != null) {
                    customMarker?.remove() // Remove existing marker if it exists

                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18f))
                    customMarker = googleMap.addMarker(
                        MarkerOptions()
                            .position(latLng)
                            .title(place.name)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.avomarker))
                    )
                }
            }

            override fun onError(status: com.google.android.gms.common.api.Status) {
                // Trate erros de seleção de lugar aqui, se necessário
            }
        })



        childFragmentManager.beginTransaction()
            .replace(R.id.autocompleteContainer, autocompleteFragment)
            .commit()
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userRecyclerView = view.findViewById(R.id.recycle)
        adapter = MyAdapter(requireContext())
        userRecyclerView.adapter = adapter
        userRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val bottomSheetBehavior = BottomSheetBehavior.from(view.findViewById(R.id.sheetswipe))
        bottomSheetBehavior.peekHeight = 850
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        viewModel.allUsers.observe(viewLifecycleOwner, Observer {
            adapter.updateUserList(it)
        })
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            googleMap.isMyLocationEnabled = true

            val locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (location != null) {
                val userLatLng = LatLng(location.latitude, location.longitude)
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 18f))
                //    googleMap.addMarker(MarkerOptions().position(userLatLng).title("Minha localização"))
            }
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }

        val announcementsRef = FirebaseDatabase.getInstance().reference.child("announcements")

        announcementsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                googleMap.clear() // Clear existing markers

                for (announcementSnapshot in dataSnapshot.children) {
                    val announcementId = announcementSnapshot.key
                    val latitudeStr = announcementSnapshot.child("latitude").getValue(String::class.java)
                    val longitudeStr = announcementSnapshot.child("longitude").getValue(String::class.java)

                    if (announcementId != null && latitudeStr != null && longitudeStr != null) {
                        val latitude = latitudeStr.toDouble()
                        val longitude = longitudeStr.toDouble()
                        val latLng = LatLng(latitude, longitude)

                        val markerOptions = MarkerOptions()
                            .position(latLng)
                            .title(announcementId)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.avomarker))

                        val customMarker = googleMap.addMarker(markerOptions)
                        // Store the custom marker if needed

                        // Perform any other actions with the marker
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error
            }
        })

    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    private fun isLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun enableMyLocation() {
        try {
            googleMap.isMyLocationEnabled = true
        } catch (e: SecurityException) {
            // Tratar a exceção de segurança aqui, se necessário
        }
    }

    private fun requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            // Se necessário, exibir um diálogo explicativo sobre a permissão de localização para o usuário

        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableMyLocation()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
}