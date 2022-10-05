package ba.etf.rma21.projekat.viewmodel

import ba.etf.rma21.projekat.data.models.KvizTaken
import ba.etf.rma21.projekat.data.models.Odgovor
import ba.etf.rma21.projekat.data.models.OdgovorenoPitanje
import ba.etf.rma21.projekat.data.models.Pitanje
import ba.etf.rma21.projekat.data.repositories.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OdgovorViewModel {

    fun getOdgovoriKviz(idKviza:Int, onSuccess: (odgovori: List<Odgovor>?) -> Unit,
                    onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = OdgovorRepository.getOdgovoriKviz(idKviza)
            when (result) {
                is List<Odgovor>? -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

    fun getOdgovoriKvizAPI(idKviza:Int, onSuccess: (odgovori: List<Odgovor>?) -> Unit,
                        onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = OdgovorRepository.getOdgovoriKvizAPI(idKviza)
            when (result) {
                is List<Odgovor>? -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

    fun postaviOdgovorKviz(idKvizTaken: Int, idPitanje: Int, odgovor: Int, onSuccess: (odgovori: Int?) -> Unit,
                        onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = OdgovorRepository.postaviOdgovorKviz(idKvizTaken, idPitanje, odgovor)
            when (result) {
                is Int? -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

    fun predajOdgovore(idKviza: Int, onSuccess: (odgovori: Boolean) -> Unit,
                           onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = OdgovorRepository.predajOdgovore(idKviza)
            when (result) {
                is Boolean -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

    fun dajTacneOdgovore(idPokusajaKviza: Int, onSuccess: (odgovori: List<Odgovor>) -> Unit,
                           onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = OdgovorRepository.dajTacneOdgovore(idPokusajaKviza)
            when (result) {
                is List<Odgovor> -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

    fun dajTacnostOdgovora(idPokusajaKviza: Int, pitanje: Pitanje, onSuccess: (odgovori: Boolean) -> Unit,
                           onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = OdgovorRepository.dajTacnostOdgovora(idPokusajaKviza, pitanje)
            when (result) {
                is Boolean -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

    fun dajNetacneOdgovore(idPokusajaKviza: Int, onSuccess: (odgovori: List<Odgovor>) -> Unit,
                         onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = OdgovorRepository.dajNetacneOdgovore(idPokusajaKviza)
            when (result) {
                is List<Odgovor> -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }
}