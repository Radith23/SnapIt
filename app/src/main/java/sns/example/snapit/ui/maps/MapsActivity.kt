package sns.example.snapit.ui.maps

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.paging.ExperimentalPagingApi
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import sns.example.snapit.R
import sns.example.snapit.data.remote.api.ApiResponse
import sns.example.snapit.databinding.ActivityMapsBinding
import sns.example.snapit.utils.SessionManager
import sns.example.snapit.utils.ext.showOKDialog
import timber.log.Timber

@ExperimentalPagingApi
@AndroidEntryPoint
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private val mapsViewModel: MapsViewModel by viewModels()
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private lateinit var token: String
    private lateinit var pref: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pref = SessionManager(this)
        token = "Bearer ${pref.getToken}"

        initUI()
    }

    private fun initUI() {
        title = getString(R.string.title_story_maps)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        mapsViewModel.getStoriesWithLocation(token, 1).observe(this) { response ->
            when (response) {
                is ApiResponse.Success -> {
                    response.data.listStory.forEach {
                        val latLng = LatLng(it.lat, it.lon)
                        mMap.addMarker(MarkerOptions().position(latLng).title(it.name))
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                    }
                }

                is ApiResponse.Error -> showOKDialog(
                    getString(R.string.title_dialog_error),
                    response.errorMessage
                )

                is ApiResponse.Loading -> Timber.d(getString(R.string.message_loading_map))
                else -> {
                    Timber.d(getString(R.string.message_unknown_state))
                }
            }
        }
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, MapsActivity::class.java)
            context.startActivity(intent)
        }
    }
}