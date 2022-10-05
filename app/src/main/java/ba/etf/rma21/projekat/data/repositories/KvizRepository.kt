package ba.etf.rma21.projekat.data.repositories

import android.content.Context
import android.util.Log
import ba.etf.rma21.projekat.data.AppDatabase
import ba.etf.rma21.projekat.data.models.*
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class KvizRepository {
    companion object {
        private lateinit var otvoreniKviz : Kviz
        private var predaniKvizovi: List<Kviz> = emptyList()
        private var odabirPregledaKviza = 0
        private lateinit var context: Context

        fun setContext(_context:Context){
            context=_context
        }

        fun getOdabirPregledaKviza(): Int{
            return odabirPregledaKviza
        }

        fun setOdabirPregledaKviza(pozicija: Int){
            odabirPregledaKviza = pozicija
        }

        suspend fun getMyKvizes(): List<Kviz> {
            val kvizovi = getUpisani()
            postaviDatume(kvizovi)
            postaviImena(kvizovi)
            postaviPredanost(kvizovi)
            return kvizovi!!.distinct()
        }

        suspend fun getMyKvizesDB(): List<Kviz>{
            return withContext(Dispatchers.IO) {
                var db = AppDatabase.getInstance(context)
                DBRepository.updateNow()
                var kvizovi = db!!.kvizDao().getAll()
                postaviDatume(kvizovi)
                postaviImena(kvizovi)
                postaviPredanost(kvizovi)
                return@withContext kvizovi
            }
        }

        suspend fun getUpisani(): List<Kviz>?{
            val upisaneGrupe = PredmetIGrupaRepository.getUpisaneGrupe()
            var upisaniKvizovi: List<Kviz> = mutableListOf()
            if(upisaneGrupe != null)
            for(grupa in upisaneGrupe){
                var listaZaGrupu: List<Kviz> = getKvizoveZaGrupe(grupa.id)
                upisaniKvizovi += listaZaGrupu
            }
            postaviDatume(upisaniKvizovi)
            postaviImena(upisaniKvizovi)
            postaviPredanost(upisaniKvizovi)
            return upisaniKvizovi.distinct()
        }

        suspend fun getKvizoveZaGrupe(idGrupe: Int): List<Kviz>{
            return withContext(Dispatchers.IO){
                val response = ApiAdapter.retrofit.getKvizoveZaGrupu(idGrupe)
                val responseBody = response.body()
                return@withContext responseBody!!
            }
        }

        suspend fun getAll(): List<Kviz>?{
            return withContext(Dispatchers.IO){
                val response = ApiAdapter.retrofit.getKv()
                val responseBody = response.body()
                postaviDatume(responseBody)
                postaviImena(responseBody)
                return@withContext responseBody!!
            }
        }

        suspend fun getDone(): List<Kviz> {
            var listaKvizova: List<Kviz> = mutableListOf()
            for(k in getMyKvizesDB()){
                if(k.datumRada != null) listaKvizova += k
            }
            postaviDatume(listaKvizova)
            postaviImena(listaKvizova)
            postaviPredanost(listaKvizova)
            return listaKvizova
        }

        suspend fun getFuture(): List<Kviz> {
            val formater = SimpleDateFormat("yyyy-MM-dd")
            var listaKvizova : List<Kviz> = mutableListOf()
            for(k in getMyKvizesDB()){
                if(k.datumPocetka != null)
                if(formater.parse(k.datumPocetka).after(Calendar.getInstance().time)) listaKvizova += k
            }
            postaviDatume(listaKvizova)
            postaviImena(listaKvizova)
            postaviPredanost(listaKvizova)
            return listaKvizova
        }

        suspend fun getNotTaken(): List<Kviz> {
            val formater = SimpleDateFormat("yyyy-MM-dd")
            var listaKvizova: List<Kviz> = mutableListOf()
            for(k in getMyKvizesDB()){
                if(k.datumKraj != null)
                if(k.datumRada == null && formater.parse(k.datumPocetka).before(Calendar.getInstance().time) && formater.parse(k.datumKraj).before(Calendar.getInstance().time)) listaKvizova += k
            }
            return listaKvizova
        }

        fun otvoriKviz(kviz: Kviz){
            otvoreniKviz = kviz
        }

        fun getOtvoreniKviz(): Kviz {
            return otvoreniKviz
        }


        suspend fun predajKviz(kviz: Kviz){
            /*val predan = KvizTakenRepository.daLiJePredan(TakeKvizRepository.getZadnjiZapocetiKviz().id)
            if(!predan){
                val odgovoriNaPokusaju = OdgovorRepository.getOdgovoriKviz(kviz.id)
                val pitanjaNaKvizu = PitanjeKvizRepository.getPitanja(kviz.id)
                if(pitanjaNaKvizu != null)
                for(pitanje in pitanjaNaKvizu){
                    var odgovoreno = false
                    if(odgovoriNaPokusaju != null)
                    for(odgovor in odgovoriNaPokusaju)
                        if(pitanje.id == odgovor.pitanjeId)
                            odgovoreno = true
                    if(odgovoreno == false){
                        val opcije = pitanje.opcije.split(",")
                        OdgovorRepository.postaviOdgovorKviz(TakeKvizRepository.getZadnjiZapocetiKviz().id, pitanje.id, opcije.size)
                    }
                }
            }*/

        }

        fun getPredaniKvizovi(): List<Kviz>{
            return predaniKvizovi
        }

        suspend fun getById(id:Int): Kviz?{
            return withContext(Dispatchers.IO){
                val response = ApiAdapter.retrofit.getKvizById(id)
                val responseBody = response.body()
                return@withContext responseBody
            }
        }

        private suspend fun postaviImena(kvizovi: List<Kviz>?){
            if(kvizovi != null)
            for(kviz in kvizovi){
                val grupeNaKojimaJeKvizDostupan = PredmetIGrupaRepository.getGrupeZaKviz(kviz.id)
                var predmetiNaKojimaJeKvizDostupanZaGrupe: List<Int> = mutableListOf()
                for(grupa in grupeNaKojimaJeKvizDostupan)
                    predmetiNaKojimaJeKvizDostupanZaGrupe += grupa.idPredmeta
                var listaNazivaPredmeta: List<String> = mutableListOf()
                val sviPredmeti = PredmetIGrupaRepository.getPredmeti()
                for(idPredmeta in predmetiNaKojimaJeKvizDostupanZaGrupe)
                    if(sviPredmeti != null)
                    for(predm in sviPredmeti)
                        if(predm.id == idPredmeta)
                            listaNazivaPredmeta += predm.naziv
                var nazivPredmeta = ""
                for(i in 0..listaNazivaPredmeta.distinct().size-1){
                    if(i == listaNazivaPredmeta.distinct().size-1) nazivPredmeta += listaNazivaPredmeta.distinct()[i]
                    else nazivPredmeta += listaNazivaPredmeta.distinct()[i]  + ", "
                }
                kviz.nazivPredmeta = nazivPredmeta
            }
        }

        suspend fun postaviPredanost(kvizovi: List<Kviz>?){
            val formater = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            var db = AppDatabase.getInstance(AccountRepository.getContext())
            val pokusajiKvizova = KvizTakenRepository.getPokusaje(AccountRepository.acHash)
            for(pokusaj in pokusajiKvizova)
                if(kvizovi != null)
                    for(kviz in kvizovi)
                        if(kviz.id == pokusaj.KvizId){
                            val pitanja = PitanjeKvizRepository.getPitanja(kviz.id)
                            val odgovori = OdgovorRepository.getOdgovoriKviz(kviz.id)
//                            Log.d("datumi123", pitanja!!.size.toString() + " " + odgovori!!.size.toString())
//                            kviz.osvojeniBodovi = pokusaj.osvojeniBodovi.toFloat()
                            if(odgovori!!.size == pitanja!!.size){
                                db!!.kvizDao().predajKviz(kviz.id, true)
                                Log.d("datumi123", "kviz  je predan " + kviz.naziv)
                            }
                            kviz.datumRada = formater.format(pokusaj.datumRada)
                        }
            }

        suspend fun isPredan(kviz: Kviz): Boolean{
            Log.d("probakviza", "proba")
            val pokusajiKvizova = KvizTakenRepository.getPokusaje(AccountRepository.acHash)
            for(pokusaj in pokusajiKvizova)
                if(kviz.id == pokusaj.KvizId)
                {
                    val pitanja = PitanjeKvizRepository.getPitanja(kviz.id)
                    val odgovori = OdgovorRepository.getOdgovoriKvizAPI(kviz.id)
                    Log.d("probakviza", pitanja!!.size.toString() + ", " + odgovori!!.size.toString())
                    if(odgovori!!.size==pitanja!!.size) return true
                }
            return false
        }

        private suspend fun postaviDatume(kvizovi: List<Kviz>?){
            val formater = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            val pokusajiKvizova = KvizTakenRepository.getPokusaje(AccountRepository.acHash)
            for(pokusaj in pokusajiKvizova)
                if(kvizovi != null)
                for(kviz in kvizovi)
                       if(kviz.id == pokusaj.KvizId){
                       val pitanja = PitanjeKvizRepository.getPitanja(kviz.id)
                        val odgovori = OdgovorRepository.getOdgovoriKviz(kviz.id)
                           Log.d("datumi123", pitanja!!.size.toString() + " " + odgovori!!.size.toString())
                        kviz.osvojeniBodovi = pokusaj.osvojeniBodovi.toFloat()
                        if(odgovori!!.size == pitanja!!.size)
                            kviz.datumRada = formater.format(pokusaj.datumRada)
                }
            }

        suspend fun isMojKviz(probniKviz: Kviz): Boolean{
            val sviMojiKvizovi = getUpisani()
            if(sviMojiKvizovi!=null)
            for(kviz in sviMojiKvizovi)
                if(kviz.id == probniKviz.id)
                    return true
            return false
        }

        suspend fun updateKvizPokusaj(idKviza: Int, idKvizTaken: Int): String?{
            return withContext(Dispatchers.IO) {
                try{
                    var db = AppDatabase.getInstance(context)
                    db!!.kvizDao().updatePokusaj(idKviza, idKvizTaken)
                    return@withContext "success"
                }
                catch(error:Exception){
                    return@withContext null
                }
            }
        }

        suspend fun updateKviz(idKviza: Int, predan: Boolean, context: Context): String?{
            return withContext(Dispatchers.IO) {
                try{
                    var db = AppDatabase.getInstance(context)
                    db!!.kvizDao().predajKviz(idKviza, predan)
                    return@withContext "success"
                }
                catch(error:Exception){
                    return@withContext null
                }
            }
        }

        suspend fun getAllDB(context: Context): List<Kviz>{
            return withContext(Dispatchers.IO) {
                var db = AppDatabase.getInstance(context)
                var kvizovi = db!!.kvizDao().getAll()
                postaviDatume(kvizovi)
                postaviImena(kvizovi)
                return@withContext kvizovi
            }
        }

        suspend fun deleteAllDB(context: Context){
            return withContext(Dispatchers.IO) {
                var db = AppDatabase.getInstance(context)
                return@withContext db!!.kvizDao().deleteAll()
            }
        }

        suspend fun writeKvizDB(context: Context, kviz: Kviz) : String?{
            return withContext(Dispatchers.IO) {
                try{
                    var db = AppDatabase.getInstance(context)
                    db!!.kvizDao().insertAll(kviz)
                    return@withContext "success"
                }
                catch(error:Exception){
                    return@withContext null
                }
            }
        }
    }
}

