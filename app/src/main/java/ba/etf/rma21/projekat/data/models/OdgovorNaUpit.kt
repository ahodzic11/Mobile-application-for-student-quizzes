package ba.etf.rma21.projekat.data.models

import com.google.gson.annotations.SerializedName

data class OdgovorNaUpit (
    @SerializedName("message") var message: String,
    @SerializedName("changed") var changed: Boolean
)