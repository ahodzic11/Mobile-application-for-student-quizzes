package ba.etf.rma21.projekat.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ba.etf.rma21.projekat.data.models.Grupa
import ba.etf.rma21.projekat.data.models.Kviz

@Dao
interface GrupaDAO {
    @Query("SELECT * FROM grupa")
    suspend fun getAll(): List<Grupa>

    @Insert
    suspend fun insertAll(vararg grupa: Grupa)

    @Query("DELETE FROM grupa")
    suspend fun deleteAll()
}