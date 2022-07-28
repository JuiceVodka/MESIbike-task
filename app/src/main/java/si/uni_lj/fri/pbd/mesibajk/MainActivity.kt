package si.uni_lj.fri.pbd.mesibajk

import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import si.uni_lj.fri.pbd.mesibajk.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), RecyclerAdapter.bikeClickListener, BikesFragment.bikeLst {
    private lateinit var binding : ActivityMainBinding;
    private var fragmentTransaction : FragmentTransaction? = null

    var dbHelper :DatabaseHelper? = null

    var bikeList: List<BikeModel>? = null

    var listener: ReservationFragment.reserveBikeInterface = object : ReservationFragment.reserveBikeInterface {
        override fun reserveBike(
            bikeName: String,
            izposojevalec: String,
            sektor: String,
            namen: String,
            odDatum: String,
            doDatum: String,
            km: String
        ) {
            Log.d("DEBUG", "INTERFACE")

            //dbHelper?.returnReservations(bikeName)

            val values: ContentValues = ContentValues()
            values.put(DatabaseHelper.BIKE_NAME, bikeName);
            values.put(DatabaseHelper.IZPOSOJEVALEC_NAME, izposojevalec);
            values.put(DatabaseHelper.IZPOSOJEVALEC_NAMEN, namen);
            values.put(DatabaseHelper.IZPOSOJEVALEC_SEKTOR, sektor);
            values.put(DatabaseHelper.TERMIN_OD, odDatum);
            values.put(DatabaseHelper.TERMIN_DO, doDatum);
            values.put(DatabaseHelper.BIKE_KM, km);
            dbHelper?.writableDatabase?.insert(DatabaseHelper.TABLE_RESERVATIONS, null, values)
            values.clear()

            fragmentTransaction = supportFragmentManager.beginTransaction()
            supportFragmentManager.popBackStack("bikes", FragmentManager.POP_BACK_STACK_INCLUSIVE)
            val bikesFragment :BikesFragment = BikesFragment()
            fragmentTransaction?.replace(R.id.fragmentFrame, bikesFragment)
            fragmentTransaction?.commit()
            updateBikes()
        }
    }

    public fun gtListener(): ReservationFragment.reserveBikeInterface {
        return listener
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)

        val prefs : SharedPreferences = getApplicationContext().getSharedPreferences("preferences", Context.MODE_PRIVATE)
        Log.d("DEBUG", prefs.all.toString())
        if(prefs.getBoolean("frstRun", true)){
            prefs.edit().putBoolean("frstRun", false).commit()
            Log.d("DEBUG", "FRST")
            fillDb()
        }else{
            Log.d("DEBUG", "SCND")
            //fillDb()
        }

        bikeList = dbHelper!!.returnBikes()
        if(bikeList!!.size < 7){
            fillDb()
            bikeList = dbHelper!!.returnBikes()
        }

        updateBikes()

        fragmentTransaction = supportFragmentManager.beginTransaction()


        val bikesFragment :BikesFragment = BikesFragment()


        fragmentTransaction?.add(R.id.fragmentFrame, bikesFragment)
        fragmentTransaction?.commit()

    }

    fun updateBikes(){
        for(bike in bikeList!!){
            val free = dbHelper?.returnReservationFree(bike.name, SimpleDateFormat("HH dd/MM/yyyy",
                Locale.ENGLISH).format(Date()))
            if(free == true){
                bike.status = "Na voljo"
            }else{
                bike.status = "Izposojeno"
            }
        }
    }

    fun fillDb(){
        val values: ContentValues = ContentValues()
        values.put(DatabaseHelper.BIKE_NAME, "Modro kolo");
        values.put(DatabaseHelper.BIKE_KM, 0);
        //values.put(DatabaseHelper.BIKE_STATUS, "Na voljo");
        dbHelper?.writableDatabase?.insert(DatabaseHelper.TABLE_BIKES, null, values)
        values.clear()

        values.put(DatabaseHelper.BIKE_NAME, "Zeleno kolo");
        values.put(DatabaseHelper.BIKE_KM, 0);
        //values.put(DatabaseHelper.BIKE_STATUS, "Na voljo");
        dbHelper?.writableDatabase?.insert(DatabaseHelper.TABLE_BIKES, null, values)
        values.clear()

        values.put(DatabaseHelper.BIKE_NAME, "Hitro kolo");
        values.put(DatabaseHelper.BIKE_KM, 0);
        //values.put(DatabaseHelper.BIKE_STATUS, "Na voljo");
        dbHelper?.writableDatabase?.insert(DatabaseHelper.TABLE_BIKES, null, values)
        values.clear()

        values.put(DatabaseHelper.BIKE_NAME, "Popraskano kolo");
        values.put(DatabaseHelper.BIKE_KM, 0);
        //values.put(DatabaseHelper.BIKE_STATUS, "Na voljo");
        dbHelper?.writableDatabase?.insert(DatabaseHelper.TABLE_BIKES, null, values)
        values.clear()

        values.put(DatabaseHelper.BIKE_NAME, "Staro kolo");
        values.put(DatabaseHelper.BIKE_KM, 0);
        //values.put(DatabaseHelper.BIKE_STATUS, "Na voljo");
        dbHelper?.writableDatabase?.insert(DatabaseHelper.TABLE_BIKES, null, values)
        values.clear()

        values.put(DatabaseHelper.BIKE_NAME, "Pocasno kolo");
        values.put(DatabaseHelper.BIKE_KM, 0);
        //values.put(DatabaseHelper.BIKE_STATUS, "Na voljo");
        dbHelper?.writableDatabase?.insert(DatabaseHelper.TABLE_BIKES, null, values)
        values.clear()

        values.put(DatabaseHelper.BIKE_NAME, "Elektricno kolo");
        values.put(DatabaseHelper.BIKE_KM, 0);
        //values.put(DatabaseHelper.BIKE_STATUS, "Na voljo");
        dbHelper?.writableDatabase?.insert(DatabaseHelper.TABLE_BIKES, null, values)
        values.clear()

        //dbHelper?.returnBikes()

        Log.d("DEBUG", "Baza napolnjena")
    }

    //from BikesFragment
    override fun detailClick(bikeName: String) {
        fragmentTransaction = supportFragmentManager.beginTransaction()
        val bundle = Bundle()
        bundle.putString("bikename", bikeName)

        val detailFragment : DetailsFragment = DetailsFragment()
        detailFragment.arguments = bundle

        fragmentTransaction?.addToBackStack("bikes")
        if (detailFragment != null) {
            fragmentTransaction?.replace(R.id.fragmentFrame, detailFragment)
        }
        fragmentTransaction?.commit()
    }

    override fun rezClick(bikeName: String) {
        Log.d("DEBUG", bikeName)
        fragmentTransaction = supportFragmentManager.beginTransaction()

        val bundle = Bundle()
        bundle.putString("bikename", bikeName)

        val reservationFragment :ReservationFragment = ReservationFragment()
        reservationFragment.arguments = bundle
        fragmentTransaction?.addToBackStack("bikes")
        fragmentTransaction?.replace(R.id.fragmentFrame, reservationFragment)
        fragmentTransaction?.commit()
    }

    //from ReservationFragment
    /*override fun reserveBike(
        bikeName: String,
        izposojevalec: String,
        sektor: String,
        namen: String,
        odDatum: String,
        doDatum: String,
        km: String
    ) {
        Log.d("DEBUG", "INTERFACE")

        //dbHelper?.returnReservations(bikeName)

        val values: ContentValues = ContentValues()
        values.put(DatabaseHelper.BIKE_NAME, bikeName);
        values.put(DatabaseHelper.IZPOSOJEVALEC_NAME, izposojevalec);
        values.put(DatabaseHelper.IZPOSOJEVALEC_NAMEN, namen);
        values.put(DatabaseHelper.IZPOSOJEVALEC_SEKTOR, sektor);
        values.put(DatabaseHelper.TERMIN_OD, odDatum);
        values.put(DatabaseHelper.TERMIN_DO, doDatum);
        values.put(DatabaseHelper.BIKE_KM, km);
        dbHelper?.writableDatabase?.insert(DatabaseHelper.TABLE_RESERVATIONS, null, values)
        values.clear()

        fragmentTransaction = supportFragmentManager.beginTransaction()
        supportFragmentManager.popBackStack("bikes", FragmentManager.POP_BACK_STACK_INCLUSIVE)
        val bikesFragment :BikesFragment = BikesFragment()
        fragmentTransaction?.replace(R.id.fragmentFrame, bikesFragment)
        fragmentTransaction?.commit()
        updateBikes()
    }*/


    override fun gbl(): List<BikeModel>? {
        return bikeList
    }

}