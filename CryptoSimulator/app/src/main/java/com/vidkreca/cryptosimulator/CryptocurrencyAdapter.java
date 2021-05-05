package com.vidkreca.cryptosimulator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.vidkreca.data.Cryptocurrency;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CryptocurrencyAdapter extends RecyclerView.Adapter<CryptocurrencyAdapter.ViewHolder> {

    private App app;
    private OnItemClickListener listener;

    public CryptocurrencyAdapter(App app,  OnItemClickListener listener) {
        this.app = app;
        this.listener = listener;
    }


    // Create view object from layout file
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Get context from parent
        Context context = parent.getContext();

        // Create an LayoutInflater object from parent context
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.cryptocurrency, parent, false);

        CryptocurrencyAdapter.ViewHolder viewHolder = new CryptocurrencyAdapter.ViewHolder(view);

        return viewHolder;
    }

    // Bind Component object to UI
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Cryptocurrency tmp = app.GetStore().GetAtIndex(position);

        // Set Cryptocurrency views values
        if (tmp != null) {
            holder.name.setText(tmp.getName());
            holder.symbol.setText(tmp.getSymbol());
            holder.price.setText(Double.toString(tmp.getPrice()) + "€");    // TODO - change € to selected FIAT

            // Load icon image into imageview using Picasso
            String imageUrl = API.baseUrl + "/img/" + tmp.getSymbol().toLowerCase() + ".png";
            Picasso.with(holder.icon.getContext()).load(imageUrl)
                    .centerCrop()
                    .fit()
                    .into(holder.icon);
        } else {
            Toast.makeText(app.getApplicationContext(), "null"+position, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return app.GetStore().Size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView icon;
        public TextView name;
        public TextView symbol;
        public TextView price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.crypto_name);
            symbol = itemView.findViewById(R.id.crypto_symbol);
            price = itemView.findViewById(R.id.crypto_price);
            icon = itemView.findViewById(R.id.crypto_icon);

            itemView.setOnClickListener(view -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(itemView, position);
                    }
                }
            });

            itemView.setOnLongClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemLongClick(itemView, position);
                    }
                }
                return true;
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
        void onItemLongClick(View itemView, int position);
    }

}
