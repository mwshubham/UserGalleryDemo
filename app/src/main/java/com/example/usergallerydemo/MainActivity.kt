package com.example.usergallerydemo

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.usergallerydemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        viewModel = ViewModelProvider.AndroidViewModelFactory(application)
            .create(MainActivityViewModel::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(
                    this, Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> {
                    viewModel.getAllImages()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                    AlertDialog.Builder(this).create()
                        .apply {
                            setMessage("Storage permission is required to display images from gallery.")
                            setButton(AlertDialog.BUTTON_POSITIVE, "Ok") { _, _ ->
                                requestPermissions(
                                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                                    1001
                                )
                            }
                            setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel") { dialog, _ ->
                                dialog.dismiss()
                                Toast.makeText(
                                    this@MainActivity,
                                    "Storage permission is required to display images from gallery.",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }
                        .show()
                }
                else -> {
                    requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1001)
                }
            }
        }

        val adapter = MainRvAdapter()
        binding.rvGallery.adapter = adapter
        binding.rvGallery.addItemDecoration(
            StaggeredGridLayoutManagerEqualSpacingItemDecoration(
                resources.getDimension(R.dimen.default_spacing).toInt()
            )
        )
        viewModel.images.observe(this, Observer {
            it ?: return@Observer
            Log.i(TAG, "onCreate: $it")
            adapter.submitList(it)
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            RC_READ_EXTERNAL_STORAGE -> {
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    viewModel.getAllImages()
                } else {
                    Toast.makeText(
                        this,
                        "Storage permission is required to display images from gallery.",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }
    }

    companion object {
        const val TAG = "MainActivity"
        const val RC_READ_EXTERNAL_STORAGE = 1001
    }
}