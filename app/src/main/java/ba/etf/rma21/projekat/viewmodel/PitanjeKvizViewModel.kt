package ba.etf.rma21.projekat.viewmodel

import ba.etf.rma21.projekat.data.models.Kviz
import ba.etf.rma21.projekat.data.models.Pitanje
import ba.etf.rma21.projekat.data.repositories.ApiAdapter
import ba.etf.rma21.projekat.data.repositories.KvizRepository
import ba.etf.rma21.projekat.data.repositories.PitanjeKvizRepository
import kotlinx.coroutines.*

class PitanjeKvizViewModel {
    val scope = CoroutineScope(Job() + Dispatchers.Main)

    fun getPitanja(idKviza: Int, onSuccess: (kvizovi: List<Pitanje>?) -> Unit,
                  onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = PitanjeKvizRepository.getPitanja(idKviza)
            when (result) {
                is List<Pitanje>? -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

    fun getSvaPitanja(onSuccess: (pitanja: List<Pitanje>) -> Unit,
                            onError: () -> Unit) {
        scope.launch {
            val result = PitanjeKvizRepository.getSvaPitanja()
            when (result) {
                is List<Pitanje> -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

}