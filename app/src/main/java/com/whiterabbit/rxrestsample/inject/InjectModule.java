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

package com.whiterabbit.rxrestsample.inject;

import android.app.Application;

import com.whiterabbit.rxrestsample.rest.GitHubClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class InjectModule {
    private Application mApp;

    public InjectModule(Application app) {
        mApp = app;
    }

    @Provides
    @Singleton
    GitHubClient provideGitHubClient() {
        return null;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return mApp;
    }
}
