package com.github.deweyreed.bluetooth.qs

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Build
import android.provider.Settings
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService

class BluetoothTileService : TileService(), BluetoothBroadcastReceiver.BlueToothStateListener {
    private var receiver: BroadcastReceiver? = null

    override fun onClick() {
        super.onClick()
        if (!isBluetoothEnabled()) {
            startActivityAndCollapseCompat(
                Intent(Settings.ACTION_BLUETOOTH_SETTINGS)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        }
        toggleBluetooth()
    }

    private fun startActivityAndCollapseCompat(intent: Intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            startActivityAndCollapse(
                PendingIntent.getActivity(
                    this,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )
        } else {
            @Suppress("DEPRECATION", "StartActivityAndCollapseDeprecated")
            startActivityAndCollapse(intent)
        }
    }

    override fun onStartListening() {
        super.onStartListening()
        receiver?.let {
            unregisterReceiver(it)
        }
        receiver = BluetoothBroadcastReceiver.register(this, this)
    }

    override fun onBluetoothEnabled() {
        qsTile.state = Tile.STATE_ACTIVE
        qsTile.icon = Icon.createWithResource(this, R.drawable.round_bluetooth_24)
        qsTile.subtitle = "On"
        qsTile.updateTile()
    }

    override fun onBluetoothConnected() {
        qsTile.state = Tile.STATE_ACTIVE
        qsTile.icon = Icon.createWithResource(this, R.drawable.round_bluetooth_connected_24)
        qsTile.subtitle = "Connected"
        qsTile.updateTile()
    }

    override fun onBluetoothDisabled() {
        qsTile.state = Tile.STATE_INACTIVE
        qsTile.icon = Icon.createWithResource(this, R.drawable.round_bluetooth_disabled_24)
        qsTile.subtitle = "Off"
        qsTile.updateTile()
    }

    override fun onStopListening() {
        super.onStopListening()
        receiver?.let {
            unregisterReceiver(it)
            receiver = null
        }
    }
}
