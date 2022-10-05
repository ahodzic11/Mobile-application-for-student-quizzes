package ba.etf.rma21.projekat.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "pitanje")
data class Pitanje(
    @PrimaryKey @SerializedName("id") val id: Int,
    @ColumnInfo(name = "naziv") @SerializedName("naziv") val naziv: String,
    @ColumnInfo(name = "tekstPitanja") @SerializedName("tekstPitanja") val tekstPitanja: String,
    @ColumnInfo(name = "opcije") @SerializedName("opcije") val opcije: String,
    @ColumnInfo(name = "tacan") @SerializedName("tacan") val tacan: Int
)