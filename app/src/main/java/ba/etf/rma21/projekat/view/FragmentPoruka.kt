package ba.etf.rma21.projekat.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import ba.etf.rma21.projekat.R
import ba.etf.rma21.projekat.data.models.Grupa
import ba.etf.rma21.projekat.data.models.Predmet
import ba.etf.rma21.projekat.viewmodel.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentPoruka : Fragment(){
    private lateinit var text: TextView
    private lateinit var sharedViewModel: SharedViewModel
    private var kvizVM = KvizViewModel()
    private var grupaVM = GrupaViewModel()
    private var predmetVM = PredmetViewModel()
    private var kvizTakenVM = KvizTakenViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.poruka_fragment, container, false)
        sharedViewModel = ViewModelProviders.of(activity!!).get(SharedViewModel::class.java)
        val pozvanOd = arguments!!.getString("predajKviz")
        if(pozvanOd!=null){
            if(sharedViewModel.getPredanost()!=2) sharedViewModel.setPredan(1)
        }
        text = view.findViewById(R.id.tvPoruka)
        if(pozvanOd!=null){
            val kviz = kvizVM.getOtvoreniKviz()
            if(kviz != null){
                kvizTakenVM.dajBodoveZaKviz(kviz.id, onSuccess = ::dobavljenProcenatTacnosti, onError = ::onError)
            }
        }
        else{
            text = view.findViewById(R.id.tvPoruka)
            grupaVM.getGrupu(grupaVM.getIdZadnjeGrupe(), onSuccess = ::dobavljenaGrupa, onError = ::onError)
        }
        return view
    }

    fun dobavljenProcenatTacnosti(procenat: Int){
        GlobalScope.launch(Dispatchers.IO){
            withContext(Dispatchers.Main){
                text.setText("Završili ste kviz ${kvizVM.getOtvoreniKviz()!!.naziv} sa tačnosti ${procenat}")
            }
        }
    }

    fun dobavljenaGrupa(grupa: Grupa){
        GlobalScope.launch(Dispatchers.IO){
            withContext(Dispatchers.Main){
                grupaVM.setNazivZadnjeUpisaneGrupe(grupa.naziv!!)
                predmetVM.dajPredmetZaGrupu(grupa, onSuccess = ::dobavljenPredmet, onError = ::onError)
            }
        }
    }

    fun dobavljenPredmet(predmet: Predmet){
        GlobalScope.launch(Dispatchers.IO){
            withContext(Dispatchers.Main){
                text.setText("Uspješno ste upisani u grupu ${grupaVM.getNazivZadnjeUpisaneGrupe()} predmeta ${predmet.naziv}!")
            }
        }
    }

    companion object {
        fun newInstance(): FragmentPoruka = FragmentPoruka()
    }

    fun onError(){
        val toast = Toast.makeText(context, "Error", Toast.LENGTH_SHORT)
        toast.show()
    }
}