package edu.sv.ues.dam235.apirestclient.network;

import java.util.List;

import edu.sv.ues.dam235.apirestclient.models.LoginRequest;
import edu.sv.ues.dam235.apirestclient.models.Product;
import edu.sv.ues.dam235.apirestclient.models.RegisterRequest;
import edu.sv.ues.dam235.apirestclient.models.TokenResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    // ============ AUTH ============

    // POST /auth/login
    @POST("auth/login")
    Call<TokenResponse> login(@Body LoginRequest loginRequest);

    // POST /auth/register
    @POST("auth/register")
    Call<TokenResponse> register(@Body RegisterRequest registerRequest);

    // POST /auth/logout  (requiere token en el header)
    @POST("auth/logout")
    Call<TokenResponse> logout(@Header("Authorization") String token);

    // ============ PRODUCTS (CRUD) ============

    // GET /products  (listar todos, requiere token)
    @GET("products")
    Call<List<Product>> getProducts(@Header("Authorization") String token);

    // GET /products/{code}  (obtener uno, requiere token)
    @GET("products/{code}")
    Call<Product> getProductById(@Header("Authorization") String token,
                                 @Path("code") Integer code);

    // POST /products  (crear, requiere token)
    @POST("products")
    Call<Product> createProduct(@Header("Authorization") String token,
                                @Body Product product);

    // PUT /products/{code}  (actualizar, requiere token)
    @PUT("products/{code}")
    Call<Product> updateProduct(@Header("Authorization") String token,
                                @Path("code") Integer code,
                                @Body Product product);

    // DELETE /products/{code}  (eliminar, requiere token)
    @DELETE("products/{code}")
    Call<Void> deleteProduct(@Header("Authorization") String token,
                             @Path("code") Integer code);
}