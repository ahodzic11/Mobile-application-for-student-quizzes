package ba.etf.rma21.projekat.view

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import ba.etf.rma21.projekat.MainActivity
import ba.etf.rma21.projekat.R
import ba.etf.rma21.projekat.data.models.KvizTaken
import ba.etf.rma21.projekat.data.models.Odgovor
import ba.etf.rma21.projekat.data.models.Pitanje
import ba.etf.rma21.projekat.viewmodel.*
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class FragmentPokusaj(listaPitanja: List<Pitanje>) : Fragment(){
    private lateinit var navPitanja: NavigationView
    private lateinit var sharedViewModel: SharedViewModel
    private var listaPitanja: List<Pitanje> = listaPitanja
    private var kvizoviModel = KvizViewModel()
    private var takeKvizVM = TakeKvizViewModel()
    private var odgovoriVM = OdgovorViewModel()
    private var trenutnoPitanje = 0
    private var brojacPitanja = 0
    private var kvizTakenVM = KvizTakenViewModel()
    private var odgovorVM = OdgovorViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.pokusaj_fragment, container, false)
        sharedViewModel = ViewModelProviders.of(activity!!).get(SharedViewModel::class.java)
        sharedViewModel.setBrojOdgovora(0)
        sharedViewModel.setPredan(0)
        takeKvizVM.otvoriIliKreirajPokusajKviza(kvizoviModel.getOtvoreniKviz()!!.id, onSuccess = ::dodajKvizTakenId, onError = ::onError)
        return view
    }

    fun dodajKvizTakenId(zapocetiKviz: KvizTaken){
        GlobalScope.launch(Dispatchers.IO){
            withContext(Dispatchers.Main){
                takeKvizVM.setZadnjiZapocetiKviz(zapocetiKviz)
                kvizoviModel.updateKvizPokusaj(zapocetiKviz.KvizId, zapocetiKviz.id, onSuccess = ::zapocniKviz, onError = ::onError)
            }
        }
    }


    fun zapocniKviz(poruka: String?){
        GlobalScope.launch(Dispatchers.IO){
            withContext(Dispatchers.Main){
                kvizTakenVM.daLiJePredan(takeKvizVM.getZadnjiZapocetiKviz().id, onSuccess = ::predan, onError = ::onError)
            }
        }
    }

    fun predan(predan: Boolean){
        GlobalScope.launch(Dispatchers.IO){
            withContext(Dispatchers.Main){
                takeKvizVM.setPredan(predan)
                context?.let {
                    odgovorVM.getOdgovoriKvizAPI(kvizoviModel.getOtvoreniKviz()!!.id, onSuccess = ::odgovori, onError = ::onError)
                }
            }
        }
    }

    fun odgovori(listaOdgovora: List<Odgovor>?){
        GlobalScope.launch(Dispatchers.IO){
            withContext(Dispatchers.Main){
                var listaTacnoOdgovorenih: List<Pitanje> = mutableListOf()
                var listaNetacnoOdgovorenih: List<Pitanje> = mutableListOf()
                if(listaOdgovora != null)
                for(odgovor in listaOdgovora)
                    for(pitanje in listaPitanja){
                        if(odgovor.pitanjeId == pitanje.id && odgovor.odgovoreno == pitanje.tacan)
                            listaTacnoOdgovorenih += pitanje
                        if(odgovor.pitanjeId == pitanje.id && odgovor.odgovoreno != pitanje.tacan)
                            listaNetacnoOdgovorenih += pitanje
                    }
                listaTacnoOdgovorenih =  listaTacnoOdgovorenih.distinct()
                listaNetacnoOdgovorenih = listaNetacnoOdgovorenih.distinct()
                navPitanja = view!!.findViewById(R.id.navigacijaPitanja)
                var postoji = false
                val activity = activity as MainActivity
                val menu = activity.getBottomNavigation().menu
                if(kvizoviModel.isPredaniliUradjen(kvizoviModel.getOtvoreniKviz()!!))
                    postoji = true
                kvizoviModel.isPredan(kvizoviModel.getOtvoreniKviz()!!, onSuccess = ::predanKviz, onError = ::onError)
                if(takeKvizVM.getPredan()) postoji=true
                if(postoji){
                    menu.findItem(R.id.kvizovi).isVisible = true
                    menu.findItem(R.id.predmeti).isVisible = true
                    menu.findItem(R.id.predajKviz).isVisible = false
                    menu.findItem(R.id.zaustaviKviz).isVisible = false
                }else{
                    menu.findItem(R.id.kvizovi).isVisible = false
                    menu.findItem(R.id.predmeti).isVisible = false
                    menu.findItem(R.id.predajKviz).isVisible = true
                    menu.findItem(R.id.zaustaviKviz).isVisible = true
                }
                uncheck(menu)
                val menuPitanja = navPitanja.menu
                for(pitanje in listaPitanja) {
                    menuPitanja.add(R.id.grupa, brojacPitanja, brojacPitanja, (brojacPitanja+1).toString())
                    var postoji = false
                    for(odgovor in listaTacnoOdgovorenih)
                        if(pitanje.naziv == odgovor.naziv && pitanje.tekstPitanja == odgovor.tekstPitanja)
                            postoji = true
                    var postojiNetacan = false
                    for(odgovor in listaNetacnoOdgovorenih)
                        if(pitanje.naziv == odgovor.naziv && pitanje.tekstPitanja == odgovor.tekstPitanja)
                            postojiNetacan = true
                    if(postoji || listaTacnoOdgovorenih.contains(pitanje)){
                        val menuItem = navPitanja.menu.getItem(brojacPitanja)
                        val s = SpannableString(menuItem.title)
                        s.setSpan(ForegroundColorSpan(Color.parseColor("#3DDC84")), 0, s.length, 0)
                        menuItem.title = s
                    }else if(listaNetacnoOdgovorenih.contains(pitanje) || postojiNetacan){
                        val menuItem = navPitanja.menu.getItem(brojacPitanja)
                        val s = SpannableString(menuItem.title)
                        s.setSpan(ForegroundColorSpan(Color.parseColor("#DB4F3D")), 0, s.length, 0)
                        menuItem.title = s
                    }
                    brojacPitanja++
                }
                if(postoji){
                    navPitanja.menu.add(R.id.grupa, brojacPitanja, brojacPitanja, "Rezultat")
                    sharedViewModel.setPredan(2)
                }
                otvoriPitanje(0)
                navPitanja.setNavigationItemSelectedListener { menuItem ->
                    otvoriPitanje(menuItem.order)
                    trenutnoPitanje = menuItem.order
                    true
                }
            }
        }
    }

    fun predanKviz(predan: Boolean){
        GlobalScope.launch(Dispatchers.IO){
            withContext(Dispatchers.Main){
                if(predan){
                    val activity = activity as MainActivity
                    val menu = activity.getBottomNavigation().menu
                    menu.findItem(R.id.kvizovi).isVisible = true
                    menu.findItem(R.id.predmeti).isVisible = true
                    menu.findItem(R.id.predajKviz).isVisible = false
                    menu.findItem(R.id.zaustaviKviz).isVisible = false
                    navPitanja.menu.add(R.id.grupa, brojacPitanja, brojacPitanja, "Rezultat")
                    sharedViewModel.setPredan(2)
                }
            }
        }
    }

    private fun otvoriPitanje(brojPitanja: Int) {
        if(brojPitanja == brojacPitanja){
            val transaction = (activity as AppCompatActivity).supportFragmentManager.beginTransaction()
            val bundle = Bundle()
            val porukaFragment = FragmentPoruka.newInstance()
            bundle.putString("predajKviz", "predajKviz")
            porukaFragment.arguments = bundle
            transaction.replace(R.id.framePitanje, porukaFragment)
            transaction.addToBackStack("Predaj")
            transaction.commit()
        }else{
            val bundle = Bundle()
            bundle.putSerializable("kvizTaken", takeKvizVM.getZadnjiZapocetiKviz())
            val transaction = (activity as AppCompatActivity).supportFragmentManager.beginTransaction()
            val fragmentPitanje = FragmentPitanje(listaPitanja[brojPitanja])
            fragmentPitanje.arguments = bundle
            transaction.replace(R.id.framePitanje, fragmentPitanje)
            transaction.addToBackStack("Pitanje")
            transaction.commit()
        }
    }

    companion object {
        fun newInstance(): FragmentPokusaj = FragmentPokusaj(emptyList())
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sharedViewModel = ViewModelProviders.of(activity!!).get(SharedViewModel::class.java)
        sharedViewModel.getBrojOdgovora().observe(viewLifecycleOwner, Observer<Int?> { brojOdgovora ->
            if(brojOdgovora != 0){
                if(sharedViewModel.getBoja().value == "tacan"){
                    val menuItem = navPitanja.menu.getItem(trenutnoPitanje)
                    val s = SpannableString(menuItem.title)
                    s.setSpan(ForegroundColorSpan(Color.parseColor("#3DDC84")), 0, s.length, 0)
                    menuItem.title = s
                }else if(sharedViewModel.getBoja().value == "netacan"){
                    val menuItem = navPitanja.menu.getItem(trenutnoPitanje)
                    val s = SpannableString(menuItem.title)
                    s.setSpan(ForegroundColorSpan(Color.parseColor("#DB4F3D")), 0, s.length, 0)
                    menuItem.title = s
                }
            }
        })
        sharedViewModel.getPredan().observe(viewLifecycleOwner, Observer<Int?> { predan ->
            if(predan == 1) {
                navPitanja.menu.add(R.id.grupa, brojacPitanja, brojacPitanja, "Rezultat")
                sharedViewModel.setPredan(2)
            }
        })
    }

    fun uncheck(menu: Menu){
        menu.setGroupCheckable(0, true, false)
        for (i in 0 until menu.size()) {
            menu.getItem(i).isChecked = false
        }
        menu.setGroupCheckable(0, true, true)
    }

    fun onError(){
        val toast = Toast.makeText(context, "Error", Toast.LENGTH_SHORT)
        toast.show()
    }
}