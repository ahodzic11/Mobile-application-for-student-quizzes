package ba.etf.rma21.projekat.data.repositories

import android.content.Context
import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import ba.etf.rma21.projekat.data.AppDatabase
import ba.etf.rma21.projekat.data.models.*
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class DBRepository {
    companion object{
        private lateinit var context:Context

        fun setContext(_context: Context){
            context =_context
        }

        suspend fun getPromjene(hash: String, datum: String): OdgovorNaUpit {
            return withContext(Dispatchers.IO){
                val response = ApiAdapter.retrofit.getPromjene(hash, datum)
                val responseBody = response.body()
                if(responseBody != null)
                    Log.d("lespaul", "promjena " + responseBody.changed.toString())
                return@withContext responseBody!!
            }
        }

        suspend fun updateNow(): Boolean{
            return withContext(Dispatchers.IO){
                var db = AppDatabase.getInstance(context)
                val formater = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                val trenutniAccount = db!!.accountDao().getAccount()
                Log.d("lespaul", "trenutni account " + trenutniAccount.acHash + " | " + trenutniAccount.lastUpdate)
                val daLiJePromijenjena = getPromjene(trenutniAccount.acHash!!, trenutniAccount.lastUpdate)
                if(daLiJePromijenjena.changed){
                    Log.d("lespaul", "vršim promjene")
                    db!!.kvizDao().deleteAll()
                    db!!.grupaDao().deleteAll()
                    db!!.predmetDao().deleteAll()
                    db!!.pitanjeDao().deleteAll()
                    db!!.odgovorDao().deleteAll()

                    Log.d("lespaul", db!!.kvizDao().getAll().size.toString())

                    val sviKvizovi: List<Kviz> = KvizRepository.getMyKvizes()
                    for(kviz in sviKvizovi)
                        db!!.kvizDao().insertAll(kviz)

                    val upisaneGrupe = PredmetIGrupaRepository.getUpisaneGrupe()
                    if(upisaneGrupe != null)
                        for(grupa in upisaneGrupe)
                            db!!.grupaDao().insertAll(grupa)

                    val upisaniPredmeti = PredmetRepository.getUpisani()
                    for(predmet in upisaniPredmeti)
                        db!!.predmetDao().insertAll(predmet)

                    val pitanjaKvizova = PitanjeKvizRepository.getPitanjaMojihKvizova()
                    for(pitanje in pitanjaKvizova)
                        db!!.pitanjeDao().insertAll(pitanje)

                    var sviOdgovori: List<Odgovor> = mutableListOf()
                    for(kviz in sviKvizovi) {
                        val odgovoriNaKvizu: List<Odgovor>? = OdgovorRepository.getOdgovoriKvizAPI(kviz.id)
                        if(odgovoriNaKvizu != null)
                            sviOdgovori += odgovoriNaKvizu
                    }
                    var brojOdgovora=0
                    for(odgovor in sviOdgovori)
                        db!!.odgovorDao().insertAll(Odgovor(brojOdgovora++, odgovor.odgovoreno, odgovor.pitanjeId, odgovor.kvizId, odgovor.kvizTakenId))

                    db!!.accountDao().update(formater.format(Calendar.getInstance().time))
                }
                return@withContext daLiJePromijenjena.changed
            }
        }
    }
}