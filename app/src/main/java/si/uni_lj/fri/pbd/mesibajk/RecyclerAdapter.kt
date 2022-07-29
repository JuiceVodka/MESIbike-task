package si.uni_lj.fri.pbd.mesibajk

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class RecyclerAdapter(val bikeList :List<BikeModel>) : RecyclerView.Adapter<RecyclerAdapter.BikeHolder>(){

    interface bikeClickListener{
        fun rezClick(bikeName :String)
        fun detailClick(bikeName :String)
    }

    var buttonListener :bikeClickListener? = null


    inner class BikeHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!){
        var bikeStatus: TextView? = null
        var bikeName: TextView? = null
        var infoButton :Button? = null
        var rezButton :Button? = null
        init {
            bikeStatus = itemView?.findViewById(R.id.bike_status)
            bikeName = itemView?.findViewById(R.id.bike_name)
            infoButton = itemView?.findViewById(R.id.info)
            rezButton = itemView?.findViewById(R.id.rez)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BikeHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bike_card, parent, false)
        return BikeHolder(view)
    }

    override fun onBindViewHolder(holder: BikeHolder, position: Int) {
        holder.bikeStatus?.text = bikeList[holder.adapterPosition].status
        val nm = "#" + (holder.adapterPosition+1) + " " + bikeList[holder.adapterPosition].name
        holder.bikeName?.text = nm

        holder.itemView.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?){
                Toast.makeText(v!!.context, nm, Toast.LENGTH_SHORT).show()
            }
        })
        holder.rezButton?.setOnClickListener (object :View.OnClickListener{
            override fun onClick(v: View?){
                if(bikeList[holder.adapterPosition].status == "Na voljo"){
                    try{
                        buttonListener = v!!.context as bikeClickListener
                    }catch(e: ClassCastException){
                        throw ClassCastException(v!!.context.toString() + " doesnt implement listListener")
                    }
                    buttonListener?.rezClick(bikeList[holder.adapterPosition].name)
                }else{
                    Toast.makeText(v!!.context,"Kolo je že rezervirano!",Toast.LENGTH_SHORT).show();
                }
            }
        })

        holder.infoButton?.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?){
                Log.d("DEBUG", "click")
                try{
                    buttonListener = v!!.context as bikeClickListener
                }catch(e: ClassCastException){
                    throw ClassCastException(v!!.context.toString() + " doesnt implement listListener")
                }

                buttonListener?.detailClick(bikeList[holder.adapterPosition].name)
            }
        })
    }

    override fun getItemCount(): Int {
        return(bikeList.size)
    }

}