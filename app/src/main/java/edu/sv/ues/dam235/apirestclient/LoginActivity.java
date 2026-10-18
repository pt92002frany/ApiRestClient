package edu.sv.ues.dam235.apirestclient;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import edu.sv.ues.dam235.apirestclient.models.LoginRequest;
import edu.sv.ues.dam235.apirestclient.models.TokenResponse;
import edu.sv.ues.dam235.apirestclient.network.ApiService;
import edu.sv.ues.dam235.apirestclient.network.RetrofitClient;
import edu.sv.ues.dam235.apirestclient.utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    // 1. Declaramos las variables para los componentes de la pantalla
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private ProgressBar progressBar;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // Vinculamos el layout XML

        // 2. Enlazamos las variables con los IDs del XML
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        progressBar = findViewById(R.id.progressBar);
        sessionManager = new SessionManager(this);

        // 3. Si ya hay un token guardado, saltamos directo a los productos
        if (sessionManager.isLoggedIn()) {
            goToProducts();
            return;
        }

        // 4. Evento clic del botón Login
        btnLogin.setOnClickListener(v -> doLogin());
    }

    private void doLogin() {
        String email = etEmail.getText().toString().trim();
        String pass = etPassword.getText().toString().trim();

        if (email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        btnLogin.setEnabled(false);

        // 5. Creamos el servicio de Retrofit
        ApiService api = RetrofitClient.getClient().create(ApiService.class);

        // 6. Ejecutamos la petición asíncrona al backend
        api.login(new LoginRequest(email, pass)).enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                progressBar.setVisibility(View.GONE);
                btnLogin.setEnabled(true);

                if (response.isSuccessful() && response.body() != null) {
                    TokenResponse tr = response.body();

                    // Si el backend nos devuelve un token válido
                    if (tr.getToken() != null) {
                        sessionManager.saveToken(tr.getToken()); // Guardamos el JWT
                        Toast.makeText(LoginActivity.this, "Bienvenido", Toast.LENGTH_SHORT).show();
                        goToProducts(); // Navegamos
                    } else {
                        // Credenciales incorrectas (el backend devolvió msj de error)
                        Toast.makeText(LoginActivity.this,
                                "Credenciales incorrectas: " + tr.getMsj(),
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this,
                            "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                btnLogin.setEnabled(true);
                Toast.makeText(LoginActivity.this,
                        "Fallo de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void goToProducts() {
        startActivity(new Intent(LoginActivity.this, ProductsActivity.class));
        finish(); // Cerramos el login para que no se pueda volver atrás con el botón físico
    }
}