package ba.etf.rma21.projekat.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ba.etf.rma21.projekat.R
import ba.etf.rma21.projekat.data.models.Account
import ba.etf.rma21.projekat.data.models.Kviz
import ba.etf.rma21.projekat.data.models.Pitanje
import ba.etf.rma21.projekat.data.repositories.AccountRepository
import ba.etf.rma21.projekat.data.repositories.DBRepository
import ba.etf.rma21.projekat.data.repositories.KvizRepository
import ba.etf.rma21.projekat.viewmodel.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class FragmentKvizovi : Fragment() {
    private lateinit var listaKvizova: RecyclerView
    private lateinit var listaKvizovaAdapter: KvizAdapter
    private lateinit var spinner: Spinner
    private var kvizoviModel = KvizViewModel()
    private var pitanjaKvizoviVM = PitanjeKvizViewModel()
    private var accountViewModel = AccountViewModel()
    private var DBVM = DBViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.main_fragment, container, false)
        listaKvizova = view.findViewById(R.id.listaKvizova)
        spinner = view.findViewById(R.id.filterKvizova)
        listaKvizova.layoutManager = GridLayoutManager(activity, 2)
        listaKvizovaAdapter = KvizAdapter(arrayListOf()) {kviz -> otvoriPokusaj(kviz)}
        listaKvizova.adapter = listaKvizovaAdapter
     //   kvizoviModel.getMyKvizes(onSuccess = ::onSuccess, onError = ::onError)
        val opcije = arrayOf("Svi moji kvizovi", "Svi kvizovi", "Urađeni kvizovi", "Budući kvizovi", "Prošli kvizovi")
        spinner.adapter = ArrayAdapter(spinner.context, android.R.layout.simple_list_item_1, opcije)
        spinner.setSelection(kvizoviModel.getOdabirPregledaKViza())
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                kvizoviModel.setOdabirPregledaKviza(position)
                if(opcije[position] == opcije[0]) kvizoviModel.getMyKvizes(onSuccess = ::onSuccess, onError = ::onError)
                else if(opcije[position] == opcije[1]) kvizoviModel.getAll(onSuccess = ::onSuccess, onError = ::onError)
                else if(opcije[position] == opcije[2]) kvizoviModel.getDone(onSuccess = ::onSuccess, onError = ::onError)
                else if(opcije[position] == opcije[3]) kvizoviModel.getFuture(onSuccess = ::onSuccess, onError = ::onError)
                else if(opcije[position] == opcije[4]) kvizoviModel.getNotTaken(onSuccess = ::onSuccess, onError = ::onError)


             /*
             else if(opcije[position] == opcije[3]) context?.let {
                    accountViewModel.postaviHash(it, "3ff231fb-3ea5-499a-b9dc-96960e3762c6", onSuccess = ::postavljanje, onError = ::onError)
//                    val acc = Account(0, "ahodzic11@etf.unsa.ba", "noviHash", formater.format(Date(0, 0, 0)))
//                    accountViewModel.writeAccDB(it, acc, onSuccess = ::probniAcc, onError = ::onError)
//                    kvizoviModel.writeKvizDB(it, Kviz(15, "3", "-", "0", "0", "0", 1, "-"
//                        , 1f), onSuccess = ::probni, onError = ::onError)
                }
             val formater = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
             else if(opcije[position] == opcije[2]) context?.let{
                    DBVM.updateNow(onSuccess = ::updateovana, onError = ::onError)
                }
                else if(opcije[position] == opcije[3]) context?.let {
                    accountViewModel.postaviHash(it, "3ff231fb-3ea5-499a-b9dc-96960e3762c6", onSuccess = ::postavljanje, onError = ::onError)
//                    val acc = Account(0, "ahodzic11@etf.unsa.ba", "noviHash", formater.format(Date(0, 0, 0)))
//                    accountViewModel.writeAccDB(it, acc, onSuccess = ::probniAcc, onError = ::onError)
//                    kvizoviModel.writeKvizDB(it, Kviz(15, "3", "-", "0", "0", "0", 1, "-"
//                        , 1f), onSuccess = ::probni, onError = ::onError)
                }
                else if(opcije[position] == opcije[4]) context?.let{
                    context?.let {
                        //kvizoviModel.getAllDB(it, onSuccess = ::probni2, onError = ::onError)
                        accountViewModel.getAllDB(it, onSuccess = ::probni2, onError = ::onError)
                    }
                }

              */
            }
        }
         return view
        }

    fun update(string: String?){
        Log.d("lespaul", "update accounta - " + string.toString())
    }

    fun updateovana(bool: Boolean){
        GlobalScope.launch(Dispatchers.IO){
            withContext(Dispatchers.Main){
                Log.d("lespaul", "updateovana - " + bool.toString())
            }
        }

    }

    fun postavljanje(post: Boolean){
        Log.d("lespaul", post.toString())
    }

    fun probniAcc(uspjesno: String?){

    }

    fun probni(listaKvizova: String?){
        Log.d("lespaul", "kviz dodan")
        context?.let {
         //   kvizoviModel.getAllDB(it, onSuccess = ::probni2, onError = ::onError)
            //accountViewModel.getAllDB(it, onSuccess = ::probni2, onError = ::onError)
        }
    }

    fun probni2(listaKvizova: List<Account>){
        Log.d("lespaul", "u bazi ima = " + listaKvizova.size.toString())
    }

    private fun otvoriPokusaj(kviz: Kviz){
        kvizoviModel.otvoriKviz(kviz)
        kvizoviModel.isMojKviz(kvizoviModel.getOtvoreniKviz()!!, onSuccess = ::kvizMoguceOtvoriti, onError = ::onError)
    }

    fun kvizMoguceOtvoriti(mogucnostOtvaranja: Boolean){
        if(mogucnostOtvaranja) pitanjaKvizoviVM.getPitanja(kvizoviModel.getOtvoreniKviz()!!.id, onSuccess = ::otvoriPokusajSaPitanjima, onError = ::onError)
    }

    fun otvoriPokusajSaPitanjima(pitanja: List<Pitanje>?){
        val otvoreniKviz = kvizoviModel.getOtvoreniKviz()
        if(pitanja != null)
        if(pitanja.isNotEmpty() && !kvizoviModel.isBuduci(otvoreniKviz!!)) {
            val transaction = (activity as AppCompatActivity).supportFragmentManager.beginTransaction()
            val pokusajFragment = FragmentPokusaj(pitanja)
            transaction.replace(R.id.container, pokusajFragment)
            transaction.addToBackStack("Pokusaj")
            transaction.commit()
        }
    }

    fun onSuccess(kvizovi :List<Kviz>?){
        GlobalScope.launch(Dispatchers.IO){
           withContext(Dispatchers.Main){
               if(kvizovi != null)
               listaKvizovaAdapter.updateKvizove(kvizovi)
           }
        }
    }

    fun onError(){
        val toast = Toast.makeText(context, "Error", Toast.LENGTH_SHORT)
        toast.show()
    }

    companion object {
        fun newInstance(): FragmentKvizovi = FragmentKvizovi()
    }
}