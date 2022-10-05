package ba.etf.rma21.projekat.viewmodel

import ba.etf.rma21.projekat.data.models.Grupa
import ba.etf.rma21.projekat.data.models.Kviz
import ba.etf.rma21.projekat.data.models.Pitanje
import ba.etf.rma21.projekat.data.repositories.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class KorisnikViewModel {

    fun setOdabranaGodinaStudija(godina: Int){
        return KorisnikRepository.setOdabranaGodinaStudija(godina)
    }

    fun getOdabranaGodinaStudija(): Int{
        return KorisnikRepository.getOdabranaGodinaStudija()
    }

    fun getListaOdgovorenihPitanja(idKviza: Int, onSuccess: (pitanja: List<Pitanje>) -> Unit,
                 onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = KorisnikRepository.getListaOdgovorenihPitanja(idKviza)
            when (result) {
                is List<Pitanje> -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

    fun getTacnoOdgovorenaPitanjaZaKviz(kviz: Kviz, onSuccess: (pitanja: List<Pitanje>) -> Unit,
                                   onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = KorisnikRepository.getTacnoOdgovorenaPitanjaZaKviz(kviz)
            when (result) {
                is List<Pitanje> -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

    fun getNetacnoOdgovorenaPitanjaZaKviz(kviz: Kviz, onSuccess: (pitanja: List<Pitanje>) -> Unit,
                                        onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = KorisnikRepository.getNetacnoOdgovorenaPitanjaZaKviz(kviz)
            when (result) {
                is List<Pitanje> -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

    fun getOdgovorNaPitanje(pitanje: Pitanje, kviz: Kviz, onSuccess: (odg: Int) -> Unit,
                                        onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = KorisnikRepository.getOdgovorNaPitanje(pitanje, kviz)
            when (result) {
                is Int -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

    fun setTacne(lista: List<Pitanje>){
        return KorisnikRepository.setTacne(lista)
    }

    fun getTacne(): List<Pitanje>{
        return KorisnikRepository.getTacne()
    }

    fun getTacnostOdgovora(pitanje: Pitanje, kviz: Kviz): String{
  //      return KorisnikRepository.getTacnostOdgovora(pitanje, kviz)
        return ""
    }
}