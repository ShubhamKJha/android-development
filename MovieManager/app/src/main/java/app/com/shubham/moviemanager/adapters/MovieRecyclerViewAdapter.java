package app.com.shubham.moviemanager.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.method.MovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import app.com.shubham.moviemanager.R;
import app.com.shubham.moviemanager.activities.MovieDetailActivity;
import app.com.shubham.moviemanager.models.Movies;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieRecyclerViewAdapter extends RecyclerView.Adapter<MovieRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Movies> movies;
    public MovieRecyclerViewAdapter(Context context, List<Movies> movies) {
        this.context = context;
        this.movies = movies;
    }

    public Context getContext(){
        return this.context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_movies, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Movies movie = movies.get(i);
        viewHolder.tvMovieTitle.setText(movie.getTitle());
        viewHolder.tvOverView.setText(movie.getOverview());

        Picasso.with(getContext())
                .load(movie.getPosterPath())
                .into(viewHolder.ivMoviePoster);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.ivMoviePoster)
        ImageView ivMoviePoster;
        @BindView(R.id.tvMovieTitle)
        TextView tvMovieTitle;
        @BindView(R.id.tvOverView)
        TextView tvOverView;
        @BindView(R.id.cvMovies)
        CardView cvMovies;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Movies movie = movies.get(getAdapterPosition());

            Intent intent = new Intent(getContext(), MovieDetailActivity.class);
            intent.putExtra("MOVIE", movie);
            getContext().startActivity(intent);
        }
    }
}