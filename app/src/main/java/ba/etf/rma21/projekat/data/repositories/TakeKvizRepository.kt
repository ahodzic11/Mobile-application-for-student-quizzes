package ba.etf.rma21.projekat.data.repositories

import ba.etf.rma21.projekat.data.models.KvizTaken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class TakeKvizRepository {
    companion object{

        private var zadnjiZapocetiKviz: KvizTaken = KvizTaken(-1, "N/A", -1, Calendar.getInstance().time, -1)
        private var predanostZadnjegZapocetog = false

        suspend fun zapocniKviz(idKviza: Int): KvizTaken?{
            return withContext(Dispatchers.IO){
                val response = ApiAdapter.retrofit.odgovoriNaKviz(AccountRepository.acHash, idKviza)
                val responseBody = response.body()
                if(responseBody.toString().contains("ne postoji") || responseBody.toString().contains("nije upisan")) return@withContext null
                return@withContext responseBody
            }
        }

        fun setPredan(predan: Boolean){
            predanostZadnjegZapocetog = predan
        }

        fun getPredan(): Boolean{
            return predanostZadnjegZapocetog
        }

        suspend fun otvoriIliKreirajPokusajKviza(idKviza: Int): KvizTaken? {
            val sviPokusaji = getPocetiKvizovi()
            if(sviPokusaji != null)
            for(pokusaj in sviPokusaji){
                if(pokusaj.KvizId == idKviza)
                    return pokusaj
            }
            return zapocniKviz(idKviza)
        }

        suspend fun dajIdPokusajaKvizaZaKvizId(kvizId: Int): Int{
            val pokusaji = getPocetiKvizovi()
            if(pokusaji!=null)
            for(pokusaj in pokusaji)
                if(pokusaj.KvizId == kvizId)
                    return pokusaj.id
            return -1
        }

        suspend fun getPocetiKvizovi(): List<KvizTaken>?{
            val pokusajiKvizova: List<KvizTaken>? = withContext(Dispatchers.IO){
                return@withContext ApiAdapter.retrofit.getPokusajeStudenta(AccountRepository.acHash).body()
            }
            if(pokusajiKvizova!!.size==0) return null
            return pokusajiKvizova
        }

        fun setZadnjiZapocetiKviz(zadnjiKviz: KvizTaken){
            zadnjiZapocetiKviz=zadnjiKviz
        }

        fun getZadnjiZapocetiKviz(): KvizTaken{
            return zadnjiZapocetiKviz!!
        }
    }
}