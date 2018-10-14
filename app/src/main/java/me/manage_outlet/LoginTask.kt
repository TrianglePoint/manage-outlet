package me.manage_outlet

import android.os.AsyncTask
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import org.json.JSONObject
import java.io.*
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class LoginTask(val edtId: EditText, val edtPw: EditText, val textMsg: TextView, val mainActivity: MainActivity) : AsyncTask<String, Void, String>(){
    override fun doInBackground(vararg url: String?): String {

        val url = URL(url[0])
        val httpClient = url.openConnection() as HttpURLConnection
        var result = ""
        try {
            //Configure
            httpClient.requestMethod = "POST"
            httpClient.setRequestProperty("Cache-Control", "no-cache")
            httpClient.setRequestProperty("Content-Type", "application/json")
            httpClient.setRequestProperty("Accept", "text/html")
            httpClient.doOutput = true

            Log.d("check", "Try connect to Server...")
            httpClient.connect()

            //httpClient.outputStream is mean "Create Stream".
            writeStream(outputStream = httpClient.outputStream)
            result = readStream(inputStream = httpClient.inputStream)

            Log.d("check","Try outputStream.close() .")
        }catch (e: Exception){
            e.printStackTrace()
            Log.d("check", "EXCEPTION : " + e.printStackTrace().toString())
        }finally {
            httpClient.disconnect()
            Log.d("check", "Disconnected on Server.")
        }
        return result
    }
    fun readStream(inputStream: InputStream): String{
        val bufferedReader: BufferedReader = BufferedReader(InputStreamReader(inputStream))
        val stringBuffer = StringBuffer()
        var responeResult : String? = ""

        while(true) {
            responeResult = bufferedReader.readLine()
            if(responeResult != null){
                stringBuffer.append(responeResult)
            }else{
                break
            }
        }
        return stringBuffer.toString()
    }
    fun writeStream(outputStream: OutputStream){
        Log.d("check", "Entered in writeStream().")
        val jsonObject = JSONObject()
        jsonObject.put("id",edtId.text.toString())
        jsonObject.put("password", edtPw.text.toString())
        val bufferedWriter = BufferedWriter(OutputStreamWriter(outputStream))
        Log.d("check","Try send the " + jsonObject.toString() + "to Server...")
        bufferedWriter.write(jsonObject.toString())
        Log.d("check","Try outputStream.flush() .")
        bufferedWriter.flush()
        bufferedWriter.close()
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        Log.d("check","result: " + result)
    }
}