package ba.etf.rma21.projekat.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


data class PitanjeAPI(
    @SerializedName("id") val id: Int,
    @SerializedName("naziv") val naziv: String,
    @SerializedName("tekstPitanja") val tekstPitanja: String,
    @SerializedName("opcije") val opcije: List<String>,
    @SerializedName("tacan") val tacan: Int
)