package ba.etf.rma21.projekat.data.repositories

import android.content.Context
import android.util.Log
import ba.etf.rma21.projekat.data.AppDatabase
import ba.etf.rma21.projekat.data.models.Kviz
import ba.etf.rma21.projekat.data.models.Odgovor
import ba.etf.rma21.projekat.data.models.OdgovorenoPitanje
import ba.etf.rma21.projekat.data.models.Pitanje
import com.google.gson.annotations.Until
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.internal.Util

class OdgovorRepository {
    companion object{

        private lateinit var context: Context

        fun setContext(_context: Context){
            context =_context
        }

        suspend fun getOdgovoriKvizAPI(idKviza: Int): List<Odgovor>?{
            val pokusajId = TakeKvizRepository.dajIdPokusajaKvizaZaKvizId(idKviza)
            return withContext(Dispatchers.IO){
                val response = ApiAdapter.retrofit.getOdgovore(AccountRepository.acHash, pokusajId)
                val responseBody = response.body()
                return@withContext responseBody
            }
        }

        suspend fun getOdgovoriKviz(idKviza: Int): List<Odgovor>?{
            return withContext(Dispatchers.IO){
                val db = AppDatabase.getInstance(AccountRepository.getContext())
                val odgovori = db.odgovorDao().getOdgovore(idKviza)
               return@withContext  odgovori
            }
        }

        suspend fun postaviOdgovorKviz(idKvizTaken: Int, idPitanje: Int, odgovor: Int): Int?{
            return withContext(Dispatchers.IO){
                var db = AppDatabase.getInstance(context)
                val pokusajKviza = KvizTakenRepository.getKvizTakenSaIDemPokusaja(idKvizTaken)
                val pitanja = PitanjeKvizRepository.getPitanja(pokusajKviza!!.KvizId)
                val odgovori = db!!.odgovorDao().getAll()
                var brojTacnihBezTrenutnog = 0
                if(pitanja != null)
                    for(pitanje in pitanja){
                        if(odgovori != null)
                            for(odgovor in odgovori)
                                if(pitanje.id == odgovor.pitanjeId && pitanje.tacan == odgovor.odgovoreno)
                                    brojTacnihBezTrenutnog++
                    }
                var brojBodovaPrijeTrenutnog = ((brojTacnihBezTrenutnog.toFloat()/pitanja!!.size.toFloat())*100f).toInt()
          //      Log.d("lespaul", "odgovori size " + odgovori!!.size.toString() + ", idPitanja " + idPitanje.toString())
          //      Log.d("lespaul", "uslo u postavi odgovor")
                var postojiOdgovor = false
                if(pitanja != null)
                for(pitanje in pitanja)
                    for(odgovor in odgovori)
                        if(idPitanje == odgovor.pitanjeId && odgovor.kvizTakenId == pokusajKviza.id && odgovor.kvizId == pokusajKviza.KvizId){
                            postojiOdgovor = true
                        }
                if(!postojiOdgovor){
                    var brojTacnih = 0
                    if(pitanja != null)
                        for(pitanje in pitanja){
                            if(pitanje.id == idPitanje && pitanje.tacan == odgovor) brojTacnih++
                            if(odgovori != null)
                                for(odgovor in odgovori)
                                    if(pitanje.id == odgovor.pitanjeId && pitanje.tacan == odgovor.odgovoreno)
                                        brojTacnih++
                                }
                    var brojBodova = ((brojTacnih.toFloat()/pitanja!!.size.toFloat())*100f).toInt()
                    try{
                //        Log.d("lespaul", "postoji odgovor - " + postojiOdgovor.toString())
                        if(!postojiOdgovor) db!!.odgovorDao().insertAll(Odgovor(odgovori.size, odgovor, idPitanje, pokusajKviza.KvizId, idKvizTaken))
                    }
                    catch(error:Exception){
                        return@withContext brojBodova
                    }
                    return@withContext brojBodova
                }
                return@withContext brojBodovaPrijeTrenutnog
            }
        }

        suspend fun predajOdgovore(idKviza: Int): Boolean{
            return withContext(Dispatchers.IO){
                var db = AppDatabase.getInstance(context)
                val sviOdgovori = db!!.odgovorDao().getAll()
                val trenutniAccount = db!!.accountDao().getAccount()
                var odgovoriNaKvizu: List<Odgovor> = mutableListOf()
                for(odgovor in sviOdgovori)
                    if(odgovor.kvizId == idKviza)
                        odgovoriNaKvizu += odgovor
                for(odgovor in odgovoriNaKvizu){
                    val pokusajKviza = KvizTakenRepository.getKvizTakenSaIDemPokusaja(odgovor.kvizTakenId)
                    var odgovorenoPitanje = OdgovorenoPitanje(odgovor.odgovoreno, odgovor.pitanjeId, pokusajKviza!!.osvojeniBodovi)
                    ApiAdapter.retrofit.postaviOdgovor(trenutniAccount.acHash!!, odgovor.kvizTakenId, odgovorenoPitanje)
                }
                  db!!.kvizDao().predajKviz(idKviza, true)
                return@withContext true
            }
        }



        suspend fun dajTacneOdgovore(idKviza: Int): List<Odgovor>{
            val sviOdgovori = getOdgovoriKviz(idKviza)
            var tacniOdgovori: List<Odgovor> = mutableListOf()
            var indeksTacnog = 0
            if(sviOdgovori != null)
            for(odgovor in sviOdgovori){
                indeksTacnog = PitanjeKvizRepository.getTacanOdgovorZaPitanjeId(odgovor.pitanjeId)
                Log.d("odgovori", odgovor.odgovoreno.toString() + " | " + odgovor.kvizTakenId + " | " + odgovor.pitanjeId + " tacan=" + indeksTacnog.toString())
                if(odgovor.odgovoreno == indeksTacnog)
                    tacniOdgovori += odgovor
            }
            return tacniOdgovori
        }

        suspend fun dajTacnostOdgovora(idKviza: Int, pitanje: Pitanje): Boolean{
            var tacan = false
            val sviTacniOdgovori = dajTacneOdgovore(idKviza)
            for(odgovor in sviTacniOdgovori)
                if(odgovor.pitanjeId == pitanje.id) tacan=true
            return tacan
        }

        suspend fun dajNetacneOdgovore(idPokusajaKviza: Int): List<Odgovor>{
            val sviOdgovori= getOdgovoriKviz(idPokusajaKviza)
            var tacniOdgovori: List<Odgovor> = mutableListOf()
            var indeksTacnog = 0
            if(sviOdgovori != null)
            for(odgovor in sviOdgovori){
                indeksTacnog = PitanjeKvizRepository.getTacanOdgovorZaPitanjeId(odgovor.pitanjeId)
                //   Log.d("odgovori", odgovor.odgovoreno.toString() + " | " + odgovor.kvizTakenId + " | " + odgovor.pitanjeId + " tacan=" + indeksTacnog.toString())
                if(odgovor.odgovoreno != indeksTacnog)
                    tacniOdgovori += odgovor
            }
            return tacniOdgovori
        }

//        suspend fun postaviOdgovor(idKvizTaken: Int, idPitanje: Int, odgovor: Int) : String?{
//            return withContext(Dispatchers.IO) {
//
//            }
//        }
    }
}