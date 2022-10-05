package ba.etf.rma21.projekat.viewmodel

import android.content.Context
import ba.etf.rma21.projekat.data.models.Kviz
import ba.etf.rma21.projekat.data.models.OdgovorNaUpit
import ba.etf.rma21.projekat.data.repositories.AccountRepository
import ba.etf.rma21.projekat.data.repositories.DBRepository
import ba.etf.rma21.projekat.data.repositories.KvizRepository
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class DBViewModel {
    val scope = CoroutineScope(Job() + Dispatchers.Main)

    fun updateNow(onSuccess: (kvizovi: Boolean) -> Unit,
                 onError: () -> Unit){
        scope.launch{
            val result = DBRepository.updateNow()
            when (result) {
                is Boolean -> onSuccess?.invoke(result)
                else-> onError?.invoke()
            }
        }
    }
}