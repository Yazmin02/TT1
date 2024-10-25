package com.example.tt1.view

import android.app.DatePickerDialog
import android.content.Context
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import com.example.tt1.R
import java.util.Calendar

class EventoView(private val rootView: View) {

    private val tituloEditText: EditText = rootView.findViewById(R.id.etTitulo)
    private val descripcionEditText: EditText = rootView.findViewById(R.id.etDescripcion)
    private val fechaEditText: EditText = rootView.findViewById(R.id.et_date)
    private val spinnerCategorias: Spinner = rootView.findViewById(R.id.spinnerEtiqueta)

    fun configurarSpinner(context: Context, categorias: List<String>) {
        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, categorias)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategorias.adapter = adapter
    }

    fun mostrarDatePicker(context: Context) {
        fechaEditText.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(
                context,
                { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                    val fechaSeleccionada = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                    fechaEditText.setText(fechaSeleccionada)
                },
                year, month, day
            )
            datePicker.show()
        }
    }

    fun obtenerTitulo(): String = tituloEditText.text.toString()

    fun obtenerDescripcion(): String = descripcionEditText.text.toString()

    fun obtenerFecha(): String = fechaEditText.text.toString()

    fun obtenerEtiqueta(): Int = spinnerCategorias.selectedItemPosition // Cambia esto según cómo manejes las etiquetas

    fun setOnGuardarEventoListener(listener: () -> Unit) {
        rootView.findViewById<ImageButton>(R.id.btnGuardarTarea).setOnClickListener {
            listener()
        }
    }
    fun obtenerLugar(): String = tituloEditText.text.toString()
}
