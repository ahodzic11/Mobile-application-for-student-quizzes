package ba.etf.rma21.projekat.data.repositories

import android.util.Log
import ba.etf.rma21.projekat.data.AppDatabase
import ba.etf.rma21.projekat.data.models.Grupa
import ba.etf.rma21.projekat.data.models.Account
import ba.etf.rma21.projekat.data.models.Odgovor
import ba.etf.rma21.projekat.data.models.Predmet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GrupaRepository {
    companion object {
        private var odabranaGrupa = 0
        private var zadnjaOdabranaGrupa: Grupa? = null
        private var idZadnjeGrupe = 0
        private var nazivZadnjeUpisaneGrupe = ""

        suspend fun getGroupsByPredmet(nazivPredmeta: String): List<Grupa> {
            var listaGrupa: List<Grupa> = mutableListOf()
            val listaSvihGrupa = getAll()
            val idPredmeta = PredmetRepository.dajIdPredmetaZaImePredmeta(nazivPredmeta)
            if(listaSvihGrupa != null)
            for(grupa in listaSvihGrupa){
                if(grupa.idPredmeta == idPredmeta) listaGrupa += grupa
            }
            return listaGrupa
        }

        suspend fun getGrupu(id: Int): Grupa{
            return withContext(Dispatchers.IO){
                val response = ApiAdapter.retrofit.getGrupu(id)
                val responseBody = response.body()
                return@withContext responseBody!!
            }
        }

        fun setIdZadnjeGrupe(id: Int){
            idZadnjeGrupe = id
        }

        fun getIdZadnjeGrupe(): Int{
            return idZadnjeGrupe
        }

        suspend fun getIdZadnjeGrupe(nazivGrupe: String): Int{
            var listaSvihGrupa = getAll()
            if(listaSvihGrupa != null)
            for(grupa in listaSvihGrupa)
                if(grupa.naziv == nazivGrupe)
                    return grupa.id
            return -1
        }

        suspend fun setZadnjaUpisanaGrupa(nazivGrupe: String, idPredmeta: Int){
            val listaSvihGrupa = getAll()
            if(listaSvihGrupa != null)
            for(grupa in listaSvihGrupa)
                if(grupa.naziv == nazivGrupe && grupa.idPredmeta == idPredmeta)
                    zadnjaOdabranaGrupa = grupa
        }

        fun getZadnjaUpisanaGrupa(): Grupa?{
            return zadnjaOdabranaGrupa
        }

//        suspend fun getAll(): List<Grupa>?{
//            return withContext(Dispatchers.IO){
//                val db = AppDatabase.getInstance(AccountRepository.getContext())
//                val grupe = db.grupaDao().getAll()
//                return@withContext  grupe
//            }
//        }

        suspend fun getAll(): List<Grupa>?{
            return PredmetIGrupaRepository.getGrupe()
        }

        fun setOdabranaGrupa(pozicija: Int){
            odabranaGrupa = pozicija
        }

        fun getOdabranaGrupa(): Int{
            return odabranaGrupa
        }

        fun setNazivZadnjeUpisaneGrupe(naziv: String){
            nazivZadnjeUpisaneGrupe = naziv
        }

        fun getNazivZadnjeUpisaneGrupe(): String{
            return nazivZadnjeUpisaneGrupe
        }
    }
}