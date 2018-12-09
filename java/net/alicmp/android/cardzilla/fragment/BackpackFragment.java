package net.alicmp.android.cardzilla.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import net.alicmp.android.cardzilla.R;
import net.alicmp.android.cardzilla.adapter.BackpackAdapter;
import net.alicmp.android.cardzilla.helper.SimpleDividerItemDecoration;
import net.alicmp.android.cardzilla.helper.Utility;
import net.alicmp.android.cardzilla.model.Packet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ali on 8/13/15.
 */
public class BackpackFragment extends Fragment {
    private RecyclerView mRecycleView;
    private BackpackAdapter mBackpackAdapter;
    private ArrayList<Packet> list;
    private LinearLayout emptyStateView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_backpack, container, false);

        mRecycleView = (RecyclerView) rootView.findViewById(R.id.recyclerview_backpack);
        emptyStateView = (LinearLayout) rootView.findViewById(R.id.lv_backpack_empty_state);

        list = (ArrayList<Packet>) getPackets();

        mBackpackAdapter = new BackpackAdapter(getActivity(), list);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecycleView.addItemDecoration(new SimpleDividerItemDecoration(getActivity(), 0));
        mRecycleView.setAdapter(mBackpackAdapter);

        checkEmptyState();

        return rootView;
    }

    private List<Packet> getPackets() {
        return Utility.getPackets(getActivity());
    }

    private void checkEmptyState() {
        if(list.size() == 0) {
            mRecycleView.setVisibility(View.GONE);
            emptyStateView.setVisibility(View.VISIBLE);
        } else {
            mRecycleView.setVisibility(View.VISIBLE);
            emptyStateView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        list.clear();
        list.addAll(getPackets());
        mBackpackAdapter.notifyDataSetChanged();
        checkEmptyState();
    }
}
