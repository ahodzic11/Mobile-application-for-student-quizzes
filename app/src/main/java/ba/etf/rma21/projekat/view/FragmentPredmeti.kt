package ba.etf.rma21.projekat.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ba.etf.rma21.projekat.R
import ba.etf.rma21.projekat.data.models.Grupa
import ba.etf.rma21.projekat.data.models.Predmet
import ba.etf.rma21.projekat.data.repositories.AccountRepository
import ba.etf.rma21.projekat.data.repositories.ProbniResponse
import ba.etf.rma21.projekat.viewmodel.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class FragmentPredmeti : Fragment() {
    private lateinit var spinnerGodina: Spinner
    private lateinit var spinnerPredmeta: Spinner
    private lateinit var spinnerGrupa: Spinner
    private lateinit var dodajPredmet: Button
    private var predmetModel = PredmetViewModel()
    private var grupaModel = GrupaViewModel()
    private var korisnikModel = KorisnikViewModel()
    private var predmetGrupaVM = PredmetIGrupaViewModel()
    private var neupisaniPredmeti: List<String> = emptyList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.upis_predmet_fragment, container, false)
        spinnerGodina = view.findViewById(R.id.odabirGodina)
        spinnerPredmeta = view.findViewById(R.id.odabirPredmet)
        spinnerGrupa = view.findViewById(R.id.odabirGrupa)
        dodajPredmet = view.findViewById(R.id.dodajPredmetDugme)

        val opcijeGodina = arrayOf("Odaberite godinu", "1", "2", "3", "4", "5")

        spinnerGodina.adapter = ArrayAdapter(spinnerGodina.context, android.R.layout.simple_list_item_1, opcijeGodina)
        spinnerGodina.setSelection(korisnikModel.getOdabranaGodinaStudija())
        spinnerGodina.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                dodajPredmet.isEnabled = false
                var izabranaGodina = 0
                if (position != 0) izabranaGodina = opcijeGodina[position].toInt()
                korisnikModel.setOdabranaGodinaStudija(izabranaGodina)
                predmetModel.getPredmeteKojiNisuUpisani(izabranaGodina, onSuccess = ::onSuccess, onError = ::onError)
            }
        }
        return view
    }

    fun popuniNeupisanePredmete(predmeti: List<String>){
        if(predmeti.isEmpty()){
            neupisaniPredmeti = emptyList()
        }
        var listaImenaPredmeta: List<String> = mutableListOf("Odaberite predmet")
        val opcijePredmeta = listaImenaPredmeta + predmeti
        val arrayAdapterPredmeta = ArrayAdapter(spinnerGodina.context, android.R.layout.simple_list_item_1, opcijePredmeta)
        spinnerPredmeta.adapter = arrayAdapterPredmeta
        spinnerPredmeta.setSelection(predmetModel.getOdabraniPredmet())
        spinnerPredmeta.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, positionPredmeta: Int, id: Long) {
            //  predmetModel.setOdabraniPredmet(positionPredmeta)
                dodajPredmet.isEnabled = false
                val izabraniPredmet = opcijePredmeta[positionPredmeta]
                grupaModel.getGroupsByPredmet(izabraniPredmet, onSuccess = ::onSuccess2, onError = ::onError)
            }
        }
    }

    fun onSuccess(predmeti: List<String>){
        GlobalScope.launch(Dispatchers.IO){
            withContext(Dispatchers.Main){
                neupisaniPredmeti = predmeti
                popuniNeupisanePredmete(neupisaniPredmeti)
            }
        }
    }

    fun onSuccess2(grupeNove: List<Grupa>){
        GlobalScope.launch(Dispatchers.IO){
            withContext(Dispatchers.Main){
                popuniGrupe(grupeNove)
            }
        }
    }

    fun popuniGrupe(grupe: List<Grupa>){
        var listaImenaGrupa: List<String> = mutableListOf()
        listaImenaGrupa += "Odaberite grupu"
        for (grupa in grupe)
            if(grupa.naziv != null)
                listaImenaGrupa += grupa.naziv
        val opcijeGrupa = listaImenaGrupa
        val arrayAdapterGrupa = ArrayAdapter(spinnerGodina.context, android.R.layout.simple_list_item_1, opcijeGrupa)
        spinnerGrupa.adapter = arrayAdapterGrupa
        spinnerGrupa.setSelection(grupaModel.getOdabranaGrupa())
        spinnerGrupa.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, positionGrupe: Int, id: Long) {
             //   grupaModel.setOdabranaGrupa(positionGrupe)
                dodajPredmet.isEnabled = false
                val izabranaGrupa = opcijeGrupa[positionGrupe]
                if (positionGrupe != 0) dodajPredmet.isEnabled = true
                dodajPredmet.setOnClickListener {
                    korisnikModel.setOdabranaGodinaStudija(0)
                    predmetModel.setOdabraniPredmet(0)
                    grupaModel.setOdabranaGrupa(0)
                    grupaModel.getIdZadnjeGrupe(izabranaGrupa, onSuccess = ::upisiKorisnikaUGrupu, onError = ::onError)
                }
            }
        }
    }

    fun upisiKorisnikaUGrupu(id: Int){
        GlobalScope.launch(Dispatchers.IO){
            withContext(Dispatchers.Main){
                grupaModel.setIdZadnjeGrupe(id)
                predmetGrupaVM.upisiUGrupu(id, onSuccess = ::onSuccess3, onError = ::onError)
            }
        }
    }

    fun onSuccess3(poruka: Boolean?){
        val transaction = (activity as AppCompatActivity).supportFragmentManager.beginTransaction()
        val bundle = Bundle()
        val porukaFragment = FragmentPoruka.newInstance()
        porukaFragment.arguments = bundle
        transaction.replace(R.id.container, porukaFragment)
        transaction.addToBackStack("Poruka")
        transaction.commit()
    }

    fun onError(){
        val toast = Toast.makeText(context, "Error", Toast.LENGTH_SHORT)
        toast.show()
    }

    companion object {
        fun newInstance(): FragmentPredmeti = FragmentPredmeti()
    }
}