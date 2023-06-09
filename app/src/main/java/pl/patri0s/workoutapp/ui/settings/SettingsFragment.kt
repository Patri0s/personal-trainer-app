package pl.patri0s.workoutapp.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import pl.patri0s.workoutapp.R
import pl.patri0s.workoutapp.database.WorkOutApp
import pl.patri0s.workoutapp.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null

    private val binding get() = _binding!!

    private lateinit var activity: AppCompatActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as AppCompatActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        if (savedInstanceState == null) {
            childFragmentManager.beginTransaction().replace(R.id.settings, SettingsFragment())
                .commit()
        }

        activity.supportActionBar?.subtitle = getString(R.string.nav_settings)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            val summaryDao = (requireActivity().application as WorkOutApp).db.summaryDao()
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            findPreference<Preference>("deleteData")?.setOnPreferenceClickListener {
                lifecycleScope.launch {
                    summaryDao.delete(summaryDao.fetchAllDates().first())
                    summaryDao.deleteBmi(summaryDao.fetchBmiData().first())
                    Toast.makeText(
                        activity,
                        R.string.settings_delete_toast,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                true
            }
            val sharedPreferences =
                this.context?.let { PreferenceManager.getDefaultSharedPreferences(it.applicationContext) }

            findPreference<Preference>("changeLanguage")?.setOnPreferenceChangeListener { _, _ ->
                sharedPreferences?.registerOnSharedPreferenceChangeListener { preferences, _ ->
                    var appLanguage: String? = null
                    when (preferences?.getString("changeLanguage", "English")) {
                        "Polish" -> appLanguage = "pl-PL"
                        "English" -> appLanguage = "en-US"
                    }
                    val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(appLanguage)
                    AppCompatDelegate.setApplicationLocales(appLocale)
                }
                true
            }
        }
    }
}