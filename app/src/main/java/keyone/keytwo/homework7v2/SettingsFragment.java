package keyone.keytwo.homework7v2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {

    RadioButton radioButtonAdd;
    RadioButton radioButtonReplace;
    SwitchCompat switchBackStack;
    SwitchCompat switchBackAsRemove;
    SwitchCompat switchBackDeleteBeforeAdd;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        radioButtonAdd = view.findViewById(R.id.radioButtonAdd);
        radioButtonReplace = view.findViewById(R.id.radioButtonReplace);
        switchBackStack = view.findViewById(R.id.switchBackStack);
        switchBackAsRemove = view.findViewById(R.id.switchBackAsRemove);
        switchBackDeleteBeforeAdd = view.findViewById(R.id.switchBackDeleteBeforeAdd);

        radioButtonAdd.setChecked(Settings.isAddFragment);
        radioButtonReplace.setChecked(Settings.isReplaceFragment);
        switchBackStack.setChecked(Settings.isBackStack);
        switchBackAsRemove.setChecked(Settings.isBackAsRemove);
        switchBackDeleteBeforeAdd.setChecked(Settings.isDeleteBeforeAdd);

        radioButtonAdd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Settings.isAddFragment = isChecked;
                saveSettings();
            }
        });
        radioButtonReplace.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Settings.isReplaceFragment = isChecked;
                saveSettings();
            }
        });
        switchBackStack.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Settings.isBackStack = isChecked;
                saveSettings();

            }
        });
        switchBackAsRemove.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Settings.isBackAsRemove = isChecked;
                saveSettings();

            }
        });
        switchBackDeleteBeforeAdd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Settings.isDeleteBeforeAdd = isChecked;
                saveSettings();
            }
        });
    }

    private void saveSettings() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(Settings.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Settings.IS_BACK_STACK_USED, Settings.isBackStack);
        editor.putBoolean(Settings.IS_DELETE_FRAGMENT_BEFORE_ADD, Settings.isDeleteBeforeAdd);
        editor.putBoolean(Settings.IS_BACK_AS_REMOVE_FRAGMENT, Settings.isBackAsRemove);
        editor.putBoolean(Settings.IS_ADD_FRAGMENT_USED, Settings.isAddFragment);
        editor.putBoolean(Settings.IS_REPLACE_FRAGMENT_USED, Settings.isReplaceFragment);
        editor.apply();
    }


}