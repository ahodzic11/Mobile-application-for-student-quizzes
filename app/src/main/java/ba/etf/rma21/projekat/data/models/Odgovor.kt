package ba.etf.rma21.projekat.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "odgovor")
data class Odgovor (
    @PrimaryKey @ColumnInfo(name = "id") @SerializedName("id") val id: Int,
    @ColumnInfo(name = "odgovoreno") @SerializedName("odgovoreno") val odgovoreno: Int,
    @ColumnInfo(name = "PitanjeId") @SerializedName("PitanjeId") val pitanjeId: Int,
    @ColumnInfo(name = "KvizId") @SerializedName("KvizId") var kvizId: Int,
    @ColumnInfo(name = "KvizTakenId") @SerializedName("KvizTakenId") val kvizTakenId: Int
)