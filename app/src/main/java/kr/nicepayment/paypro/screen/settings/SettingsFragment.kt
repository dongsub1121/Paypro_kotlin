package kr.nicepayment.paypro.screen.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import kr.nicepayment.paypro.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}