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

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.subjects.PublishSubject;

public class RepoDbObservable {
    private PublishSubject<List<Repo>> mSubject = PublishSubject.create();
    private RepoDbHelper mDbHelper;

    public RepoDbObservable(Context c) {
        mDbHelper = new RepoDbHelper(c);
    }

    public Observable<List<Repo>> getObservable() {
        return mSubject.startWith(getAllReposFromDb());
    }

    private List<Repo> getAllReposFromDb() {
        mDbHelper.openForRead();
        List<Repo> repos = new ArrayList<>();
        Cursor c = mDbHelper.getAllRepo();
        c.moveToFirst();
        while (c.moveToNext()) {
            repos.add(new Repo(c.getString(RepoDbHelper.REPO_ID_COLUMN_POSITION),
                               c.getString(RepoDbHelper.REPO_NAME_COLUMN_POSITION),
                               c.getString(RepoDbHelper.REPO_FULLNAME_COLUMN_POSITION),
                               new Repo.Owner(c.getString(RepoDbHelper.REPO_OWNER_COLUMN_POSITION),
                                              "", "", "")));
        }
        c.close();
        mDbHelper.close();
        return repos;
    }

    public void insertRepoList(List<Repo> repos) {
        // This could have been done inside a transaction + yieldIfContendedSafely
        mDbHelper.open();
        mDbHelper.removeAllRepo();
        for (Repo r : repos) {
            mDbHelper.addRepo(r.getId(),
                              r.getName(),
                              r.getFullName(),
                              r.getOwner().getLogin());
        }
        mDbHelper.close();
        mSubject.onNext(repos);
    }

    public void propagateError(Throwable e) {
        mSubject.onError(e);
    }

    public void insertRepo(Repo r) {
        mDbHelper.open();
        mDbHelper.addRepo(r.getId(),
                r.getName(),
                r.getFullName(),
                r.getOwner().getLogin());

        mDbHelper.close();

        List<Repo> result = getAllReposFromDb();
        mSubject.onNext(result);
    }
}
