package com.example.tt1.view

import android.app.Activity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.tt1.R

class LoginView(private val activity: Activity) {

    private val emailInput: EditText = activity.findViewById(R.id.email_input)
    private val passwordInput: EditText = activity.findViewById(R.id.password_input)
    private val loginButton: Button = activity.findViewById(R.id.login_button)

    fun getEmail(): String {
        return emailInput.text.toString()
    }

    fun getPassword(): String {
        return passwordInput.text.toString()
    }

    fun setLoginButtonClickListener(listener: View.OnClickListener) {
        loginButton.setOnClickListener(listener)
    }

    fun showError(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    fun showSuccess(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }
}
