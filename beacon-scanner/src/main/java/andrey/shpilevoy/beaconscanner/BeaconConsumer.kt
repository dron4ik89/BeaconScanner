package andrey.shpilevoy.beaconscanner

import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.RemoteException
import android.util.Log
import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.MonitorNotifier
import org.altbeacon.beacon.RangeNotifier
import org.altbeacon.beacon.Region
import java.util.ArrayList

class BeaconConsumer(val context: Context, val beaconManager: BeaconManager, val callback: Callback) : org.altbeacon.beacon.BeaconConsumer {

    val beaconsResult = ArrayList<Result>()
    val region = Region("myBeacons", null, null, null)

    fun end(){
        beaconManager.stopRangingBeaconsInRegion(region)
        beaconManager.stopMonitoringBeaconsInRegion(region)
    }

    override fun getApplicationContext(): Context {
        return context.applicationContext
    }

    override fun onBeaconServiceConnect() {



        beaconManager.addMonitorNotifier(object : MonitorNotifier {

            override fun didEnterRegion(region: Region) {
                println("ENTER ------------------->")
                try {
                    beaconManager?.startRangingBeaconsInRegion(region)
                } catch (e: RemoteException) {
                    e.printStackTrace()
                }

            }

            override fun didExitRegion(region: Region) {
                println("EXIT----------------------->")
                try {
                    beaconManager?.stopRangingBeaconsInRegion(region)
                } catch (e: RemoteException) {
                    e.printStackTrace()
                }

            }

            override fun didDetermineStateForRegion(state: Int, region: Region) {
                println("I have just switched from seeing/not seeing beacons: $state")
            }
        })

        beaconManager?.addRangeNotifier(RangeNotifier { beacons, region ->

            beaconsResult.clear()

            if (beacons.size > 0) {

                for (b in beacons) {

                    val uuid = b.id1.toString()
                    val major = b.id2.toString()
                    val minor = b.id3.toString()
                    val distance = Math.round(b.distance * 100.0) / 100.0

                    beaconsResult.add(
                        Result(
                            uuid,
                            major,
                            minor,
                            distance
                        )
                    )

                    Log.e("BEACON_LOG", "UUID $uuid")
                    Log.e("BEACON_LOG", "major $major")
                    Log.e("BEACON_LOG", "minor $minor")
                    Log.e("BEACON_LOG", "$distance meters")

                }

            } else if (beacons.isEmpty()) {

                Log.e("BEACON_LOG", "NULL")

            }

            callback.onRange(beaconsResult)
        })
        try {
            beaconManager?.startMonitoringBeaconsInRegion(region)
        } catch (e: RemoteException) {
        }

    }

    override fun unbindService(serviceConnection: ServiceConnection) {
        this.unbindService(serviceConnection)
    }

    override fun bindService(
        intent: Intent,
        serviceConnection: ServiceConnection,
        i: Int
    ): Boolean {
        return this.bindService(intent, serviceConnection, i)
    }

}