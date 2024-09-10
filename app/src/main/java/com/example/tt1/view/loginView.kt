package com.example.tt1.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tt1.R
import com.example.tt1.controller.LoginController

class LoginView : AppCompatActivity() {

    private lateinit var loginController: LoginController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginController = LoginController(this)
    }
}
