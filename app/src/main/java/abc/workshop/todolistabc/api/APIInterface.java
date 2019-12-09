package abc.workshop.todolistabc.api;

import java.util.ArrayList;

import abc.workshop.todolistabc.ProfileResponse;
import abc.workshop.todolistabc.TodoObject;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIInterface {

    @POST("/users/userWithId")
    Call<ResponseBody> createUser(@Query("id") String id);

    @POST("/api/todo/dto/")
    Call<ResponseBody> createTodo(@Body TodoObject todo);

    @GET("/api/todo/user/{userId}")
    Call<ArrayList<TodoObject>> getTodosByUser(@Path("userId") String userId);

    @DELETE("/api/todo/{id}")
    Call<ResponseBody> deleteTodo(@Path("id") int id);

    @PUT("/api/todo/editDto")
    Call<ResponseBody> updateTodo(@Body TodoObject todo);

    @GET("/users/user/{userId}")
    Call<ProfileResponse> getProfile(@Path("userId") String userId);

    @PUT("/users/user_update")
    Call<ResponseBody> updateProfile(@Body ProfileResponse todo);

}