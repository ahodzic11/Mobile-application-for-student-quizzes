package ba.etf.rma21.projekat

import ba.etf.rma21.projekat.data.models.*
import ba.etf.rma21.projekat.data.repositories.ProbniResponse
import retrofit2.Response
import retrofit2.http.*

interface Api {

    @GET("/kviz/{id}/pitanja")
    suspend fun getPit(@Path("id") idKviza: Int): Response<List<PitanjeAPI>>

    @GET("/kviz")
    suspend fun getKv(): Response<List<Kviz>>

    @GET("/kviz/{id}")
    suspend fun getKvizById(@Path("id") idKviza: Int): Response<Kviz>

    @GET("/kviz/{id}/grupa")
    suspend fun getGrupeZaKviz(@Path("id") idKviza: Int): Response<List<Grupa>>

    @GET("/predmet")
    suspend fun getPredmete(): Response<List<Predmet>>

    @GET("/grupa")
    suspend fun getGrupe(): Response<List<Grupa>>

    @GET("/grupa/{id}")
    suspend fun getGrupu(@Path("id") idGrupe: Int): Response<Grupa>

    @GET("/predmet/{id}/grupa")
    suspend fun getGrupePredmeta(@Path("id") idPredmeta: Int): Response<List<Grupa>>

    @POST("/grupa/{gid}/student/{id}")
    suspend fun upisiGrupu(@Path("gid") idGrupe: Int, @Path("id") hashStudenta: String): Response<ProbniResponse>

    @GET("/student/{id}")
    suspend fun getAccount(@Path("id") idStudenta: String): Response<Account>

    @DELETE("/student/{id}/upisugrupeipokusaji")
    suspend fun obrisiPodatke(@Path("id") idStudenta: String): Response<String>

    @GET("/student/{id}/kviztaken")
    suspend fun getPokusajeStudenta(@Path("id") idStudenta: String): Response<List<KvizTaken>>

    @POST("/student/{id}/kviz/{kid}")
    suspend fun odgovoriNaKviz(@Path("id") idStudenta:String, @Path("kid") idKviza: Int): Response<KvizTaken>

    @GET("/student/{id}/grupa")
    suspend fun getUpisaneGrupe(@Path("id") idStudenta: String): Response<List<Grupa>>

    @GET("/grupa/{id}/kvizovi")
    suspend fun getKvizoveZaGrupu(@Path("id") idGrupe: Int): Response<List<Kviz>>

    @POST("/student/{id}/kviztaken/{kid}/odgovor")
    suspend fun postaviOdgovor(@Path("id") hash: String, @Path("kid") idKvizTaken: Int, @Body odgovor: OdgovorenoPitanje)

    @GET("/student/{id}/kviztaken/{kid}/odgovori")
    suspend fun getOdgovore(@Path("id") hash: String, @Path("kid") idKvizTaken: Int): Response<List<Odgovor>>

    @GET("/account/{id}/lastUpdate?=date")
    suspend fun getPromjene(@Path("id") id:String, @Query("date") date:String): Response<OdgovorNaUpit>
}