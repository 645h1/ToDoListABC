package abc.workshop.todolistabc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import abc.workshop.todolistabc.adapters.TodoAdapter;
import abc.workshop.todolistabc.api.APIClient;
import abc.workshop.todolistabc.api.APIInterface;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private EditText todoNameEdittext;
    private Button addButton;
    private RecyclerView todoRecycleView;
    private RelativeLayout loaderLayout;
    private TodoAdapter adapter;
    private TextView profileTextview;
    private String uniqueIdTillDeviceReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        todoNameEdittext = findViewById(R.id.todo_name_edittext);
        addButton = findViewById(R.id.add_todo_button);
        todoRecycleView = findViewById(R.id.todo_recyclerview);
        profileTextview = findViewById(R.id.profile_textview);
        loaderLayout = findViewById(R.id.loader_layout);

        uniqueIdTillDeviceReset = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        initList();
        interactionListener();

        createUserAnytime(uniqueIdTillDeviceReset);
    }

    private void interactionListener(){
        profileTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profileIntent = new Intent(MainActivity.this,ProfileActivity.class);
                profileIntent.putExtra("deviceId",uniqueIdTillDeviceReset);
                startActivity(profileIntent);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createTodo(todoNameEdittext.getText().toString(),uniqueIdTillDeviceReset);
            }
        });

    }

    private void initList(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        adapter = new TodoAdapter(this, uniqueIdTillDeviceReset);
        todoRecycleView.setLayoutManager(linearLayoutManager);
        todoRecycleView.setAdapter(adapter);
    }

    private void createUserAnytime(final String id){
        APIInterface  apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<ResponseBody> call = apiInterface.createUser(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                getTodos(id);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
            }
        });
    }

    private void createTodo(String todoTitle, final String userId){
        loaderLayout.setVisibility(View.VISIBLE);
        APIInterface  apiInterface = APIClient.getClient().create(APIInterface.class);
        TodoObject todoToBeCreated = new TodoObject();
        todoToBeCreated.setCompleted(false);
        todoToBeCreated.setTitle(todoTitle);
        todoToBeCreated.setDescription(" ");
        todoToBeCreated.setUserId(userId);

        Call<ResponseBody> call = apiInterface.createTodo(todoToBeCreated);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                loaderLayout.setVisibility(View.GONE);
                getTodos(userId);
                todoNameEdittext.setText("");
                Toast.makeText(MainActivity.this,"Your awesome todo is created!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                loaderLayout.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this,"Oops something went wrong :(",Toast.LENGTH_SHORT).show();
               call.cancel();
            }
        });
    }

    private void getTodos(String userId){
        loaderLayout.setVisibility(View.VISIBLE);
        APIInterface  apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<ArrayList<TodoObject>> call = apiInterface.getTodosByUser(userId);
        call.enqueue(new Callback<ArrayList<TodoObject>>() {
            @Override
            public void onResponse(Call<ArrayList<TodoObject>> call, Response<ArrayList<TodoObject>> response) {
                loaderLayout.setVisibility(View.GONE);
                if (response.code() == 200)
                adapter.setmData(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<TodoObject>> call, Throwable t) {
                loaderLayout.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this,"Oops something went wrong :(",Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });
    }
}
