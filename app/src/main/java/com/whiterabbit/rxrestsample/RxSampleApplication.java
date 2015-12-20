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

import android.app.Application;

import com.whiterabbit.rxrestsample.inject.DaggerInjectComponent;
import com.whiterabbit.rxrestsample.inject.InjectComponent;
import com.whiterabbit.rxrestsample.inject.InjectModule;

public class RxSampleApplication extends Application {
    private InjectComponent mComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mComponent = DaggerInjectComponent.builder()
                    .injectModule(new InjectModule(this)).build();

    }

    public InjectComponent getComponent() {
        return mComponent;
    }
}
