package com.whiterabbit.rxrestsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.whiterabbit.rxrestsample.rest.GitHubClient;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @Inject GitHubClient mGithubClient;

    @Bind(R.id.main_list) RecyclerView mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ((RxSampleApplication) getApplication()).getComponent().inject(this);
    }
}
