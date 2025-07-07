package com.tlcsdm.eclipse.minimap.preferences;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.tlcsdm.eclipse.minimap.Activator;

public class MinimapPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {
	public static String ID = "com.tlcsdm.eclipse.minimap.preferences.minimapPreferencePage";

	private IPreferenceStore store() {
		return Activator.getDefault().getPreferenceStore();
	}

	@Override
	public void init(IWorkbench workbench) {
		// Do nothing
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
//		layout = newGridLayout[
//		       				numColumns = 3
//		       			]
//		       			
//		       			newLabel[
//		       				text = "Fog Color:"
//		       			]
//		       			
//		       			fogColorWell = newColorWell[
//		       				selection = HSB::deserialize(store.getString(FOG_COLOR))
//		       			]
//		       			
//		       			newPushButton[
//		       				text = "change"
//		       				onClick = [
//		       					fogColorWell.showColorPicker()
//		       				]
//		       			]
//		       			
//		       			newLabel[
//		       				text = "Fog Transparency"
//		       			]
//		       			
//		       			fogTransparencyScale = newScale[
//		       				minimum = 0
//		       				maximum = 255
//		       				selection = 0
//		       				pageIncrement = 17
//		       				
//		       				selection = store.getInt(FOG_TRANSPARENCY)
//		       				
//		       				layoutData = FILL_HORIZONTAL[
//		       					horizontalSpan = 2
//		       				]
//		       			]
		return composite;
	}

//	def private void showColorPicker(ColorWell well) { 
//		var picker = new ColorPicker()
//		var original = well.selection
//		
//		picker.selection = well.selection
//		picker.continuosSelectionHandler = [
//			well.selection = it
//		]
//		if(well.getData("lock-hue") == true){
//			picker.lockHue = true
//		}
//
//		if(picker.open() == IDialogConstants::OK_ID){
//			well.selection = picker.selection
//		}else{
//			well.selection = original
//		}
//	}
//	
//	override performOk() {
//		store.setValue(FOG_COLOR, fogColorWell.selection.serialize)
//		store.setValue(FOG_TRANSPARENCY, fogTransparencyScale.selection)
//		super.performOk()
//	}
//	
//	override protected performDefaults() {
//		fogColorWell.selection = HSB::deserialize(store.getDefaultString(FOG_COLOR))
//		fogTransparencyScale.selection = store.getDefaultInt(FOG_TRANSPARENCY)
//		super.performDefaults()
//	}
//	
//	def private newColorWell(Composite parent, (ColorWell)=>void initializer) {
//		var result = new ColorWell(parent, SWT::NORMAL)
//		initializer.apply(result)
//		return result;
//	}

}
