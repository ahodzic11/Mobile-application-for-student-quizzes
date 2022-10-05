package ba.etf.rma21.projekat.viewmodel

import ba.etf.rma21.projekat.data.models.Kviz
import ba.etf.rma21.projekat.data.models.KvizTaken
import ba.etf.rma21.projekat.data.models.Odgovor
import ba.etf.rma21.projekat.data.repositories.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class KvizTakenViewModel {

    fun getPokusaje(idStudenta: String, onSuccess: (kvizovi: List<KvizTaken>) -> Unit,
               onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = KvizTakenRepository.getPokusaje(idStudenta)
            when (result) {
                is List<KvizTaken> -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

    fun getKvizTakenSaIDemPokusaja(idPokusajaKviza: Int, onSuccess: (kvizovi: KvizTaken?) -> Unit,
                     onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = KvizTakenRepository.getKvizTakenSaIDemPokusaja(idPokusajaKviza)
            when (result) {
                is KvizTaken? -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

    fun dajBodoveZaKviz(idKviza: Int, onSuccess: (bodovi: Int) -> Unit,
                     onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = KvizTakenRepository.dajBodoveZaKviz(idKviza)
            when (result) {
                is Int -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

    fun daLiJePredan(idPokusajaKviza: Int, onSuccess: (kvizovi: Boolean) -> Unit,
                    onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = KvizTakenRepository.daLiJePredan(idPokusajaKviza)
            when (result) {
                is Boolean -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }
}
