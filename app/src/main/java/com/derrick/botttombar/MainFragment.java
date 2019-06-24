package com.derrick.botttombar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.derrick.botttombar.databinding.FragmentMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private List<String> mList = new ArrayList<>();
    FragmentMainBinding binding;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);

        mRecyclerView = binding.mainContent.recyclerView;

        mList.add(getString(R.string.ipsum_lorem));
        mList.add(getString(R.string.ipsum_lorem));
        mList.add(getString(R.string.ipsum_lorem));
        mList.add(getString(R.string.ipsum_lorem));
        mList.add(getString(R.string.ipsum_lorem));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(new RecyclerViewAdapter(mList));


        return binding.getRoot();
    }


}
