package com.zookanews.egyptlatestnews.UI;


import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.zookanews.egyptlatestnews.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }
}
