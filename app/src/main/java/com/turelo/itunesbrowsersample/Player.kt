package com.turelo.itunesbrowsersample

import android.media.MediaPlayer
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class Player() : LifecycleObserver {
    private var mediaPlayer: MediaPlayer = MediaPlayer()

    private val statusSubject: BehaviorSubject<PlayerStatus> =
        BehaviorSubject.createDefault(PlayerStatus.STOP)

    val status: Observable<PlayerStatus> get() = statusSubject

    fun start(url: String, prepare: () -> (Unit)) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepare()
    }

    fun resume() {
        mediaPlayer.start()
    }

    fun pause() {
        statusSubject.onNext(PlayerStatus.PAUSE)
        mediaPlayer.pause()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun stop() {
        statusSubject.onNext(PlayerStatus.STOP)
        mediaPlayer.stop()
        mediaPlayer.release()
    }
}

enum class PlayerStatus {
    PLAY, PAUSE, STOP
}