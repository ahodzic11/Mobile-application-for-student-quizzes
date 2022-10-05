package ba.etf.rma21.projekat.data.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

data class KvizTaken(
    @SerializedName("id") val id: Int,
    @SerializedName("student") val student: String,
    @SerializedName("osvojeniBodovi") val osvojeniBodovi: Int,
    @SerializedName("datumRada") val datumRada: Date,
    @SerializedName("KvizId") val KvizId: Int
): Serializable{}