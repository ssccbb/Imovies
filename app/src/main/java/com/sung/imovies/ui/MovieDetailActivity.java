package com.sung.imovies.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.sung.imovies.R;
import com.sung.imovies.Utils.NetWorkUtils;
import com.sung.imovies.bean.MoviesItem;

import java.net.URL;

/**
 * Created by sung on 2017/11/7.
 */

public class MovieDetailActivity extends AppCompatActivity {
    public static final String MOVIES_DETAIL_BUNDLE = "movies_detail_bundle";
    //views
    private ImageView mCover,mBgCover;
    private TextView mTittle,mDetail;
    private TextView mInfo1,mInfo2,mInfo3;

    //args
    private MoviesItem mMoviesItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        getIntentData();
        initUI();
    }

    private void initUI(){
        findviewbyId();
        setData();
    }

    private void findviewbyId(){

        mBgCover = (ImageView) findViewById(R.id.iv_cover_bg);
        mCover = (ImageView) findViewById(R.id.iv_cover);
        mTittle = (TextView) findViewById(R.id.tv_tittle);
        mDetail = (TextView) findViewById(R.id.tv_detail);
        mInfo1 = (TextView) findViewById(R.id.tv_info_1);
        mInfo2 = (TextView) findViewById(R.id.tv_info_2);
        mInfo3 = (TextView) findViewById(R.id.tv_info_3);
    }

    private void setData(){
        if (mMoviesItem == null)
            return;

        mTittle.setText(mMoviesItem.getTitle());
        mInfo1.setText(mMoviesItem.getOriginal_title()+"("+mMoviesItem.getOriginal_language()+")");
        mInfo2.setText("评分："+mMoviesItem.getVote_average()+"("+mMoviesItem.getVote_count()+"已评)");
        mInfo3.setText("上映日期："+mMoviesItem.getRelease_date());
        mDetail.setText(mMoviesItem.getOverview());

        URL cover = NetWorkUtils.buildImageUri(mMoviesItem.getPoster_path());
        URL coverbg = NetWorkUtils.buildImageUri(mMoviesItem.getBackdrop_path());
        Picasso.with(this).load(cover.toString()).into(mCover);
        Picasso.with(this).load(coverbg.toString()).into(mBgCover);
    }

    private void getIntentData(){
        Intent bundleData = this.getIntent();
        mMoviesItem =
                (MoviesItem) bundleData.getSerializableExtra(MOVIES_DETAIL_BUNDLE);
    }
}
