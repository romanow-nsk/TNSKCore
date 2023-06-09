package romanow.abc.core.API;

import romanow.abc.core.DBRequest;
import romanow.abc.core.ErrorList;
import romanow.abc.core.entity.EntityRefList;
import romanow.abc.core.entity.artifacts.Artifact;
import romanow.abc.core.entity.baseentityes.*;
import retrofit2.Call;
import retrofit2.http.*;
import romanow.abc.core.entity.server.TCare;
import romanow.abc.core.entity.server.TSegmentStatistic;
import romanow.abc.core.entity.subjectarea.TRoute;
import romanow.abc.core.entity.subjectarea.TSegment;
import romanow.abc.core.prepare.DayCellList;
import romanow.abc.core.prepare.WeekCellList;
import romanow.abc.core.reports.TableData;
import romanow.abc.core.utils.GPSPoint;

import java.util.ArrayList;

public interface RestAPI {
    //==================================  API ПРЕДМЕТНОЙ ОБЛАСТИ =======================================================
    /** Импорт ДС из NskGorTrans */
    @GET("/api/tnsk/import")
    Call<ErrorList> gorTransImport(@Header("SessionToken") String token, @Query("pass") String pass);
    /** Включение/выключение сканирования */
    @POST("/api/tnsk/changescan")
    Call<ErrorList> changeScanState(@Header("SessionToken") String token, @Query("pass") String pass);
    /** Состояние сканирования */
    @GET("/api/tnsk/getscan")
    Call<JBoolean> getScanState(@Header("SessionToken") String token);
    /** Состояние сканирования */
    @GET("/api/tnsk/roads")
    Call<EntityRefList<TSegment>> getRoads(@Header("SessionToken") String token);
    /** Состояние сканирования */
    @POST("/api/tnsk/cares/nearest")
    Call<EntityRefList<TCare>> getNearestCares(@Header("SessionToken") String token, @Query("distance") int dist, @Body GPSPoint point);
    /** список актуальных бортов */
    @GET("/api/tnsk/cares/actual")
    Call<EntityRefList<TCare>> getActualCares(@Header("SessionToken") String token,@Query("route") String route);
    /** борт с историей */
    @GET("/api/tnsk/care/story")
    Call<TCare> getCareStory(@Header("SessionToken") String token,@Query("carekey") String careKey);
    /** борт с историей */
    @GET("/api/tnsk/route")
    Call<TRoute> getRoute(@Header("SessionToken") String token, @Query("id") long id);
    /** сегмент со статистикой */
    @GET("/api/tnsk/segment/statistic")
    Call<TSegmentStatistic> getSegmentStatistic(@Header("SessionToken") String token, @Query("id") long id);
    /** сегменты с точками */
    @GET("/api/tnsk/segments")
    Call<EntityRefList<TSegment>> getSegments(@Header("SessionToken") String token);
    /** Добавить группу к экзамену (создание EMTicket для студентов) */
    //@POST("/api/rating/group/add")
    //Call<JLong> addGroupToDiscipline(@Header("SessionToken") String token, @Body() SAGroupRating rating);
    /** Удалить группу с экзамена (удаление EMTicket для студентов) */
    //@POST("/api/rating/group/remove")
    //Call<JEmpty> removeGroupFromExam(@Header("SessionToken") String token, @Query("ratingId") long ratingId);
    /** Получить тикеты студентов для экзамена */
    //@GET("/api/rating/group/get")
    //Call<SAGroupRating> getRatingsForGroup(@Header("SessionToken") String token, @Query("ratingId") long examId);
    /** Получить тикеты студентов для приема */
    //@GET("/api/rating/taking/get")
    //Call<SAExamTaking> getRatingsForTaking(@Header("SessionToken") String token, @Query("takingId") long takingId);
    /** Выполнить функцию перехода */
    //@POST("/api/state/change")
    //Call<JEmpty> execTransition(@Header("SessionToken") String token, @Body DBRequest body);
    /** Получить тикеты студентов для приема */
    //@POST("/api/rating/takingforall")
    //Call<JInt> setTakingForAll(@Header("SessionToken") String token, @Query("takingId") long takingId);
    /** По рейтингу группы */
    //@GET("/api/report/group/artifact")
    //Call<Artifact> createGroupReportArtifact(@Header("SessionToken") String token, @Query("ratingId") long ratingId, @Query("filetype") int filetype);
    //@GET("/api/report/group/table")
    //Call<TableData> createGroupReportTable(@Header("SessionToken") String token, @Query("ratingId") long ratingId);
}
