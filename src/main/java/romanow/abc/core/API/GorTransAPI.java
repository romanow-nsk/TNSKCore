package romanow.abc.core.API;

import retrofit2.Call;
import retrofit2.http.*;
import romanow.abc.core.entity.nskgortrans.GorTransCareList;
import romanow.abc.core.entity.nskgortrans.GorTransRouteList;

import java.util.ArrayList;

public interface GorTransAPI {
    @GET("/markers.php")
    Call<GorTransCareList> getCareList(@Query("r") String param);
    @GET("/listmarsh.php")
    Call<ArrayList<GorTransRouteList>> getRouteList(@Query("r") boolean param);
}
