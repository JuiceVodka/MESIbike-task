package si.uni_lj.fri.pbd.mesibajk

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import si.uni_lj.fri.pbd.mesibajk.databinding.FragmentDetailsBinding
import java.text.SimpleDateFormat
import java.util.*

class DetailsFragment() : Fragment() {

    private lateinit var binding: FragmentDetailsBinding

    var dbHelper :DatabaseHelper? = null

    var bikeName :String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        val view = binding.root

        dbHelper = DatabaseHelper(view.context)

        bikeName = arguments?.getString("bikename")

        val lastCursor = bikeName?.let { dbHelper!!.returnLastRez(it) }
        Log.d("DEBUG", lastCursor?.count.toString())
        Log.d("DEBUG", lastCursor?.columnCount.toString())
        var trenutnaRezervacija = "Ni podatkov o trenutni rezervaciji"
        val exsistsOne = lastCursor?.moveToNext()
        if(exsistsOne == true){
            trenutnaRezervacija = String.format("Trenutna rezervacija:\nIzposojevalec: %s \nOd: %s\nDo: %s \nOddelek: %s \nNamen: %s", lastCursor?.getString(2), lastCursor?.getString(5), lastCursor?.getString(6), lastCursor?.getString(3), lastCursor?.getString(4))
        }

        val isFree = bikeName?.let {
            dbHelper!!.returnReservationFree(
                it, SimpleDateFormat("HH dd/MM/yyyy",
                    Locale.ENGLISH).format(Date()))
        }

        val secondLastCursor = bikeName?.let { dbHelper!!.returnSecondLastRez(it) }
        var zadnjaRezervacija = "Ni podatkov o zadnji rezervaciji"
        val exsistsTwo = secondLastCursor?.moveToNext()
        if(exsistsTwo == true && !isFree!!){
            zadnjaRezervacija = String.format("Zadnja rezervacija:\nIzposojevalec: %s \nOd: %s\nDo:%s \nOddelek: %s \nNamen: %s", secondLastCursor?.getString(2), secondLastCursor?.getString(5), secondLastCursor?.getString(6), secondLastCursor?.getString(3), secondLastCursor?.getString(4))
        }else if(isFree == true && exsistsOne == true){
            zadnjaRezervacija = String.format("Zadnja rezervacija:\nIzposojevalec: %s \nOd: %s\nDo: %s \nOddelek: %s \nNamen: %s", lastCursor?.getString(2), lastCursor?.getString(5), lastCursor?.getString(6), lastCursor?.getString(3), lastCursor?.getString(4))
            trenutnaRezervacija = "Ni podatkov o trenutni rezervaciji"
        }


        val km = bikeName?.let { dbHelper!!.getBikeKm(it) }
        val kmFormat = String.format("Stevilo prevozenih kilometrov: %d", km)

        val stNamen = bikeName?.let { dbHelper!!.returnNumNamen(it) }
        val namenString = String.format("Stevilo izposoj po namenih:\n-Sluzbeni: %d\n-Privatni: %d",
            stNamen?.get(0) ?: "/", stNamen?.get(1) ?: "/"
        )

        val stOddelek = bikeName?.let { dbHelper!!.returnNumOddelek(it) }
        val oddelekString = String.format("Stevilo izposoj po oddelku:\n-Razvoj: %d\n-Prodaja: %d\n-Marketing :%d\n-Proizvodnja: %d",
            stOddelek?.get(0) ?: "/", stOddelek?.get(1) ?: "/", stOddelek?.get(2) ?: "/",
            stOddelek?.get(3) ?: "/"
        )

        binding.next.setText(trenutnaRezervacija)
        binding.last.setText(zadnjaRezervacija)
        binding.km.setText(kmFormat)
        binding.izposojaOddelek.setText(oddelekString)
        binding.izposojaNamen.setText(namenString)

        return view
    }

}