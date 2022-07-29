package si.uni_lj.fri.pbd.mesibajk

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import si.uni_lj.fri.pbd.mesibajk.databinding.FragmentBikesBinding
import java.lang.ClassCastException

class BikesFragment() : Fragment() {

    interface bikeLst{
        fun gbl():List<BikeModel>?
    }

    private lateinit var binding: FragmentBikesBinding

    var bikeListener: BikesFragment.bikeLst? = null

    var bikelist :List<BikeModel>? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            bikeListener = context as bikeLst
        }catch(e: ClassCastException){
            throw ClassCastException(context.toString() + " doesnt implement required interface")
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
        binding = FragmentBikesBinding.inflate(inflater, container, false)
        val view = binding.root

        bikelist = bikeListener?.gbl()

        val adapter = bikelist?.let { RecyclerAdapter(it) }
        val recyclerView = binding.recyclerView
        val layoutmanager = LinearLayoutManager(view.context)
        recyclerView.layoutManager = layoutmanager
        recyclerView.adapter = adapter

        return view
    }

}