package ba.etf.rma21.projekat.data.models

import com.google.gson.annotations.SerializedName

data class OdgovorenoPitanje (
    @SerializedName("odgovor")var odgovor: Int,
    @SerializedName("pitanje")var idPitanje : Int,
    @SerializedName("bodovi")var bodovi: Int
)