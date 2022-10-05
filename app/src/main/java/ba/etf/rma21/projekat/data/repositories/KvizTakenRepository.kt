package ba.etf.rma21.projekat.data.repositories

import android.util.Log
import ba.etf.rma21.projekat.data.models.Kviz
import ba.etf.rma21.projekat.data.models.KvizTaken
import ba.etf.rma21.projekat.data.models.Odgovor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class KvizTakenRepository {
    companion object{
        suspend fun getPokusaje(idStudenta: String): List<KvizTaken>{
            return withContext(Dispatchers.IO){
                val response = ApiAdapter.retrofit.getPokusajeStudenta(idStudenta)
                val responseBody = response.body()
                return@withContext responseBody!!
            }
        }

        suspend fun getKvizTakenSaIDemPokusaja(idPokusajaKviza: Int): KvizTaken?{
            val sviPokusaji = getPokusaje(AccountRepository.acHash)
            for(pokusaj in sviPokusaji)
                if(pokusaj.id == idPokusajaKviza)
                    return pokusaj
            return null
        }

        suspend fun dajBodoveZaKviz(idKviza: Int): Int{
            val sviPokusaji = getPokusaje(AccountRepository.acHash)
            for(pokusaj in sviPokusaji)
                if(pokusaj.KvizId == idKviza)
                    return pokusaj.osvojeniBodovi
            return 0
        }

        suspend fun daLiJePredan(idPokusajaKviza: Int): Boolean{
            val pokusajKviza = getKvizTakenSaIDemPokusaja(idPokusajaKviza)
            if(pokusajKviza == null) return false
            val odgovoriZaPokusaj = OdgovorRepository.getOdgovoriKviz(pokusajKviza.KvizId)
            val brojPitanjaNaKvizu = PitanjeKvizRepository.getPitanja(pokusajKviza!!.KvizId)
            if(odgovoriZaPokusaj!!.size == brojPitanjaNaKvizu!!.size) return true
            return false
        }
    }
}