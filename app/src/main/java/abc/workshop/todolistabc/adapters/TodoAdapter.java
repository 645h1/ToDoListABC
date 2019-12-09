package abc.workshop.todolistabc.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import abc.workshop.todolistabc.R;
import abc.workshop.todolistabc.TodoObject;
import abc.workshop.todolistabc.api.APIClient;
import abc.workshop.todolistabc.api.APIInterface;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {

    private ArrayList<TodoObject> mData = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context context;
    private String userId;

    // data is passed into the constructor
    public TodoAdapter(Context context, String userId) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.userId = userId;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.todo_list_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final TodoObject todo = mData.get(position);
        holder.todoTextView.setText(todo.getTitle());

        if (todo.getCompleted()){
            holder.todoTextView.setPaintFlags(holder.todoTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else {
            holder.todoTextView.setPaintFlags(0);
        }

        holder.checkBox.setChecked(todo.getCompleted());

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isPressed()){
                    changeActive(todo);
                    notifyItemChanged(position);
                }
            }
        });

        holder.deleteImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete(todo.getId(),position);
            }
        });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView todoTextView, deleteImageview;
        CheckBox checkBox;

        ViewHolder(View itemView) {
            super(itemView);
            todoTextView = itemView.findViewById(R.id.todo_textview);
            deleteImageview = itemView.findViewById(R.id.delete_imageview);
            checkBox = itemView.findViewById(R.id.checkbox);
        }
    }

    public void setmData(ArrayList<TodoObject> mData) {
        this.mData = mData;
        notifyDataSetChanged();
    }

    private void changeActive(TodoObject todo){
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        todo.setCompleted(!todo.getCompleted());
        todo.setUserId(userId);
        Call<ResponseBody> call = apiInterface.updateTodo(todo);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(context,"Changed successfully!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context,"Oops something went wrong :(",Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });
    }

    private void delete(int id, final int position){
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);

        Call<ResponseBody> call = apiInterface.deleteTodo(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(context,"Todo deleted!",Toast.LENGTH_SHORT).show();
                mData.remove(position);
                notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context,"Oops something went wrong :(",Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });
    }
}