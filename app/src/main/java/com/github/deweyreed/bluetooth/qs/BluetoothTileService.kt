package com.github.deweyreed.bluetooth.qs

import android.content.BroadcastReceiver
import android.graphics.drawable.Icon
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService

class BluetoothTileService : TileService(), BluetoothBroadcastReceiver.BlueToothStateListener {
    private var receiver: BroadcastReceiver? = null

    override fun onClick() {
        super.onClick()
        toggleBluetooth()
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
        qsTile.updateTile()
    }

    override fun onBluetoothConnected() {
        qsTile.state = Tile.STATE_ACTIVE
        qsTile.icon = Icon.createWithResource(this, R.drawable.round_bluetooth_connected_24)
        qsTile.updateTile()
    }

    override fun onBluetoothDisabled() {
        qsTile.state = Tile.STATE_INACTIVE
        qsTile.icon = Icon.createWithResource(this, R.drawable.round_bluetooth_disabled_24)
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
