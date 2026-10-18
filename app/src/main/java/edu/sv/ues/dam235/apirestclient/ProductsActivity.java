package edu.sv.ues.dam235.apirestclient;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.sv.ues.dam235.apirestclient.adapters.ProductAdapter;
import edu.sv.ues.dam235.apirestclient.models.Product;
import edu.sv.ues.dam235.apirestclient.models.TokenResponse;
import edu.sv.ues.dam235.apirestclient.network.ApiService;
import edu.sv.ues.dam235.apirestclient.network.RetrofitClient;
import edu.sv.ues.dam235.apirestclient.utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsActivity extends AppCompatActivity {

    private RecyclerView recyclerProducts;
    private ProgressBar progressBar;
    private Button btnLogout;
    private SessionManager sessionManager;
    private ApiService api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        // 1. Enlazamos componentes
        recyclerProducts = findViewById(R.id.recyclerProducts);
        progressBar = findViewById(R.id.progressBarProducts);
        btnLogout = findViewById(R.id.btnLogout);

        sessionManager = new SessionManager(this);
        api = RetrofitClient.getClient().create(ApiService.class);

        // 2. Configuramos el RecyclerView
        recyclerProducts.setLayoutManager(new LinearLayoutManager(this));

        // 3. Cargamos los datos
        loadProducts();

        // 4. Evento del botón Logout
        btnLogout.setOnClickListener(v -> doLogout());
    }

    private void loadProducts() {
        progressBar.setVisibility(View.VISIBLE);

        // Obtenemos el token y le agregamos el prefijo "Bearer " que exige Spring Security
        String token = "Bearer " + sessionManager.getToken();

        api.getProducts(token).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    // Si todo va bien, creamos el adaptador y se lo pasamos al RecyclerView
                    ProductAdapter adapter = new ProductAdapter(response.body());
                    recyclerProducts.setAdapter(adapter);
                } else if (response.code() == 401) {
                    // El token expiró o es inválido
                    Toast.makeText(ProductsActivity.this,
                            "Sesión expirada. Inicie sesión de nuevo", Toast.LENGTH_LONG).show();
                    sessionManager.clear();
                    startActivity(new Intent(ProductsActivity.this, LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(ProductsActivity.this,
                            "Sin productos o error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ProductsActivity.this,
                        "Error de red: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void doLogout() {
        String token = "Bearer " + sessionManager.getToken();

        // Llamamos al endpoint de logout (para que el servidor invalide el token)
        api.logout(token).enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                // Borramos el token localmente y volvemos al Login
                sessionManager.clear();
                Toast.makeText(ProductsActivity.this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ProductsActivity.this, LoginActivity.class));
                finish();
            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
                // Aunque falle en el servidor (ej. sin internet), limpiamos localmente por seguridad
                sessionManager.clear();
                startActivity(new Intent(ProductsActivity.this, LoginActivity.class));
                finish();
            }
        });
    }
}