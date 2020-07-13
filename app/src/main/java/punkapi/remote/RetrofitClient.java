package punkapi.remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit = null;

    //Link for API Punk
    private static final String BASE_URL = "https://api.punkapi.com/v2/";

    //API call and Gson serialization conversion
    public static Retrofit getRetrofitClient(){
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
