package ba.etf.rma21.projekat.data.repositories

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ba.etf.rma21.projekat.data.AppDatabase
import ba.etf.rma21.projekat.data.models.*
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PitanjeKvizRepository {
    companion object {

        suspend fun getPitanja(idKviza: Int): List<Pitanje>?{
            return withContext(Dispatchers.IO){
                val response = ApiAdapter.retrofit.getPit(idKviza)
                val responseBody = response.body()
                var listaPitanja: List<Pitanje> = mutableListOf()
                if(responseBody != null)
                for(pitanje in responseBody){
                    var opcije = ""
                    for(i in 0 until pitanje.opcije.size){
                        if(i == pitanje.opcije.size -1) opcije += pitanje.opcije[i]
                        else opcije += pitanje.opcije[i] + ","
                    }
                    listaPitanja += Pitanje(pitanje.id, pitanje.naziv, pitanje.tekstPitanja, opcije, pitanje.tacan)
                }
                return@withContext listaPitanja!!
            }
        }

        suspend fun getPitanjaMojihKvizova(): List<Pitanje>{
            val mojiKvizovi = KvizRepository.getMyKvizes()
            var svaPitanja: List<Pitanje>? = mutableListOf()
            if(mojiKvizovi != null)
                for(kviz in mojiKvizovi){
                    val pitanja = getPitanja(kviz.id)
                    if (svaPitanja != null) {
                        if(pitanja != null)
                            svaPitanja += pitanja
                    }
                }
            return svaPitanja!!.distinct()
        }

        suspend fun getSvaPitanja(): List<Pitanje>{
            val sviKvizovi = KvizRepository.getAll()
            var svaPitanja: List<Pitanje>? = mutableListOf()
            if(sviKvizovi != null)
            for(kviz in sviKvizovi){
                val pitanja = getPitanja(kviz.id)
                if (svaPitanja != null) {
                    if(pitanja != null)
                        svaPitanja += pitanja
                }
            }
            return svaPitanja!!.distinct()
        }

        suspend fun getTacanOdgovorZaPitanjeId(idPitanja: Int): Int{
            val svaPitanja = getPitanja(KvizRepository.getOtvoreniKviz().id)
            if(svaPitanja != null)
            for(pitanje in svaPitanja)
                if(pitanje.id == idPitanja)
                    return pitanje.tacan
            return -1
        }
    }
}