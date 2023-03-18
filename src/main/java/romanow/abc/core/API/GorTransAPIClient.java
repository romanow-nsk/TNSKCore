package romanow.abc.core.API;

import romanow.abc.core.UniException;
import romanow.abc.core.Utils;
import romanow.abc.core.constants.Values;
import romanow.abc.core.constants.ValuesBase;
import romanow.abc.core.entity.nskgortrans.GorTransCareList;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import romanow.abc.core.entity.nskgortrans.GorTransRouteList;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class GorTransAPIClient {
    private RestAPI api;
    private boolean on=false;
    private GorTransAPI service=null;
    private abstract class APICall<T> {
        public abstract Call<T> apiFun();
        //---------------------------------------------------------------------------------------------------
        public APICall(final I_APICallBack back) {
            try {
                if (!on) {
                    back.onError(ValuesBase.HTTPAuthorization, "Не стартанул клиент", null);
                    return;
                }
                long tt = System.currentTimeMillis();
                Response<T> res = apiFun().execute();
                if (!res.isSuccessful()) {
                    if (res.code() == Values.HTTPAuthorization) {
                        back.onException(UniException.user("Сеанс закрыт"));
                    } else {
                        back.onError(res.code(), res.message(), res.errorBody());
                    }
                    return;
                }
                //System.out.println("time=" + (System.currentTimeMillis() - tt) + " мс");
                back.onSuccess(res.body());
            } catch (Exception ex) {
                back.onException(ex);
                Utils.printFatalMessage(ex);
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
        on = true;
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
   public void getCareList(int routeType, String route, I_APICallBack back){
        new APICall<GorTransCareList>(back){
            @Override
            public Call<GorTransCareList> apiFun() {
                return service.getCareList(""+(routeType+1)+"-"+route+"-W");
                }
            };
        }
    //---------------------------------------------------------------------------------
    public void getRouteList(I_APICallBack back){
        new APICall<ArrayList<GorTransRouteList>>(back){
            @Override
            public Call<ArrayList<GorTransRouteList>> apiFun() {
                return service.getRouteList(true);
            }
        };
    }
   //---------------------------------------------------------------------------------
   public void testAPI(){
        Values.init();          // Для локальной фабрики
        GorTransAPIClient client = new GorTransAPIClient();
        client.getCareList( 0,"8",new Back<GorTransCareList>() {
            @Override
            public void onSuccess(GorTransCareList result) {
                System.out.println(result);
                }
            });
       client.getRouteList(new Back<ArrayList<GorTransRouteList>>() {
           @Override
           public void onSuccess(ArrayList<GorTransRouteList> result) {
               System.out.println(result);
                }
            });
        }
    public static void main(String ss[]) {
        GorTransAPIClient client = new GorTransAPIClient();
        client.testAPI();
    }
        }
