package ba.etf.rma21.projekat.data.repositories

import android.content.Context
import ba.etf.rma21.projekat.data.AppDatabase
import ba.etf.rma21.projekat.data.models.Predmet
import ba.etf.rma21.projekat.data.models.Grupa
import ba.etf.rma21.projekat.data.models.Kviz
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.http.Path

class PredmetIGrupaRepository {

    companion object{

        private lateinit var context: Context

        fun setContext(_context: Context){
            context =_context
        }

//        suspend fun getPredmeti(): List<Predmet>? {
//            return withContext(Dispatchers.IO){
//                val db = AppDatabase.getInstance(AccountRepository.getContext())
//                val grupe = db.predmetDao().getAll()
//                return@withContext  grupe
//            }
//        }

        suspend fun getPredmeti(): List<Predmet>? {
            return withContext(Dispatchers.IO){
                val response = ApiAdapter.retrofit.getPredmete()
                val responseBody = response.body()
                return@withContext responseBody!!
            }
        }

        suspend fun getGrupe(): List<Grupa>? {
            return withContext(Dispatchers.IO){
                val response = ApiAdapter.retrofit.getGrupe()
                val responseBody = response.body()
                return@withContext responseBody!!
            }
        }

//        suspend fun getGrupe(): List<Grupa>? {
//            return withContext(Dispatchers.IO){
//                val db = AppDatabase.getInstance(AccountRepository.getContext())
//                val grupe = db.grupaDao().getAll()
//                return@withContext  grupe
//            }
//        }

        suspend fun getGrupeZaKviz(idKviza: Int): List<Grupa> {
            return withContext(Dispatchers.IO){
                val response = ApiAdapter.retrofit.getGrupeZaKviz(idKviza)
                val responseBody = response.body()
                return@withContext responseBody!!
            }
        }

        suspend fun getUpisaneGrupe(): List<Grupa>? {
            return withContext(Dispatchers.IO){
                val response = ApiAdapter.retrofit.getUpisaneGrupe(AccountRepository.acHash)
                val responseBody = response.body()
                return@withContext responseBody!!
            }
        }

        suspend fun getUpisaneGrupeDB(): List<Grupa>? {
            return withContext(Dispatchers.IO) {
                var db = AppDatabase.getInstance(context)
                var grupe = db!!.grupaDao().getAll()
                return@withContext grupe
            }
        }

        suspend fun getGrupeZaPredmet(idPredmeta: Int): List<Grupa>? {
            return withContext(Dispatchers.IO){
                val response = ApiAdapter.retrofit.getGrupePredmeta(idPredmeta)
                val responseBody = response.body()
                return@withContext responseBody!!
            }
        }

        suspend fun upisiUGrupu(idGrupa: Int): Boolean? {
            return withContext(Dispatchers.IO){
                val message = ApiAdapter.retrofit.upisiGrupu(idGrupa, AccountRepository.acHash)
                return@withContext message.body()!!.poruka.contains("je dodan")
            }
        }

        suspend fun writeGrupaDB(context: Context, grupa: Grupa) : String?{
            return withContext(Dispatchers.IO) {
                try{
                    var db = AppDatabase.getInstance(context)
                    db!!.grupaDao().insertAll(grupa)
                    return@withContext "success"
                }
                catch(error:Exception){
                    return@withContext null
                }
            }
        }

        suspend fun writePredmetDB(context: Context, predmet: Predmet) : String?{
            return withContext(Dispatchers.IO) {
                try{
                    var db = AppDatabase.getInstance(context)
                    db!!.predmetDao().insertAll(predmet)
                    return@withContext "success"
                }
                catch(error:Exception){
                    return@withContext null
                }
            }
        }
    }
}