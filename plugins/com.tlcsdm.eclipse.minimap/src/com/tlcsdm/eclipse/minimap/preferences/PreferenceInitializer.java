package com.tlcsdm.eclipse.minimap.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.tlcsdm.eclipse.minimap.Activator;
import com.tlcsdm.eclipse.minimap.util.HSB;
import com.tlcsdm.eclipse.minimap.util.MinimapConstants;

public class PreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(MinimapConstants.INVERT_SWIPE_GESTURE, true);
		store.setDefault(MinimapConstants.FOG_COLOR, new HSB().serialize());
		store.setDefault(MinimapConstants.FOG_TRANSPARENCY, 20);
	}

}
