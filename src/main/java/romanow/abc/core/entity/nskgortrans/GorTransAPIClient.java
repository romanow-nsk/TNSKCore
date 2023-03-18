package romanow.abc.core.entity.nskgortrans;

import romanow.abc.core.API.GorTransAPI;
import romanow.abc.core.API.I_APICallBack;
import romanow.abc.core.API.RestAPI;
import romanow.abc.core.UniException;
import romanow.abc.core.Utils;
import romanow.abc.core.constants.Values;
import romanow.abc.core.constants.ValuesBase;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import romanow.abc.core.entity.nskgortrans.json.GorTransTrasses;
import romanow.abc.core.utils.Pair;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class GorTransAPIClient {
    private GorTransAPI service=null;
    private abstract class APICall<T> {
        public abstract Call<T> apiFun();
        //---------------------------------------------------------------------------------------------------
        public APICall(){}
        public Pair<String,T> call() {
            try {
                long tt = System.currentTimeMillis();
                Response<T> res = apiFun().execute();
                if (!res.isSuccessful()) {
                    return new Pair<>(""+res.code()+" "+res.message()+" "+res.errorBody().string(),null);
                    }
                //System.out.println("time=" + (System.currentTimeMillis() - tt) + " мс");
                return new Pair<>(null, res.body());
                } catch (Exception ex) {
                    return new Pair<>("Ошибка GorTransAPI: "+ex.toString());
                    }
            }
        }
    //--------------------------------------------------------------------------------
    public GorTransAPIClient() {
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(Values.HTTPTimeOut, TimeUnit.SECONDS)
                .connectTimeout(Values.HTTPTimeOut, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + Values.NskGorTransURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        service = retrofit.create(GorTransAPI.class);
        }
    private abstract class Back<T> implements  I_APICallBack<T> {
        @Override
        public void onError(int code, String message, ResponseBody body) {
            System.out.println(""+code+" "+message);
        }
        @Override
        public void onException(Exception ee) {
            System.out.println(ee.toString());
        }
        }
   //---------------------------------------------------------------------------------
   public Pair<String,GorTransCareList> getCareList(int routeType, String route){
        return new APICall<GorTransCareList>(){
            @Override
            public Call<GorTransCareList> apiFun() {
                return service.getCareList(""+(routeType+1)+"-"+route+"-W");
                }
            }.call();
        }
    //---------------------------------------------------------------------------------
    public Pair<String,ArrayList<GorTransPoint>> getTrasse(int routeType, String route){
        Pair<String,GorTransTrasses> result = new APICall<GorTransTrasses>(){
            @Override
            public Call<GorTransTrasses> apiFun() {
                return service.getTrasse(""+(routeType+1)+"-"+route+"-W");
                }
            }.call();
        if (result.o1!=null)
            return new Pair<>(result.o1,null);
        return new Pair<>(null,result.o2.trasses.get(0).r.get(0).u);
        }
    //---------------------------------------------------------------------------------
    public Pair<String,ArrayList<GorTransRouteList>> getRouteList(){
        return new APICall<ArrayList<GorTransRouteList>>(){
            @Override
            public Call<ArrayList<GorTransRouteList>> apiFun() {
                return service.getRouteList(true);
            }
        }.call();
    }
   //---------------------------------------------------------------------------------
   public void testAPI(){
        Values.init();          // Для локальной фабрики
        GorTransAPIClient client = new GorTransAPIClient();
        Pair<String,GorTransCareList> list1 = client.getCareList( 0,"8");
        if (list1.o1!=null)
            System.out.println(list1.o1);
        else
            System.out.println(list1.o2);
        Pair<String,ArrayList<GorTransRouteList>> list2 = client.getRouteList();
        if (list2.o1!=null)
            System.out.println(list2.o1);
        else
            System.out.println(list2.o2);
        Pair<String,ArrayList<GorTransPoint>> list3 = client.getTrasse(0,"8");
        if (list3.o1!=null)
            System.out.println(list3.o1);
        else {
            for (GorTransPoint point : list3.o2)
                System.out.println(point);
            }
        }
    public static void main(String ss[]) {
        GorTransAPIClient client = new GorTransAPIClient();
        client.testAPI();
    }
        }
