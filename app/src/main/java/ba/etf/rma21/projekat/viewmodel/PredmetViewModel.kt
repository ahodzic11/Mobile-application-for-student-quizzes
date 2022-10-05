package ba.etf.rma21.projekat.viewmodel

import ba.etf.rma21.projekat.data.models.Account
import ba.etf.rma21.projekat.data.models.Grupa
import ba.etf.rma21.projekat.data.models.Predmet
import ba.etf.rma21.projekat.data.repositories.AccountRepository
import ba.etf.rma21.projekat.data.repositories.PredmetIGrupaRepository
import ba.etf.rma21.projekat.data.repositories.PredmetRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PredmetViewModel {


    fun getAll(onSuccess: (predmeti: List<Predmet>) -> Unit,
                    onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = PredmetIGrupaRepository.getPredmeti()
            when (result) {
                is List<Predmet> -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

    fun getPredmetsByGodina(godina:Int, onSuccess: (predmeti: List<Predmet>) -> Unit,
               onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = PredmetRepository.getPredmetsByGodina(godina)
            when (result) {
                is List<Predmet> -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

    fun getPredmeteKojiNisuUpisani(godina: Int, onSuccess: (predmeti: List<String>) -> Unit,
               onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = PredmetRepository.getPredmeteKojiNisuUpisaniSaGodine(godina)
            when (result) {
                is List<String> -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

    fun getUpisani(idStudenta: String, onSuccess: (predmeti: List<Predmet>) -> Unit,
                   onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = PredmetRepository.getUpisani()
            when (result) {
                is List<Predmet> -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

    fun getPredmeteSaUpisaneGodine(godina: Int, onSuccess: (predmeti: List<Predmet>) -> Unit,
                                   onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = PredmetRepository.getPredmeteSaUpisaneGodine(godina)
            when (result) {
                is List<Predmet> -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

    fun setOdabraniPredmet(pozicija: Int){
        return PredmetRepository.setOdabraniPredmet(pozicija)
    }

    fun getOdabraniPredmet(): Int{
        return PredmetRepository.getOdabraniPredmet()
    }

    fun dajIdPredmetaZaImePredmeta(nazivPredmeta: String, onSuccess: (id: Int) -> Unit,
                                   onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = PredmetRepository.dajIdPredmetaZaImePredmeta(nazivPredmeta)
            when (result) {
                is Int -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

    fun dajPredmetZaGrupu(grupa: Grupa, onSuccess: (predmeti: Predmet) -> Unit,
                                   onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = PredmetRepository.dajPredmetZaGrupu(grupa)
            when (result) {
                is Predmet -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

}