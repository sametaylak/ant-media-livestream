package com.example.livestream

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.core.app.ActivityCompat

class SplashActivity : AppCompatActivity() {
    private var delayHandler: Handler? = null

    private val splashDelay: Long = 3000
    private val permissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO
    )
    private val runnable: Runnable = Runnable {
        if (!isFinishing) {
            passSplashScreen()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!hasPermissions(*permissions)) {
                ActivityCompat.requestPermissions(this, permissions, 1)
            } else {
                passSplashScreen()
            }
        } else {
            delayHandler = Handler()
            delayHandler!!.postDelayed(runnable, splashDelay)
        }
    }

    private fun hasPermissions(vararg permissions: String): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun passSplashScreen() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                passSplashScreen()
            } else {
                finish()
            }
        }
    }
}
