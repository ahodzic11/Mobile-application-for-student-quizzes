package ba.etf.rma21.projekat.view

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import ba.etf.rma21.projekat.R
import ba.etf.rma21.projekat.data.models.KvizTaken
import ba.etf.rma21.projekat.data.models.Odgovor
import ba.etf.rma21.projekat.data.models.Pitanje
import ba.etf.rma21.projekat.viewmodel.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentPitanje(pitanje: Pitanje) : Fragment(){
    private lateinit var tekst: TextView
    private lateinit var listaOdgovora: ListView
    private lateinit var sharedViewModel: SharedViewModel
    private var listaOdgovorenihPitanja= listOf<Pitanje>()
    private var korisnikVM = KorisnikViewModel()
    private var kvizVM = KvizViewModel()
    private var odgovorVM = OdgovorViewModel()
    private var pitanje: Pitanje = pitanje
    private var takeKvizVM = TakeKvizViewModel()
    private var kvizTakenVM = KvizTakenViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.pitanje_fragment, container, false)
        sharedViewModel = ViewModelProviders.of(activity!!).get(SharedViewModel::class.java)
        tekst = view.findViewById(R.id.tekstPitanja)
        listaOdgovora = view.findViewById(R.id.odgovoriLista)
        tekst.text = pitanje.tekstPitanja
        korisnikVM.getListaOdgovorenihPitanja(kvizVM.getOtvoreniKviz()!!.id, onSuccess = ::updateListuOdgovorenih, onError = ::onError)
        return view
    }

    fun updateListuOdgovorenih(pitanja: List<Pitanje>){
        GlobalScope.launch(Dispatchers.IO){
            withContext(Dispatchers.Main){
                listaOdgovorenihPitanja = pitanja
                kvizTakenVM.daLiJePredan(takeKvizVM.getZadnjiZapocetiKviz().id, onSuccess = ::predanKviz, onError = ::onError)
            }
        }
    }

    fun predanKviz(predan: Boolean){
        GlobalScope.launch(Dispatchers.IO){
            withContext(Dispatchers.Main){
                Log.d("predan", predan.toString())
                val odgovori = pitanje.opcije
                if((!kvizVM.isMoguceUraditi(kvizVM.getOtvoreniKviz()!!) || predan || listaOdgovorenihPitanja.contains(pitanje)) || (kvizVM.isMoguceUraditi(kvizVM.getOtvoreniKviz()!!) && listaOdgovorenihPitanja.contains(pitanje) && !predan)){
                    val opcije = pitanje.opcije.split(",")
                    val arrayAdapter = ArrayAdapter<String>(tekst.context, android.R.layout.simple_list_item_1, opcije)
                    listaOdgovora.adapter = arrayAdapter
                    listaOdgovora.post {
                        odgovorVM.dajTacnostOdgovora(kvizVM.getOtvoreniKviz()!!.id, pitanje, onSuccess = ::oboji, onError = ::onError)
                        listaOdgovora.isEnabled = false
                    }
                }
                else{
                    val opcije = pitanje.opcije.split(",")
                    val arrayAdapter = ArrayAdapter<String>(tekst.context, android.R.layout.simple_list_item_1, opcije)
                    listaOdgovora.adapter = arrayAdapter
                    if(!kvizVM.getPredaniKvizovi().contains(kvizVM.getOtvoreniKviz()!!)){
                        listaOdgovora.setOnItemClickListener { parent, view, position, id ->
                            if(position==pitanje.tacan){
                                (view as TextView).setTextColor(Color.parseColor("#3DDC84"))
                                sharedViewModel.setPozicija(position)
                                sharedViewModel.setBoja("tacan")
                                sharedViewModel.setBrojOdgovora(sharedViewModel.getBrojOdgovora().value!! + 1)
                            }else{
                                (listaOdgovora.get(pitanje.tacan) as TextView).setTextColor(Color.parseColor("#3DDC84"))
                                (view as TextView).setTextColor(Color.parseColor("#DB4F3D"))
                                sharedViewModel.setPozicija(position)
                                sharedViewModel.setBoja("netacan")
                                sharedViewModel.setBrojOdgovora(sharedViewModel.getBrojOdgovora().value!! + 1)
                            }
                            listaOdgovora.isEnabled = false
                            odgovorVM.postaviOdgovorKviz(takeKvizVM.getZadnjiZapocetiKviz().id, pitanje.id, position, onSuccess = ::proba, onError = ::onError)
                        }
                    }else{
                        listaOdgovora.isEnabled = false
                    }
                }
            }
        }


    }

    fun oboji(tacan: Boolean){
        GlobalScope.launch(Dispatchers.IO){
            withContext(Dispatchers.Main){
                if(tacan){
                    korisnikVM.getOdgovorNaPitanje(pitanje, kvizVM.getOtvoreniKviz()!!, onSuccess = ::obojiZeleno, onError = ::onError)
                }
                else if(!tacan){
                    korisnikVM.getOdgovorNaPitanje(pitanje, kvizVM.getOtvoreniKviz()!!, onSuccess = ::obojiCrveno, onError = ::onError)
                }
            }
        }
    }

    fun obojiZeleno(poz: Int){
        GlobalScope.launch(Dispatchers.IO){
            withContext(Dispatchers.Main){
                val opcije = pitanje.opcije.split(",")
                if(poz<opcije.size) listaOdgovora[poz].setBackgroundColor(Color.parseColor("#3DDC84"))
            }
        }
    }

    fun obojiCrveno(poz: Int){
        GlobalScope.launch(Dispatchers.IO){
            withContext(Dispatchers.Main){
                val opcije = pitanje.opcije.split(",")
                if(poz<opcije.size){
                    listaOdgovora[pitanje.tacan].setBackgroundColor(Color.parseColor("#3DDC84"))
                    listaOdgovora[poz].setBackgroundColor(Color.parseColor("#DB4F3D"))
                }
            }
        }
    }

    fun proba(id: Int?){
        korisnikVM.getTacnoOdgovorenaPitanjaZaKviz(kvizVM.getOtvoreniKviz()!!, onSuccess = ::ttt, onError = ::onError)
    }

    fun onError(){
        val toast = Toast.makeText(context, "Error", Toast.LENGTH_SHORT)
        toast.show()
    }


    fun ttt(lista: List<Pitanje>){
        Log.d("adnan", "uslo u petlju")
        for(pitanje in lista)
            Log.d("adnan", "lista-" + pitanje.id + pitanje.tekstPitanja)
    }

    companion object {
        fun newInstance(): FragmentPitanje = FragmentPitanje(
            Pitanje(0,
                "",
                "",
                "",
                2
            )
        )
    }
}