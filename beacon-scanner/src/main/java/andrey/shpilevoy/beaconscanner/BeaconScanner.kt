package andrey.shpilevoy.beaconscanner

import android.content.Context
import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.BeaconParser

class BeaconScanner {

    companion object {

        private var beaconManager: BeaconManager? = null

        var beaconConsumer: BeaconConsumer? = null

        fun start(context: Context, callback: Callback) {

            beaconManager = BeaconManager.getInstanceForApplication(context)
            beaconManager?.beaconParsers?.add(
                BeaconParser().setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25")
            )

            beaconConsumer = BeaconConsumer(context, beaconManager!!, callback)

            beaconManager?.bind(beaconConsumer!!)

        }

        fun stop() {

            if(beaconConsumer != null) {
                beaconManager?.unbind(beaconConsumer!!)

                beaconConsumer!!.end()
            }

        }

    }
}