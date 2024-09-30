package com.example.tt1.view

import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tt1.R

open class LoginView(private val activity: AppCompatActivity) {
    private val emailEditText: EditText = activity.findViewById(R.id.email_input)
    private val passwordEditText: EditText = activity.findViewById(R.id.password_input)
    private val loginButton: Button = activity.findViewById(R.id.login_button)

    fun setLoginButtonClickListener(listener: View.OnClickListener) {
        loginButton.setOnClickListener(listener)
    }

    fun getEmail(): String {
        return emailEditText.text.toString()
    }

    fun getPassword(): String {
        return passwordEditText.text.toString()
    }

    fun showError(message: String) {
        activity.runOnUiThread {
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        }
    }

    fun showSuccess(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }
}
