package uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.service

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import okio.source
import uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.data.FileData
import okio.buffer
import okio.source
import uk.gov.justice.digital.hmpps.hmppsadjudicationsinsightsapi.data.FileDataAll
import java.io.InputStream



class ChartService {

  //s3 facade
  //db

  /*
  {"incident_prison":"SKI","discovery_month":6,"discovery_year_curr":2022,"count_curr":16,"discovery_year_prev":2021,"count_prev":38}

   */

  //get chart 1
  @OptIn(ExperimentalStdlibApi::class)
  fun getChart(agencyId: String): FileData{



    //1 get file from s3.

    //2 pass data for agency etc

    // return data
    val moshi: Moshi = Moshi.Builder()
      .addLast(KotlinJsonAdapterFactory())
      .build()

    val jsonAdapter: JsonAdapter<List<FileData>> = moshi.adapter()

    val json: List<FileData>? = jsonAdapter.fromJson(
      "[{\"incident_prison\":\"SKI\",\"discovery_month\":6,\"discovery_year_curr\":2022,\"count_curr\":16,\"discovery_year_prev\":2021,\"count_prev\":38}]"
    )

    val t = json ?: throw RuntimeException("not set")

    //println(json.incident_prison)



    return json.first()

  }

  suspend inline fun <reified T> readToFlow(input: InputStream, adapter: JsonAdapter<T>): Flow<T> = flow {
    JsonReader.of(input.source().buffer())
      .readArray {
        emit(adapter.fromJson(this)!!)
      }
  }

  inline fun JsonReader.readArray(body: JsonReader.() -> Unit) {
    beginArray()
    while (hasNext()) {
      body()
    }
    endArray()
  }

  @OptIn(ExperimentalStdlibApi::class)
  suspend fun getChart2(agencyId: String): FileData? {
    val moshi: Moshi = Moshi.Builder()
      .addLast(KotlinJsonAdapterFactory())
      .build()

    val jsonAdapter: JsonAdapter<FileData> = moshi.adapter()


     return readToFlow( "[{\"incident_prison\":\"SKI\",\"discovery_month\":6,\"discovery_year_curr\":2022,\"count_curr\":16,\"discovery_year_prev\":2021,\"count_prev\":38}]"
        .byteInputStream(), jsonAdapter).firstOrNull { it.incident_prison == agencyId }
  }

}