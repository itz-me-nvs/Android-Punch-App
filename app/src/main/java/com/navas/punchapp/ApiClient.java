package com.navas.punchapp;

// Retrofit Package instance
import retrofit2.Retrofit;
import retrofit2.Response;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.Call;
import retrofit2.converter.gson.GsonConverterFactory;

// OkHttpClient package instance
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

// Api Serialization Class
// import com.google.gson.annotations.SerializedName;

public class ApiClient {

    public interface doGETModel {
        @GET("exec")
        Call<AppScriptResponse> getData(@Query("value") String val);
    }

    public class results {
        Number lastRowIndex;
        String date;
        String formated;
    }

    public class headers {
        String ContentType;
        String Date;
    }

    public class AppScriptResponse {
        private results result;

        private Number status;
        public headers header;

        public String getStatus() {
            return status.toString();
        }

        public results getResult() {
            return result;
        }
    }

    private static Retrofit retrofit;
    private static final String PUNCH_BASE_URL = "https://script.google.com/macros/s/AKfycbwtQz6idpS0bi3R_ua7lFEgI_D9tksFdodxc0jYE60Z5qyYGGhDA3Dwed6c2Wfg_Ibx/";

    private doGETModel doget;

    ApiClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(PUNCH_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        doget = retrofit.create(doGETModel.class);
    }

    public Call<AppScriptResponse> getUser(String val) {
        Call<AppScriptResponse> call = doget.getData(val);
        return call;
    }
}
