package sns.example.snapit.ui.story.add

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.paging.ExperimentalPagingApi
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import sns.example.snapit.R
import sns.example.snapit.data.remote.api.ApiResponse
import sns.example.snapit.databinding.ActivityAddBinding
import sns.example.snapit.ui.camera.CameraActivity
import sns.example.snapit.ui.main.MainViewModel
import sns.example.snapit.utils.ConstVal.CAMERA_X_RESULT
import sns.example.snapit.utils.ConstVal.KEY_PICTURE
import sns.example.snapit.utils.ConstVal.REQUEST_CODE_PERMISSIONS
import sns.example.snapit.utils.SessionManager
import sns.example.snapit.utils.ext.gone
import sns.example.snapit.utils.ext.show
import sns.example.snapit.utils.ext.showOKDialog
import sns.example.snapit.utils.ext.showToast
import sns.example.snapit.utils.reduceFileImage
import sns.example.snapit.utils.uriToFile
import java.io.File

@ExperimentalPagingApi
@AndroidEntryPoint
class AddActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    private lateinit var binding: ActivityAddBinding

    private var uploadFile: File? = null
    private var token: String? = null
    private var currentLocation: Location? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var pref: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pref = SessionManager(this)
        token = pref.getToken
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        initUI()
        initAction()
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    getMyLastLocation()
                }

                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    getMyLastLocation()
                }

                else -> {
                    Snackbar
                        .make(
                            binding.root,
                            getString(R.string.message_location_not_found),
                            Snackbar.LENGTH_SHORT
                        )
                        .setActionTextColor(ContextCompat.getColor(this, R.color.white))
                        .setAction(getString(R.string.action_change_setting)) {
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            val uri = Uri.fromParts("package", packageName, null)
                            intent.data = uri
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                        .show()

                    binding.cbShareLocation.isChecked = false
                }
            }
        }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (!allPermissionsGranted()) {
            showToast(getString(R.string.message_not_permitted))
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getMyLastLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    currentLocation = location
                    showToast("Location ${currentLocation!!.longitude} ${currentLocation!!.latitude}")
                } else {
                    showToast(getString(R.string.message_location_not_found))

                    binding.cbShareLocation.isChecked = false
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun initUI() {
        title = getString(R.string.title_new_story)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initAction() {
        binding.btnOpenCamera.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            launchIntentCamera.launch(intent)
        }
        binding.btnOpenGallery.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            val chooser = Intent.createChooser(intent, getString(R.string.title_choose_a_picture))
            launchIntentGallery.launch(chooser)
        }
        binding.btnUpload.setOnClickListener {
            uploadImage()
        }
        binding.cbShareLocation.setOnCheckedChangeListener { _, checked ->
            if (checked) {
                getMyLastLocation()
            } else {
                currentLocation = null
            }
        }
    }

    @Suppress("DEPRECATION")
    private val launchIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val file = it?.data?.getSerializableExtra(KEY_PICTURE) as File

            uploadFile = file

            val result = BitmapFactory.decodeFile(file.path)

            binding.imgPreview.setImageBitmap(result)
        }
    }

    private val launchIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val file = uriToFile(selectedImg, this)

            uploadFile = file
            binding.imgPreview.setImageURI(selectedImg)
        }
    }

    private fun uploadImage() {
        if (uploadFile != null) {
            val file = reduceFileImage(uploadFile as File)
            val description = binding.edtStoryDesc.text
            if (description.isBlank()) {
                binding.edtStoryDesc.requestFocus()
                binding.edtStoryDesc.error = getString(R.string.error_desc_empty)
            } else {
                val descMediaTyped =
                    description.toString().toRequestBody("text/plain".toMediaType())
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart = MultipartBody.Part.createFormData(
                    "photo",
                    file.name,
                    requestImageFile
                )

                var latitude: RequestBody? = null
                var longitude: RequestBody? = null

                if (currentLocation != null) {
                    longitude = currentLocation?.longitude.toString()
                        .toRequestBody("text/plain".toMediaType())
                    latitude = currentLocation?.latitude.toString()
                        .toRequestBody("text/plain".toMediaType())
                }

                mainViewModel.addNewStory(
                    "Bearer $token",
                    imageMultipart,
                    descMediaTyped,
                    latitude,
                    longitude
                )
                    .observe(this) { response ->
                        when (response) {
                            is ApiResponse.Loading -> {
                                showLoading(true)
                            }

                            is ApiResponse.Success -> {
                                showLoading(false)
                                showToast(getString(R.string.upload_successful))
                                finish()
                            }

                            is ApiResponse.Error -> {
                                showLoading(false)
                                showOKDialog(
                                    getString(R.string.upload_info),
                                    response.errorMessage
                                )
                            }

                            else -> {
                                showLoading(false)
                                showToast(getString(R.string.message_unknown_state))
                            }
                        }
                    }
            }
        } else {
            showOKDialog(getString(R.string.title_message), getString(R.string.message_pick_image))
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.progressBar.show() else binding.progressBar.gone()
        binding.apply {
            btnUpload.isClickable = !isLoading
            btnUpload.isEnabled = !isLoading
            btnOpenGallery.isClickable = !isLoading
            btnOpenGallery.isEnabled = !isLoading
            btnOpenCamera.isClickable = !isLoading
            btnOpenCamera.isEnabled = !isLoading
        }
    }

    @Suppress("DEPRECATION")
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, AddActivity::class.java)
            context.startActivity(intent)
        }

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}