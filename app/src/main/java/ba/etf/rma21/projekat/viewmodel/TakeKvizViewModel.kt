package ba.etf.rma21.projekat.viewmodel

import ba.etf.rma21.projekat.data.models.Grupa
import ba.etf.rma21.projekat.data.models.KvizTaken
import ba.etf.rma21.projekat.data.repositories.AccountRepository
import ba.etf.rma21.projekat.data.repositories.ApiAdapter
import ba.etf.rma21.projekat.data.repositories.GrupaRepository
import ba.etf.rma21.projekat.data.repositories.TakeKvizRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TakeKvizViewModel {
    fun zapocniKviz(idKviza:Int, onSuccess: (kviz: KvizTaken?) -> Unit,
                 onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = TakeKvizRepository.zapocniKviz(idKviza)
            when (result) {
                is KvizTaken? -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

    fun otvoriIliKreirajPokusajKviza(idKviza:Int, onSuccess: (kviz: KvizTaken) -> Unit,
                    onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = TakeKvizRepository.otvoriIliKreirajPokusajKviza(idKviza)
            when (result) {
                is KvizTaken -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

    fun getPocetiKvizovi(onSuccess: (kviz: List<KvizTaken>?) -> Unit,
                    onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = TakeKvizRepository.getPocetiKvizovi()
            when (result) {
                is List<KvizTaken>? -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

    fun setZadnjiZapocetiKviz(zadnjiZapoceti: KvizTaken){
        return TakeKvizRepository.setZadnjiZapocetiKviz(zadnjiZapoceti)
    }

    fun setPredan(predan: Boolean){
        return TakeKvizRepository.setPredan(predan)
    }

    fun getPredan(): Boolean{
        return TakeKvizRepository.getPredan()
    }

    fun getZadnjiZapocetiKviz(): KvizTaken{
        return TakeKvizRepository.getZadnjiZapocetiKviz()
    }
}