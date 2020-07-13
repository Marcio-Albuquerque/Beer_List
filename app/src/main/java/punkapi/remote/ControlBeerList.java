package punkapi.remote;

public class ControlBeerList {

    public static final String BASE_URL = "https://api.punkapi.com/v2";

    public static PunkAPIService getPunkAPIClient() {
        return RetrofitClient.getRetrofitClient().create(PunkAPIService.class);
    }


}
