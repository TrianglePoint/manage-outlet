package me.manage_outlet

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(){
    val localUrl = "http://192.168.1.4:3000"
    val herokuUrl = "https://get-and-post-as-json.herokuapp.com"
    var url = ""
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var checkFillout = arrayOf(0, 0)

        // First, unable login button.
        btnLogin.isEnabled = false
        editId.addTextChangedListener(object:TextWatcher{

            // Fill out Id.
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                // If did fill out?
                when(editId.text.toString().length){
                    0 -> checkFillout[0] = 0
                    else -> checkFillout[0] = 1
                }

                // If did fill out the all, enable login button.
                when(checkFillout[0] == 1 && checkFillout[1] == 1){
                    true -> btnLogin.isEnabled = true
                    else -> btnLogin.isEnabled = false
                }
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
        })
        editPw.addTextChangedListener(object:TextWatcher{

            // Fill out Password.
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                // If did fill out?
                when(editPw.text.toString().length){
                    0 -> checkFillout[1] = 0
                    else -> checkFillout[1] = 1
                }

                // If did fill out the all, enable login button.
                when(checkFillout[0] == 1 && checkFillout[1] == 1){
                    true -> btnLogin.isEnabled = true
                    else -> btnLogin.isEnabled = false
                }
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
        })

        // Click Login button.
        btnLogin.setOnClickListener{
            if(radioButtonHeroku.isChecked){
                url = herokuUrl
            }else{
                url = localUrl
            }

            // Communicate with Server.
            val result = LoginTask(editId, editPw).execute(url + "/login").get()

            // and when confirmed, run intent of loginWelcome().
            when(result){
                "Login" -> loginWelcome()

                // "" is if none receive the text or fail connect to server.
                "" -> textMsg.text = "What..."
                else -> textMsg.text = result
            }
        }
    }
    fun loginWelcome(){
        textMsg.text = "Welcome"
        val intent = Intent(this, ManageActivity::class.java)

        // it is used to "hi, id" on next activity.
        intent.putExtra("id",editId.text.toString())

        // it is used to communicate with server on next activity.
        intent.putExtra("url",url)
        startActivity(intent)
    }
}

