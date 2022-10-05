package ba.etf.rma21.projekat

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ba.etf.rma21.projekat.data.AppDatabase
import ba.etf.rma21.projekat.data.repositories.AccountRepository
import ba.etf.rma21.projekat.data.repositories.DBRepository
import ba.etf.rma21.projekat.data.repositories.KvizRepository
import ba.etf.rma21.projekat.data.repositories.OdgovorRepository
import ba.etf.rma21.projekat.view.FragmentKvizovi
import ba.etf.rma21.projekat.view.FragmentPoruka
import ba.etf.rma21.projekat.view.FragmentPredmeti
import ba.etf.rma21.projekat.viewmodel.AccountViewModel
import ba.etf.rma21.projekat.viewmodel.KvizViewModel
import ba.etf.rma21.projekat.viewmodel.OdgovorViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity(){
    private lateinit var bottomNavigation: BottomNavigationView
    private var kvizVM = KvizViewModel()
    private var accountViewModel = AccountViewModel()
    private var odgovorViewModel = OdgovorViewModel()
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener{ item ->
            when(item.itemId){
                R.id.kvizovi -> {
                    val kvizoviFragment = FragmentKvizovi.newInstance()
                    openFragment(kvizoviFragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.predmeti -> {
                    val upisFragment = FragmentPredmeti.newInstance()
                    openFragment(upisFragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.predajKviz -> {
                    val transaction = supportFragmentManager.beginTransaction()
                    val bundle = Bundle()
                    val porukaFragment = FragmentPoruka.newInstance()
                    val otvoreniKviz = kvizVM.getOtvoreniKviz()
                      //  if(otvoreniKviz != null) kvizVM.predajKviz(otvoreniKviz)
                    if(otvoreniKviz != null) odgovorViewModel.predajOdgovore(otvoreniKviz.id, onSuccess = ::predajOdgovore, onError = ::onError)
                        bundle.putString("predajKviz", "predajKviz")
                        porukaFragment.arguments = bundle
                        transaction.replace(R.id.framePitanje, porukaFragment)
                        transaction.addToBackStack("Predaj")
                        transaction.commit()
                        bottomNavigation.menu.findItem(R.id.kvizovi).isVisible = true
                        bottomNavigation.menu.findItem(R.id.predmeti).isVisible = true
                        bottomNavigation.menu.findItem(R.id.predajKviz).isVisible = false
                        bottomNavigation.menu.findItem(R.id.zaustaviKviz).isVisible = false
                    return@OnNavigationItemSelectedListener true
                }
                R.id.zaustaviKviz -> {
                        val kvizoviFragment = FragmentKvizovi.newInstance()
                        openFragment(kvizoviFragment)
                        bottomNavigation.menu.findItem(R.id.kvizovi).isVisible = true
                        bottomNavigation.menu.findItem(R.id.predmeti).isVisible = true
                        bottomNavigation.menu.findItem(R.id.predajKviz).isVisible = false
                        bottomNavigation.menu.findItem(R.id.zaustaviKviz).isVisible = false
                    return@OnNavigationItemSelectedListener true
                }
            }
        false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val data: Uri? = intent?.data
        if(data!=null){
            var lista  = data.path?.split("/")
            for(x in lista!!)
                println(" ->" + x)
            val strExtra = intent.getStringExtra("payload")
            if(strExtra!=null)
                accountViewModel.postaviHash(strExtra, onSuccess = ::onSuccess, onError= ::onError)
            else
                accountViewModel.postaviHash(lista[lista.size-1], onSuccess = ::onSuccess, onError= ::onError)
        }else{
                accountViewModel.upisiDefaultniAccount(onSuccess = ::onSuccess, onError= ::onError)
        }
        DBRepository.setContext(this@MainActivity)
        KvizRepository.setContext(this@MainActivity)
        OdgovorRepository.setContext(this@MainActivity)
        AccountRepository.setContext(this@MainActivity)
     //   AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        bottomNavigation= findViewById(R.id.bottomNav)
        bottomNavigation.menu.findItem(R.id.kvizovi).isVisible = true
        bottomNavigation.menu.findItem(R.id.predmeti).isVisible = true
        bottomNavigation.menu.findItem(R.id.predajKviz).isVisible = false
        bottomNavigation.menu.findItem(R.id.zaustaviKviz).isVisible = false
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        bottomNavigation.selectedItemId= R.id.kvizovi
        val kvizoviFragment = FragmentKvizovi.newInstance()
        openFragment(kvizoviFragment)
    }

    fun predajOdgovore(string: Boolean){
        Log.d("lespaul", "predaj odgovore - " + string.toString())
    }

    private fun onSuccess(string: Boolean){

    }

    fun onError(){
    }

    private fun probni(bool: Boolean){

    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        if(fragment.id == R.id.kvizovi) transaction.addToBackStack("Kvizovi")
        else transaction.addToBackStack("Predmeti")
        transaction.commit()
    }

    override fun onBackPressed() {
        bottomNavigation.selectedItemId= R.id.kvizovi
        bottomNavigation.menu.findItem(R.id.kvizovi).isVisible = true
        bottomNavigation.menu.findItem(R.id.predmeti).isVisible = true
        bottomNavigation.menu.findItem(R.id.predajKviz).isVisible = false
        bottomNavigation.menu.findItem(R.id.zaustaviKviz).isVisible = false
        if(bottomNavigation.selectedItemId != R.id.kvizovi){
            val kvizoviFragment = FragmentKvizovi.newInstance()
            openFragment(kvizoviFragment)
        }
    }

    fun getBottomNavigation(): BottomNavigationView{
        return bottomNavigation
    }
}

