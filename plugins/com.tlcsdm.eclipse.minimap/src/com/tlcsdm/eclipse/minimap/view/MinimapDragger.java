package com.tlcsdm.eclipse.minimap.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import com.tlcsdm.eclipse.minimap.Activator;
import com.tlcsdm.eclipse.minimap.util.MinimapConstants;
import com.tlcsdm.eclipse.minimap.util.SWTExtensions;

public class MinimapDragger {
	private SWTExtensions swt = SWTExtensions.INSTANCE;

	private static final int STATE_NONE = 0;
	private static final int STATE_DRAGGING = 1;

	private int state = STATE_NONE;

	private MinimapCanvas canvas;
	private Rectangle offsetSelection;
	private Point offsetLocation;
	private Cursor handCursor;
	private Cursor grabCursor;

	public MinimapDragger(MinimapCanvas canvas) {
		this.canvas = canvas;
		canvas.addListener(SWT.MouseDown, new Listener() {
			@Override
			public void handleEvent(Event event) {
				onMouseDown(event);
			}
		});
		canvas.addListener(SWT.MouseUp, new Listener() {
			@Override
			public void handleEvent(Event event) {
				onMouseUp(event);
			}
		});

		canvas.addListener(SWT.MouseMove, new Listener() {
			@Override
			public void handleEvent(Event event) {
				onMouseMove(event);
			}
		});

		canvas.addListener(SWT.Gesture, new Listener() {
			@Override
			public void handleEvent(Event event) {
				onGesture(event);
			}
		});
	}

	@SuppressWarnings("deprecation")
	public Cursor getGrabCursor() {
		grabCursor = new Cursor(canvas.getDisplay(), Activator.getImageDescriptor("icons/grab.gif").getImageData(100),
				8, 8);
		canvas.addListener(SWT.Dispose, new Listener() {
			@Override
			public void handleEvent(Event event) {
				grabCursor.dispose();
			}
		});
		return grabCursor;
	}

	@SuppressWarnings("deprecation")
	public Cursor getHandCursor() {
		if (handCursor == null || handCursor.isDisposed()) {
			handCursor = new Cursor(canvas.getDisplay(),
					Activator.getImageDescriptor("icons/hand.gif").getImageData(100), 8, 8);
			canvas.addListener(SWT.Dispose, new Listener() {
				@Override
				public void handleEvent(Event event) {
					handCursor.dispose();
				}
			});
		}
		return handCursor;
	}

	private void onGesture(Event event) {
		if (event.detail == SWT.GESTURE_PAN) {
			Rectangle selection = canvas.getSelection();
			if (selection == null) {
				return;
			}
			float revertScale = 1f / canvas.getScale();

			int dx = event.xDirection;
			int dy = event.yDirection;

			boolean invert = Activator.getDefault().getPreferenceStore()
					.getBoolean(MinimapConstants.INVERT_SWIPE_GESTURE);

			if (invert) {
				dx *= -1;
				dy *= -1;
			}

			Rectangle newSelection = swt.getTranslated(selection, (int) (revertScale * dx), (int) (revertScale * dy));

			canvas.setSelection(newSelection, true);
		}
	}

	private void onMouseDown(Event event) {
		Point location = scaledPoint(event);
		if (state == STATE_NONE) {
			Rectangle selection = canvas.getSelection();
			if (swt.contains(selection, location)) {
				offsetSelection = swt.getCopy(selection);
				offsetLocation = location;
				state = STATE_DRAGGING;
				canvas.setCursor(getGrabCursor());
			} else {
				offsetSelection = swt.getCopy(selection);
				offsetSelection.x = location.x - offsetSelection.width / 2;
				offsetSelection.y = location.y - offsetSelection.height / 2;
				offsetLocation = location;
				state = STATE_DRAGGING;
				canvas.setSelection(offsetSelection, true);
				canvas.setCursor(getGrabCursor());
			}
		}
	}

	private void onMouseMove(Event event) {
		Point location = scaledPoint(event);
		Rectangle selection = canvas.getSelection();

		if (state == STATE_DRAGGING) {
			Point delta = swt.getDifference(offsetLocation, location);
			Rectangle newSelection = swt.getTranslated(offsetSelection, delta);
			canvas.setSelection(newSelection, true);
		}

		else {
			if (swt.contains(selection, location)) {
				canvas.setCursor(getHandCursor());
			} else {
				canvas.setCursor(null);
			}
		}
	}

	private void onMouseUp(Event event) {
		if (state == STATE_DRAGGING) {
			state = STATE_NONE;
		}
	}

	private Point scaledPoint(Event event) {
		float invertedScale = 1f / canvas.getScale();
		return swt.scale(new Point(event.x, event.y), invertedScale);
	}
}
