package com.android.gt6707a.orderup;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.widget.Toast;

/** A simple {@link Fragment} subclass. */
public class SettingsFragment extends PreferenceFragmentCompat
    implements SharedPreferences.OnSharedPreferenceChangeListener,
        Preference.OnPreferenceChangeListener {

  public SettingsFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
    addPreferencesFromResource(R.xml.settings);

    SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
    PreferenceScreen prefScreen = getPreferenceScreen();

    int count = prefScreen.getPreferenceCount();

    // Go through all of the preferences, and set up their preference summary.
    for (int i = 0; i < count; i++) {
      Preference p = prefScreen.getPreference(i);

      if (!(p instanceof CheckBoxPreference)) {
        String value = sharedPreferences.getString(p.getKey(), "");
        setPreferenceSummary(p, String.format(getString(R.string.settings_user_name_description), value));
      }
    }

    Preference preference = findPreference(getString(R.string.settings_user_name_key));
    preference.setOnPreferenceChangeListener(this);
  }

  @Override
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    // Figure out which preference was changed
    Preference preference = findPreference(key);
    if (null != preference) {
      // Updates the summary for the preference
      if (!(preference instanceof CheckBoxPreference)) {
        String value = sharedPreferences.getString(preference.getKey(), "");
        setPreferenceSummary(preference,  String.format(getString(R.string.settings_user_name_description), value));
      }
    }
  }

  @Override
  public boolean onPreferenceChange(Preference preference, Object newValue) {
    Toast error = Toast.makeText(getContext(), getString(R.string.settings_user_name_cannot_be_blank), Toast.LENGTH_SHORT);
    String sizeKey = getString(R.string.settings_user_name_key);
    if (preference.getKey().equals(sizeKey)) {
      String newName = (String) newValue;

      if (newName == null || newName.isEmpty()) {
        error.show();
        return false;
      }
    }
    return true;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
  }

  /**
   * Updates the summary for the preference
   *
   * @param preference The preference to be updated
   * @param value The value that the preference was updated to
   */
  private void setPreferenceSummary(Preference preference, String value) {
    if (preference instanceof ListPreference) {
      // For list preferences, figure out the label of the selected value
      ListPreference listPreference = (ListPreference) preference;
      int prefIndex = listPreference.findIndexOfValue(value);
      if (prefIndex >= 0) {
        // Set the summary to that label
        listPreference.setSummary(listPreference.getEntries()[prefIndex]);
      }
    } else if (preference instanceof EditTextPreference) {
      // For EditTextPreferences, set the summary to the value's simple string representation.
      preference.setSummary(value);
    }
  }
}
