package si.uni_lj.fri.pbd.mesibajk

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat
import android.text.format.DateFormat.is24HourFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import si.uni_lj.fri.pbd.mesibajk.databinding.FragmentReservationBinding
import java.text.SimpleDateFormat
import java.util.*


class ReservationFragment() : Fragment()/*, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener */{


    interface reserveBikeInterface{
        fun reserveBike(bikeName: String, izposojevalec :String ,sektor :String, namen :String, odDatum :String, doDatum :String, km :String)
    }

    var resListener: ReservationFragment.reserveBikeInterface? = null

    private lateinit var binding: FragmentReservationBinding

    var ch :Int = 0
    var cd :Int = 0
    var cm :Int = 0
    var cy :Int = 0
    var cmin :Int = 0

    var odY = ""
    var odM = ""
    var odD = ""
    var odH = ""
    var odmin = ""
    var doY = ""
    var doM = ""
    var doD = ""
    var doH = ""
    var domin = ""


    var bikeName :String? = null

    var from_dateListener :OnDateSetListener = object : OnDateSetListener{
        override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
            odD = dayOfMonth.toString()
            odM = month.toString()
            odY = year.toString()
            val timePickerDialog = TimePickerDialog(activity,
                from_timeListener, ch, cmin, true
            )
            timePickerDialog.show()
        }

    }
    var to_dateListener: OnDateSetListener = object : OnDateSetListener{
        override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
            doD = dayOfMonth.toString()
            doM = month.toString()
            doY = year.toString()
            val timePickerDialog = TimePickerDialog(activity,
                to_timeListener, ch, cmin, true
            )
            timePickerDialog.show()
        }
    }

    var from_timeListener :TimePickerDialog.OnTimeSetListener = object : TimePickerDialog.OnTimeSetListener {
        override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
            odH = hourOfDay.toString()
            odmin = minute.toString()
            binding.odText.text = String.format("OD: %02d:%02d %02d/%02d/%02d", odH.toInt(), odmin.toInt(), odD.toInt(), odM.toInt(), odY.toInt())
        }

    }
    var to_timeListener: TimePickerDialog.OnTimeSetListener = object : TimePickerDialog.OnTimeSetListener {
        override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
            doH = hourOfDay.toString()
            domin = minute.toString()
            binding.doText.text = String.format("DO: %02d:%02d %02d/%02d/%02d", doH.toInt(), domin.toInt(), doD.toInt(), doM.toInt(), doY.toInt())
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
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
        ch = rightNow.get(Calendar.HOUR_OF_DAY)
        cd = rightNow.get(Calendar.DAY_OF_MONTH)
        cm = rightNow.get(Calendar.MONTH) +1
        cy = rightNow.get(Calendar.YEAR)
        cmin = rightNow.get(Calendar.MINUTE)

        binding.odF.setOnClickListener {
            val datePickerDialog =
                DatePickerDialog(view.context,
                    from_dateListener, cy, cm, cd)
            datePickerDialog.show()
        }

        binding.doF.setOnClickListener {
            val datePickerDialog =
            DatePickerDialog(view.context,
                to_dateListener, cy, cm, cd)
            datePickerDialog.show()
        }

        binding.add.setOnClickListener {
            val izposojevalec = binding.izposojevalec.text.toString()
            val sektor = binding.sektor.selectedItem.toString()
            val namen = binding.namen.selectedItem.toString()
            val km = binding.kmSlide.value.toInt().toString()


            val datesOk = Validator.validateDate(doH, doD, doM, domin) && Validator.validateDate(odH, odD, odM, odmin)
            val ostaloOk = Validator.validateName(izposojevalec)

            if(izposojevalec.length < 1){
                binding.izposojevalec.setError("Vsa polja morajo biti izpolnjena")
            }

            if(datesOk){
                val odDatum = String.format("%02d:%02d %02d/%02d/%02d", odH.toInt(), odmin.toInt(), odD.toInt(), odM.toInt(), odY.toInt())
                val odFormat = SimpleDateFormat("HH:mm dd/MM/yyyy").parse(odDatum)

                val doDatum = String.format("%02d:%02d %02d/%02d/%02d", doH.toInt(), domin.toInt(), doD.toInt(), doM.toInt(), odY.toInt())
                val doFormat = SimpleDateFormat("HH:mm dd/MM/yyyy").parse(doDatum)

                val currtime = SimpleDateFormat("HH:mm dd/MM/yyyy",
                    Locale.ENGLISH).format(Date())
                var curFormat = SimpleDateFormat("HH:mm dd/MM/yyyy").parse(currtime)

                if(odFormat > curFormat && doFormat > odFormat){
                    Log.d("DEBUG", "DATUMI OK")
                    if(ostaloOk){
                        bikeName?.let { it1 -> resListener?.reserveBike(it1, izposojevalec, sektor, namen, odDatum, doDatum, km) }
                    }
                }else if(odFormat <= curFormat){
                    Toast.makeText(view.context, "ČAS IZPOSOJE MORA BITI VEČJI OD TRENUTNEGA ČASA", Toast.LENGTH_SHORT).show()
                    binding.odText.setError("NAPAKA")
                    if(doFormat <= odFormat){
                        Toast.makeText(view.context, "ČAS VRAČILA MORA BITI VEČJI OD ČASA IZPOSOJE", Toast.LENGTH_SHORT).show()
                        binding.doText.setError("NAPAKA")
                    }else{
                        binding.doText.setError(null)
                    }
                }else if(doFormat <= odFormat){
                    binding.odText.setError(null)
                    Toast.makeText(view.context, "ČAS VRAČILA MORA BITI VEČJI OD ČASA IZPOSOJE", Toast.LENGTH_SHORT).show()
                    binding.doText.setError("NAPAKA")
                }
            }else {
                Toast.makeText(view.context, "NASTAVITE VELJAVEN DATUM IZPOSOJE IN VRAČILA", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}