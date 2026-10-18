package edu.sv.ues.dam235.apirestclient.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    // ⚠️ IMPORTANTE:
    //   - Si usas EMULADOR: usa 10.0.2.2 (apunta al localhost de tu PC)
    //   - Si usas DISPOSITIVO FÍSICO: usa la IP de tu PC en la red WiFi,
    //     por ejemplo: "http://192.168.1.100:8085/api/v1/demoapirestdam235/"
    //   - La URL SIEMPRE debe terminar en "/"
    private static final String BASE_URL = "http://10.0.2.2:8085/api/v1/demoapirestdam235/";

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {

            // Interceptor para ver en Logcat las peticiones y respuestas
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}