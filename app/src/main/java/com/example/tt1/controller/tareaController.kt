package com.example.tt1.controller

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tt1.R
import com.example.tt1.model.TareaModel
import com.example.tt1.view.TareaView

class AgregarTareaController : AppCompatActivity() {

    private lateinit var tareaView: TareaView<Any?>
    private val tareaModel = TareaModel()

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tarea)

        // Inicializa la vista
        tareaView = TareaView(findViewById(android.R.id.content))

        // Configura el Spinner de etiquetas
        tareaView.configurarSpinner(this, tareaModel.categorias)

        // Muestra el DatePicker cuando el campo de fecha es clicado
        tareaView.mostrarDatePicker(this)
    }

    fun guardarTarea() {
        // Lógica para guardar la tarea
    }
}
