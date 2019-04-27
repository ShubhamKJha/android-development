package app.com.shubham.practice_retrofit.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import app.com.shubham.practice_retrofit.R;
import app.com.shubham.practice_retrofit.adapter.CustomAdapter;
import app.com.shubham.practice_retrofit.model.Example;
import app.com.shubham.practice_retrofit.model.Photo;
import app.com.shubham.practice_retrofit.network.GetDataService;
import app.com.shubham.practice_retrofit.network.RetrofitClientInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private CustomAdapter adapter;
    private RecyclerView recyclerView;
    ProgressDialog progressDialog;
    private Call<Example> caller;
    private List<Photo> photoList = null;

    private String api_key = "6f102c62f41998d151e5a1b48713cf13";
    private String method = "flickr.photos.getRecent";
    private int per_page = 20;
    private int page = 1;
    private int nojsoncallback = 1;
    private String format = "json";
    private String extras = "url_s";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        /*Create handle for the RetrofitInstance interface*/

        refresh_page(page);

        BottomNavigationView navigationView = (BottomNavigationView)findViewById(R.id.navigationView);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_more:
                        page += 1;
                        progressDialog.show();
                        refresh_page(page);
                        break;
                    case R.id.nav_home:
                        page = 1;
                        progressDialog.show();
                        refresh_page(page);
                        break;
                    case R.id.nav_search:
                        search_bar();
                        break;
                    case R.id.nav_back:
                        if(page>1){
                            page -= 1;
                            progressDialog.show();
                            refresh_page(page);
                        }
                        break;

                }
                return true;
            }
        });


    }

    private void refresh_page(int page) {
        if(caller!=null)
            caller.cancel();

        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        caller = service.getAllPhotos(method, per_page,page,api_key,
                format,nojsoncallback, extras);
        caller.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(@NonNull  Call<Example> call,@NonNull Response<Example> response) {
                progressDialog.dismiss();
                generateDataList(response.body());
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*Method to generate List of data using RecyclerView with custom adapter*/
    private void generateDataList(Example example) {
        recyclerView = findViewById(R.id.customRecyclerView);
        photoList = example.getPhotos().getPhoto();

        adapter = new CustomAdapter(this,photoList);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    public void show_picture(View v){
        ImageView picture2 = (ImageView)v;
        String url = (String)((LinearLayout)picture2.getParent()).getTag();

        RelativeLayout relativeLayout = new RelativeLayout(this);
        relativeLayout.setPadding(16,16,16,16);
        relativeLayout.setGravity(RelativeLayout.CENTER_HORIZONTAL);


        ImageView picture = new ImageView(this);
        picture.setMinimumHeight(400);
        picture.setMinimumWidth(400);
        Glide.with(this)
                .load(url)
                .placeholder((R.drawable.ic_launcher_background))
                .error(R.drawable.ic_launcher_background)
                .into(picture);
        relativeLayout.addView(picture);


        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setMessage("Preview")
                .setView(relativeLayout)
                .setNegativeButton("BACK",null).create();
        alertDialog.show();
    }

    public void search_bar(){
        LinearLayout llm = new LinearLayout(this);
        llm.setOrientation(LinearLayout.VERTICAL);

        final EditText textView = new EditText(this);
        llm.addView(textView);

//      TODO: No action is added to search option. Should be addded.
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("Search Dialog Box")
                .setMessage("Enter something to search:")
                .setView(llm)
                .setPositiveButton("SEARCH", null)
                .setNegativeButton("BACK",null).create();
        alertDialog.show();
    }
}