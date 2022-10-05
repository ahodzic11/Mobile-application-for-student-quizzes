package ba.etf.rma21.projekat.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ba.etf.rma21.projekat.data.models.Kviz
import ba.etf.rma21.projekat.data.models.Odgovor
import ba.etf.rma21.projekat.data.models.Pitanje

@Dao
interface PitanjeDAO {
    @Query("SELECT * FROM pitanje")
    suspend fun getAll(): List<Pitanje>

    @Insert
    suspend fun insertAll(vararg pitanje: Pitanje)

    @Query("DELETE FROM pitanje")
    suspend fun deleteAll()
}