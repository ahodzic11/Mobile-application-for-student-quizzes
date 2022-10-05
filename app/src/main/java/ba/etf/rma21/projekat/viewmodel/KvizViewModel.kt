package ba.etf.rma21.projekat.viewmodel

import android.content.Context
import ba.etf.rma21.projekat.data.models.Grupa
import ba.etf.rma21.projekat.data.models.Kviz
import ba.etf.rma21.projekat.data.repositories.AccountRepository
import ba.etf.rma21.projekat.data.repositories.ApiAdapter
import ba.etf.rma21.projekat.data.repositories.KvizRepository
import ba.etf.rma21.projekat.data.repositories.PredmetIGrupaRepository
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class KvizViewModel {
    val scope = CoroutineScope(Job() + Dispatchers.Main)

    fun getAll(onSuccess: (kvizovi: List<Kviz>?) -> Unit,
               onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = KvizRepository.getAll()
            when (result) {
                is List<Kviz>? -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

    fun getById(id: Int, onSuccess: (kvizovi: Kviz?) -> Unit,
                    onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = KvizRepository.getById(id)
            when (result) {
                is Kviz? -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

    fun getMyKvizes(onSuccess: (kvizovi: List<Kviz>) -> Unit,
                    onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = KvizRepository.getMyKvizesDB()
            when (result) {
                is List<Kviz> -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

    fun getFuture(onSuccess: (kvizovi: List<Kviz>) -> Unit,
                  onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = KvizRepository.getFuture()
            when (result) {
                is List<Kviz> -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

    fun getUpisani(onSuccess: (kvizovi: List<Kviz>?) -> Unit,
                onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = KvizRepository.getUpisani()
            when (result) {
                is List<Kviz>? -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

    fun getDone(onSuccess: (kvizovi: List<Kviz>) -> Unit,
                onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = KvizRepository.getDone()
            when (result) {
                is List<Kviz> -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

    fun getNotTaken(onSuccess: (kvizovi: List<Kviz>) -> Unit,
                    onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = KvizRepository.getNotTaken()
            when (result) {
                is List<Kviz> -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }



    fun predajKviz(kviz: Kviz) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = KvizRepository.predajKviz(kviz)
            return@launch result
        }
    }

    fun getPredaniKvizovi(): List<Kviz>{
        return KvizRepository.getPredaniKvizovi()
    }

    fun otvoriKviz(kviz: Kviz){
        return KvizRepository.otvoriKviz(kviz)
    }

    fun getOtvoreniKviz(): Kviz?{
        return KvizRepository.getOtvoreniKviz()
    }

    fun sortiraj(listaKvizova: List<Kviz>): List<Kviz>{
        return listaKvizova.sortedBy { it.datumPocetka }
    }

    fun isMoguceUraditi(kviz: Kviz): Boolean{
        val formater = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        if(kviz.datumKraj!=null && kviz.datumPocetka!=null)
        if(formater.parse(kviz.datumKraj).before(Calendar.getInstance().time) || kviz.datumRada != null || formater.parse(kviz.datumPocetka).after(
                Calendar.getInstance().time)) return false
        return true
    }

    fun isPredaniliUradjen(kviz: Kviz): Boolean{
        val formater = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        if(kviz.datumKraj!=null)
        if(kviz.datumRada != null || formater.parse(kviz.datumKraj).before(Calendar.getInstance().time))
            return true
        return false
    }

    fun setOdabirPregledaKviza(pozicija: Int){
        return KvizRepository.setOdabirPregledaKviza(pozicija)
    }

    fun getOdabirPregledaKViza(): Int{
        return KvizRepository.getOdabirPregledaKviza()
    }

    fun isBuduci(kviz: Kviz): Boolean{
        val formater = SimpleDateFormat("yyyy-MM-dd")
        if(formater.parse(kviz.datumPocetka).after(Calendar.getInstance().time)) return true
        return false
    }

    fun isMojKviz(kviz: Kviz, onSuccess: (moj: Boolean) -> Unit,
                    onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = KvizRepository.isMojKviz(kviz)
            when (result) {
                is Boolean -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

    fun getAllDB(context: Context, onSuccess: (kvizovi: List<Kviz>) -> Unit,
                     onError: () -> Unit){
        scope.launch{
            val result = KvizRepository.getAllDB(context)
            when (result) {
                is List<Kviz> -> onSuccess?.invoke(result)
                else-> onError?.invoke()
            }
        }
    }

    fun updateKvizPokusaj(idKviza: Int, idKvizTaken: Int, onSuccess: (account: String?) -> Unit,
                      onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = KvizRepository.updateKvizPokusaj(idKviza, idKvizTaken)
            when (result) {
                is String? -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

    fun isPredan(kviz: Kviz, onSuccess: (account: Boolean) -> Unit,
                          onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = KvizRepository.isPredan(kviz)
            when (result) {
                is Boolean -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

    fun updateAccount(idKviza: Int, predan: Boolean, context: Context, onSuccess: (account: String?) -> Unit,
                      onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = KvizRepository.updateKviz(idKviza, predan, context)
            when (result) {
                is String? -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

    fun writeKvizDB(context: Context, kviz: Kviz, onSuccess: (kvizovi: String) -> Unit,
                onError: () -> Unit){
        scope.launch{
            val result = KvizRepository.writeKvizDB(context, kviz)
            when (result) {
                is String -> onSuccess?.invoke(result)
                else-> onError?.invoke()
            }
        }
    }
}

