package app.com.shubham.practice_retrofit.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import app.com.shubham.practice_retrofit.R;
import app.com.shubham.practice_retrofit.adapter.CustomAdapter;
import app.com.shubham.practice_retrofit.fragments.Main_Fragment;
import app.com.shubham.practice_retrofit.fragments.Search_Fragment;
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
    private String search_method = "flickr.photos.search";
    private boolean search_window_on = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new Main_Fragment())
                .commit();

        refresh_page(page);

        BottomNavigationView navigationView = (BottomNavigationView)findViewById(R.id.navigationView);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                TextView page_num = null;
                switch (menuItem.getItemId()){
                    case R.id.nav_more:
                        if(search_window_on) break;
                        page+=1;
                        show_page(page);
                        break;
                    case R.id.nav_search:
                        if(!search_window_on) {
                            start_search_window();
                            search_page(null);
                            search_window_on = ! search_window_on;
                            break;
                        }
                        search_window_on = false;
                        start_main_window();
                        show_page(page);
                        break;
                    case R.id.nav_home:
                        search_window_on = false;
                        page = 1;
                        start_main_window();
                        show_page(page);
                        break;
                    case R.id.nav_back:
                        if(search_window_on) break;
                        if(page>1){
                            page-=1;
                            show_page(page);
                            break;
                        }
                        break;

                }
                return true;
            }
        });

    }

    private void refresh_page(final int page) {
        if(caller!=null)
            caller.cancel();

        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        caller = service.getAllPhotos(method, per_page,page,api_key,
                format,nojsoncallback, extras);
        caller.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(@NonNull  Call<Example> call,@NonNull Response<Example> response) {
                progressDialog.dismiss();
                photoList = response.body().getPhotos().getPhoto();
                generateDataList();
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                progressDialog.dismiss();
                RelativeLayout main_view = (RelativeLayout)findViewById(R.id.main_layout);
                Snackbar.make(main_view,"Something went wrong! ",Snackbar.LENGTH_INDEFINITE)
                        .setAction("Retry", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                search_window_on = false;
                                refresh_page(page);
                            }
                        })
                        .show();
            }
        });
    }

    private void search_page(final String text){
        if(caller!=null)
            caller.cancel();

        if(text == null || text.equals("")){
            photoList = new ArrayList<Photo>();
            generateDataList();
        }

        else {
            GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
            caller = service.getSearchResults(
                    search_method, api_key, format, nojsoncallback, extras, text
            );
            caller.enqueue(new Callback<Example>() {
                @Override
                public void onResponse(@NonNull Call<Example> call, @NonNull Response<Example> response) {
                    progressDialog.dismiss();
                    photoList = response.body().getPhotos().getPhoto();
                    generateDataList();
                }

                @Override
                public void onFailure(Call<Example> call, Throwable t) {
                    progressDialog.dismiss();
                    RelativeLayout main_view = (RelativeLayout)findViewById(R.id.main_layout);
                    Snackbar.make(main_view,"Something went wrong! ",Snackbar.LENGTH_INDEFINITE)
                            .setAction("Retry", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    search_window_on = false;
                                    start_search(null);
                                }
                            })
                            .show();
                }
            });
        }
    }

    /*Method to generate List of data using RecyclerView with custom adapter*/
    private void generateDataList() {
        recyclerView = findViewById(R.id.customRecyclerView);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 3);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CustomAdapter(this,photoList);
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

    private void start_main_window(){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new Main_Fragment())
                .addToBackStack(null)
                .commit();
        getSupportFragmentManager().executePendingTransactions();
    }

    private void start_search_window(){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new Search_Fragment())
                .addToBackStack(null)
                .commit();
        getSupportFragmentManager().executePendingTransactions();
    }

    public void start_search(View v){
        EditText search_text_box = (EditText)findViewById(R.id.et_search_bar);
        String search_query = search_text_box.getText().toString();
        progressDialog.show();
        search_page(search_query);
    }

    private void show_page(int page){
        progressDialog.show();
        refresh_page(page);
        TextView page_num = (TextView)findViewById(R.id.tv_main_page_no);
        page_num.setText("PAGE "+page);
    }
}