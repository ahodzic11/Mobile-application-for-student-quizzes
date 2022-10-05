package ba.etf.rma21.projekat.data.repositories

import com.google.gson.annotations.SerializedName

data class ProbniResponse (
    @SerializedName("message") val poruka: String
)