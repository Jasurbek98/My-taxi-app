package uz.jsoft.mytaxiapp.presentation.ui.screens.map_fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.res.Resources
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.airbnb.lottie.LottieDrawable
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import uz.jsoft.mytaxiapp.R
import uz.jsoft.mytaxiapp.data.local.LocalStorage
import uz.jsoft.mytaxiapp.data.local_models.dashboard.DataWrapper
import uz.jsoft.mytaxiapp.data.model.Point
import uz.jsoft.mytaxiapp.data.remote.response.SearchResponse
import uz.jsoft.mytaxiapp.databinding.FragmentMapBinding
import uz.jsoft.mytaxiapp.presentation.ui.adapters.SearchAdapter
import uz.jsoft.mytaxiapp.presentation.ui.dialogs.SavedSearchBottomSheet
import uz.jsoft.mytaxiapp.presentation.viewmodels.MapViewModel
import uz.jsoft.mytaxiapp.utils.*
import uz.jsoft.mytaxiapp.utils.extensions.LocationSetting
import uz.jsoft.mytaxiapp.utils.extensions.checkPermission
import uz.jsoft.mytaxiapp.utils.extensions.checkPermissions
import javax.inject.Inject


@AndroidEntryPoint
class MapFragment : Fragment() {


    private val binding: FragmentMapBinding
        get() = _binding ?: throw NullPointerException("View wasn't created")
    private var _binding: FragmentMapBinding? = null

    private val mapViewModel: MapViewModel by viewModels()

    private lateinit var bottomSheetBehavior1: BottomSheetBehavior<View>

    private lateinit var searchAdapter: SearchAdapter

    private val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
    )


    @Inject
    lateinit var storage: LocalStorage

    @Inject
    lateinit var dialogMaker: SavedSearchBottomSheet

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mMap: GoogleMap

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { mapObject ->
        mMap = mapObject
        mapObject.mapType = GoogleMap.MAP_TYPE_TERRAIN
        mapObject.uiSettings.isMyLocationButtonEnabled = false

        setMapStyle(mapObject)
        cameraMoveStartedListener(mapObject)
        if (storage.currentLat != 0.0 && storage.currentLong != 0.0) {
            mMap.isMyLocationEnabled = true
            mMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        storage.currentLat, storage.currentLong
                    ), 17.0f
                )
            )
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMapBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        loadViews()
        loadObservers()
        loadClickListeners()
        configureGoogleMap()
    }


    private fun loadObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            mapViewModel.geoResponse.collect { dataWrapper ->
                when (dataWrapper) {
                    is DataWrapper.Empty -> Unit
                    is DataWrapper.Error -> {
                        Toast.makeText(
                            requireContext(), dataWrapper.error.toString(), Toast.LENGTH_SHORT
                        ).show()
                    }
                    is DataWrapper.Success -> {
                        if (storage.isOnCurrentLocation) {
                            mapViewModel.currentDestination = dataWrapper.data
                            binding.currentLocation.text = dataWrapper.data.name
                        } else {
                            mapViewModel.toDestination = SearchResponse.SearchAddressItem(
                                formattedAddress = dataWrapper.data.name,
                                address = dataWrapper.data.name,
                                distance = dataWrapper.data.distance.toString(),
                                location = SearchResponse.Location(
                                    dataWrapper.data.longitude, dataWrapper.data.latitude
                                )
                            )
                            binding.mapDestinationConfirmLayout.currentLocation.setText(dataWrapper.data.name)
                        }
                    }
                    else -> {
                        binding.currentLocation.text =
                            requireContext().resources.getString(R.string.mock_hint)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            mapViewModel.searchResponse.collect { dataWrapper ->
                when (dataWrapper) {
                    is DataWrapper.Empty -> Unit
                    is DataWrapper.Error -> {
                        showToast(dataWrapper.error.toString())
                    }
                    is DataWrapper.Loading -> {
                        binding.searchIncludeLayout.shimmerViewContainer.show()
                    }
                    is DataWrapper.Success -> {
                        binding.searchIncludeLayout.shimmerViewContainer.gone()
                        binding.searchIncludeLayout.searchList.show()
                        searchAdapter.submitList(dataWrapper.data)
                    }
                }
            }
        }


        viewLifecycleOwner.lifecycleScope.launch {
            mapViewModel.subSearchResponse.collect { dataWrapper ->
                when (dataWrapper) {
                    is DataWrapper.Empty -> Unit
                    is DataWrapper.Error -> {
                        showToast(dataWrapper.error.toString())
                    }
                    is DataWrapper.Success -> {
                        searchAdapter.currentList[storage.searchItemPosition].subSearchList =
                            dataWrapper.data
                        searchAdapter.notifyItemChanged(storage.searchItemPosition)

                    }
                    else -> {
                        //TO DO
                    }
                }
            }
        }
    }


    private fun loadBottomSheetBehavior() {
        bottomSheetBehavior1 = BottomSheetBehavior.from(binding.bottomSheetBehaviorLayout1)
        bottomSheetBehavior1.isHideable = false
        bottomSheetBehavior1.peekHeight = 460
        bottomSheetBehavior1.isDraggable = true
        bottomSheetBehavior1.isGestureInsetBottomIgnored = true
        bottomSheetBehavior1.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetBehavior1.saveFlags = BottomSheetBehavior.SAVE_FIT_TO_CONTENTS


        bottomSheetBehavior1.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                configureView(newState)
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

    }


    private fun loadViews() {

        configureGpsSetting()
        loadBottomSheetBehavior()
        searchAdapter = SearchAdapter(this::subSearchItemClick)
        binding.searchIncludeLayout.searchList.adapter = searchAdapter
    }


    private fun configureGoogleMap() {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }


    private fun loadClickListeners() {


        /*mMap.setOnMapClickListener(object :GoogleMap.OnMapClickListener{
            override fun onMapClick(p0: LatLng) {

            }

        })*/



        binding.apply {
            savedButton.setOnClickListener {
                dialogMaker.showSavedSearchBottomSheetDialog(requireContext())
            }

            searchAdapter.onItemClickListener { searchAddressItem, position ->
                storage.searchItemPosition = position
                if (searchAddressItem.addressId != null) {
                    mapViewModel.getSubSearchItems(
                        Point(storage.currentLat, storage.currentLong),
                        searchAddressItem.streetPoiId!!
                    )
                } else {
                    mapViewModel.toDestination = searchAddressItem

                }
            }

            imgNavigationHome.setOnClickListener {
                LocationSetting()
                checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) {
                    checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION) {
                        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                            mMap.isMyLocationEnabled = true
                            if (location != null) navigateCurrentLocation(location)
                        }
                    }
                }
            }
            destination.setOnClickListener {
                bottomSheetBehavior1.state = BottomSheetBehavior.STATE_EXPANDED
                binding.searchIncludeLayout.currentAddress.setText(mapViewModel.currentDestination.name)
            }

            searchIncludeLayout.apply {

                destinationAddress.afterTextChanged {
                    storage.searchText = it.trim()
                    mapViewModel.getSearchItems(
                        Point(storage.currentLat, storage.currentLong), it
                    )
                }

                currentAddress.afterTextChanged {
                    storage.searchText = it.trim()
                    mapViewModel.getSearchItems(
                        Point(storage.currentLat, storage.currentLong), it
                    )
                }

                currentAddress.setOnFocusChangeListener { _, b ->
                    setBackground(b)
                }
                imgArrowBtn.setOnClickListener {
                    configureBottomSheet(true)
                }
                locationImageButton.setOnClickListener {
                    setBackground()
                    configureBottomSheet()
                }
                moveToMapButton.setOnClickListener {
                    configureBottomSheet()
                }
            }
        }
    }


    private fun configureBottomSheet(bool: Boolean = false) {
        hideKeyboard()
        storage.isOnCurrentLocation = bool
        bottomSheetBehavior1.peekHeight = 460
        bottomSheetBehavior1.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetBehavior1.isDraggable = bool
        binding.apply {
            if (bool) {
                constraintContainer.visible(true)
                mapDestinationConfirmLayout.currentLocationLayout.gone(true)
                imgHomeMenu.visible(true)
                imgArrowBtn.gone(true)
            } else {
                bottomSheetBehavior1.peekHeight = 560
                constraintContainer.invisible(true)
                mapDestinationConfirmLayout.currentLocationLayout.visible(true)
                imgHomeMenu.gone(true)
                imgArrowBtn.visible(true)
            }
        }
    }


    private fun cameraMoveStartedListener(googleMap: GoogleMap) {
        binding.apply {
            googleMap.setOnCameraMoveStartedListener {
                mapPoint.playAnimation()
                mapPoint.repeatCount = LottieDrawable.RESTART
            }

            googleMap.setOnCameraIdleListener {
                storage.currentLat = googleMap.cameraPosition.target.latitude
                storage.currentLong = googleMap.cameraPosition.target.longitude

                val point = Point(storage.currentLat, storage.currentLong)
                mapViewModel.getGeoPosition(point)
            }
        }
    }


    private fun navigateCurrentLocation(location: Location) {
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude, location.longitude), 17.0f)
        )
    }

    private fun subSearchItemClick(searchAddressItem: SearchResponse.SearchAddressItem) {
        mapViewModel.toDestination = searchAddressItem
    }

    private fun setBackground(focusable: Boolean = false) {
        binding.searchIncludeLayout.apply {
            if (focusable) {

                currentAddress.setBackgroundResource(R.drawable.edit_text_with_stroke)
                destinationAddressLayout.setBackgroundResource(R.drawable.edit_text_bg)
                locationImageButton.setBackgroundResource(R.drawable.edit_text_bg)
            } else {
                currentAddress.setBackgroundResource(R.drawable.edit_text_bg)
                destinationAddressLayout.setBackgroundResource(R.drawable.edit_text_with_stroke)
                locationImageButton.setBackgroundResource(R.drawable.edit_text_with_stroke)
            }
        }
    }

    private fun setMapStyle(map: GoogleMap) {
        try {
            map.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    requireActivity(), R.raw.map_style
                )
            )
        } catch (e: Resources.NotFoundException) {
            e.printStackTrace()
        }
    }

    private fun configureGpsSetting() {
        LocationSetting()
        if (!storage.isPermissioned) {
            checkPermissions(permissions) {
                storage.isPermissioned = true
            }
        }
    }

    private fun configureView(bottomSheetBehaviorValue: Int) {
        when (bottomSheetBehaviorValue) {
            BottomSheetBehavior.STATE_EXPANDED -> {
                binding.searchIncludeLayout.destinationAddressLayout.setBackgroundResource(R.drawable.edit_text_with_stroke)
                binding.searchIncludeLayout.locationImageButton.setBackgroundResource(R.drawable.edit_text_with_stroke)
                binding.constraintContainer.invisible(true)
                binding.searchIncludeLayout.searchContainter.show()
                binding.imgNavigationHome.invisible(true)
                binding.searchIncludeLayout.searchContainter.visible(true)
            }
            BottomSheetBehavior.STATE_COLLAPSED -> {
                binding.constraintContainer.visible(true)
                binding.imgNavigationHome.visible(true)
                binding.searchIncludeLayout.searchContainter.invisible(true)
            }
        }
    }


}