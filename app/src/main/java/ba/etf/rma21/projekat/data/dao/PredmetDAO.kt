package ba.etf.rma21.projekat.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ba.etf.rma21.projekat.data.models.Grupa
import ba.etf.rma21.projekat.data.models.Predmet

@Dao
interface PredmetDAO {
    @Query("SELECT * FROM predmet")
    suspend fun getAll(): List<Predmet>

    @Insert
    suspend fun insertAll(vararg predmet: Predmet)

    @Query("DELETE FROM predmet")
    suspend fun deleteAll()
}