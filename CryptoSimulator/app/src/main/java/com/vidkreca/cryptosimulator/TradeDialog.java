package com.vidkreca.cryptosimulator;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import static androidx.core.content.ContextCompat.getSystemService;

public class TradeDialog extends AppCompatDialogFragment {

    private EditText amountFiat;
    private SeekBar seekBar;
    private Context context;
    private TextView title;

    // Values for the amount of fiat to trade
    private final double min = 1;
    private double max;
    private double value = min;
    private String type;


    private TradeDialogListener listener;


    public TradeDialog(double max, String type, Context context) {
        this.max = max;
        this.type = type;
        this.context = context;
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
                        listener.getResult(value, type);
                    }
                });

        title = view.findViewById(R.id.trade_title);
        title.setText(type.equals("buy") ? "BUY" : "SELL");
        amountFiat = view.findViewById(R.id.amountFiat);
        seekBar = view.findViewById(R.id.seekBar);
        seekBar.setMax((int)this.max);

        // Set events for SeekBar
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

        // Set events for EditText
        amountFiat.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    value = Double.parseDouble(s.toString());
                    if (value > max) {
                        value = max;
                        String text = String.valueOf(value);
                        amountFiat.setText(text);
                    }
                    seekBar.setProgress((int)value);
                } catch (Exception ex) {
                    value = 0;
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
        });

        // Focus the EditText and open the keyboard
        amountFiat.requestFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        return builder.create();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        listener = (TradeDialogListener) context;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        // Close keyboard
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public interface TradeDialogListener {
        void getResult(double amount, String type);
    }
}
