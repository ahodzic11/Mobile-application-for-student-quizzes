package ba.etf.rma21.projekat.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ba.etf.rma21.projekat.data.models.Kviz
import ba.etf.rma21.projekat.data.models.Odgovor

@Dao
interface OdgovorDAO {
    @Query("SELECT * FROM odgovor")
    suspend fun getAll(): List<Odgovor>

    @Insert
    suspend fun insertAll(vararg odgovor: Odgovor)

    @Query("DELETE FROM odgovor")
    suspend fun deleteAll()

    @Query("SELECT * FROM odgovor WHERE KvizId=:idKviza")
    suspend fun getOdgovore(idKviza: Int): List<Odgovor>
}