package punkapi.remote;

import java.util.List;

import punkapi.Beer;
import retrofit2.Call;
import retrofit2.http.GET;

public interface PunkAPIService {
    @GET("beers")
    Call<List<Beer>> getAllBeer();
}
