package com.firetv.infinitekitten.api.flickr

import com.firetv.infinitekitten.api.ApiConstants
import com.firetv.infinitekitten.api.flickr.model.PhotoSizesResponse
import com.firetv.infinitekitten.api.flickr.model.SearchResponse
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


interface FlickrApiService {

    @GET("?method=flickr.photos.search")
    fun getPhotos(@Query("api_key") key: String = ApiConstants.FLICKR_API_KEY,
                  @Query("user_id") userId: String = "110705684@N04",
                  @Query("safe_search") safeSearch: Int = 1,
                  @Query("media") media: String = "photos",
                  @Query("content_type") contentType: Int = 1,
                  @Query("text") query: String,
                  @Query("tags") tags: String = "cat,cats,kitten,kittens",
                  @Query("per_page") hitsPerPage: Int = ApiConstants.FLICKR_HITS_PER_PAGE,
                  @Query("page") page: Int,
                  @Query("is_public") isPublic: Int = 1,
                  @Query("format") format: String = "json",
                  @Query("nojsoncallback") noJsonCallback: Int = 1,
                  @Query("group_id") groupId: String = "2918390@N25"): Call<SearchResponse>

    @GET("?method=flickr.photos.getSizes")
    fun getPhotosSizes(@Query("api_key") key: String = ApiConstants.FLICKR_API_KEY,
                       @Query("photo_id") photoId: String,
                       @Query("format") format: String = "json",
                       @Query("nojsoncallback") noJsonCallback: Int = 1): Call<PhotoSizesResponse>

    /**
     * Companion object to create the FlickrApiService
     */
    companion object {

        private fun getUnsafeOkHttpClient(): OkHttpClient {
            try {
                // Create a trust manager that does not validate certificate chains
                val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        return arrayOf()
                    }

                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {
                    }

                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {
                    }
                })


                // Install the all-trusting trust manager
                val sslContext = SSLContext.getInstance("SSL")
                sslContext.init(null, trustAllCerts, java.security.SecureRandom())
                // Create an ssl socket factory with our all-trusting manager


                val builder = OkHttpClient.Builder().sslSocketFactory(sslContext.socketFactory)
                builder.hostnameVerifier { hostname, session -> true }

                return builder.build()
            } catch (e: Exception) {
                throw RuntimeException(e)
            }

        }

        fun create(): FlickrApiService {
            /*  val hostnameVerifier = HostnameVerifier(object : (String, SSLSession) -> Boolean {
                  override fun invoke(hostname: String, session: SSLSession): Boolean {
                      return hostname.equals("api.flickr.com")
                  }
              })

              val httpClient = OkHttpClient.Builder()
                      .hostnameVerifier(hostnameVerifier)
                      .build()*/

            val retrofit = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(ApiConstants.FLICKR_API_URL)
                    .client(getUnsafeOkHttpClient())
                    .build()

            return retrofit.create(FlickrApiService::class.java)
        }
    }
}