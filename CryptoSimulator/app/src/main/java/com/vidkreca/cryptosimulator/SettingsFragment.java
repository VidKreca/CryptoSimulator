package com.vidkreca.cryptosimulator;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    private App app;

    private Button resetBtn;
    private SwitchCompat notificationSwitch;


    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SettingsFragment.
     */
    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
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
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }


    @Override
    public void onViewCreated(@NonNull @org.jetbrains.annotations.NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        resetBtn = view.findViewById(R.id.resetBtn);
        notificationSwitch = view.findViewById(R.id.notificationSwitch);
    }


    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        app = (App) getActivity().getApplication();

        // Set current notificationSwitch value
        notificationSwitch.setChecked(app.getNotificationEnabled());

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = requireContext();

                new AlertDialog.Builder(context)
                        .setTitle("Confirmation")
                        .setMessage("Do you really want to reset all progress? This cannot be reversed.")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                // Reset our UUID, effectively deleting our user account
                                app.resetUUID();

                                // Restart the app
                                PackageManager packageManager = context.getPackageManager();
                                Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
                                ComponentName componentName = intent.getComponent();
                                Intent mainIntent = Intent.makeRestartActivityTask(componentName);
                                context.startActivity(mainIntent);
                                Runtime.getRuntime().exit(0);
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });


        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                app.setNotificationEnabled(isChecked);
            }
        });
    }
}