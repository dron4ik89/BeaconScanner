package andrey.shpilevoy.beaconscanner

import java.util.ArrayList

interface Callback {

    fun onRange(array: ArrayList<Result>)

}