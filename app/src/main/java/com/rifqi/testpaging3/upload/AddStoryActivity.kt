package com.rifqi.testpaging3.upload

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.rifqi.testpaging3.AuthViewModel
import com.rifqi.testpaging3.UserPrefferences
import com.rifqi.testpaging3.ViewModelFactory
import com.rifqi.testpaging3.databinding.ActivityAddStoryBinding
import com.rifqi.testpaging3.login.dataStore
import com.rifqi.testpaging3.menu.MenuActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class AddStoryActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddStoryBinding
    private val addStoryViewModel: AddStoryViewModel by viewModels()
    private var getFile: File? = null
    private val timeStamp: String = SimpleDateFormat(
        "dd-MMM-yyyy",
        Locale.US
    ).format(System.currentTimeMillis())
    private var result: Bitmap? = null
    private val authViewModel: AuthViewModel by viewModels() {
        ViewModelFactory(UserPrefferences.getInstance(dataStore))
    }

    companion object {
        const val CAMERA_X_RESULT = 200

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (!allPermissionsGranted()) {
            Toast.makeText(
                this,
                "Tidak mendapatkan Izin untuk memulai Kamera",
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding?.apply {


            btnCamera.setOnClickListener { goToCamera() }
            btnGallery.setOnClickListener { goToGallery() }
            btnUploadStory.setOnClickListener { uploadStory() }


        }

        showLoading()


    }

    private fun showLoading() {
        binding?.apply {
            addStoryViewModel.loading.observe(this@AddStoryActivity) { isLoading ->
                if (isLoading) {
                    pbAddStory.visibility = View.VISIBLE
                } else {
                    pbAddStory.visibility = View.INVISIBLE
                }

            }
        }
    }

    //    main actor = uploadStory
    private fun uploadStory() {

        binding?.apply {
            if (getFile != null) {
                val desc = edtAddStory.text.trim()
                if (desc.isEmpty()) {
                    edtAddStory.error = "please fill the description"
                } else {
                    val file = reduceFileImage(getFile as File)
                    val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    val imageMulti: MultipartBody.Part =
                        MultipartBody.Part.createFormData("photo", file.name, requestImageFile)

                    authViewModel.getUser().observe(this@AddStoryActivity) {
                        addStoryViewModel.insertStory(
                            it.userToken, desc.toString(), imageMulti
                        )
                    }
                }
                addStoryViewModel.msgDialog.observe(this@AddStoryActivity) {
                    if (it.toString() == "false") {
                        Toast.makeText(
                            this@AddStoryActivity,
                            " $it :  Berhasil menambahkan Story",
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(Intent(this@AddStoryActivity, MenuActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(
                            this@AddStoryActivity,
                            "Error : $it, Gagal menambahkan tory",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                Toast.makeText(
                    this@AddStoryActivity,
                    "silahkan masukan berkas terlebih dahulu",
                    Toast.LENGTH_SHORT
                ).show()

            }
        }
    }

    // Gallery Stuff
    private fun goToGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    fun reduceFileImage(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path)
        var compressQuality = 100
        var streamLength: Int
        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > 1000000)
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
        return file
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val selectedImg: Uri = it.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@AddStoryActivity)
            getFile = myFile
            binding.ivCameraPreview.setImageURI(selectedImg)
        }
    }

    private fun createTempFiles(context: Context): File {
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(timeStamp, ".jpg", storageDir)
    }

    private fun uriToFile(selectedImg: Uri, context: Context): File {
        val contentResolver: ContentResolver = context.contentResolver
        val myFile = createTempFiles(context)

        val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
        val outputStream: OutputStream = FileOutputStream(myFile)
        val buf = ByteArray(1024)
        var len: Int

        while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
        outputStream.close()
        inputStream.close()

        return myFile
    }

    //    CameraX Stuff
    private fun goToCamera() {
        val intent = Intent(this, CameraXActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            getFile = myFile
            result =
//                rotateBitmap(
                    BitmapFactory.decodeFile(getFile?.path)
//                    isBackCamera
//                )


        }
        binding.ivCameraPreview.setImageBitmap(result)
    }

    fun rotateBitmap(bitmap: Bitmap, isBackCamera: Boolean = false): Bitmap {
        val matrix = Matrix()
        return if (isBackCamera) {
            matrix.postRotate(90f)
            Bitmap.createBitmap(
                bitmap,
                0,
                0,
                bitmap.width,
                bitmap.height,
                matrix,
                true
            )
        } else {
            matrix.postRotate(-90f)
            matrix.postScale(-1f, 1f, bitmap.width / 2f, bitmap.height / 2f)  // flip gambar
            Bitmap.createBitmap(
                bitmap,
                0,
                0,
                bitmap.width,
                bitmap.height,
                matrix,
                true
            )
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        finish()
    }
}