package com.vidkreca.cryptosimulator;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import events.UpdateEvent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PortfolioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PortfolioFragment extends Fragment {

    private App app;
    private API api;

    private PortfolioAdapter adapter;
    private RecyclerView portfolio_rv;
    private TextView balance;
    private TextView portfolioValue;



    public PortfolioFragment() {
        // Required empty public constructor
    }
    public static PortfolioFragment newInstance() {
        PortfolioFragment fragment = new PortfolioFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_portfolio, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Assign UI elements
        portfolio_rv = view.findViewById(R.id.portfolio_recyclerview);
        balance = view.findViewById(R.id.balance);
        portfolioValue = view.findViewById(R.id.portfolio_value);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        app = (App) getActivity().getApplication();
        api = app.getApi();

        initialSetup();
    }


    @Override
    public void onResume() {
        super.onResume();
        getUser();
    }


    @Override
    public void onStart() {
        super.onStart();

        // Register subscriber
        EventBus.getDefault().register(this);
    }


    @Override
    public void onStop() {
        // Unregister subscriber
        EventBus.getDefault().unregister(this);

        super.onStop();
    }



    /**
     * Call all initialization methods, get all data.
     */
    private void initialSetup() {
        getUser();
        initAdapter();
    }


    /**
     * Initialize the CryptocurrencyAdapter for the RecyclerView.
     */
    private void initAdapter() {
        adapter = new PortfolioAdapter(app, new PortfolioAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                String symbol_query = app.getUser().GetPortfolio()[position].crypto_symbol;

                Intent i = new Intent(getContext(), SingleActivity.class);
                i.putExtra("symbol", symbol_query);

                startActivity(i);
            }
            @Override
            public void onItemLongClick(View itemView, int position) {

            }
        });

        portfolio_rv.setAdapter(adapter);
        portfolio_rv.setLayoutManager(new LinearLayoutManager(getContext()));
    }


    /**
     * Refresh the user object.
     */
    private void getUser() {
        app.refreshUser();
    }


    /**
     * Update the users fiat balance value.
     */
    private void updateData() {
        // Update portfolio value
        double sum = app.getUser().GetPortfolioValue();
        String portfolioValueStr = String.format("%.2f€", sum);
        portfolioValue.setText(portfolioValueStr);

        // Update balance value
        String balanceStr = String.format("%.2f€", app.getUser().getBalance());
        balance.setText(balanceStr);

        // Update portfolio items
        adapter.notifyDataSetChanged();
    }



    /**
     * This gets called when a UpdateEvent is posted
     */
    @Subscribe
    public void onUpdateEvent(UpdateEvent event) {
        this.updateData();
    }
}