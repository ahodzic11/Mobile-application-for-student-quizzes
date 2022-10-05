package ba.etf.rma21.projekat.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import ba.etf.rma21.projekat.data.models.Account
import ba.etf.rma21.projekat.data.models.Kviz

@Dao
interface AccountDAO {
    @Query("SELECT * FROM account")
    suspend fun getAll(): List<Account>

    @Insert
    suspend fun insertAll(vararg account: Account)

    @Query("SELECT * FROM account WHERE id=0")
    suspend fun getAccount(): Account

    @Query("DELETE FROM account")
    suspend fun deleteAll()

    @Query("UPDATE account SET lastUpdate = :lastUpdate WHERE id = 0")
    suspend fun update(lastUpdate: String)
}