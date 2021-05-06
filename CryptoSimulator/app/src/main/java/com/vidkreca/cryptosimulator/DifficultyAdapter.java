package com.vidkreca.cryptosimulator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.vidkreca.data.ProtocolMessages.Difficulty;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DifficultyAdapter extends RecyclerView.Adapter<DifficultyAdapter.ViewHolder> {

    private App app;
    private DifficultyAdapter.OnItemClickListener listener;
    private ArrayList<Difficulty> difficulties;

    public DifficultyAdapter(App app, ArrayList<Difficulty> difficulties, DifficultyAdapter.OnItemClickListener listener) {
        this.app = app;
        this.listener = listener;
        this.difficulties = difficulties;
    }


    // Create view object from layout file
    @NonNull
    @Override
    public DifficultyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Get context from parent
        Context context = parent.getContext();

        // Create an LayoutInflater object from parent context
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.difficulty, parent, false);

        DifficultyAdapter.ViewHolder viewHolder = new DifficultyAdapter.ViewHolder(view);

        return viewHolder;
    }

    // Bind Component object to UI
    @Override
    public void onBindViewHolder(@NonNull DifficultyAdapter.ViewHolder holder, int position) {

        Difficulty tmp = difficulties.get(position);

        // Set Difficulty views values
        if (tmp != null) {
            holder.difficulty.setText(tmp.difficulty.toUpperCase());
            holder.startingBalance.setText(tmp.balance + "€");    // TODO - change € to selected FIAT
        } else {
            Toast.makeText(app.getApplicationContext(), "null"+position, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return difficulties.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView difficulty;
        public TextView startingBalance;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            difficulty = itemView.findViewById(R.id.difficulty);
            startingBalance = itemView.findViewById(R.id.starting_balance);

            itemView.setOnClickListener(view -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(itemView, position);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }
}
