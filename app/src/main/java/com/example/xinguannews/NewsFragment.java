package com.example.xinguannews;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NewsFragment extends Fragment {
    String data;
    private TextView tv;
    private FloatingActionButton fab;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_item,container,false);
        tv = (TextView) view.findViewById(R.id.text_response);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        return view;
    }

    @SuppressLint("HandlerLeak")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onAttach(getActivity());
        Bundle bundle = getArguments();
        data =bundle.getString("name","top");
        tv.setText(data);

    }

}