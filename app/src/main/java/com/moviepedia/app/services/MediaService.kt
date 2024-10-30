package com.moviepedia.app.services

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import com.moviepedia.app.R

class MediaService : Service() {

    private var mediaPlayer: MediaPlayer? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.getStringExtra("action")
        when (action) {
            "play" -> playAudio()
            "stop" -> stopAudio()
        }
        return START_STICKY
    }

    private fun playAudio() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, R.raw.audiofile).apply {
                setOnCompletionListener {
                    stopSelf()
                }
                start()
            }
        } else {
            mediaPlayer?.start()
        }
    }

    private fun stopAudio() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        stopAudio()
    }
}
