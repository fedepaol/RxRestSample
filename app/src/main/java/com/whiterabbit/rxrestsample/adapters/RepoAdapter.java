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

package com.whiterabbit.rxrestsample.adapters;

import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whiterabbit.rxrestsample.R;
import com.whiterabbit.rxrestsample.rest.Repo;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RepoAdapter extends RecyclerView.Adapter<RepoAdapter.RepoViewHolder> {
    private List<Repo> mRepos;

    public RepoAdapter(List<Repo> repos) {
        mRepos = repos;
    }

    @Override
    public RepoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                               .inflate(R.layout.list_element, parent, false);
        return new RepoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RepoViewHolder holder, int position) {
        Repo repo = mRepos.get(position);
        holder.mRepoName.setText(repo.getFullName());
        holder.mOwnerName.setText(repo.getOwner().getLogin());
    }

    @Override
    public int getItemCount() {
        return mRepos.size();
    }

    public static class RepoViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.repoName) TextView mRepoName;
        @Bind(R.id.ownerName) TextView mOwnerName;

        public RepoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
