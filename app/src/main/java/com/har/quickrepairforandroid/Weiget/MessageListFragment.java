package com.har.quickrepairforandroid.Weiget;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.har.quickrepairforandroid.R;

public class MessageListFragment extends Fragment {

	private RecyclerView mMessageRecyclerView;

	public static MessageListFragment newInstance() {
		return new MessageListFragment();
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_message_list, container, false);
		mMessageRecyclerView = (RecyclerView)v.findViewById(R.id.message_recycler_view);
		mMessageRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

		return v;
	}
}
