package me.manage_outlet

import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.*
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnLogin.setOnClickListener{LoginTask(editId).execute()}
    }
}

class LoginTask(val edt: EditText) : AsyncTask<String, Void, String>(){
    override fun doInBackground(vararg p0: String?): String {
        val url = URL("https://get-and-post-as-json.herokuapp.com/")
        val httpClient = url.openConnection() as HttpURLConnection
        var result = ""
        try {
            //Configure
            httpClient.requestMethod = "POST"
            httpClient.setRequestProperty("Cache-Control", "no-cache")
            httpClient.setRequestProperty("Content-Type", "application/json")
            httpClient.setRequestProperty("Accept", "text/html")
//            httpClient.doInput = true
            httpClient.doOutput = true

            Log.d("check", "Try connect to Server...")
            httpClient.connect()

            //httpClient.outputStream is mean "Create Stream".
            writeStream(outputStream = httpClient.outputStream)
            result = readStream(inputStream = httpClient.inputStream)
//            httpClient.responseCode

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
        jsonObject.put("id",edt.text.toString())
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
        edt.setText(result)
    }
}