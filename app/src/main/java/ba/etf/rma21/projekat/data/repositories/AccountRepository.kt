package ba.etf.rma21.projekat.data.repositories

import android.content.Context
import android.util.Log
import ba.etf.rma21.projekat.data.models.Account
import ba.etf.rma21.projekat.data.AppDatabase
import ba.etf.rma21.projekat.data.models.Kviz
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class AccountRepository {
    companion object{
        var acHash = "3ff231fb-3ea5-499a-b9dc-96960e3762c6"
        private lateinit var context: Context

        fun setContext(_context:Context){
            context=_context
        }

        fun getContext(): Context{
            return context
        }

        suspend fun postaviHash(hash: String): Boolean {
            return withContext(Dispatchers.IO) {
            ///    Log.d("lespaul", "uslo u postavi hash")
                try{
                    acHash = hash
                    val formater = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    var db = AppDatabase.getInstance(context)
                    val brojAccounta = db!!.accountDao().getAll()
             //       Log.d("lespaul", "broj prethodnih korisnika - " + brojAccounta.size.toString())
                    if(brojAccounta.isEmpty()){
            //            Log.d("lespaul", "nema prethodnih korisnika, pravi se novi")
                        val noviAcc = Account(0, "email", hash, formater.format(Calendar.getInstance().time))
                        db!!.accountDao().insertAll(noviAcc)
                    }else if(db!!.accountDao().getAccount().acHash == hash) {
              //          Log.d("lespaul", "isti je korisnik")
                        return@withContext true
                    }
                    else{
             //           Log.d("lespaul", "ima prethodnog korisnika, brisu se podaci od prije i pravi se novi")
                        db!!.kvizDao().deleteAll()
                        db!!.accountDao().deleteAll()
                        db!!.pitanjeDao().deleteAll()
                        db!!.predmetDao().deleteAll()
                        db!!.grupaDao().deleteAll()
                        db!!.odgovorDao().deleteAll()
                        val noviAcc = Account(0, "email", hash, formater.format(Calendar.getInstance().time))
                        db!!.accountDao().insertAll(noviAcc)
                    }
             //       Log.d("lespaul", "PH SUCCESS")
                    return@withContext true
                }
                catch(error:Exception){
                    return@withContext true
                }
            }
        }

        suspend fun upisiDefaultniAccount(): Boolean{
            if(postaviHash(acHash)) return true
            return false
        }

        suspend fun deleteAllDB(context: Context){
            return withContext(Dispatchers.IO) {
                var db = AppDatabase.getInstance(context)
                return@withContext db!!.accountDao().deleteAll()
            }
        }

        suspend fun writeAccDB(context: Context, kviz: Account) : String?{
            return withContext(Dispatchers.IO) {
                try{
                    var db = AppDatabase.getInstance(context)
                    db!!.accountDao().insertAll(kviz)
                    return@withContext "success"
                }
                catch(error:Exception){
                    return@withContext null
                }
            }
        }

        suspend fun getAllDB(context: Context): List<Account>{
            return withContext(Dispatchers.IO) {
                var db = AppDatabase.getInstance(context)
                var movies = db!!.accountDao().getAll()
                return@withContext movies
            }
        }

        fun getHash(): String{
            return acHash
        }

        suspend fun getAccount(idStudenta: String): Account{
            return withContext(Dispatchers.IO){
                val response = ApiAdapter.retrofit.getAccount(idStudenta)
                val responseBody = response.body()
                return@withContext responseBody!!
            }
        }

        suspend fun obrisiPodatke(idStudenta: String): String{
            return withContext(Dispatchers.IO){
                val response = ApiAdapter.retrofit.obrisiPodatke(idStudenta)
                val responseBody = response.body()
                return@withContext responseBody!!
            }
        }

        suspend fun updateAccount(lastUpdate: String, context: Context): String?{
            return withContext(Dispatchers.IO) {
                try{
                    var db = AppDatabase.getInstance(context)
                    db!!.accountDao().update(lastUpdate)
                    return@withContext "success"
                }
                catch(error:Exception){
                    return@withContext null
                }
            }
        }

        suspend fun getAccountDB(context: Context): Account{
            return withContext(Dispatchers.IO) {
                var db = AppDatabase.getInstance(context)
                var movies = db!!.accountDao().getAccount()
                return@withContext movies
            }
        }
    }
}