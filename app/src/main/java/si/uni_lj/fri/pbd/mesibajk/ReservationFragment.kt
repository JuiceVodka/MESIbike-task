package si.uni_lj.fri.pbd.mesibajk

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import si.uni_lj.fri.pbd.mesibajk.databinding.FragmentReservationBinding
import java.text.SimpleDateFormat
import java.util.*

class ReservationFragment() : Fragment() {

    interface reserveBikeInterface{
        fun reserveBike(bikeName: String, izposojevalec :String ,sektor :String, namen :String, odDatum :String, doDatum :String, km :String)
    }

    var resListener: ReservationFragment.reserveBikeInterface? = null

    private lateinit var binding: FragmentReservationBinding

    var bikeName :String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        /*try{
            resListener = context as reserveBikeInterface
        }catch(e: ClassCastException){
            throw ClassCastException(context.toString() + " doesnt implement required interface")
        }*/
        if (context is MainActivity) {
            resListener = (context as MainActivity).gtListener()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentReservationBinding.inflate(inflater, container, false)
        val view = binding.root

        bikeName = arguments?.getString("bikename")

        val rightNow = Calendar.getInstance()
        val ch :Int = rightNow.get(Calendar.HOUR_OF_DAY)
        val cd :Int = rightNow.get(Calendar.DAY_OF_MONTH)
        val cm :Int = rightNow.get(Calendar.MONTH) +1

        binding.odDan.setText(cd.toString())
        binding.odMesec.setText(cm.toString())
        binding.odUra.setText(ch.toString())
        binding.doDan.setText(cd.toString())
        binding.doMesec.setText(cm.toString())
        binding.doUra.setText(ch.toString())

        //binding.izposojevalec.setText("Niko Sok")


        binding.add.setOnClickListener {
            //val bikeId = binding.numBike.text.toString().toInt()
            val izposojevalec = binding.izposojevalec.text.toString()
            val sektor = binding.sektor.selectedItem.toString()
            val namen = binding.namen.selectedItem.toString()
            val odM = binding.odMesec.text.toString()
            val odD = binding.odDan.text.toString()
            val odH = binding.odUra.text.toString()
            val doM = binding.doMesec.text.toString()
            val doD = binding.doDan.text.toString()
            val doH = binding.doUra.text.toString()
            val km = binding.kmSlide.value.toInt().toString()


            val datesOk = Validator.validateDate(doH, doD, doM) && Validator.validateDate(odH, odD, odM)
            val ostaloOk = Validator.validateName(izposojevalec)

            if(izposojevalec.length < 1){
                binding.izposojevalec.setError("Vsa polja morajo biti izpolnjena")
            }
            if(odM.length < 1 || !odM.all { Character.isDigit(it) }){
                binding.odMesec.setError("Vsa polja morajo biti izpolnjena")
            }
            if(odD.length < 1 || !odD.all { Character.isDigit(it) }){
                binding.odDan.setError("Vsa polja morajo biti izpolnjena")
            }
            if(odH.length < 1 || !odH.all { Character.isDigit(it) }){
                binding.odUra.setError("Vsa polja morajo biti izpolnjena")
            }
            if(doM.length < 1 || !doM.all { Character.isDigit(it) }){
                binding.doMesec.setError("Vsa polja morajo biti izpolnjena")
            }
            if(doD.length < 1 || !doD.all { Character.isDigit(it) }){
                binding.doDan.setError("Vsa polja morajo biti izpolnjena")
            }
            if(doH.length < 1 || !doH.all { Character.isDigit(it) }){
                binding.doUra.setError("Vsa polja morajo biti izpolnjena")
            }

            //SimpleDateFormat("HH:mm:ss dd/MM/yyyy")

            if(datesOk){
                val odDatum = String.format("%2d %2d/%2d/%2d", odH.toInt(), odD.toInt(), odM.toInt(), 2022)
                val odFormat = SimpleDateFormat("HH dd/MM/yyyy").parse(odDatum)

                val doDatum = String.format("%2d %2d/%2d/%2d", doH.toInt(), doD.toInt(), doM.toInt(), 2022)
                val doFormat = SimpleDateFormat("HH dd/MM/yyyy").parse(doDatum)

                val currtime = SimpleDateFormat("HH dd/MM/yyyy",
                    Locale.ENGLISH).format(Date())
                var curFormat = SimpleDateFormat("HH dd/MM/yyyy").parse(currtime)

                Log.d("DEBUG", curFormat.toString())


                if(odFormat >= curFormat && doFormat > odFormat){
                    Log.d("DEBUG", "DATUMI OK")

                    if(ostaloOk){
                        bikeName?.let { it1 -> resListener?.reserveBike(it1, izposojevalec, sektor, namen, odDatum, doDatum, km) }
                    }
                }
            }
        }

        return view
    }

}