package ba.etf.rma21.projekat.viewmodel

import ba.etf.rma21.projekat.data.models.Account
import ba.etf.rma21.projekat.data.models.Grupa
import ba.etf.rma21.projekat.data.repositories.AccountRepository
import ba.etf.rma21.projekat.data.repositories.GrupaRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GrupaViewModel {

    fun getGroupsByPredmet(nazivPredmeta: String, onSuccess: (grupe: List<Grupa>) -> Unit,
                   onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = GrupaRepository.getGroupsByPredmet(nazivPredmeta)
            when (result) {
                is List<Grupa> -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

    fun getIdZadnjeGrupe(nazivGrupe: String, onSuccess: (id: Int) -> Unit,
               onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = GrupaRepository.getIdZadnjeGrupe(nazivGrupe)
            when (result) {
                is Int -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

    fun getAll(onSuccess: (grupe: List<Grupa>) -> Unit,
                           onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = GrupaRepository.getAll()
            when (result) {
                is List<Grupa> -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

    fun getGrupu(idGrupe: Int, onSuccess: (grupe: Grupa) -> Unit,
               onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = GrupaRepository.getGrupu(idGrupe)
            when (result) {
                is Grupa -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

    fun setOdabranaGrupa(pozicija: Int){
        return GrupaRepository.setOdabranaGrupa(pozicija)
    }

    fun getOdabranaGrupa(): Int{
        return GrupaRepository.getOdabranaGrupa()
    }

/*    fun setZadnjaUpisanaGrupa(nazivGrupe: String, idPredmeta: Int, onSuccess: () -> Unit,
               onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = GrupaRepository.setZadnjaUpisanaGrupa(nazivGrupe, idPredmeta)
            when (result) {
                is List<Grupa> -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

 */

    fun setNazivZadnjeUpisaneGrupe(naziv: String){
        return GrupaRepository.setNazivZadnjeUpisaneGrupe(naziv)
    }

    fun getNazivZadnjeUpisaneGrupe(): String{
        return GrupaRepository.getNazivZadnjeUpisaneGrupe()
    }

    fun setIdZadnjeGrupe(id: Int){
        return GrupaRepository.setIdZadnjeGrupe(id)
    }

    fun getIdZadnjeGrupe(): Int{
        return GrupaRepository.getIdZadnjeGrupe()
    }

    fun getZadnjaUpisanaGrupa(): Grupa?{
        return GrupaRepository.getZadnjaUpisanaGrupa()
    }
}