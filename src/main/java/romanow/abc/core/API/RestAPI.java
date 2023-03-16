package romanow.abc.core.API;

import romanow.abc.core.DBRequest;
import romanow.abc.core.entity.artifacts.Artifact;
import romanow.abc.core.entity.baseentityes.*;
import romanow.abc.core.entity.subjectarea.*;
import retrofit2.Call;
import retrofit2.http.*;
import romanow.abc.core.reports.GroupRatingReport;
import romanow.abc.core.reports.TableData;

public interface RestAPI {
    //==================================  API ПРЕДМЕТНОЙ ОБЛАСТИ =======================================================
    /** Добавить группу к экзамену (создание EMTicket для студентов) */
    @POST("/api/rating/group/add")
    Call<JLong> addGroupToDiscipline(@Header("SessionToken") String token, @Body() SAGroupRating rating);
    /** Удалить группу с экзамена (удаление EMTicket для студентов) */
    @POST("/api/rating/group/remove")
    Call<JEmpty> removeGroupFromExam(@Header("SessionToken") String token, @Query("ratingId") long ratingId);
    /** Получить тикеты студентов для экзамена */
    @GET("/api/rating/group/get")
    Call<SAGroupRating> getRatingsForGroup(@Header("SessionToken") String token, @Query("ratingId") long examId);
    /** Получить тикеты студентов для приема */
    @GET("/api/rating/taking/get")
    Call<SAExamTaking> getRatingsForTaking(@Header("SessionToken") String token, @Query("takingId") long takingId);
    /** Выполнить функцию перехода */
    @POST("/api/state/change")
    Call<JEmpty> execTransition(@Header("SessionToken") String token, @Body DBRequest body);
    /** Получить тикеты студентов для приема */
    @POST("/api/rating/takingforall")
    Call<JInt> setTakingForAll(@Header("SessionToken") String token, @Query("takingId") long takingId);
    /** По рейтингу группы */
    @GET("/api/report/group/artifact")
    Call<Artifact> createGroupReportArtifact(@Header("SessionToken") String token, @Query("ratingId") long ratingId, @Query("filetype") int filetype);
    @GET("/api/report/group/table")
    Call<TableData> createGroupReportTable(@Header("SessionToken") String token, @Query("ratingId") long ratingId);
}
