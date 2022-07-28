package si.uni_lj.fri.pbd.mesibajk

import android.content.ContentValues
import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.text.SimpleDateFormat
import java.util.*



@RunWith(JUnit4::class)
class DatabaseHelperTest{

    lateinit var instrumentationContext: Context
    lateinit var dbHelper :DatabaseHelper

    @Before
    fun setup() {
        instrumentationContext = InstrumentationRegistry.getInstrumentation().targetContext
        dbHelper = DatabaseHelper(instrumentationContext)
        if(dbHelper.returnBikes().size < 7){
            fillDb()
        }
    }

    /*This messes with the app
    @After
    fun closeDb(){
        dbHelper.deleteDatabase(instrumentationContext)
    }*/

    @Test
    fun returnBikes() {
        val numBikes = 7
        val bikesList = dbHelper.returnBikes()
        assertEquals(numBikes, bikesList.size)
    }

    @Test
    fun returnReservationFree() {
        val free = dbHelper.returnReservationFree(dbHelper.returnBikes()[0].name, SimpleDateFormat("HH dd/MM/yyyy",
            Locale.ENGLISH).format(Date()))
        assertEquals(true, free)
    }

    @Test
    fun getBikeKm() {
        val km = dbHelper.getBikeKm(dbHelper.returnBikes()[0].name)
        assertEquals(0, km)
    }

    @Test
    fun returnLastRez() {
        val lastRez = dbHelper.returnLastRez(dbHelper.returnBikes()[0].name)
        assertEquals(8, lastRez?.columnCount)
    }

    @Test
    fun returnSecondLastRez() {
        val rez = dbHelper.returnSecondLastRez(dbHelper.returnBikes()[0].name)
        assertEquals(8, rez?.columnCount)
    }

    @Test
    fun returnNumOddelek() {
        val numOddelek = dbHelper.returnNumOddelek(dbHelper.returnBikes()[0].name)
        assertEquals(4, numOddelek.size)
    }

    @Test
    fun returnNumNamen() {
        val numNamen = dbHelper.returnNumNamen(dbHelper.returnBikes()[0].name)
        assertEquals(2, numNamen.size)
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

    }
}