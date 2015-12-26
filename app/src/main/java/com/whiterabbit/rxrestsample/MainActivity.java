package com.whiterabbit.rxrestsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.whiterabbit.rxrestsample.adapters.RepoAdapter;
import com.whiterabbit.rxrestsample.rest.GitHubClient;
import com.whiterabbit.rxrestsample.rest.Repo;


import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    @Inject GitHubClient mGithubClient;
    @Bind(R.id.main_list) RecyclerView mList;
    private Subscription mSubscription;
    private Observable<List<Repo>> mObservable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ((RxSampleApplication) getApplication()).getComponent().inject(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mList.setLayoutManager(layoutManager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mObservable = mGithubClient.getRepos("fedepaol");
        mObservable.subscribeOn(Schedulers.newThread())
                   .observeOn(AndroidSchedulers.mainThread()).subscribe(l -> {
                    RepoAdapter a = new RepoAdapter(l);
                    mList.setAdapter(a);
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mObservable != null) {
            mObservable.unsubscribeOn(Schedulers.computation());
        }
    }
}
