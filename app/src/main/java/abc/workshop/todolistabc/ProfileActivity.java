package abc.workshop.todolistabc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import abc.workshop.todolistabc.api.APIClient;
import abc.workshop.todolistabc.api.APIInterface;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    private TextView back;
    private EditText nameEdittext;
    private Button saveButton;
    private String deviceId;
    private ProfileResponse profile;
    private RelativeLayout loaderLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        back = findViewById(R.id.back);
        saveButton = findViewById(R.id.save_button);
        nameEdittext = findViewById(R.id.name_edittext);
        loaderLayout = findViewById(R.id.loader_layout);

        deviceId = getIntent().getStringExtra("deviceId");
        interactionListener();
        getProfile(deviceId);
    }

    private void interactionListener(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profile.setName(nameEdittext.getText().toString());
                updateProfile();
            }
        });
    }

    private void getProfile(String userId){
        loaderLayout.setVisibility(View.VISIBLE);
        APIInterface apiInterface =APIClient.getAPIInterface();
        Call<ProfileResponse> call = apiInterface.getProfile(userId);
        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                loaderLayout.setVisibility(View.GONE);
                if (response.code() == 200){
                    profile = response.body();
                    if ( response.body().getName() != null)
                    nameEdittext.setText(response.body().getName());
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                loaderLayout.setVisibility(View.GONE);
                Toast.makeText(ProfileActivity.this,"Oops something went wrong :(",Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });
    }

    private void updateProfile(){
        loaderLayout.setVisibility(View.VISIBLE);
        APIInterface apiInterface = APIClient.getAPIInterface();
        Call<ResponseBody> call = apiInterface.updateProfile(profile);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                loaderLayout.setVisibility(View.GONE);
                Toast.makeText(ProfileActivity.this,"Saved successfully!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                loaderLayout.setVisibility(View.GONE);
                Toast.makeText(ProfileActivity.this,"Oops something went wrong :(",Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });
    }

}
