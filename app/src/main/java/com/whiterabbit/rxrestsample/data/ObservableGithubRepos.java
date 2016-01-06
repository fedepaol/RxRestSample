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

package com.whiterabbit.rxrestsample.data;

import android.app.Application;

import com.whiterabbit.rxrestsample.rest.GitHubClient;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

public class ObservableGithubRepos {
    @Inject
    GitHubClient mClient;
    @Inject
    ObservableRepoDb mDatabase;
    @Inject
    Application mApplication;

    @Inject
    public ObservableGithubRepos() {
    }

    public Observable<List<Repo>> getDbObservable() {
        return mDatabase.getObservable();
    }

    public Observable<String> updateRepo(String userName) {
        BehaviorSubject<String> requestSubject = BehaviorSubject.create();

        Observable<List<Repo>> observable = mClient.getRepos(userName);
        observable.subscribeOn(Schedulers.io())
                  .observeOn(Schedulers.io())
                  .subscribe(l -> {
                                    mDatabase.insertRepoList(l);
                                    requestSubject.onNext(userName);},
                             e -> requestSubject.onError(e),
                             () -> requestSubject.onCompleted());
        return requestSubject.asObservable();
    }
}
