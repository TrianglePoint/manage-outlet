package me.manage_outlet

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.transition.Slide
import android.transition.TransitionManager
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.*
import kotlinx.android.synthetic.main.activity_manage.*
import org.json.JSONArray
import org.json.JSONObject

class ManageActivity : AppCompatActivity() {
    var url = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage)
        if(intent.hasExtra("url")){
            url = intent.getStringExtra("url")
        }

        loadScreen(tlayout)

        if(intent.hasExtra("id")) {
            textViewId.text = "Hi, " + intent.getStringExtra("id")
        }
    }
    fun loadScreen(tlayout: TableLayout){
        var reRun = false

        val result = LoadTask().execute(url + "/load").get()
        val jsonObject = JSONObject(result)

        val jsonArray = jsonObject.get("outlet")

        tlayout.removeAllViews()

        var tableRow = TableRow(this)
        var rowArray = arrayOf(TextView(this),TextView(this),TextView(this),TextView(this))
        rowArray[0].text = "Name"
        rowArray[1].text = "min"
        rowArray[2].text = "MAX"
        rowArray[3].text = "Power"
        tableRow.addView(rowArray[0])
        tableRow.addView(rowArray[1])
        tableRow.addView(rowArray[2])
        tableRow.addView(rowArray[3])

        val setTextSize = 18F
        val setheight = 150
        var layoutParams = TableRow.LayoutParams(0,TableRow.LayoutParams.WRAP_CONTENT).apply {
            weight = 3F
            height = setheight - 70
        }
        rowArray[0].layoutParams = layoutParams
        rowArray[0].textSize = setTextSize
        rowArray[0].typeface = (Typeface.DEFAULT_BOLD)

        layoutParams = TableRow.LayoutParams(0,TableRow.LayoutParams.MATCH_PARENT).apply {
            weight = 1F
            height = setheight - 70
        }
        rowArray[1].layoutParams = layoutParams
        rowArray[1].textSize = setTextSize
        rowArray[1].typeface = (Typeface.DEFAULT_BOLD)
        rowArray[2].layoutParams = layoutParams
        rowArray[2].textSize = setTextSize
        rowArray[2].typeface = (Typeface.DEFAULT_BOLD)
        rowArray[3].layoutParams = layoutParams
        rowArray[3].textSize = setTextSize
        rowArray[3].typeface = (Typeface.DEFAULT_BOLD)

        tlayout.addView(tableRow)

        if(jsonArray is JSONArray) {
            Log.d("check", "Length of json : " + jsonArray.length().toString())

            for(i in 0..(jsonArray.length()-1)){
                tableRow = TableRow(this)
                rowArray = arrayOf(Button(this), TextView(this), TextView(this))
                val switch = Switch(this)
                rowArray[0].text = jsonArray.getJSONObject(i).getString("name")
                rowArray[1].text = jsonArray.getJSONObject(i).getString("min")
                rowArray[2].text = jsonArray.getJSONObject(i).getString("max")
                switch.isChecked = jsonArray.getJSONObject(i).getBoolean("power")
                tableRow.addView(rowArray[0])
                tableRow.addView(rowArray[1])
                tableRow.addView(rowArray[2])
                tableRow.addView(switch)

                // Design element of Outlet.
                var layoutParams = TableRow.LayoutParams(0,TableRow.LayoutParams.WRAP_CONTENT).apply {
                    weight = 3F
                    height = setheight
                    rightMargin = 50
                }
                rowArray[0].layoutParams = layoutParams
                rowArray[0].textSize = setTextSize
                rowArray[0].setOnClickListener{
                    Log.d("check", "Set the temperature of the " + jsonArray.getJSONObject(i).getString("name"))
                    val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    val view = inflater.inflate(R.layout.popup_set_temperature, null)
                    Log.d("check", "Init popupWindow.")
                    val popupWindow = PopupWindow(
                            view,
                            800,
                            700
                    )
                    popupWindow.isFocusable = true
                    popupWindow.update()
                    Log.d("check", "Configure like Build.VERSION.")
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                        popupWindow.elevation = 10.0F
                    }

                    // If API 23 or higher.
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        val slideIn = Slide()
                        slideIn.slideEdge = Gravity.TOP
                        popupWindow.enterTransition = slideIn

                        val slideOut = Slide()
                        slideOut.slideEdge = Gravity.RIGHT
                        popupWindow.exitTransition = slideOut
                    }
                    Log.d("check", "init setOnClickListener of btnTemp...")
                    val btnTemperature = view.findViewById<Button>(R.id.btnTemperature)
                    btnTemperature.isEnabled = false
                    val editTemp = arrayOf(view.findViewById<EditText>(R.id.editMin), view.findViewById<EditText>(R.id.editMax))
                    editTemp[0].hint = jsonArray.getJSONObject(i).getString("min")
                    editTemp[1].hint = jsonArray.getJSONObject(i).getString("max")
                    var checkFillout = arrayOf(0, 0)
                    editTemp[0].addTextChangedListener(object : TextWatcher{
                        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                            when(editTemp[0].text.toString().length){
                                0 -> checkFillout[0] = 0
                                else -> checkFillout[0] = 1
                            }
                            when(checkFillout[0] == 1 || checkFillout[1] == 1){
                                true -> btnTemperature.isEnabled = true
                                else -> btnTemperature.isEnabled = false
                            }
                        }
                        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                        override fun afterTextChanged(p0: Editable?) {}
                    })
                    editTemp[1].addTextChangedListener(object : TextWatcher{
                        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                            when(editTemp[1].text.toString().length){
                                0 -> checkFillout[1] = 0
                                else -> checkFillout[1] = 1
                            }
                            when(checkFillout[0] == 1 || checkFillout[1] == 1){
                                true -> btnTemperature.isEnabled = true
                                else -> btnTemperature.isEnabled = false
                            }
                        }
                        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                        override fun afterTextChanged(p0: Editable?) {}
                    })
                    var didClick = false
                    var minTemp = ""
                    var maxTemp = ""
                    btnTemperature.setOnClickListener {
                        minTemp = editTemp[0].text.toString()
                        maxTemp = editTemp[1].text.toString()

                        when(minTemp.toIntOrNull() is Int){
                            false -> minTemp = jsonArray.getJSONObject(i).getString("min")
                        }
                        when(maxTemp.toIntOrNull() is Int){
                            false -> maxTemp = jsonArray.getJSONObject(i).getString("max")
                        }
                        if(minTemp.toInt() > 100){
                            minTemp = "100"
                        }
                        if(maxTemp.toInt() > 100){
                            maxTemp = "100"
                        }

                        // min is higher than max!
                        if(minTemp.toInt() > maxTemp.toInt()) {
                            Toast.makeText(applicationContext, "min is higher than max", Toast.LENGTH_SHORT).show()
                            editTemp[0].setText("")
                            editTemp[1].setText("")
                        }else{
                            var result = ""
                            Log.d("check", "Submit the data (" + minTemp + ", " + maxTemp + ") to " +
                                    jsonArray.getJSONObject(i).getString("name") + " in server.")
                            result = TemperatureTask(jsonArray.getJSONObject(i).getString("name"), minTemp, maxTemp).execute(url + "/temperature").get()
                            when(result){
                                "Temperature" -> didClick = true
                                "Error" -> Toast.makeText(applicationContext, result, Toast.LENGTH_SHORT).show()
                                else -> Toast.makeText(applicationContext,"What...", Toast.LENGTH_SHORT).show()
                            }
                            popupWindow.dismiss()
                        }
                    }
                    Log.d("check", "init setOnDismissListener.")
                    popupWindow.setOnDismissListener {
                        popupWindow.isFocusable = false
                        popupWindow.update()
                        if(didClick) {
                            Toast.makeText(applicationContext, "Submitted", Toast.LENGTH_SHORT).show()
                            refresh()
                        }
                    }

                    TransitionManager.beginDelayedTransition(layoutManage)
                    popupWindow.showAtLocation(
                            layoutManage,
                            Gravity.CENTER,
                            0,0
                    )
                }

                layoutParams = TableRow.LayoutParams(0,TableRow.LayoutParams.MATCH_PARENT).apply {
                    weight = 1F
                    height = setheight
                }
                rowArray[1].layoutParams = layoutParams
                rowArray[1].textSize = setTextSize
                rowArray[2].layoutParams = layoutParams
                rowArray[2].textSize = setTextSize
                switch.layoutParams = layoutParams
                switch.setOnCheckedChangeListener{buttonView, isChecked ->
                    var result = ""
                    if(isChecked){
                        Log.d("check", "Power on the " + jsonArray.getJSONObject(i).getString("name"))
                        result = PowerTask(jsonArray.getJSONObject(i).getString("name"), true).execute(url + "/power").get()
                        when(result){
                            "Power" -> reRun = true
                            "Error" -> Toast.makeText(applicationContext, result, Toast.LENGTH_SHORT).show()
                            else -> Toast.makeText(applicationContext,"What...", Toast.LENGTH_SHORT).show()
                        }
                        if(reRun){
                            Toast.makeText(applicationContext, "Power on", Toast.LENGTH_SHORT).show()
                            refresh()
                        }
                    }else{
                        Log.d("check", "Power off the " + jsonArray.getJSONObject(i).getString("name"))
                        result = PowerTask(jsonArray.getJSONObject(i).getString("name"), false).execute(url + "/power").get()
                        when(result){
                            "Power" -> reRun = true
                            "Error" -> Toast.makeText(applicationContext, result, Toast.LENGTH_SHORT).show()
                            else -> Toast.makeText(applicationContext,"What...", Toast.LENGTH_SHORT).show()
                        }
                        if(reRun){
                            Toast.makeText(applicationContext, "Power off", Toast.LENGTH_SHORT).show()
                            refresh()
                        }
                    }
                }
                tlayout.addView(tableRow)
            }
        }
    }
    fun refresh(){
        val intent = intent
        finish()
        startActivity(intent)
    }
}