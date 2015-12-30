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
    private static PublishSubject<List<Repo>> mSubject = PublishSubject.create();
    private static RepoDbHelper mDbHelper;

    private RepoDbObservable(Context c) {
        mDbHelper = new RepoDbHelper(c);
    }

    public static Observable<List<Repo>> getObservable(Context ctx) {
        if (mDbHelper == null) {
            mDbHelper = new RepoDbHelper(ctx);
        }
        return mSubject.startWith(getAllReposFromDb());
    }

    private static List<Repo> getAllReposFromDb() {
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
        return repos;
    }

    public void insertRepoList(List<Repo> repos) {
        // This could have been done inside a transaction + yieldIfContendedSafely
        mDbHelper.removeAllRepo();
        for (Repo r : repos) {
            mDbHelper.addRepo(r.getId(),
                              r.getName(),
                              r.getFullName(),
                              r.getOwner().getLogin());
        }
        mSubject.onNext(repos);
    }

    public void propagateError(Throwable e) {
        mSubject.onError(e);
    }

    public void insertRepo(Repo r) {
        mDbHelper.addRepo(r.getId(),
                r.getName(),
                r.getFullName(),
                r.getOwner().getLogin());


        List<Repo> result = getAllReposFromDb();
        mSubject.onNext(result);
    }
}
