package com.vidkreca.cryptosimulator;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.vidkreca.data.PortfolioItem;
import com.vidkreca.data.ProtocolMessages.List;
import com.vidkreca.data.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private App app;
    private API api;

    private CryptocurrencyAdapter adapter;
    private RecyclerView crypto_rv;
    private TextView balance;
    private TextView portfolioValue;
    private SwipeRefreshLayout pullToRefresh;




    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeFragment.
     */
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Assign UI elements
        crypto_rv = view.findViewById(R.id.crypto_recyclerview);
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
     * Refresh data on activity resume.
     */
    @Override
    public void onResume() {
        super.onResume();

        UpdateBalance();
    }


    /**
     * Call all initialization methods, get all data.
     */
    private void InitialSetup() {
        InitSwipeToRefresh();
        pullToRefresh.setRefreshing(true);
        GetData(pullToRefresh);
        GetUser();
        InitAdapter();
    }


    /**
     * Initialize the CryptocurrencyAdapter for the RecyclerView.
     */
    private void InitAdapter() {
        adapter = new CryptocurrencyAdapter(app, new CryptocurrencyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                String symbol_query = app.GetStore().GetAtIndex(position).getSymbol();

                Intent i = new Intent(getContext(), SingleActivity.class);
                i.putExtra("symbol", symbol_query);

                startActivity(i);
            }
            @Override
            public void onItemLongClick(View itemView, int position) {

            }
        });

        crypto_rv.setAdapter(adapter);
        crypto_rv.setLayoutManager(new LinearLayoutManager(getContext()));
    }


    /**
     * Get all cryptocurrency data.
     */
    private void GetData(SwipeRefreshLayout pullToRefresh) {
        api.GetList(new VolleyCallback() {
            @Override
            public void onSuccess(String json) {
                List response = API.gson.fromJson(json, List.class);

                app.GetStore().UpdateCryptocurrencies(response.currencies);

                adapter.notifyDataSetChanged();

                if (pullToRefresh != null)
                    pullToRefresh.setRefreshing(false);     // Signal that we have finished refreshing

                //Toast.makeText(getContext(), app.GetStore().toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getContext(), "Could not retrieve list of cryptocurrencies.\n"+message, Toast.LENGTH_LONG).show();
            }
        });
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
     * Set onRefresh event handler for swipe.
     */
    private void InitSwipeToRefresh() {
        pullToRefresh = getView().findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GetData(pullToRefresh);
                GetUser();
            }
        });
    }


    /**
     * Update the users fiat balance value.
     */
    private void UpdateBalance() {
        // Update portfolio value
        double sum = app.GetUser().GetPortfolioValue();
        String portfolioValueStr = String.format("%.2f€", sum);
        portfolioValue.setText(portfolioValueStr);

        String balanceStr = String.format("%.2f€", app.GetUser().getBalance());
        balance.setText(balanceStr);
    }
}