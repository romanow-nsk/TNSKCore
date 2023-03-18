package romanow.abc.core.API;

import retrofit2.Call;
import retrofit2.http.*;
import romanow.abc.core.entity.nskgortrans.GorTransCareList;
import romanow.abc.core.entity.nskgortrans.GorTransRouteList;
import romanow.abc.core.entity.nskgortrans.json.GorTransTrasses;

import java.util.ArrayList;

//------------ API Гортранса для retrofit2----------------------------------------
public interface GorTransAPI {
    @GET("/markers.php")
    Call<GorTransCareList> getCareList(@Query("r") String param);
    @GET("/listmarsh.php")
    Call<ArrayList<GorTransRouteList>> getRouteList(@Query("r") boolean param);
    @GET("/trasses.php")
    Call<GorTransTrasses> getTrasse(@Query("r") String param);
}
