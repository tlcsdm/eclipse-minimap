package com.tlcsdm.eclipse.minimap.view;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.dialogs.IPageChangedListener;
import org.eclipse.jface.dialogs.PageChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.part.Page;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.texteditor.AbstractTextEditor;

public class MultiPage extends Page implements IPageChangedListener {
	private PageBook pageBook;
	private MultiPageEditorPart multiPageEditor;
	private Map<Object, Control> canvasMap = new HashMap<>();
	private Label fallBack;

	public MultiPage(MultiPageEditorPart multiPageEditor) {
		this.multiPageEditor = multiPageEditor;
		multiPageEditor.addPageChangedListener(this);
	}

	@Override
	public void createControl(Composite parent) {
		pageBook = new PageBook(parent, SWT.NORMAL);
		fallBack = new Label(pageBook, SWT.NORMAL);
		fallBack.setText("Unsupported Context");
		pageChanged(null);
	}

	@Override
	public Control getControl() {
		return pageBook;
	}

	@Override
	public void setFocus() {
		pageBook.setFocus();
	}

	@Override
	public void pageChanged(PageChangedEvent event) {
		Object page = multiPageEditor.getSelectedPage();

		Control cache = canvasMap.get(page);
		if (cache != null) {
			pageBook.showPage(cache);
			return;
		}

		if (page instanceof AbstractTextEditor) {
			AbstractTextEditor textEditor = (AbstractTextEditor) page;
			MinimapPage crazyPage = new MinimapPage(textEditor);
			crazyPage.createControl(pageBook);
			Control canvas = crazyPage.getControl();
			canvasMap.put(page, canvas);
			pageBook.showPage(canvas);
		} else {
			canvasMap.put(page, fallBack);
			pageBook.showPage(fallBack);
		}
	}

	@Override
	public void dispose() {
		super.dispose();
	}

}
