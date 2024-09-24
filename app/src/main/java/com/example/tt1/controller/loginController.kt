import android.view.View
import com.example.tt1.model.LoginModel
import com.example.tt1.view.LoginView
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import java.io.IOException

class LoginController(
    private val model: LoginModel,
    private val view: LoginView
) {

    init {
        view.setLoginButtonClickListener(View.OnClickListener {
            handleLogin()
        })
    }

    private fun handleLogin() {
        // Obtener los valores de los campos de email y contraseña desde la vista
        model.email = view.getEmail()
        model.password = view.getPassword()

        // Validar si los campos están vacíos
        if (model.email.isEmpty()) {
            view.showError("El campo de correo no puede estar vacío.")
            return
        }

        if (model.password.isEmpty()) {
            view.showError("El campo de contraseña no puede estar vacío.")
            return
        }

        // Crear un cliente HTTP para enviar la solicitud a la API
        val client = OkHttpClient()
        val url = "http://localhost:5000/login" // Cambia esto según tu configuración

        val json = JSONObject()
        json.put("email", model.email)
        json.put("password", model.password)

        val body = RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), json.toString())
        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        // Enviar la solicitud
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Manejar error de red
                view.showError("Error de conexión")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    // Leer la respuesta del servidor
                    val responseBody = response.body?.string()
                    val jsonResponse = JSONObject(responseBody)

                    // Mostrar el éxito o manejar el idUsuario si es necesario
                    view.showSuccess(jsonResponse.getString("message"))
                } else {
                    view.showError("Correo o contraseña incorrectos")
                }
            }
        })
    }
}
