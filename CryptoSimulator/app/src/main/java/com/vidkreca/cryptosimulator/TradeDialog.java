package com.vidkreca.cryptosimulator;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class TradeDialog extends AppCompatDialogFragment {

    private EditText amountFiat;
    private SeekBar seekBar;

    // Values for the amount of fiat to trade
    private final int min = 1;
    private int max;
    private int value = min;

    private TradeDialogListener listener;


    public TradeDialog(int max) {
        this.max = max;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.trade_dialog, null);

        builder.setView(view).setTitle("Trade")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.getResult(value);
                    }
                });

        amountFiat = view.findViewById(R.id.amountFiat);
        seekBar = view.findViewById(R.id.seekBar);

        // Set maximum value of seekBar to users balance
        seekBar.setMax(this.max);

        // Set events for both controls
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    value = progress;
                    amountFiat.setText(String.valueOf(value));
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        amountFiat.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    value = Integer.parseInt(s.toString());
                    if (value > max) {
                        value = max;
                        String text = String.valueOf(value);
                        amountFiat.setText(text);
                    }
                } catch (Exception ex) {
                    value = 0;
                }
                seekBar.setProgress(value);
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
        });

        return builder.create();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        listener = (TradeDialogListener) context;
    }


    public interface TradeDialogListener {
        void getResult(int amount);
    }
}
