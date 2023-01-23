package io.github.larsrosenkilde.musicplayer.services

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import io.github.larsrosenkilde.musicplayer.MainActivity
import io.github.larsrosenkilde.musicplayer.MusicPlayer
import io.github.larsrosenkilde.musicplayer.utils.Eventer

enum class PermissionEvents {
    MEDIA_PERMISSION_GRANTED,
}

data class PermissionsState(
    val required: List<String>,
    val granted: List<String>,
    val denied: List<String>,
) {
    fun hasAll() = denied.isEmpty()
}

class PermissionsManager(private val musicplayer: MusicPlayer) {
    val onUpdate = Eventer<PermissionEvents>()

    fun handle(activity: MainActivity) {
        val state = getState(activity)
        if (state.hasAll()) return
        activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions.count { it.value } == state.denied.size) {
                onUpdate.dispatch(PermissionEvents.MEDIA_PERMISSION_GRANTED)
            }
        }.launch(state.denied.toTypedArray())
    }

    private fun getState(activity: MainActivity): PermissionsState {
        val required = mutableListOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            required.add(Manifest.permission.READ_MEDIA_AUDIO)
        }
        val granted = mutableListOf<String>()
        val denied = mutableListOf<String>()
        required.forEach {
            if (activity.checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED) {
                granted.add(it)
            } else {
                denied.add(it)
            }
        }
        return PermissionsState(
            required = required,
            granted = granted,
            denied = denied,
        )
    }
}