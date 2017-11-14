package com.sung.imovies.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import com.sung.imovies.R;
import com.sung.imovies.Utils.NetWorkUtils;
import com.sung.imovies.bean.MoviesItem;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private int SINGLE_LINE_ITEM_NUM = 2;
    //args
    private Context mContext;
    private String mode = null;//当前排序模式

    //views
    private RecyclerView mMoviesList;
    private ProgressBar mPbRefresh;

    //datas
    private List<MoviesItem> mMoviesData;
    private GetMoviesData mGetDataTask;
    private MoviesListAdapter mMoviesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUi();
    }

    private void initUi(){
        findViewByID();
        setData();
    }

    private void findViewByID(){
        mMoviesList = (RecyclerView) findViewById(R.id.rc_moview_list);
        mPbRefresh = (ProgressBar) findViewById(R.id.pb_refresh);
    }

    private void setData(){
        mContext = MainActivity.this;
        mMoviesData = new ArrayList<>();
        mGetDataTask = new GetMoviesData();
        //getdata
        mode = NetWorkUtils.MODE_TOP;
        mGetDataTask.execute(NetWorkUtils.buildListUri(mode));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId){
            case R.id.action_refresh:
                break;
            case R.id.action_hot:
                mode = NetWorkUtils.MODE_TOP;
                break;
            case R.id.action_score:
                mode = NetWorkUtils.MODE_POPULAR;
                break;
            default:
                break;
        }
        refresh();
        return true;
    }

    private void refresh(){
        clearData();
        if (mPbRefresh.getVisibility() == View.VISIBLE){
            mGetDataTask.cancel(true);
            mGetDataTask = null;
        }
        mGetDataTask = new GetMoviesData();
        mGetDataTask.execute(NetWorkUtils.buildListUri(mode));
    }

    private void clearData(){
        if (mMoviesData != null && mMoviesData.size() != 0 && mMoviesAdapter != null){
            mMoviesData.clear();
            mMoviesAdapter.notifyDataSetChanged();
            Toast.makeText(mContext, "reset data!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            SINGLE_LINE_ITEM_NUM = 2;
        }
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            SINGLE_LINE_ITEM_NUM = 3;
        }
        refresh();
    }

    class GetMoviesData extends AsyncTask<URL,Void,List>{

        @Override
        protected void onPreExecute() {
            if (isCancelled())
                return;
            if (mPbRefresh == null)
                return;

            mPbRefresh.setVisibility(View.VISIBLE);
        }

        @Override
        protected List doInBackground(URL... params) {
            if (isCancelled())
                return null;
            URL url = params[0];
            if (url == null || url.toString().length() == 0)
                return null;

            Log.e(TAG, "doInBackground: "+url.toString() );
            String responseWithMoviesList = null;
            try {
                responseWithMoviesList = NetWorkUtils.getResponseWithMoviesList(url);
                Log.d(NetWorkUtils.TAG, "response: "+responseWithMoviesList );
                List data = NetWorkUtils.parserMoviesListJSON(responseWithMoviesList);
                if (data.size() != 0)
                    return data;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List list) {
            if (isCancelled())
                return;
            if (mPbRefresh != null) {
                mPbRefresh.setVisibility(View.GONE);
            }

            if (list == null) {
                Toast.makeText(mContext, "列表数据为空！", Toast.LENGTH_SHORT).show();
                return;
            }

            mMoviesData = list;

            GridLayoutManager manager = new GridLayoutManager(mContext,SINGLE_LINE_ITEM_NUM);
            mMoviesAdapter = new MoviesListAdapter(mMoviesData.size());
            mMoviesList.setLayoutManager(manager);
            mMoviesList.setHasFixedSize(true);
            mMoviesList.setAdapter(mMoviesAdapter);
        }
    }

    class MoviesListAdapter extends RecyclerView.Adapter<MoviesHolder>{
        private int count;

        public MoviesListAdapter(int count) {
            this.count = count;
        }

        @Override
        public MoviesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.layout_movies_item, parent, false);
            return new MoviesHolder(view);
        }

        @Override
        public void onBindViewHolder(MoviesHolder holder, int position) {
            holder.setData(position);
        }

        @Override
        public int getItemCount() {
            return count;
        }
    }

    class MoviesHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView mCover;
        private int position;

        public MoviesHolder(View itemView) {
            super(itemView);
            mCover = itemView.findViewById(R.id.iv_cover);

            mCover.setOnClickListener(this);
        }

        public void setData(int position){
            this.position = position;
            if (mMoviesData != null && mMoviesData.size() != 0 && mContext != null){
                URL url = NetWorkUtils.buildImageUri(mMoviesData.get(position).poster_path);
                if (url == null)
                    return;

                Picasso.with(mContext).load(url.toString()).into(mCover);
            }
        }

        @Override
        public void onClick(View v) {
            if (v instanceof ImageView) {
                Intent intent = new Intent();
                intent.putExtra(
                        MovieDetailActivity.MOVIES_DETAIL_BUNDLE,mMoviesData.get(position));
                intent.setClass(MainActivity.this,MovieDetailActivity.class);
                startActivity(intent);
            }
        }
    }
}
