package ba.etf.rma21.projekat.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ba.etf.rma21.projekat.data.models.Kviz

class SharedViewModel : ViewModel(){
    private var predan = MutableLiveData<Int>()
    private var boja = MutableLiveData<CharSequence>()
    private var pozicija = MutableLiveData<Int>()
    private var brojOdgovora = MutableLiveData<Int>()
    private var postojiRezultat = MutableLiveData<Boolean>()
    private lateinit var otvoreniKviz: Kviz

    fun setPostojiRezultat(postoji: Boolean){
        postojiRezultat.value = postoji
    }

    fun getPostojiRezultat(): LiveData<Boolean>{
        return postojiRezultat
    }

    fun setOtvoreniKviz(kviz: Kviz){
        otvoreniKviz = kviz
    }

    fun setBrojOdgovora(broj: Int){
        brojOdgovora.value = broj
    }

    fun getBrojOdgovora(): LiveData<Int>{
        return brojOdgovora
    }

    fun getKviz(): Kviz {
        return otvoreniKviz
    }

    fun setBoja(input: CharSequence){
        boja.value = input
    }

    fun getPredanost(): Int?{
        return predan.value
    }

    fun setPredan(input: Int){
        predan.value = input
    }

    fun getPredan(): LiveData<Int>{
        return predan
    }

    fun getBoja(): LiveData<CharSequence>{
        return boja
    }

    fun setPozicija(input: Int){
        pozicija.value = input
    }

    fun getPozicija(): LiveData<Int>{
        return pozicija
    }
}