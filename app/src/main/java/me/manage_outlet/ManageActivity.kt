package me.manage_outlet

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_manage.*

class ManageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage)

        if(intent.hasExtra("Id")) {
            textViewId.text = "Hi, " + intent.getStringExtra("Id")
        }
    }
}