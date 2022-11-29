package com.wms.superadmin.modules.attedence.ui


import android.Manifest
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.wms.superadmin.R
import com.wms.superadmin.appcomponents.base.BaseActivity
import com.wms.superadmin.appcomponents.utility.PreferenceHelper
import com.wms.superadmin.appcomponents.utility.URIPathHelper
import com.wms.superadmin.appcomponents.views.SuccessMsgDialogue
import com.wms.superadmin.databinding.ActivityAttendenceBinding
import com.wms.superadmin.extensions.NoInternetConnection
import com.wms.superadmin.extensions.Sessions
import com.wms.superadmin.extensions.isJSONObject
import com.wms.superadmin.extensions.showProgressDialog
import com.wms.superadmin.modules.attedence.data.viewmodel.AttedenceVM
import com.wms.superadmin.network.models.Login.LoginResponse
import com.wms.superadmin.network.models.pojos.CreateSaveAttendencetDataRequest
import com.wms.superadmin.network.models.pojos.CreateSaveAttendencetDataResponse
import com.wms.superadmin.network.resources.ErrorResponse
import com.wms.superadmin.network.resources.SuccessResponse
import org.json.JSONObject
import org.koin.core.KoinComponent
import org.koin.core.inject
import retrofit2.HttpException
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

public class AttedenceActivity : BaseActivity<ActivityAttendenceBinding>(R.layout.activity_attendence),AdapterView.OnItemSelectedListener,KoinComponent
{
    private val viewModel: AttedenceVM by viewModels<AttedenceVM>()
    var currentweekday = SimpleDateFormat("EEEE", Locale.getDefault()).format(Date())
    var currentdate = SimpleDateFormat("yyyy-MMM-dd", Locale.getDefault()).format(Date())
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    val pref: PreferenceHelper by inject()
    private var image: File? = null
    private var currentsession: String? = null
    private var mLongitude=""
    private var mLatitude=""
    private var mCurrentLocation=""
    private var mISLogin=false
    var userdetails=pref.getSADetails<LoginResponse>()!!

    public override fun onInitialized(): Unit {

        val sessionList= listOf<String>(Sessions.FIRST_HALF,Sessions.SECOND_HALF)
        val sessionAdapter= ArrayAdapter(this, R.layout.spinner_item,sessionList)
        binding.spinnerCategories.adapter=sessionAdapter
        binding.spinnerCategories.onItemSelectedListener=this
        mFusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this)
        getCurrentLocation()

    }
    fun getCurrentLocation(){
        if(checkPermisson()){
            if(isLocationEnabled()){
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                {
                    requestpermission()
                    return
                }
                mFusedLocationProviderClient.lastLocation.addOnCompleteListener(this){ task->
                    var location:Location?=task.result
                    if(location!=null)
                    {
                        mLatitude=location.latitude.toString()
                        mLongitude=location.longitude.toString()
                        val geocoder = Geocoder(this, Locale.getDefault())
                        var addresses : List<Address>?=null
                        try {
                            addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1) // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                        if (addresses!!.size > 0) {
                            Log.d("max", " " + addresses[0].getMaxAddressLineIndex())
                            val address: String = addresses[0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                            val city: String = addresses[0].getLocality()
                            val state: String = addresses[0].getAdminArea()
                            val country: String = addresses[0].getCountryName()
                            val postalCode: String = addresses[0].getPostalCode()
                            val knownName: String = addresses[0].getFeatureName() // Only if available else return NULLaddresses[0].getAdminArea()

                            mCurrentLocation = "$address $knownName $country $state$city $postalCode "
                            Log.e("Location",mCurrentLocation)
                        }
                    } else
                        showToast("Location Not Found")

                }

            }else{
                showToast("Turn on Location")
                val intent=Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(intent)

            }

        }else{
            requestpermission()

        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER))
    }

    private fun requestpermission() {
        ActivityCompat.requestPermissions(this,
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_COARSE_LOCATION),
            PERMISSION_REQUEST_ACCESS_LOCATION)
    }

    public override fun setUpClicks(): Unit {
        binding.imageArrowleft.setOnClickListener {
            finish()
        }
        Log.e("togglebuttntext", binding.toggleButton.text.toString())
        binding.toggleButton.setOnClickListener {
            if (binding.toggleButton.text == "OUT") {
                binding.login.visibility = View.GONE
                binding.logout.visibility = View.VISIBLE
            } else {
                binding.logout.visibility = View.GONE
                binding.login.visibility = View.VISIBLE

            }
        }
        val currentclocktime = binding.txtTime.text
        Log.e("currentclocktime", currentclocktime.toString())



        Log.e("latinattendance", pref.getlattitude().toString())
        //  binding.txtTime.text=currentTime

        binding.txtWeekday.text = currentweekday
        binding.txtDate.text = currentdate
        binding.imageCamera.setOnClickListener {

            val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(takePicture, 0)


            //}
        }
//
//    }

        binding.login.setOnClickListener {
            mISLogin=true
            if (image != null) {
                val saveattendanceobject = CreateSaveAttendencetDataRequest(
                    attOuttime = "",
                    attIntime = binding.txtTime.text.toString(),
                    creationDate = currentdate,
                    attStatus = "",
                    createdBy = userdetails.userID?.toString()?:"0",
                    attPhoto = "",
                    attDateTime = currentdate,
                    attType = currentsession,
                    difference = "",
                    attSubType = "IN",
                    gPSLattitude =mLatitude,
                    gPSLongitude =mLongitude,
                    gPSLocation = mCurrentLocation,
                    userID = userdetails.userID?.toString()?:"0"
                )
                Log.e("save", saveattendanceobject.toString())
                viewModel.saveattendanceVM(saveattendanceobject, image!!)
            } else
                Toast.makeText(this, "Please Take the Picture", Toast.LENGTH_SHORT).show()

        }
        binding.logout.setOnClickListener {
            mISLogin=false
            if (image != null) {
                val saveattendanceobject = CreateSaveAttendencetDataRequest(
                    attOuttime = binding.txtTime.text.toString(),
                    attIntime = "",
                    creationDate = currentdate,
                    attStatus = "",
                    createdBy =userdetails.userID?.toString()?:"0",
                    attPhoto = "",
                    attDateTime = currentdate,
                    attType = currentsession,
                    difference = "",
                    attSubType = "OUT",
                    gPSLattitude = mLatitude,
                    gPSLongitude = mLongitude,
                    gPSLocation = mCurrentLocation,
                    userID = userdetails.userID?.toString()?:"0"
                )
                Log.e("save", saveattendanceobject.toString())
                viewModel.saveattendanceVM(saveattendanceobject, image!!)
            } else
                Toast.makeText(this, "Please Take the Picture", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //  locationManager.onActivityResult(requestCode, resultCode, data)

        if (resultCode != RESULT_CANCELED) {

            if (resultCode == RESULT_OK && data != null) {
                val bitmap = data.extras!!["data"] as Bitmap?
                Log.e("bitmap", bitmap.toString())
                binding.imageCamera.setImageBitmap(bitmap)


                val path = bitmapToFile(bitmap!!)
                Log.e("uri", path.toString())
                val finalfile = File(getRealPathFromUri(path))
                Log.e("finalfile", finalfile.toString())

                val uriPathHelper = URIPathHelper()
                val filePath = uriPathHelper.getPath(this, path!!)
                val selectedimage = File(path.path)
                Log.e("ImagePath", "path is $selectedimage")
                image = selectedimage
                //var selectedimage = File(path.path)


            }
        }

    }

    fun getRealPathFromUri(contentUri: Uri?): String? {
        var path = ""
        if (contentResolver != null) {
            val cursor: Cursor? = contentResolver.query(contentUri!!, null, null, null, null)
            if (cursor != null) {
                cursor.moveToFirst()
                val idx: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                path = cursor.getString(idx)
                cursor.close()
            }
        }
        return path
    }




    private fun bitmapToFile(bitmap: Bitmap): Uri {
        // Get the context wrapper
        val wrapper = ContextWrapper(applicationContext)

        // Initialize a new file instance to save bitmap object
        var file = wrapper.getDir("Images", Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")

        try {
            // Compress the bitmap and save in jpg format
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        // Return the saved bitmap uri
        return Uri.parse(file.absolutePath)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode== PERMISSION_REQUEST_ACCESS_LOCATION){
            if(grantResults.isNotEmpty()&&grantResults[0]== PackageManager.PERMISSION_GRANTED){
                showToast("Permission Granted")
                getCurrentLocation()

            }
            else
                showToast("Permission Denied")

        }
    }


    fun showToast(message:String){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }

    public override fun addObservers(): Unit {
        var progressDialog: AlertDialog? = null
        viewModel.progressLiveData.observe(this@AttedenceActivity) {
            if (it) {
                progressDialog?.dismiss()
                progressDialog = null
                progressDialog = this@AttedenceActivity.showProgressDialog()
            } else {
                progressDialog?.dismiss()
            }
        }
        viewModel.saveattendanceLiveData.observe(this@AttedenceActivity) {
            if (it is SuccessResponse) {
                val response = it.getContentIfNotHandled()
                onSuccessCreate(it)
            } else if (it is ErrorResponse) {
                onErrorCreate(it.data ?: Exception())
            }
        }
    }

    private fun onSuccessCreate(response: SuccessResponse<CreateSaveAttendencetDataResponse>): Unit {
        Snackbar.make(binding.root, "${response.`data`.status}", Snackbar.LENGTH_LONG).show()
        if(mISLogin)
            SuccessMsgDialogue(this, "Hi ${userdetails.username}..Have a good day").show()
        else
            SuccessMsgDialogue(this, "Thank you ${userdetails.username}..Hope you had a good day").show()
    }


    private fun onErrorCreate(exception: Exception): Unit {
        when (exception) {
            is NoInternetConnection -> {
                Snackbar.make(binding.root, exception.message ?: "", Snackbar.LENGTH_LONG).show()
            }
            is HttpException -> {
                val errorBody = exception.response()?.errorBody()?.string()
                val errorObject = if (errorBody != null && errorBody.isJSONObject())
                    JSONObject(errorBody) else JSONObject()
                Snackbar.make(
                    binding.root, "Error",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }
    fun checkPermisson():Boolean{
        return (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
    }
    public companion object {
        public const val TAG: String = "ATTEDENCE_ACTIVITY"
        private const val PERMISSION_REQUEST_ACCESS_LOCATION: Int = 100

        public fun getIntent(context: Context, bundle: Bundle?): Intent {
            val destIntent = Intent(context, AttedenceActivity::class.java)
            destIntent.putExtra("bundle", bundle)
            return destIntent
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        if (p2 == 0) {
            currentsession = Sessions.FIRST_HALF
        } else {
            currentsession = Sessions.SECOND_HALF
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }
}
