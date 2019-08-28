package com.example.livestream

import android.Manifest.*
import android.content.pm.PackageManager
import android.opengl.GLSurfaceView
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.takusemba.rtmppublisher.Publisher
import com.takusemba.rtmppublisher.PublisherListener

class MainActivity : AppCompatActivity(), PublisherListener {
    override fun onFailedToConnect() {
        Toast.makeText(this, R.string.unexpected_error, Toast.LENGTH_LONG).show()
    }

    override fun onStarted() {
        publishButton.text = resources.getString(R.string.stop)
        liveBadge.visibility = View.VISIBLE
    }

    override fun onDisconnected() {
        Toast.makeText(this, R.string.disconnected, Toast.LENGTH_SHORT).show()
    }

    override fun onStopped() {
        publishButton.text = resources.getString(R.string.publish)
        liveBadge.visibility = View.INVISIBLE
    }

    private lateinit var glSurface: GLSurfaceView
    private lateinit var liveBadge: TextView
    private lateinit var publisher: Publisher
    private lateinit var publishButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupPublisher()
    }

    private fun setupPublisher() {
        glSurface = findViewById(R.id.glSurface)
        liveBadge = findViewById(R.id.liveBadge)
        publishButton = findViewById(R.id.publishButton)

        liveBadge.visibility = View.INVISIBLE

        publisher = Publisher.Builder(this)
            .setGlView(glSurface)
            .setUrl(resources.getString(R.string.broadcast_url))
            .setSize(Publisher.Builder.DEFAULT_WIDTH, Publisher.Builder.DEFAULT_HEIGHT)
            .setAudioBitrate(128000)
            .setVideoBitrate(256000)
            .setCameraMode(Publisher.Builder.DEFAULT_MODE)
            .setListener(this)
            .build()

        publishButton.setOnClickListener {
            if (publisher.isPublishing) {
                publisher.stopPublishing()
            } else {
                publisher.startPublishing()
            }
        }
    }


}
