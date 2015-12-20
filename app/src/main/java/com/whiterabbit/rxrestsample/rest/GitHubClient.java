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

package com.whiterabbit.rxrestsample.rest;

import java.util.List;

import retrofit.Retrofit;
import rx.Observable;

public class GitHubClient {
    private GitHubService mClient;

    public GitHubClient() {
        mClient = new Retrofit.Builder()
                              .baseUrl("https://api.github.com")
                              .build()
                              .create(GitHubService.class);
    }

    public Observable<List<Repo>> getRepos(String username) {
        return mClient.listRepos(username);
    }
}
