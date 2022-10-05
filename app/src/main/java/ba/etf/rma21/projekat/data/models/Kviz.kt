package ba.etf.rma21.projekat.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(tableName = "kviz")
data class Kviz(
    @PrimaryKey @SerializedName("id") val id: Int,
    @ColumnInfo(name = "naziv") @SerializedName("naziv") val naziv: String,
    @ColumnInfo(name = "nazivPredmeta") @SerializedName("nazivPredmeta") var nazivPredmeta: String?,
    @ColumnInfo(name = "datumPocetka") @SerializedName("datumPocetak") val datumPocetka: String?,
    @ColumnInfo(name = "datumKraj") @SerializedName("datumKraj") val datumKraj: String?,
    @ColumnInfo(name = "datumRada") @SerializedName("datumRada") var datumRada: String?,
    @ColumnInfo(name = "trajanje") @SerializedName("trajanje") val trajanje: Int,
    @ColumnInfo(name = "nazivGrupe") @SerializedName("nazivGrupe") val nazivGrupe: String?,
    @ColumnInfo(name = "predan") @SerializedName("predan") val predan: Boolean,
    @ColumnInfo(name = "osvojeniBodovi") @SerializedName("osvojeniBodovi") var osvojeniBodovi: Float?,
    @ColumnInfo(name = "kvizTakenId") @SerializedName("kvizTakenId") var kvizTakenId: Int
)

