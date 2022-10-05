package ba.etf.rma21.projekat.data.models

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun stringToListaStringova(opcije: String): List<String>{
        var listaOpcija: List<String> = opcije.split(',')
        return listaOpcija
    }

    @TypeConverter
    fun listaStringovaToString(opcije: List<String>): String{
        var stringovno = ""
        for(i in 0..opcije.size){
      //       stringovno += opcije[i]
        }
        return stringovno
    }
}