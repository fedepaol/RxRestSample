/*
 * Copyright (C) 2015 Federico Paolinelli
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.whiterabbit.rxrestsample;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.whiterabbit.rxrestsample.adapters.RepoAdapter;
import com.whiterabbit.rxrestsample.data.ObservableGithubRepos;
import com.whiterabbit.rxrestsample.data.Repo;

import java.util.List;


import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CachedActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    @Inject
    ObservableGithubRepos mRepo;

    @Bind(R.id.cached_list) RecyclerView mList;
    @Bind(R.id.activity_cached_swipe) SwipeRefreshLayout mSwipeLayout;

    private Observable<List<Repo>> mObservable;
    private Observable<String> mProgressObservable;
    private RepoAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_cached);
        ButterKnife.bind(this);
        ((RxSampleApplication) getApplication()).getComponent().inject(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mList.setLayoutManager(layoutManager);
        mSwipeLayout.setOnRefreshListener(this);
        mObservable = mRepo.getDbObservable();
        mProgressObservable = mRepo.getProgressObservable();
        mAdapter = new RepoAdapter();
        mList.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mObservable.subscribeOn(Schedulers.io())
                   .observeOn(AndroidSchedulers.mainThread()).subscribe(l -> {
                    mAdapter.updateData(l);
                });

        fetchUpdates();
        Toast toast = Toast.makeText(this, "Updating..", Toast.LENGTH_SHORT);
        toast.show();
    }

    private void fetchUpdates() {
        mProgressObservable.subscribeOn(Schedulers.io())
                           .observeOn(AndroidSchedulers.mainThread())
                           .subscribe(s -> {},
                                      e -> { Log.d("RX", "There has been an error");
                                            mSwipeLayout.setRefreshing(false);
                                      },
                                      () -> mSwipeLayout.setRefreshing(false));

        mRepo.updateRepo("fedepaol");
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mObservable != null) {
            mObservable.unsubscribeOn(Schedulers.computation());
        }
        if (mProgressObservable != null) {
            mProgressObservable.unsubscribeOn(Schedulers.computation()); // TODO this could be done
                                                                         // without explicit thread
        }
    }

    @Override
    public void onRefresh() {
        fetchUpdates();
    }
}
