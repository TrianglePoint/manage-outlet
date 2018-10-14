package me.manage_outlet

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(){
//    val localUrl = "https://a8274755.ngrok.io"
    val herokuUrl = "https://get-and-post-as-json.herokuapp.com"
    val url = herokuUrl
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var checkFillout = arrayOf(0, 0)
        btnLogin.isEnabled = false
        editId.addTextChangedListener(object:TextWatcher{
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                when(editId.text.toString().length){
                    0 -> checkFillout[0] = 0
                    else -> checkFillout[0] = 1
                }
                when(checkFillout[0] == 1 && checkFillout[1] == 1){
                    true -> btnLogin.isEnabled = true
                    else -> btnLogin.isEnabled = false
                }
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
        })
        editPw.addTextChangedListener(object:TextWatcher{
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                when(editPw.text.toString().length){
                    0 -> checkFillout[1] = 0
                    else -> checkFillout[1] = 1
                }
                when(checkFillout[0] == 1 && checkFillout[1] == 1){
                    true -> btnLogin.isEnabled = true
                    else -> btnLogin.isEnabled = false
                }
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
        })
        btnLogin.setOnClickListener{
            val result = LoginTask(editId, editPw).execute(url + "/login").get()
            when(result){
                "Login" -> loginWelcome()
                "Wrong id" -> textMsg.text = result
                "Wrong password" -> textMsg.text = result
                else -> textMsg.text = "What..."
            }
        }
    }
    fun loginWelcome(){
        textMsg.text = "Welcome"
        val intent = Intent(this, ManageActivity::class.java)
        intent.putExtra("id",editId.text.toString())
        intent.putExtra("url",url)
        startActivity(intent)
    }
}

