package com.vidkreca.cryptosimulator;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        api = app.GetApi();

        InitialSetup();
    }


    /**
     * Call all initialization methods, get all data.
     */
    private void InitialSetup() {
        GetUser();
        InitAdapter();
    }


    /**
     * Initialize the CryptocurrencyAdapter for the RecyclerView.
     */
    private void InitAdapter() {
        adapter = new PortfolioAdapter(app, new PortfolioAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                String symbol_query = app.GetUser().GetPortfolio()[position].crypto_symbol;

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
    private void GetUser() {
        app.RefreshUser(new RefreshCallback() {
            @Override
            public void onRefresh() {
                UpdateBalance();
            }
        });
    }


    /**
     * Update the users fiat balance value.
     */
    private void UpdateBalance() {
        // Calculate portfolio value
        double sum = app.GetUser().GetPortfolioValue();
        String portfolioValueStr = String.format("%.2f€", sum);
        portfolioValue.setText(portfolioValueStr);

        String balanceStr = String.format("%.2f€", app.GetUser().getBalance());
        balance.setText(balanceStr);
    }
}