package ba.etf.rma21.projekat.viewmodel

import android.content.Context
import android.service.voice.AlwaysOnHotwordDetector
import ba.etf.rma21.projekat.data.AppDatabase
import ba.etf.rma21.projekat.data.models.Account
import ba.etf.rma21.projekat.data.models.Kviz
import ba.etf.rma21.projekat.data.repositories.AccountRepository
import ba.etf.rma21.projekat.data.repositories.ApiAdapter
import ba.etf.rma21.projekat.data.repositories.KvizRepository
import kotlinx.coroutines.*

class AccountViewModel {
    val scope = CoroutineScope(Job() + Dispatchers.Main)

    fun postaviHash(hash: String, onSuccess: (movies: Boolean) -> Unit,
                    onError: () -> Unit){
        scope.launch{
            val result = AccountRepository.postaviHash(hash)
            when (result) {
                is Boolean -> onSuccess?.invoke(result)
                else-> onError?.invoke()
            }
        }
    }

    fun upisiDefaultniAccount(onSuccess: (movies: Boolean) -> Unit,
                    onError: () -> Unit){
        scope.launch{
            val result = AccountRepository.upisiDefaultniAccount()
            when (result) {
                is Boolean -> onSuccess?.invoke(result)
                else-> onError?.invoke()
            }
        }
    }

    fun getAccount(idStudenta: String, onSuccess: (account: Account) -> Unit,
               onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = AccountRepository.getAccount(idStudenta)
            when (result) {
                is Account -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

    fun updateAccount(lastUpdate: String, context: Context, onSuccess: (account: String?) -> Unit,
                   onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = AccountRepository.updateAccount(lastUpdate, context)
            when (result) {
                is String? -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

    fun obrisiPodatke(idStudenta: String, onSuccess: (poruka: String) -> Unit,
                   onError: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = AccountRepository.obrisiPodatke(idStudenta)
            when (result) {
                is String -> onSuccess.invoke(result)
                else -> onError.invoke()
            }
        }
    }

    fun getAllDB(context: Context, onSuccess: (kvizovi: List<Account>) -> Unit,
                 onError: () -> Unit){
        scope.launch{
            val result = AccountRepository.getAllDB(context)
            when (result) {
                is List<Account> -> onSuccess?.invoke(result)
                else-> onError?.invoke()
            }
        }
    }

    fun writeAccDB(context: Context, kviz: Account, onSuccess: (kvizovi: String) -> Unit,
                    onError: () -> Unit){
        scope.launch{
            val result = AccountRepository.writeAccDB(context, kviz)
            when (result) {
                is String -> onSuccess?.invoke(result)
                else-> onError?.invoke()
            }
        }
    }
}