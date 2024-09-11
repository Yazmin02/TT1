package com.example.tt1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tt1.controller.LoginController
import com.example.tt1.model.LoginModel
import com.example.tt1.view.LoginView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginModel = LoginModel()
        val loginView = LoginView(this)
        val loginController = LoginController(loginModel, loginView)

        loginController.setOnLoginSuccessListener {
            // Redirigir a la pantalla principal
            val intent = Intent(this, PrincipalActivity::class.java)
            startActivity(intent)
            finish()  // Opcional: Terminar la actividad de login
        }
    }
}
