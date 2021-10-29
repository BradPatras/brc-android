package io.github.bradpatras.basicremoteconfigs

import java.io.BufferedReader
import java.io.IOException
import java.net.URL
import java.io.InputStream
import javax.net.ssl.HttpsURLConnection

class HttpHelper(private val url: URL) {

    fun makeGetRequest(): String? {
        val inputStream: InputStream
        var result: String? = null

        try {
            // Create HttpURLConnection
            val conn: HttpsURLConnection = url.openConnection() as HttpsURLConnection

            // Launch GET request
            conn.connect()

            // Receive response as inputStream
            inputStream = conn.inputStream

            result = inputStream?.bufferedReader()?.use(BufferedReader::readText)
        } catch (exception: IOException) {
            print("Error when executing get request: " + exception.localizedMessage)
        } catch (err: Error) {
            print("Error when executing get request: " + err.localizedMessage)
        }

        return result
    }

    //        var connection: HttpURLConnection? = null
//        var reader: BufferedReader? = null
//        var responseJson = ""
//
//        // Opening a new connection
//
//        connection = url.openConnection() as HttpURLConnection
//        connection.requestMethod = "GET"
//        connection.connect()
//
//        // Getting the result back
//        val stream = connection.inputStream ?: return@withContext null
//        reader = BufferedReader(InputStreamReader(stream))
//        val buffer = StringBuffer()
//        var line: String
//        while (reader.readLine().also { line = it } != null) {
//            buffer.append("\n $line \n")
//        }
//        if (buffer.isEmpty()) {
//            // response was empty
//            // no point in parsing
//            return@withContext null
//        }
//
//        return@withContext buffer.toString()

//            } catch (e: MalformedURLException) {
//                e.printStackTrace()
//                return@async null
//            } catch (e: ProtocolException) {
//                e.printStackTrace()
//            } catch (e: IOException) {
//                e.printStackTrace()
//                return@async null
//            } finally {
//                connection?.disconnect()
//                if (reader != null) {
//                    try {
//                        reader.close()
//                    } catch (e: IOException) {
//                        e.printStackTrace()
//                    }
//                }
//            }

            //JsonParser parser = new JsonParser(mContext, responseJson);
            //ArrayList<PostModel> postModelArrayList = parser.parse();
//            return@async responseJson
//        }
//
//        return
//    }
}