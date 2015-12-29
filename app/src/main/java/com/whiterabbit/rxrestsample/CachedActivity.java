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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.whiterabbit.rxrestsample.adapters.RepoAdapter;
import com.whiterabbit.rxrestsample.data.Repo;
import com.whiterabbit.rxrestsample.data.RepoDbObservable;
import com.whiterabbit.rxrestsample.rest.GitHubClient;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CachedActivity extends AppCompatActivity {
    @Bind(R.id.main_list) RecyclerView mList;
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
        mObservable = RepoDbObservable.getObservable(getApplicationContext());

        mObservable.delay(3, TimeUnit.SECONDS) // delayed for demonstration purpouse
                   .subscribeOn(Schedulers.io())
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
