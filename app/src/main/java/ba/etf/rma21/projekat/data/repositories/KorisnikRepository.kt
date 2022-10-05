package ba.etf.rma21.projekat.data.repositories

import android.util.Log
import ba.etf.rma21.projekat.data.*
import ba.etf.rma21.projekat.data.models.Grupa
import ba.etf.rma21.projekat.data.models.Kviz
import ba.etf.rma21.projekat.data.models.Pitanje
import ba.etf.rma21.projekat.data.models.Predmet

class KorisnikRepository{

    companion object{
        private var odabranaGodinaStudija = 0
        private var listaTacnoOdgovorenih: List<Pitanje> = listOf()

        fun setTacne(lista: List<Pitanje>){
            listaTacnoOdgovorenih = lista
        }

        fun getTacne(): List<Pitanje>{
            return listaTacnoOdgovorenih
        }

        fun setOdabranaGodinaStudija(godina: Int){
            odabranaGodinaStudija = godina
        }

        fun getOdabranaGodinaStudija(): Int{
            return odabranaGodinaStudija
        }
        
        suspend fun getListaOdgovorenihPitanja(idKviza: Int): List<Pitanje>{
            var listaPitanja: List<Pitanje> = mutableListOf()
            var svaPitanja = PitanjeKvizRepository.getSvaPitanja()
            var listaOdgovora = OdgovorRepository.getOdgovoriKvizAPI(idKviza)
            for(pitanje in svaPitanja)
                if(listaOdgovora != null)
                for(odgovor in listaOdgovora)
                    if(pitanje.id==odgovor.pitanjeId)
                        listaPitanja += pitanje
            return listaPitanja.distinct()
        }

    suspend fun getOdgovorNaPitanje(pitanje: Pitanje, kviz: Kviz): Int{
        var sviOdgovori = OdgovorRepository.getOdgovoriKviz(kviz.id)
        if(sviOdgovori != null)
        if(sviOdgovori.isEmpty()) Log.d("odgovorix", "prazno")
        Log.d("odgovorix", "uslo")
        if(sviOdgovori != null)
        for(odgovor in sviOdgovori){
        //    Log.d("odgovorix", odgovor.id.toString() + "|" + odgovor.odgovoreno.toString())
        }
        if(sviOdgovori != null)
        for(odgovor in sviOdgovori){
            Log.d("odgovorix", odgovor.pitanjeId.toString() + "|" + pitanje.id.toString() + "|" + odgovor.odgovoreno)
            if(odgovor.pitanjeId == pitanje.id)
                return odgovor.odgovoreno
        }
        return 0
    }

        suspend fun getTacnoOdgovorenaPitanjaZaKviz(kviz: Kviz): List<Pitanje>{
            val svaPitanjaNaKvizu = PitanjeKvizRepository.getPitanja(kviz.id)
            val odgovoriNaPitanja = OdgovorRepository.dajTacneOdgovore(kviz.id)
            var tacnoOdgovorenaPitanja: List<Pitanje> = mutableListOf()
            if(svaPitanjaNaKvizu != null)
            for(pitanje in svaPitanjaNaKvizu)
                for(odgovor in odgovoriNaPitanja)
                    if(pitanje.tacan == odgovor.odgovoreno && pitanje.id == odgovor.pitanjeId)
                        tacnoOdgovorenaPitanja += pitanje
            return tacnoOdgovorenaPitanja
        }

        suspend fun getNetacnoOdgovorenaPitanjaZaKviz(kviz: Kviz): List<Pitanje>{
            val svaPitanjaNaKvizu = PitanjeKvizRepository.getPitanja(kviz.id)
            val odgovoriNaPitanja = OdgovorRepository.dajNetacneOdgovore(kviz.id)
            var netacnoOdgovorenaPitanja: List<Pitanje> = mutableListOf()
            if(svaPitanjaNaKvizu != null)
            for(pitanje in svaPitanjaNaKvizu)
                for(odgovor in odgovoriNaPitanja)
                    if(pitanje.tacan == odgovor.odgovoreno && pitanje.id == odgovor.pitanjeId)
                        netacnoOdgovorenaPitanja += pitanje
            return netacnoOdgovorenaPitanja
        }
    }
}