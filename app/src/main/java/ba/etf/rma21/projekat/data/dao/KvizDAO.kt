package ba.etf.rma21.projekat.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ba.etf.rma21.projekat.data.models.Kviz

@Dao
interface KvizDAO {
    @Query("SELECT * FROM kviz")
    suspend fun getAll(): List<Kviz>

    @Insert
    suspend fun insertAll(vararg kviz: Kviz)

    @Query("DELETE FROM kviz")
    suspend fun deleteAll()

    @Query("UPDATE kviz SET predan = :predan WHERE id = :idKviza")
    suspend fun predajKviz(idKviza: Int, predan: Boolean)

    @Query("UPDATE kviz SET kvizTakenId = :kvizTakenId WHERE id = :idKviza")
    suspend fun updatePokusaj(idKviza: Int, kvizTakenId: Int)
}