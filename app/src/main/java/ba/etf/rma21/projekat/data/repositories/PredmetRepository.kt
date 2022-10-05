package ba.etf.rma21.projekat.data.repositories

import android.util.Log
import ba.etf.rma21.projekat.data.models.Grupa
import ba.etf.rma21.projekat.data.models.Predmet

class PredmetRepository {
    companion object {
        private var odabraniPredmet = 0

        fun setOdabraniPredmet(pozicija: Int){
            odabraniPredmet = pozicija
        }

        fun getOdabraniPredmet(): Int{
            return odabraniPredmet
        }

        suspend fun getUpisani(): List<Predmet>{
            val grupeNaKojimaJeUpisan = PredmetIGrupaRepository.getUpisaneGrupe()
            var upisaniPredmeti: List<Predmet> = mutableListOf()
            if(grupeNaKojimaJeUpisan != null)
            for(grupa in grupeNaKojimaJeUpisan){
                val predmet = dajPredmetZaGrupu(grupa)
                upisaniPredmeti += predmet
            }
            return upisaniPredmeti
        }

        suspend fun dajPredmetZaGrupu(grupa: Grupa): Predmet{
            val predmeti = PredmetIGrupaRepository.getPredmeti()
            if(predmeti != null)
            for(predmet in predmeti)
                if(predmet.id==grupa.idPredmeta)
                    return predmet
            return Predmet(0,"",0)
        }

        suspend fun getAll(): List<Predmet>? {
            return PredmetIGrupaRepository.getPredmeti()
        }

        suspend fun getPredmetsByGodina(godina:Int): List<Predmet>{
            var listaPredmeta: List<Predmet> = mutableListOf()
            val listaSvih = getAll()
            if(listaSvih != null)
            for(predmet in listaSvih){
                if(predmet.godina==godina) listaPredmeta += predmet
            }
            return listaPredmeta
        }

        suspend fun getPredmeteSaUpisaneGodine(godina: Int): List<Predmet>{
            val sviPredmeti = getAll()
            val upisaniPredmeti = getUpisani()
            var listaPredmetaKojiNisuUpisani: List<Predmet> = mutableListOf()
            if(sviPredmeti != null)
            for(predmet in sviPredmeti){
                if(predmet.godina==godina)
                    for(pr in upisaniPredmeti)
                        if(pr.id==predmet.id)
                            listaPredmetaKojiNisuUpisani += pr
            }
            return listaPredmetaKojiNisuUpisani
        }

        /*suspend fun getPredmeteKojiNisuUpisani(): List<String>{
            val listaPredmetaSaUpisaneGodine: List<String> = getPredmeteSaUpisaneGodine()
            var listaUpisanihPredmeta: List<String> = mutableListOf()
            var listaPredmetaKojiNisuUpisani: List<String> = mutableListOf()
            for(par in KorisnikRepository.getKorisnik().informacije) listaUpisanihPredmeta += par.first.naziv
            for(predmet in listaPredmetaSaUpisaneGodine)
                if(!listaUpisanihPredmeta.contains(predmet))
                    listaPredmetaKojiNisuUpisani += predmet
            return listaPredmetaKojiNisuUpisani
        }

         */
        suspend fun getPredmeteKojiNisuUpisaniSaGodine(godina: Int): List<String>{
            val sviPredmeti = getAll()
            val upisaniPredmeti: List<Predmet> = getPredmeteSaUpisaneGodine(godina)
            var neupisaniPredmeti: List<String> = mutableListOf()
            if(sviPredmeti != null)
            for(predmet in sviPredmeti){
                if(predmet.godina == godina && !upisaniPredmeti.contains(predmet))
                    neupisaniPredmeti += predmet.naziv
            }
            return neupisaniPredmeti
        }

        suspend fun dajIdPredmetaZaImePredmeta(nazivPredmeta: String): Int{
            val sviPredmeti = getAll()
            if(sviPredmeti != null)
            for(predmet in sviPredmeti)
                if(predmet.naziv == nazivPredmeta)
                    return predmet.id
            return -1
        }
    }
}