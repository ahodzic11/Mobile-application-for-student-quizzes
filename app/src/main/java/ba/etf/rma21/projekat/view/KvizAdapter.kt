package ba.etf.rma21.projekat.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ba.etf.rma21.projekat.R
import ba.etf.rma21.projekat.data.models.Kviz
import java.text.SimpleDateFormat
import java.util.*

class KvizAdapter(
    private var kvizovi: List<Kviz>,
    private val onItemClicked: (kviz: Kviz)-> Unit) : RecyclerView.Adapter<KvizAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_list_item, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int = kvizovi.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val formater = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        var kviz : Kviz = kvizovi[position]
       // val datumFormat = SimpleDateFormat("dd.MM.yyyy")
        val datumFormat = SimpleDateFormat("dd.MM.yyyy")
        holder.imePredmeta.text = kviz.nazivPredmeta
        holder.nazivKviza.text = kviz.naziv
        holder.vrijemeKviza.text = kviz.trajanje.toString() + " min"
        if(kviz.datumRada == null) holder.brojBodova.text = ""
        else holder.brojBodova.text = kviz.osvojeniBodovi.toString()
        if(kviz.datumRada != null) holder.statusKviza.setImageResource(R.drawable.plava)
        else if(kviz.datumKraj == null || (kviz.datumRada == null && kviz.datumPocetka!=null && kviz.datumKraj!=null && formater.parse(kviz.datumPocetka).before(Calendar.getInstance().time) && formater.parse(kviz.datumKraj).after(Calendar.getInstance().time))) holder.statusKviza.setImageResource(R.drawable.zelena)
        else if(kviz.datumRada == null && kviz.datumPocetka!=null && formater.parse(kviz.datumPocetka).after(Calendar.getInstance().time)) holder.statusKviza.setImageResource(R.drawable.zuta)
        else if(kviz.datumRada == null && kviz.datumKraj!=null && formater.parse(kviz.datumKraj).before(Calendar.getInstance().time)) holder.statusKviza.setImageResource(R.drawable.crvena)

        if(kviz.datumRada != null) holder.datumKviza.text = datumFormat.format(formater.parse(kviz.datumRada))
        if(kviz.datumPocetka!=null && kviz.datumKraj!=null)
        if((kviz.datumRada == null  && formater.parse(kviz.datumKraj).before(Calendar.getInstance().time)) || (kviz.datumRada == null && formater.parse(kviz.datumPocetka).before(Calendar.getInstance().time) && formater.parse(kviz.datumKraj).after(Calendar.getInstance().time))) holder.datumKviza.text = datumFormat.format(kviz.datumKraj)
      //  if(kviz.datumRada == null && kviz.datumPocetka!=null && formater.parse(kviz.datumPocetka).after(Calendar.getInstance().time)) holder.datumKviza.text = datumFormat.format(kviz.datumPocetka)
        holder.itemView.setOnClickListener{ onItemClicked(kvizovi[position])}
    }

    fun updateKvizove(kvizovi: List<Kviz>){
        this.kvizovi=kvizovi
        notifyDataSetChanged()
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val imePredmeta: TextView = itemView.findViewById(R.id.imePredmeta)
        val nazivKviza: TextView = itemView.findViewById(R.id.nazivKviza)
        val datumKviza: TextView = itemView.findViewById(R.id.datumKviza)
        val vrijemeKviza: TextView = itemView.findViewById(R.id.vrijemeKviza)
        val brojBodova: TextView = itemView.findViewById(R.id.brojBodova)
        val statusKviza: ImageView = itemView.findViewById(R.id.statusKviza)
    }
}