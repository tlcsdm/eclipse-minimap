package com.tlcsdm.eclipse.minimap.preferences;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

import com.tlcsdm.eclipse.minimap.Activator;
import com.tlcsdm.eclipse.minimap.util.HSB;
import com.tlcsdm.eclipse.minimap.util.MinimapConstants;

public class MinimapPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {
	public static String ID = "com.tlcsdm.eclipse.minimap.preferences.minimapPreferencePage";
	private Button fogColorWell;
	private Scale fogTransparencyScale;

	private IPreferenceStore store() {
		return Activator.getDefault().getPreferenceStore();
	}

	@Override
	public void init(IWorkbench workbench) {
		// Do nothing
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(3, false);
		container.setLayout(layout);

		Label colorLabel = new Label(container, SWT.NONE);
		colorLabel.setText("Fog Color:");

		fogColorWell = new Button(container, SWT.NONE);
		fogColorWell.setEnabled(false);
		fogColorWell.setBackground(new Color(HSB.deserialize(store().getString(MinimapConstants.FOG_COLOR)).toRGB()));

		Button changeButton = new Button(container, SWT.PUSH);
		changeButton.setText("change");
		changeButton.addListener(SWT.Selection, e -> showColorPicker());

		Label transparencyLabel = new Label(container, SWT.NONE);
		transparencyLabel.setText("Fog Transparency");

		fogTransparencyScale = new Scale(container, SWT.HORIZONTAL);
		fogTransparencyScale.setMinimum(0);
		fogTransparencyScale.setMaximum(255);
		fogTransparencyScale.setPageIncrement(17);
		fogTransparencyScale.setSelection(store().getInt(MinimapConstants.FOG_TRANSPARENCY));

		GridData scaleData = new GridData(GridData.FILL_HORIZONTAL);
		scaleData.horizontalSpan = 2;
		fogTransparencyScale.setLayoutData(scaleData);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, Activator.PLUGIN_ID + ".prefs_minimap");
		return container;
	}

	private void showColorPicker() {
		ColorDialog dialog = new ColorDialog(Display.getDefault().getActiveShell());
		dialog.setRGB(fogColorWell.getBackground().getRGB());
		dialog.setText("Select a Color");
		RGB selected = dialog.open();
		if (selected != null) {
			fogColorWell.setBackground(new Color(selected));
		}
	}

	@Override
	public boolean performOk() {
		store().setValue(MinimapConstants.FOG_COLOR, new HSB(fogColorWell.getBackground().getRGB()).serialize());
		store().setValue(MinimapConstants.FOG_TRANSPARENCY, fogTransparencyScale.getSelection());
		return super.performOk();
	}

	@Override
	protected void performDefaults() {
		fogColorWell.setBackground(
				new Color(HSB.deserialize(store().getDefaultString(MinimapConstants.FOG_COLOR)).toRGB()));
		fogTransparencyScale.setSelection(store().getDefaultInt(MinimapConstants.FOG_TRANSPARENCY));
		super.performDefaults();
	}

}
