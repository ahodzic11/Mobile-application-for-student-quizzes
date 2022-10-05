package ba.etf.rma21.projekat.viewmodel

import android.content.Context
import ba.etf.rma21.projekat.data.models.Account
import ba.etf.rma21.projekat.data.models.Grupa
import ba.etf.rma21.projekat.data.models.Predmet
import ba.etf.rma21.projekat.data.repositories.*
import kotlinx.coroutines.*

class PredmetIGrupaViewModel {
    val scope = CoroutineScope(Job() + Dispatchers.Main)
    fun getPredmeti(onSuccess: (predmeti: List<Predmet>?) -> Unit,
                   onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = PredmetIGrupaRepository.getPredmeti()
            when (result) {
                is List<Predmet>? -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

    fun getGrupe(onSuccess: (predmeti: List<Grupa>?) -> Unit,
                    onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = PredmetIGrupaRepository.getGrupe()
            when (result) {
                is List<Grupa>? -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

    fun upisiUGrupu(idGrupa: Int, onSuccess: (bool: Boolean?) -> Unit,
                    onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = PredmetIGrupaRepository.upisiUGrupu(idGrupa)
            when (result) {
                is Boolean? -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

    fun getGrupeZaPredmet(idPredmeta: Int, onSuccess: (grupe: List<Grupa>?) -> Unit,
                    onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = PredmetIGrupaRepository.getGrupeZaPredmet(idPredmeta)
            when (result) {
                is List<Grupa>? -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

    fun getUpisaneGrupe(onSuccess: (grupe: List<Grupa>?) -> Unit,
                    onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = PredmetIGrupaRepository.getUpisaneGrupe()
            when (result) {
                is List<Grupa>? -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

    fun writeGrupaDB(context: Context, kviz: Grupa, onSuccess: (grupa: String) -> Unit,
                   onError: () -> Unit){
        scope.launch{
            val result = PredmetIGrupaRepository.writeGrupaDB(context, kviz)
            when (result) {
                is String -> onSuccess?.invoke(result)
                else-> onError?.invoke()
            }
        }
    }

    fun writePredmetDB(context: Context, predmet: Predmet, onSuccess: (grupa: String) -> Unit,
                     onError: () -> Unit){
        scope.launch{
            val result = PredmetIGrupaRepository.writePredmetDB(context, predmet)
            when (result) {
                is String -> onSuccess?.invoke(result)
                else-> onError?.invoke()
            }
        }
    }
}