package com.github.deweyreed.bluetooth.qs

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.service.quicksettings.TileService

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent?.action == TileService.ACTION_QS_TILE_PREFERENCES) {
            openBluetoothSettings()
        } else {
            if (!isBluetoothEnabled()) {
                openBluetoothSettings()
            }
            toggleBluetooth()
        }
        finish()
    }

    private fun openBluetoothSettings() {
        startActivity(
            Intent(Settings.ACTION_BLUETOOTH_SETTINGS)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }
}
