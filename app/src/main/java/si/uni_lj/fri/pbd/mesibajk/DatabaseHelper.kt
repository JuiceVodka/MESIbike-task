package si.uni_lj.fri.pbd.mesibajk

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.text.SimpleDateFormat

class DatabaseHelper(context: Context) :SQLiteOpenHelper(
    context, DATABASE_NAME, null, DATABASE_VERSION
){

    companion object{
        const val TAG = "DatabaseHelper"
        const val DATABASE_VERSION = 2
        const val DATABASE_NAME = "bikes_db"
        const val TABLE_BIKES = "bikes"
        const val TABLE_RESERVATIONS = "reservations"
        const val BIKE_NAME = "name"
        const val BIKE_KM = "km"
        const val BIKE_STATUS = "status"
        const val IZPOSOJEVALEC_NAME = "imepriimek"
        const val IZPOSOJEVALEC_SEKTOR = "sektor"
        const val IZPOSOJEVALEC_NAMEN = "namen"
        const val TERMIN_OD = "od"
        const val TERMIN_DO = "do"
        const val _ID = "_id"
        const val _IDR = "_idr"
        val COLUMNS_BIKE = arrayOf(_ID, BIKE_NAME, BIKE_KM)
        val COLUMNS_RESERVATION = arrayOf(_IDR, BIKE_NAME, IZPOSOJEVALEC_NAME, IZPOSOJEVALEC_NAMEN, IZPOSOJEVALEC_SEKTOR, TERMIN_OD, TERMIN_DO)
    }

    override fun onCreate(db: SQLiteDatabase?) {
        Log.d("DEBUG", "onCreate")
        val CREATE_RESERVATIONS_TABLE = ("CREATE TABLE "+ TABLE_RESERVATIONS+"("
                + _IDR+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BIKE_NAME + " TEXT NOT NULL,"
                + IZPOSOJEVALEC_NAME + " TEXT NOT NULL,"
                + IZPOSOJEVALEC_SEKTOR + " TEXT NOT NULL,"
                + IZPOSOJEVALEC_NAMEN + " TEXT NOT NULL,"
                + TERMIN_OD + " TEXT NOT NULL,"
                + TERMIN_DO + " TEXT NOT NULL,"
                + BIKE_KM + " TEXT NOT NULL)")

        val CREATE_BIKES_TABLE = ("CREATE TABLE "+ TABLE_BIKES+"("
                + _ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BIKE_NAME + " TEXT NOT NULL,"
                + BIKE_KM + " INTEGER)")

        db?.execSQL(CREATE_BIKES_TABLE)
        db?.execSQL(CREATE_RESERVATIONS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.d(TAG, "onUpgrade")

        db?.execSQL("DROP TABLE IF EXISTS "+ TABLE_BIKES)
        db?.execSQL("DROP TABLE IF EXISTS "+ TABLE_RESERVATIONS)
        onCreate(db)
    }

    fun deleteDatabase(context: Context){
        context.deleteDatabase(DATABASE_NAME)
    }

    fun returnBikes() :List<BikeModel>{
        val cursor = this.readableDatabase?.rawQuery("SELECT * FROM " + TABLE_BIKES, null);

        val bikeList = mutableListOf<BikeModel>()

        if (cursor?.moveToFirst() == true) {
            do {
                bikeList.add(
                    BikeModel(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getInt(2),
                        "Na voljo"
                    )
                )
            } while (cursor.moveToNext())
        }

        return bikeList
    }

    fun returnReservations(bikeName:String) : Cursor? {
        val cursor = this.readableDatabase?.rawQuery("SELECT * FROM " + TABLE_RESERVATIONS + " WHERE name=?", arrayOf(bikeName));
        return cursor
    }

    fun returnReservationFree(bikeName:String, currTime :String) :Boolean{
        val cursor = this.readableDatabase?.rawQuery("SELECT * FROM " + TABLE_RESERVATIONS + " WHERE name=? ORDER BY _idr DESC LIMIT 1", arrayOf(bikeName));
        var doTime = ""
        var odTime = ""
        if (cursor?.moveToFirst() == true) {
            do {
                doTime = cursor.getString(6)
                odTime = cursor.getString(5)
            } while (cursor.moveToNext())

            val odFormat = SimpleDateFormat("HH:mm dd/MM/yyyy").parse(odTime)
            val doFormat = SimpleDateFormat("HH:mm dd/MM/yyyy").parse(doTime)
            var curFormat = SimpleDateFormat("HH:mm dd/MM/yyyy").parse(currTime)

            return (doFormat < curFormat)
            //Zaradi stavka v navolilih: "Vidna prihodnja rezervacija" sem nalogo naredil v smeri,
            //da je lahko aktivna samo ena rezervacija naenkrat, za spremembo kjer bio bilo možnih
            //več rezervacij naenkrat bi moral tu spremeniti kaj se vrača ter dodati nekaj logike
            //v ReservationFragment
        }else{
            return true
        }
    }

    fun getBikeKm(bikeName: String) :Int{
        val cursor = this.readableDatabase?.rawQuery("SELECT * FROM " + TABLE_RESERVATIONS + " WHERE name=?", arrayOf(bikeName));
        var km = 0
        if (cursor?.moveToFirst() == true) {
            do {
                val queryKm = cursor.getString(7)
                km += queryKm.toInt()
            } while (cursor.moveToNext())
        }

        return km
    }

    fun returnLastRez(bikeName:String) : Cursor? {
        val cursor = this.readableDatabase?.rawQuery("SELECT * FROM " + TABLE_RESERVATIONS + " WHERE name=? ORDER BY _idr DESC LIMIT 1", arrayOf(bikeName));
        return cursor
    }

    fun returnSecondLastRez(bikeName: String) : Cursor? {
        val cursor = this.readableDatabase?.rawQuery("SELECT * FROM " + TABLE_RESERVATIONS + " WHERE name=? ORDER BY _idr DESC LIMIT 1,1", arrayOf(bikeName));
        return cursor
    }

    fun returnNumOddelek(bikeName: String) :List<Int>{
        val oddelekList = mutableListOf<Int>()
        var cursor = this.readableDatabase?.rawQuery("SELECT * FROM " + TABLE_RESERVATIONS + " WHERE name=? AND sektor='Razvoj'", arrayOf(bikeName))
        cursor?.count?.let { oddelekList.add(it) }

        cursor = this.readableDatabase?.rawQuery("SELECT * FROM " + TABLE_RESERVATIONS + " WHERE name=? AND sektor='Prodaja'", arrayOf(bikeName))
        cursor?.count?.let { oddelekList.add(it) }

        cursor = this.readableDatabase?.rawQuery("SELECT * FROM " + TABLE_RESERVATIONS + " WHERE name=? AND sektor='Marketing'", arrayOf(bikeName))
        cursor?.count?.let { oddelekList.add(it) }

        cursor = this.readableDatabase?.rawQuery("SELECT * FROM " + TABLE_RESERVATIONS + " WHERE name=? AND sektor='Proizvodnja'", arrayOf(bikeName))
        cursor?.count?.let { oddelekList.add(it) }

        return oddelekList
    }

    fun returnNumNamen(bikeName: String) :List<Int>{
        val namenList = mutableListOf<Int>()
        var cursor = this.readableDatabase?.rawQuery("SELECT * FROM " + TABLE_RESERVATIONS + " WHERE name=? AND namen='Službeni'", arrayOf(bikeName))
        cursor?.count?.let { namenList.add(it) }

        cursor = this.readableDatabase?.rawQuery("SELECT * FROM " + TABLE_RESERVATIONS + " WHERE name=? AND namen='Privatni'", arrayOf(bikeName))
        cursor?.count?.let { namenList.add(it) }

        return namenList
    }
}