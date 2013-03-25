package enjoy.the.music;

import enjoy.the.music.R;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.widget.LinearLayout;

public class SettingActivity extends PreferenceActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
	}

	
}
