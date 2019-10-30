package andrey.shpilevoy.beaconscanner.demo

import andrey.shpilevoy.beaconscanner.BeaconScanner
import andrey.shpilevoy.beaconscanner.Callback
import andrey.shpilevoy.beaconscanner.Result
import android.Manifest
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.RemoteException
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import org.altbeacon.beacon.*
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), 0)
        }else{

            BeaconScanner.start(this, callback)

        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            0 -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                BeaconScanner.start(this, callback)

            }
        }
    }

    val callback = object: Callback{
        override fun onRange(array: ArrayList<Result>) {

            var content = ""
            text_view.text = content

            array.forEach {

                content += "UUID ${it.uuid} \n"
                content += "major ${it.major} \n"
                content += "minor ${it.minor} \n"
                content += "${it.distance} meters \n\n"

            }

            text_view.text = content

        }

    }

    // Override onDestroy Method
    override fun onDestroy() {
        super.onDestroy()
        BeaconScanner.stop()
    }
}
