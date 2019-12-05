package abc.workshop.todolistabc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText todoNameEdittext;
    private Button addButton;
    private RecyclerView todoRecycleView;
    private TodoAdapter adapter;
    private TextView profileTextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        todoNameEdittext = findViewById(R.id.todo_name_edittext);
        addButton = findViewById(R.id.add_todo_button);
        todoRecycleView = findViewById(R.id.todo_recyclerview);
        profileTextview = findViewById(R.id.profile_textview);
        initList();
        interactionListener();
    }

    private void interactionListener(){
        profileTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profileIntent = new Intent(MainActivity.this,ProfileActivity.class);
                startActivity(profileIntent);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    private void initList(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        adapter = new TodoAdapter(this);
        todoRecycleView.setLayoutManager(linearLayoutManager);
        todoRecycleView.setAdapter(adapter);
    }
}
