package com.tlcsdm.eclipse.minimap.util;

import java.util.Collection;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Pattern;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Resource;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.Widget;

public class SWTExtensions {
	private static Integer MENU_BAR_HEIGHT = null;
	private static Integer TOOLBAR_HEIGHT = null;
	public static final SWTExtensions INSTANCE = new SWTExtensions();

	private GC sharedGC;
	private String osName;

	/**
	 * @since 2.1
	 */
	public final int CORNER_TOP_LEFT = 1;

	/**
	 * @since 2.1
	 */
	public final int CORNER_TOP_RIGHT = 2;
	/**
	 * @since 2.1
	 */
	public final int CORNER_BOTTOM_LEFT = 4;

	/**
	 * @since 2.1
	 */
	public final int CORNER_BOTTOM_RIGHT = 8;

	/**
	 * @since 2.1
	 */
	public final int CORNER_TOP = CORNER_TOP_LEFT | CORNER_TOP_RIGHT;

	/**
	 * @since 2.1
	 */
	public final int CORNER_BOTTOM = CORNER_BOTTOM_LEFT | CORNER_BOTTOM_RIGHT;

	/**
	 * @since 2.1
	 */
	public final int CORNER_LEFT = CORNER_TOP_LEFT | CORNER_BOTTOM_LEFT;

	/**
	 * @since 2.1
	 */
	public final int CORNER_RIGHT = CORNER_TOP_RIGHT | CORNER_BOTTOM_RIGHT;

	/**
	 * @since 2.1
	 */
	public final int CORNER_ALL = CORNER_TOP | CORNER_BOTTOM;

	public Path addArc(Path path, Rectangle box, int startAngle, int angle) {
		path.addArc(box.x, box.y, box.width, box.height, startAngle, angle);
		return path;
	}

	public Path addRectangle(Path path, Rectangle rectangle) {
		path.addRectangle(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
		return path;
	}

	public Path addRoundRectangle(Path path, Rectangle rectangle, int radius) {
		Rectangle box = new Rectangle(0, 0, radius * 2, radius * 2);
		relocateTopRightWith(box, rectangle);

		moveTo(path, getTopRight(rectangle));
		addArc(path, box, 0, 90);

		relocateTopLeftWith(box, rectangle);
		lineTo(path, getTop(box));
		addArc(path, box, 90, 90);

		relocateBottomLeftWith(box, rectangle);
		lineTo(path, getLeft(box));
		addArc(path, box, 180, 90);

		relocateBottomRightWith(box, rectangle);
		lineTo(path, getBottom(box));
		addArc(path, box, 270, 90);
		lineTo(path, translate(getTopRight(rectangle), 0, radius));

		return path;
	}

	public Path addString(Path me, String text, Point location, Font font) {
		me.addString(text, location.x, location.y, font);
		return me;
	}

	/**
	 * Attaches given event {@link Listener} to given {@link Widget}s.
	 * 
	 * @param listener  A {@link Listener} to attach.
	 * @param eventType event type.
	 * @param widgets   {@link Widget}s to attach event {@link Listener}.
	 * @return Given {@link Listener} object.
	 */
	public Listener attachTo(Listener listener, int eventType, Widget... widgets) {
		for (Widget w : widgets) {
			w.addListener(eventType, listener);
		}
		return listener;
	}

	/**
	 * Schedule a disposing of given resources in next event loop.
	 * 
	 * @param resources {@link Resource}s to dispose in next event loop.
	 * @return Given {@link Resource}s.
	 */
	@SafeVarargs
	public final <T extends Resource> T[] autoDispose(T... resources) {
		for (T r : resources) {
			autoDispose(r);
		}
		return resources;
	}

	/**
	 * Promise disposing of given {@link Resource}s when the given {@link Widget} is
	 * disposed.
	 * 
	 * @param widget    {@link Widget} to fire dispose event.
	 * @param resources {@link Resource}s to dispose when given {@link Widget} is
	 *                  disposed.
	 * @return Given {@link Widget}.
	 */
	public <T extends Widget> T chainDispose(T widget, final Resource... resources) {
		widget.addListener(SWT.Dispose, new Listener() {
			@Override
			public void handleEvent(Event event) {
				for (Resource each : resources) {
					safeDispose(each);
				}
			}
		});

		return widget;
	}

	/**
	 * @return a black {@link Color} object.
	 * 
	 * @since 1.1.0
	 */
	public Color COLOR_BLACK() {
		return getDisplay().getSystemColor(SWT.COLOR_BLACK);
	}

	/**
	 * 
	 * @return a blue {@link Color} Object
	 * 
	 * @since 1.1.0
	 */
	public Color COLOR_BLUE() {
		return getDisplay().getSystemColor(SWT.COLOR_BLUE);
	}

	/**
	 * 
	 * @return a cyan {@link Color} object.
	 * @since 1.1.0
	 */
	public Color COLOR_CYAN() {
		return getDisplay().getSystemColor(SWT.COLOR_CYAN);
	}

	/**
	 * 
	 * @return a dark blue {@link Color} object.
	 * @since 1.1.0
	 */
	public Color COLOR_DARK_BLUE() {
		return getDisplay().getSystemColor(SWT.COLOR_DARK_BLUE);
	}

	/**
	 * 
	 * @return a dark cyan {@link Color} object.
	 * @since 1.1.0
	 */
	public Color COLOR_DARK_CYAN() {
		return getDisplay().getSystemColor(SWT.COLOR_DARK_CYAN);
	}

	/**
	 * 
	 * @return a dark gray {@link Color} object.
	 * @since 1.1.0
	 */
	public Color COLOR_DARK_GRAY() {
		return getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY);
	}

	/**
	 * 
	 * @return a dark green {@link Color} object.
	 * @since 1.1.0
	 */
	public Color COLOR_DARK_GREEN() {
		return getDisplay().getSystemColor(SWT.COLOR_DARK_GREEN);
	}

	/**
	 * 
	 * @return a dark magenta {@link Color} object.
	 * @since 1.1.0
	 */
	public Color COLOR_DARK_MARGENTA() {
		return getDisplay().getSystemColor(SWT.COLOR_DARK_MAGENTA);
	}

	/**
	 * 
	 * @return a dark red {@link Color} object.
	 * @since 1.1.0
	 */
	public Color COLOR_DARK_RED() {
		return getDisplay().getSystemColor(SWT.COLOR_DARK_RED);
	}

	/**
	 * 
	 * @return a dark yellow {@link Color} object.
	 * @since 1.1.0
	 */
	public Color COLOR_DARK_YELLOW() {
		return getDisplay().getSystemColor(SWT.COLOR_DARK_YELLOW);
	}

	/**
	 * 
	 * @return a gray {@link Color} object.
	 * @since 1.1.0
	 */
	public Color COLOR_GRAY() {
		return getDisplay().getSystemColor(SWT.COLOR_GRAY);
	}

	/**
	 * 
	 * @return a green {@link Color} object.
	 * @since 1.1.0
	 */
	public Color COLOR_GREEN() {
		return getDisplay().getSystemColor(SWT.COLOR_GREEN);
	}

	/**
	 * 
	 * @return info background {@link Color} object.
	 * @since 1.1.0
	 */
	public Color COLOR_INFO_BACKGROUND() {
		return getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND);
	}

	/**
	 * 
	 * @return info foreground {@link Color} object.
	 * @since 1.1.0
	 */
	public Color COLOR_INFO_FOREGROUND() {
		return getDisplay().getSystemColor(SWT.COLOR_INFO_FOREGROUND);
	}

	/**
	 * 
	 * @return link foreground {@link Color} object.
	 * @since 1.1.0
	 */
	public Color COLOR_LINK_FOREGROUND() {
		return getDisplay().getSystemColor(SWT.COLOR_LINK_FOREGROUND);
	}

	/**
	 * 
	 * @return list background {@link Color} object.
	 * @since 1.1.0
	 */
	public Color COLOR_LIST_BACKGROUND() {
		return getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND);
	}

	/**
	 * 
	 * @return list foreground {@link Color} object.
	 * @since 1.1.0
	 */
	public Color COLOR_LIST_FOREGROUND() {
		return getDisplay().getSystemColor(SWT.COLOR_LIST_FOREGROUND);
	}

	/**
	 * 
	 * @return a magenta {@link Color} object.
	 * @since 1.1.0
	 */
	public Color COLOR_MAGENTA() {
		return getDisplay().getSystemColor(SWT.COLOR_MAGENTA);
	}

	/**
	 * 
	 * @return a red {@link Color} object.
	 * @since 1.1.0
	 */
	public Color COLOR_RED() {
		return getDisplay().getSystemColor(SWT.COLOR_RED);
	}

	/**
	 * 
	 * @return title background {@link Color} object.
	 * @since 1.1.0
	 */
	public Color COLOR_TITLE_BACKGROUND() {
		return getDisplay().getSystemColor(SWT.COLOR_TITLE_BACKGROUND);
	}

	/**
	 * 
	 * @return title background gradient {@link Color} object.
	 * @since 1.1.0
	 */
	public Color COLOR_TITLE_BACKGROUND_GRADIENT() {
		return getDisplay().getSystemColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT);
	}

	/**
	 * 
	 * @return title foreground {@link Color} object.
	 * @since 1.1.0
	 */
	public Color COLOR_TITLE_FOREGROUND() {
		return getDisplay().getSystemColor(SWT.COLOR_TITLE_FOREGROUND);
	}

	/**
	 * 
	 * @return inactive title background {@link Color} object.
	 * @since 1.1.0
	 */
	public Color COLOR_TITLE_INACTIVE_BACKGROUND() {
		return getDisplay().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND);
	}

	/**
	 * 
	 * @return inactive title background {@link Color} object.
	 * @since 1.1.0
	 */
	public Color COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT() {
		return getDisplay().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT);
	}

	/**
	 * 
	 * @return inactive title foreground {@link Color} object.
	 * @since 1.1.0
	 */
	public Color COLOR_TITLE_INACTIVE_FOREGROUND() {
		return getDisplay().getSystemColor(SWT.COLOR_TITLE_INACTIVE_FOREGROUND);
	}

	/**
	 * 
	 * @return a white {@link Color} object.
	 * @since 1.1.0
	 */
	public Color COLOR_WHITE() {
		return getDisplay().getSystemColor(SWT.COLOR_WHITE);
	}

	/**
	 * 
	 * @return widget background {@link Color} object.
	 * @since 1.1.0
	 */
	public Color COLOR_WIDGET_BACKGROUND() {
		return getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
	}

	/**
	 * 
	 * @return widget border {@link Color} object.
	 * @since 1.1.0
	 */
	public Color COLOR_WIDGET_BORDER() {
		return getDisplay().getSystemColor(SWT.COLOR_WIDGET_BORDER);
	}

	/**
	 * 
	 * @return widget dark shadow {@link Color} object.
	 * @since 1.1.0
	 */
	public Color COLOR_WIDGET_DARK_SHADOW() {
		return getDisplay().getSystemColor(SWT.COLOR_WIDGET_DARK_SHADOW);
	}

	/**
	 * 
	 * @return widget foreground {@link Color} object.
	 * @since 1.1.0
	 */
	public Color COLOR_WIDGET_FOREGROUND() {
		return getDisplay().getSystemColor(SWT.COLOR_WIDGET_FOREGROUND);
	}

	/**
	 * 
	 * @return widget highlight shadow {@link Color} object.
	 * @since 1.1.0
	 */
	public Color COLOR_WIDGET_HIGHLIGHT_SHADOW() {
		return getDisplay().getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW);
	}

	/**
	 * 
	 * @return widget light shadow {@link Color} object.
	 * @since 1.1.0
	 */
	public Color COLOR_WIDGET_LIGHT_SHADOW() {
		return getDisplay().getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW);
	}

	/**
	 * 
	 * @return widget normal shadow {@link Color} object.
	 * @since 1.1.0
	 */
	public Color COLOR_WIDGET_NORMAL_SHADOW() {
		return getDisplay().getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW);
	}

	/**
	 * 
	 * @return An yellow {@link Color} object.
	 * @since 1.1.0
	 */
	public Color COLOR_YELLOW() {
		return getDisplay().getSystemColor(SWT.COLOR_YELLOW);
	}

	public Point computeTextExtent(String text, Font font) {
		GC gc = getSharedGC();
		gc.setFont(font);
		return gc.textExtent(text);
	}

	public Point computeTextExtent(String text, Font font, int flags) {
		GC gc = getSharedGC();
		gc.setFont(font);
		return gc.textExtent(text, flags);
	}

	public boolean contains(Point size, Point targetSize) {
		return size.x >= targetSize.x && size.y >= targetSize.y;
	}

	public boolean contains(Rectangle rect, int x, int y) {
		return rect.x <= x && rect.y <= y && x < rect.x + rect.width && y < rect.y + rect.height;
	}

	public boolean contains(Rectangle rect, Point point) {
		return contains(rect, point.x, point.y);
	}

	public boolean contains(Rectangle me, Rectangle other) {
		return me.x <= other.x && me.y <= other.y && (me.x + me.width) >= (other.x + other.width)
				&& (me.y + me.height) >= (other.y + other.height);
	}

	public Color createColor(HSB hsb) {
		return new Color(getDisplay(), hsb.toRGB());
	}

	public Color[] createColors(HSB[] hsb) {
		Color[] result = new Color[hsb.length];
		for (int i = 0; i < hsb.length; i++) {
			result[i] = createColor(hsb[i]);
		}

		return result;
	}

	public Path cubicTo(Path path, Point c1, Point c2, Point destination) {
		path.cubicTo(c1.x, c1.y, c2.x, c2.y, destination.x, destination.y);
		return path;
	}

	public GC draw(GC gc, int[] pointArray) {
		gc.drawPolygon(pointArray);
		return gc;
	}

	public GC draw(GC gc, Path path) {
		gc.drawPath(path);
		return gc;
	}

	public GC draw(GC gc, Point... polygon) {
		int[] array = toIntArray(polygon);
		gc.drawPolygon(array);
		return gc;
	}

	public GC draw(GC gc, Rectangle rectangle) {
		gc.drawRectangle(rectangle);
		return gc;
	}

	public GC drawGradientPath(GC gc, Path path, Color[] colors, int[] percents, boolean vertical) {
		Rectangle oldClip = gc.getClipping();

		float[] fBounds = new float[4];
		path.getBounds(fBounds);
		Rectangle bounds = new Rectangle((int) fBounds[0], (int) fBounds[1], (int) fBounds[2], (int) fBounds[3]);
		bounds = expand(bounds, getOSPathBoundsFix());
		int offset = vertical ? bounds.y : bounds.x;
		int gradientSize = 0;
		for (int i = 1; i < colors.length; i++) {
			Color from = colors[i - 1];
			Color to = colors[i];
			if (vertical) {
				gradientSize = bounds.y + bounds.height * percents[i - 1] / 100 - offset;
			} else {
				gradientSize = bounds.x + bounds.width * percents[i - 1] / 100 - offset;
			}

			if (vertical) {
				Rectangle clip = new Rectangle(bounds.x, offset, bounds.width, gradientSize);
				if (oldClip != null) {
					clip = clip.intersection(oldClip);
				}
				gc.setClipping(clip);
				Pattern pattern = new Pattern(getDisplay(), bounds.x, offset - 1, bounds.x, offset + gradientSize, from,
						to);
				gc.setForegroundPattern(pattern);
				gc.drawPath(path);
				gc.setForegroundPattern(null);
				pattern.dispose();
			} else {
				Rectangle clip = new Rectangle(offset, bounds.y, gradientSize, bounds.height);
				if (oldClip != null) {
					clip = clip.intersection(oldClip);
				}
				gc.setClipping(clip);
				Pattern pattern = new Pattern(getDisplay(), offset - 1, bounds.y, offset + gradientSize, bounds.y, from,
						to);
				gc.setForegroundPattern(pattern);
				gc.drawPath(path);
				gc.setForegroundPattern(null);
				pattern.dispose();
			}
			offset += gradientSize;
		}

		gc.setClipping(oldClip);

		return gc;
	}

	public GC drawImage(GC gc, Image image, Point location) {
		gc.drawImage(image, location.x, location.y);
		return gc;
	}

	public void drawImage(GC gc, Image image, Rectangle sourceArea, Rectangle targetArea) {
		gc.drawImage(image, sourceArea.x, sourceArea.y, sourceArea.width, sourceArea.height, targetArea.x, targetArea.y,
				targetArea.width, targetArea.height);
	}

	public GC drawLine(GC gc, Point... polygon) {
		int[] array = toIntArray(polygon);
		gc.drawPolyline(array);
		return gc;
	}

	public GC drawOval(GC gc, Rectangle rectangle) {
		gc.drawOval(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
		return gc;
	}

	public GC drawRoundRectangle(GC gc, Rectangle rectangle, int radius) {
		gc.drawRoundRectangle(rectangle.x, rectangle.y, rectangle.width, rectangle.height, radius, radius);
		return gc;
	}

	public void drawString(GC gc, String string, Point location) {
		gc.drawString(string, location.x, location.y, true);
	}

	public GC drawTestGrid(GC gc, Rectangle bounds, int gridSize, Color gridColor, int gridAlpha) {
		gc.setForeground(gridColor);
		gc.setLineStyle(SWT.LINE_SOLID);
		gc.setLineWidth(1);
		gc.setAlpha(gridAlpha);
		for (int x = bounds.x; x < bounds.x + bounds.width; x += gridSize) {
			gc.drawLine(x, bounds.y, x, bounds.y + bounds.height);
		}

		for (int y = bounds.y; y < bounds.y + bounds.height; y += gridSize) {
			gc.drawLine(bounds.x, y, bounds.x + bounds.width, y);
		}

		return gc;
	}

	public Rectangle expand(Rectangle rectangle, int amount) {
		return expand(rectangle, amount, amount, amount, amount);
	}

	public Rectangle expand(Rectangle rectangle, int horizontal, int vertical) {
		return expand(rectangle, horizontal, vertical, horizontal, vertical);
	}

	public Rectangle expand(Rectangle rectangle, int left, int top, int right, int bottom) {
		rectangle.x -= left;
		rectangle.y -= top;
		rectangle.width += left + right;
		rectangle.height += top + bottom;
		return rectangle;
	}

	public Rectangle expand(Rectangle rectangle, Point delta) {
		return expand(rectangle, delta.x, delta.y);
	}

	public Rectangle expand(Rectangle rectangle, Rectangle inset) {
		return expand(rectangle, inset.x, inset.y, inset.width, inset.height);
	}

	public GC fill(GC gc, int[] pointArray) {
		gc.fillPolygon(pointArray);
		return gc;
	}

	public GC fill(GC gc, Path path) {
		gc.fillPath(path);
		return gc;
	}

	public GC fill(GC gc, Point... polygon) {
		gc.fillPolygon(toIntArray(polygon));
		return gc;
	}

	public GC fill(GC gc, Rectangle rectangle) {
		gc.fillRectangle(rectangle);
		return gc;
	}

	public GridData FILL_BOTH() {
		GridData gridData = new GridData(GridData.FILL_BOTH);
		return gridData;
	}

	public GridData FILL_HORIZONTAL() {
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		return gridData;
	}

	/**
	 * @since 2.1
	 */
	public GC fillArc(GC gc, Rectangle arcBox, int startAngle, int angle) {
		gc.fillArc(arcBox.x, arcBox.y, arcBox.width, arcBox.height, startAngle, angle);
		return gc;
	}

	public GC fillGradientRectangle(GC gc, Rectangle bounds, Color[] colors, int[] percents, boolean vertical) {
		if (colors == null || percents == null || hasDisposed(colors)) {
			throw new IllegalArgumentException();
		}
		if (colors.length - 1 != percents.length) {
			throw new IllegalArgumentException();
		}

		int offset = vertical ? bounds.y : bounds.x;
		int max = vertical ? bounds.y + bounds.height : bounds.x + bounds.width;
		int gradientSize = 0;

		for (int i = 1; i < colors.length; i++) {
			Color from = colors[i - 1];
			Color to = colors[i];
			if (vertical) {
				gradientSize = bounds.height * percents[i - 1] / 100 - (offset - bounds.y);
			} else {
				gradientSize = bounds.width * percents[i - 1] / 100 - (offset - bounds.x);
			}

			gc.setForeground(from);
			gc.setBackground(to);

			if (vertical) {
				gc.fillGradientRectangle(bounds.x, offset, bounds.width, gradientSize, true);
			} else {
				gc.fillGradientRectangle(offset, bounds.y, gradientSize, bounds.height, false);
			}
			offset += gradientSize;
		}

		if (offset < max) {
			gc.setBackground(colors[colors.length - 1]);
			if (vertical) {
				gc.fillRectangle(bounds.x, offset, bounds.width, max - offset);
			} else {
				gc.fillRectangle(offset, bounds.y, max - offset, bounds.height);
			}
		}

		return gc;
	}

	public GC fillOval(GC gc, Rectangle rectangle) {
		gc.fillOval(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
		return gc;
	}

	public Point getBottom(Rectangle rectangle) {
		return new Point(rectangle.x + rectangle.width / 2, rectangle.y + rectangle.height);
	}

	public Point getBottomLeft(Rectangle rectangle) {
		return new Point(rectangle.x, rectangle.y + rectangle.height);
	}

	public Point getBottomRight(Rectangle rectangle) {
		return new Point(rectangle.x + rectangle.width, rectangle.y + rectangle.height);
	}

	public Rectangle getBounds(Event e) {
		return new Rectangle(e.x, e.y, e.width, e.height);
	}

	public Point getCenter(Rectangle rectangle) {
		return new Point(rectangle.x + rectangle.width / 2, rectangle.y + rectangle.height / 2);
	}

	public Point getCopy(Point point) {
		return new Point(point.x, point.y);
	}

	public Rectangle getCopy(Rectangle rectangle) {
		return new Rectangle(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
	}

	public Point getDifference(Point a, Point b) {
		return new Point(b.x - a.x, b.y - a.y);
	}

	public Display getDisplay() {
		Display display = Display.getDefault();
		return display;
	}

	public double getDistanceTo(Point me, Point to) {
		return Math.sqrt(Math.pow(to.x - me.x, 2) + Math.pow(to.y - me.y, 2));
	}

	public Rectangle getExpanded(Rectangle rectangle, int amount) {
		return expand(getCopy(rectangle), amount);
	}

	public Rectangle getExpanded(Rectangle rectangle, int horizontal, int vertical) {
		return expand(getCopy(rectangle), horizontal, vertical);
	}

	public Rectangle getExpanded(Rectangle rectangle, int left, int top, int right, int bottom) {
		return expand(getCopy(rectangle), left, top, right, bottom);
	}

	public Rectangle getExpanded(Rectangle rectangle, Point delta) {
		return expand(getCopy(rectangle), delta);
	}

	public Rectangle getExpanded(Rectangle rectangle, Rectangle insets) {
		return expand(getCopy(rectangle), insets);
	}

	public int getHeight(Point size) {
		return size.y;
	}

	public ImageRegistry getImageRegistry() {
		ImageRegistry result = (ImageRegistry) Display.getDefault().getData("imageRegistry");
		if (result == null) {
			result = new ImageRegistry();
			Display.getDefault().setData("imageRegistry", result);
		}
		return result;
	}

	public Point getLeft(Rectangle rectangle) {
		return new Point(rectangle.x, rectangle.y + rectangle.height / 2);
	}

	public Point getLocation(Event e) {
		return new Point(e.x, e.y);
	}

	public Point getLocation(Rectangle rectangle) {
		return new Point(rectangle.x, rectangle.y);
	}

	public int getMenubarHeight() {
		if (MENU_BAR_HEIGHT != null) {
			return MENU_BAR_HEIGHT;
		}

		if (Display.getCurrent() == null) {
			throw new SWTException("Invalid Thread Exception");
		}

		Shell dummy = new Shell();
		Menu menu = new Menu(dummy, SWT.BAR);
		dummy.setMenuBar(menu);
		Rectangle boundsWithMenu = dummy.computeTrim(0, 0, 0, 0);

		dummy.setMenuBar(null);
		Rectangle boundsWithoutMenu = dummy.computeTrim(0, 0, 0, 0);

		dummy.dispose();

		MENU_BAR_HEIGHT = boundsWithMenu.height - boundsWithoutMenu.height;

		return MENU_BAR_HEIGHT;
	}

	/**
	 * @return returns minimum height of a toolbar that contains an tool item who
	 *         has an 16x16 icon.
	 * 
	 * @since 2.2
	 */
	public int getMinimumToolBarHeight() {
		if (TOOLBAR_HEIGHT != null) {
			return TOOLBAR_HEIGHT;
		}

		if (Display.getCurrent() == null) {
			throw new SWTException("Invalid Thread Exception");
		}

		Shell dummy = new Shell();
		dummy.setLayout(new GridLayout());
		ToolBar toolBar = new ToolBar(dummy, SWT.FLAT | SWT.RIGHT);
		ToolItem anItem = new ToolItem(toolBar, SWT.NORMAL);
		anItem.setText("test");
		Image anImage = new Image(getDisplay(), 16, 16);
		anItem.setImage(anImage);

		dummy.pack();

		TOOLBAR_HEIGHT = toolBar.computeSize(-1, -1).y;
		dummy.dispose();
		anImage.dispose();

		return TOOLBAR_HEIGHT;
	}

	public Point getNegated(Point me) {
		return negate(getCopy(me));
	}

	private String getOS() {
		if (osName == null) {
			osName = System.getProperty("os.name");
		}
		return osName;
	}

	private Rectangle getOSPathBoundsFix() {
		String osName = getOS();
		if (osName.startsWith("Windows")) {
			return newInsets(0, 0, 3, 2);
		} else {
			return newInsets(0, 0, 1, 1);
		}
	}

	public Rectangle getRelocatedBottomLeftWith(Rectangle me, Point BottomLeft) {
		return relocateBottomLeftWith(getCopy(me), BottomLeft);
	}

	public Rectangle getRelocatedBottomLeftWith(Rectangle me, Rectangle offset) {
		return relocateBottomLeftWith(getCopy(me), offset);
	}

	public Rectangle getRelocatedBottomRightWith(Rectangle me, Point BottomRight) {
		return relocateBottomRightWith(getCopy(me), BottomRight);
	}

	public Rectangle getRelocatedBottomRightWith(Rectangle me, Rectangle offset) {
		return relocateBottomRightWith(getCopy(me), offset);
	}

	public Rectangle getRelocatedBottomTo(Rectangle me, Rectangle offset) {
		return relocateBottomWith(getCopy(me), offset);
	}

	public Rectangle getRelocatedBottomWith(Rectangle me, Point Bottom) {
		return relocateBottomWith(getCopy(me), Bottom);
	}

	public Rectangle getRelocatedBottomWith(Rectangle me, Rectangle offset) {
		return relocateBottomWith(getCopy(me), offset);
	}

	public Rectangle getRelocatedCenterWith(Rectangle me, Point Center) {
		return relocateCenterWith(getCopy(me), Center);
	}

	public Rectangle getRelocatedCenterWith(Rectangle me, Rectangle offset) {
		return relocateCenterWith(getCopy(me), offset);
	}

	public Rectangle getRelocatedCetnerWith(Rectangle me, Rectangle offset) {
		return relocateCenterWith(getCopy(me), offset);
	}

	public Rectangle getRelocatedLeftWith(Rectangle me, Point Left) {
		return relocateLeftWith(getCopy(me), Left);
	}

	public Rectangle getRelocatedLeftWith(Rectangle me, Rectangle offset) {
		return relocateLeftWith(getCopy(me), offset);
	}

	public Rectangle getRelocatedRightWith(Rectangle me, Point right) {
		return relocateRightWith(getCopy(me), right);
	}

	public Rectangle getRelocatedRightWith(Rectangle me, Rectangle offset) {
		return relocateRightWith(getCopy(me), offset);
	}

	public Rectangle getRelocatedTopLeftWith(Rectangle me, Point topLeft) {
		return relocateTopLeftWith(getCopy(me), topLeft);
	}

	public Rectangle getRelocatedTopLeftWith(Rectangle me, Rectangle offset) {
		return relocateTopLeftWith(getCopy(me), offset);
	}

	public Rectangle getRelocatedTopRightWith(Rectangle me, Point topRight) {
		return relocateTopRightWith(getCopy(me), topRight);
	}

	public Rectangle getRelocatedTopRightWith(Rectangle me, Rectangle offset) {
		return relocateTopRightWith(getCopy(me), offset);
	}

	public Rectangle getRelocatedTopWith(Rectangle me, Point topLeft) {
		return relocateTopWith(getCopy(me), topLeft);
	}

	public Rectangle getRelocatedTopWith(Rectangle me, Rectangle offset) {
		return relocateTopWith(getCopy(me), offset);
	}

	public Rectangle getResized(Rectangle rectangle, int widthDelta, int heightDelta) {
		return resize(getCopy(rectangle), widthDelta, heightDelta);
	}

	public Rectangle getResized(Rectangle rectangle, Point sizeDelta) {
		return getResized(rectangle, sizeDelta.x, sizeDelta.y);
	}

	public Point getRight(Rectangle rectangle) {
		return new Point(rectangle.x + rectangle.width, rectangle.y + rectangle.height / 2);
	}

	public Point getScaled(Point p, double scale) {
		return scale(getCopy(p), scale);
	}

	public Rectangle getScaled(Rectangle r, double scale) {
		return scale(getCopy(r), scale);
	}

	public GC getSharedGC() {
		if (sharedGC == null || sharedGC.isDisposed()) {
			sharedGC = new GC(getDisplay());
		}
		return sharedGC;
	}

	public Rectangle getShrinked(Rectangle rectangle, int amount) {
		return shrink(getCopy(rectangle), amount);
	}

	public Rectangle getShrinked(Rectangle rectangle, int horizontal, int vertical) {
		return shrink(getCopy(rectangle), horizontal, vertical);
	}

	public Rectangle getShrinked(Rectangle rectangle, int left, int top, int right, int bottom) {
		return shrink(getCopy(rectangle), left, top, right, bottom);
	}

	public Rectangle getShrinked(Rectangle rectangle, Point delta) {
		return shrink(getCopy(rectangle), delta);
	}

	public Rectangle getShrinked(Rectangle rectangle, Rectangle insets) {
		return shrink(getCopy(rectangle), insets);
	}

	public Point getSize(Event e) {
		return new Point(e.width, e.height);
	}

	public Point getSize(ImageData imageData) {
		return new Point(imageData.width, imageData.height);
	}

	public Point getSize(Rectangle rect) {
		return new Point(rect.width, rect.height);
	}

	public Point getTop(Rectangle rectangle) {
		return new Point(rectangle.x + rectangle.width / 2, rectangle.y);
	}

	public Point getTopLeft(Rectangle rectangle) {
		return new Point(rectangle.x, rectangle.y);
	}

	public Point getTopRight(Rectangle rectangle) {
		return new Point(rectangle.x + rectangle.width, rectangle.y);
	}

	public Point getTranslated(Point point, int dx, int dy) {
		return translate(getCopy(point), dx, dy);
	}

	public Point getTranslated(Point point, Point delta) {
		return getTranslated(point, delta.x, delta.y);
	}

	public Rectangle getTranslated(Rectangle source, int dx, int dy) {
		return translate(getCopy(source), dx, dy);
	}

	public Rectangle getTranslated(Rectangle source, Point delta) {
		return translate(getCopy(source), delta);
	}

	public Point getTransposed(Point me) {
		return new Point(me.y, me.x);
	}

	public Rectangle getTransposed(Rectangle me) {
		return new Rectangle(me.y, me.x, me.height, me.width);
	}

	public Rectangle getUnionized(Rectangle me, int x, int y) {
		return union(getCopy(me), x, y);
	}

	public Rectangle getUnionized(Rectangle me, int x, int y, int w, int h) {
		return union(getCopy(me), x, y, w, h);
	}

	public Rectangle getUnionized(Rectangle me, Point point) {
		return union(getCopy(me), point);
	}

	public Rectangle getUnionized(Rectangle me, Rectangle other) {
		return union(getCopy(me), other);
	}

	public int getWidth(Point size) {
		return size.x;
	}

	public boolean hasDisposed(Resource... resources) {
		for (Resource r : resources) {
			if (r == null)
				throw new IllegalArgumentException();

			if (r.isDisposed()) {
				return true;
			}
		}
		return false;
	}

	public boolean hasFlags(int flags, int... mask) {
		for (int each : mask) {
			if ((flags & each) == 0) {
				return false;
			}
		}
		return true;
	}

	public Image ICON_CANCEL() {
		return getDisplay().getSystemImage(SWT.ICON_CANCEL);
	}

	public Image ICON_ERROR() {
		return getDisplay().getSystemImage(SWT.ICON_ERROR);
	}

	public Image ICON_INFORMATION() {
		return getDisplay().getSystemImage(SWT.ICON_INFORMATION);
	}

	public Image ICON_QUESTION() {
		return getDisplay().getSystemImage(SWT.ICON_QUESTION);
	}

	public Image ICON_SEARCH() {
		return getDisplay().getSystemImage(SWT.ICON_SEARCH);
	}

	public Image ICON_WARNING() {
		return getDisplay().getSystemImage(SWT.ICON_WARNING);
	}

	public Image ICON_WORKING() {
		return getDisplay().getSystemImage(SWT.ICON_WORKING);
	}

	public boolean isAlive(Resource r) {
		return r != null && !r.isDisposed();
	}

	public boolean isAlive(Widget w) {
		return w != null && !w.isDisposed();
	}

	public boolean isEmpty(Point point) {
		return point.x == 0 && point.y == 0;
	}

	public Path lineTo(Path path, Point location) {
		path.lineTo(location.x, location.y);
		return path;
	}

	/**
	 * 
	 * @param path
	 * @param location
	 * @since 1.2.0
	 * @return current context.
	 */
	public Path moveTo(Path path, Point location) {
		path.moveTo(location.x, location.y);
		return path;
	}

	public Point negate(Point point) {
		return new Point(-point.x, -point.y);
	}

	public GridLayout newGridLayout() {
		return new GridLayout();
	}

	/**
	 * 
	 * @param width
	 * @param height
	 * @return
	 * 
	 * @since 2.1
	 */
	public Image newImage(int width, int height) {
		Image image = new Image(getDisplay(), width, height);
		return image;
	}

	public Rectangle newInsets(int insets) {
		return new Rectangle(insets, insets, insets, insets);
	}

	public Rectangle newInsets(int left, int top, int right, int bottom) {
		return new Rectangle(left, top, right, bottom);
	}

	public <T extends Widget> void onEvent(final T widget, int eventType, final Listener handler) {
		widget.addListener(eventType, handler);
	}

	/**
	 * Opens a given {@link Shell}, and runs event loop until given shell is
	 * disposed.
	 * 
	 * @param s {@link Shell} to open.
	 */
	public void openAndRunLoop(final Shell s) {
		if (s.isDisposed()) {
			return;
		}
		s.open();
		while (!s.isDisposed()) {
			if (!getDisplay().readAndDispatch()) {
				getDisplay().sleep();
			}
		}
	}

	public int operator_and(int e1, int e2) {
		return e1 & e2;
	}

	public int operator_or(int e1, int e2) {
		return e1 | e2;
	}

	public Point pointAt(Rectangle bounds, double xRatio, double yRatio) {
		return new Point((int) (bounds.x + bounds.width * xRatio), (int) (bounds.y + bounds.height * yRatio));
	}

	public Path quadTo(Path path, Point c, Point destination) {
		path.quadTo(c.x, c.y, destination.x, destination.y);
		return path;
	}

	public Rectangle relocateBottomLeftWith(Rectangle me, Point bottomLeft) {
		me.x = bottomLeft.x;
		me.y = bottomLeft.y - me.height;
		return me;
	}

	public Rectangle relocateBottomLeftWith(Rectangle me, Rectangle offset) {
		return relocateBottomLeftWith(me, getBottomLeft(offset));
	}

	public Rectangle relocateBottomRightWith(Rectangle me, Point bottomRight) {
		me.x = bottomRight.x - me.width;
		me.y = bottomRight.y - me.height;
		return me;
	}

	public Rectangle relocateBottomRightWith(Rectangle me, Rectangle offset) {
		return relocateBottomRightWith(me, getBottomRight(offset));
	}

	public Rectangle relocateBottomWith(Rectangle me, Point bottom) {
		me.x = bottom.x - me.width / 2;
		me.y = bottom.y - me.height;
		return me;
	}

	public Rectangle relocateBottomWith(Rectangle me, Rectangle offset) {
		return relocateBottomWith(me, getBottom(offset));
	}

	public Rectangle relocateCenterWith(Rectangle me, Point center) {
		me.x = center.x - me.width / 2;
		me.y = center.y - me.height / 2;
		return me;
	}

	public Rectangle relocateCenterWith(Rectangle me, Rectangle other) {
		return relocateCenterWith(me, getCenter(other));
	}

	public Rectangle relocateLeftWith(Rectangle me, Point left) {
		me.x = left.x;
		me.y = left.y - me.height / 2;
		return me;
	}

	public Rectangle relocateLeftWith(Rectangle me, Rectangle offset) {
		return relocateLeftWith(me, getLeft(offset));
	}

	public Rectangle relocateRightWith(Rectangle me, Point right) {
		me.x = right.x - me.width;
		me.y = right.y - me.height / 2;
		return me;
	}

	public Rectangle relocateRightWith(Rectangle me, Rectangle offset) {
		return relocateRightWith(me, getRight(offset));
	}

	public Rectangle relocateTopLeftWith(Rectangle me, Point topLeft) {
		me.x = topLeft.x;
		me.y = topLeft.y;
		return me;
	}

	public Rectangle relocateTopLeftWith(Rectangle me, Rectangle offset) {
		return relocateTopLeftWith(me, getTopLeft(offset));
	}

	public Rectangle relocateTopRightWith(Rectangle me, Point topRight) {
		me.x = topRight.x - me.width;
		me.y = topRight.y;
		return me;
	}

	public Rectangle relocateTopRightWith(Rectangle me, Rectangle offset) {
		return relocateTopRightWith(me, getTopRight(offset));
	}

	public Rectangle relocateTopWith(Rectangle me, Point top) {
		me.x = top.x - me.width / 2;
		me.y = top.y;
		return me;
	}

	public Rectangle relocateTopWith(Rectangle me, Rectangle offset) {
		return relocateTopWith(me, getTop(offset));
	}

	public Rectangle resize(Rectangle rectangle, int width, int height) {
		rectangle.width += width;
		rectangle.height += height;
		return rectangle;
	}

	public Rectangle resize(Rectangle rectangle, Point delta) {
		return resize(rectangle, delta.x, delta.y);
	}

	public void runLoop(final Shell s) {
		if (s.isDisposed()) {
			return;
		}
		while (!s.isDisposed()) {
			if (!getDisplay().readAndDispatch()) {
				getDisplay().sleep();
			}
		}
	}

	public <T extends Resource> void safeDispose(Collection<T> resource) {
		for (T r : resource) {
			safeDispose(r);
		}
	}

	@SafeVarargs
	public final <T extends Resource> void safeDispose(T... resource) {
		for (Resource r : resource) {
			safeDispose(r);
		}
	}

	/**
	 * @since 2.2
	 * @param widgets
	 */
	@SafeVarargs
	public final <W extends Widget> void safeDispose(W... widgets) {
		for (Widget w : widgets) {
			safeDispose(w);
		}
	}

	/**
	 * @since 2.2
	 * @param widget
	 */
	public <W extends Widget> void safeDispose(W widget) {
		if (widget != null && !widget.isDisposed()) {
			widget.dispose();
		}
	}

	public <T extends Resource> void safeDispose(T r) {
		if (r != null && !r.isDisposed()) {
			r.dispose();
		}
	}

	public Point scale(Point p, double scale) {
		p.x *= scale;
		p.y *= scale;
		return p;
	}

	public Rectangle scale(Rectangle p, double scale) {
		p.x *= scale;
		p.y *= scale;
		p.width *= scale;
		p.height *= scale;
		return p;
	}

	public Transform scale(Transform me, float scale) {
		me.scale(scale, scale);
		return me;
	}

	public Rectangle setBottom(Rectangle me, int bottom) {
		me.height = bottom - me.y;
		return me;
	}

	public Rectangle setBottomLeft(Rectangle me, int x, int y) {
		setLeft(me, x);
		setBottom(me, y);
		return me;
	}

	public Rectangle setBottomLeft(Rectangle me, Point bottomLeft) {
		return setBottomLeft(me, bottomLeft.x, bottomLeft.y);
	}

	public Rectangle setBottomRight(Rectangle me, int x, int y) {
		setRight(me, x);
		setBottom(me, y);
		return me;
	}

	public Rectangle setBottomRight(Rectangle me, Point bottomRight) {
		return setBottomRight(me, bottomRight.x, bottomRight.y);
	}

	public int setHeight(Point size, int height) {
		size.y = height;
		return height;
	}

	public Rectangle setLeft(Rectangle me, int left) {
		int right = me.x + me.width;
		me.x = left;
		me.width = right - me.x;
		return me;
	}

	public Rectangle setLocation(Rectangle rectangle, int x, int y) {
		rectangle.x = x;
		rectangle.y = y;
		return rectangle;
	}

	public Rectangle setLocation(Rectangle rectangle, Point location) {
		rectangle.x = location.x;
		rectangle.y = location.y;
		return rectangle;
	}

	public <T extends Shell> void setOnActivate(final T control, final Listener handler) {
		control.addListener(SWT.Activate, handler);
	}

	public <T extends MenuItem> void setOnArm(final T control, final Listener handler) {
		control.addListener(SWT.Arm, handler);
	}

	public void setOnClick(final Control button, final Listener listener) {
		button.addListener(SWT.Selection, listener);
	}

	public void setOnClose(final Shell control, final Listener listener) {
		control.addListener(SWT.Close, listener);
	}

	public void setOnCollapse(final Tree control, final Listener listener) {
		control.addListener(SWT.Collapse, listener);
	}

	public void setOnDeactivate(final Shell shell, final Listener handler) {
		shell.addListener(SWT.Deactivate, handler);
	}

	public void setOnDefaultSelection(final Control control, final Listener listener) {
		control.addListener(SWT.DefaultSelection, listener);
	}

	public void setOnDeiconify(final Shell control, final Listener listener) {
		control.addListener(SWT.Deiconify, listener);
	}

	public void setOnDispose(final Control control, final Listener listener) {
		control.addListener(SWT.Dispose, listener);
	}

	public <T extends Control> void setOnDragDetect(final T control, final Listener handler) {
		control.addListener(SWT.DragDetect, handler);
	}

	public <T extends Control> void setOnEraseItem(final T control, final Listener handler) {
		control.addListener(SWT.EraseItem, handler);
	}

	public <T extends Widget> void setOnEvent(final T widget, int eventType, final Listener handler) {
		widget.addListener(eventType, handler);
	}

	public void setOnExpand(final Tree control, final Listener listener) {
		control.addListener(SWT.Expand, listener);
	}

	public void setOnFocus(final Control control, final Listener handler) {
		control.addListener(SWT.FocusIn, handler);
	}

	public void setOnFocusIn(final Control control, final Listener listener) {
		control.addListener(SWT.FocusIn, listener);
	}

	public void setOnFocusOut(final Control control, final Listener handler) {
		control.addListener(SWT.FocusOut, handler);
	}

	public <T extends Control> void setOnHelp(final T control, final Listener handler) {
		control.addListener(SWT.Help, handler);
	}

	public void setOnHide(final Shell control, final Listener listener) {
		control.addListener(SWT.Hide, listener);
	}

	public void setOnIconify(final Shell control, final Listener listener) {
		control.addListener(SWT.Iconify, listener);
	}

	public void setOnKeyDown(final Control button, final Listener listener) {
		button.addListener(SWT.KeyDown, listener);
	}

	public void setOnKeyUp(final Control button, final Listener listener) {
		button.addListener(SWT.KeyUp, listener);
	}

	public <T extends Control> void setOnMeasureItem(final T control, Listener handler) {
		control.addListener(SWT.MeasureItem, handler);
	}

	public <T extends Control> void setOnModified(final T control, final Listener handler) {
		control.addListener(SWT.Modify, handler);
	}

	public <T extends Control> void setOnModify(final T control, final Listener handler) {
		control.addListener(SWT.Modify, handler);
	}

	public void setOnMouseDoubleClick(final Control button, final Listener listener) {
		button.addListener(SWT.MouseDoubleClick, listener);
	}

	public void setOnMouseDown(final Control button, final Listener listener) {
		button.addListener(SWT.MouseDown, listener);
	}

	public void setOnMouseEnter(final Control button, final Listener listener) {
		button.addListener(SWT.MouseEnter, listener);
	}

	public void setOnMouseExit(final Control button, final Listener listener) {
		button.addListener(SWT.MouseExit, listener);
	}

	public <T extends Control> void setOnMouseHorizontalWheel(final T control, final Listener handler) {
		control.addListener(SWT.MouseHorizontalWheel, handler);
	}

	public <T extends Control> void setOnMouseHover(final T control, final Listener handler) {
		control.addListener(SWT.MouseHover, handler);
	}

	public void setOnMouseMove(final Control button, final Listener listener) {
		button.addListener(SWT.MouseMove, listener);
	}

	public void setOnMouseUp(final Control button, final Listener listener) {
		button.addListener(SWT.MouseUp, listener);
	}

	public <T extends Control> void setOnMouseVerticalWheel(final T control, final Listener handler) {
		control.addListener(SWT.MouseVerticalWheel, handler);
	}

	public <T extends Control> void setOnMouseWheel(final T control, final Listener handler) {
		control.addListener(SWT.MouseWheel, handler);
	}

	public void setOnMove(final Control control, final Listener listener) {
		control.addListener(SWT.Move, listener);
	}

	public void setOnPaint(Control control, Listener renderer) {
		control.addListener(SWT.Paint, renderer);
	}

	public <T extends Control> void setOnPaintItem(final T control, Listener handler) {
		control.addListener(SWT.PaintItem, handler);
	}

	public void setOnResize(final Control control, Listener listener) {
		control.addListener(SWT.Resize, listener);
	}

	public <T extends Widget> void setOnSelection(final T w, final Listener handler) {
		w.addListener(SWT.Selection, handler);
	}

	public void setOnShow(final Shell shell, Listener listener) {
		shell.addListener(SWT.Show, listener);
	}

	public <T extends Control> void setOnTraverse(final T control, final Listener handler) {
		control.addListener(SWT.Traverse, handler);
	}

	public <T extends Control> void setOnVerify(final T control, final Listener handler) {
		control.addListener(SWT.Verify, handler);
	}

	public Rectangle setRight(Rectangle me, int right) {
		me.width = right - me.x;
		return me;
	}

	public Rectangle setSize(Rectangle rectangle, int width, int height) {
		rectangle.width = width;
		rectangle.height = height;
		return rectangle;
	}

	public Rectangle setSize(Rectangle rectangle, Point size) {
		rectangle.width = size.x;
		rectangle.height = size.y;
		return rectangle;
	}

	public Rectangle setTop(Rectangle me, int top) {
		int bottom = me.y + me.height;
		me.y = top;
		me.height = bottom - top;
		return me;
	}

	public Rectangle setTopLeft(Rectangle me, int x, int y) {
		setLeft(me, x);
		setTop(me, y);
		return me;
	}

	public Rectangle setTopLeft(Rectangle me, Point topLeft) {
		return setTopLeft(me, topLeft.x, topLeft.y);
	}

	public Rectangle setTopRight(Rectangle me, int x, int y) {
		setRight(me, x);
		setTop(me, y);
		return me;
	}

	public Rectangle setTopRight(Rectangle me, Point topRight) {
		return setTopRight(me, topRight.x, topRight.y);
	}

	public int setWidth(Point size, int width) {
		size.x = width;
		return width;
	}

	public String shortenText(GC gc, String text, int width, String ellipses) {
		return shortenText(gc, text, width, ellipses, SWT.DRAW_TRANSPARENT | SWT.DRAW_MNEMONIC);
	}

	public String shortenText(GC gc, String text, int width, String ellipses, int flags) {
		if (gc.textExtent(text, flags).x <= width)
			return text;
		int ellipseWidth = gc.textExtent(ellipses, flags).x;
		int length = text.length();
		TextLayout layout = new TextLayout(getDisplay());
		layout.setText(text);
		int end = layout.getPreviousOffset(length, SWT.MOVEMENT_CLUSTER);
		while (end > 0) {
			text = text.substring(0, end);
			int l = gc.textExtent(text, flags).x;
			if (l + ellipseWidth <= width) {
				break;
			}
			end = layout.getPreviousOffset(end, SWT.MOVEMENT_CLUSTER);
		}
		layout.dispose();
		return end == 0 ? text.substring(0, 1) : text + ellipses;
	}

	/**
	 * Promise disposing of given {@link Resource} when given {@link Widget} is
	 * disposed.
	 * 
	 * @param resource {@link Resource} to dispose when given {@link Widget} is
	 *                 disposed.
	 * @param widget   {@link Widget} to fire dispose event.
	 * @return given {@link Resource}.
	 */
	public <T extends Resource> T shouldDisposeWith(final T resource, Widget widget) {
		widget.addListener(SWT.Dispose, new Listener() {
			@Override
			public void handleEvent(Event event) {
				safeDispose(resource);
			}
		});

		return resource;
	}

	public <T extends Control> T showTestGrid(T control) {
		return showTestGrid(control, 10, COLOR_RED(), 100);
	}

	public <T extends Control> T showTestGrid(final T control, final int gridSize, final Color color, final int alpha) {
		control.addListener(SWT.Paint, new Listener() {
			@Override
			public void handleEvent(Event event) {
				Rectangle relativeBounds = relocateTopLeftWith(control.getBounds(), new Point(0, 0));
				drawTestGrid(event.gc, relativeBounds, gridSize, color, alpha);
			}
		});

		return control;
	}

	public Rectangle shrink(Rectangle rectangle, int amount) {
		return expand(rectangle, -amount);
	}

	public Rectangle shrink(Rectangle rectangle, int horizontal, int vertical) {
		return expand(rectangle, -horizontal, -vertical);
	}

	public Rectangle shrink(Rectangle rectangle, int left, int top, int right, int bottom) {
		return expand(rectangle, -left, -top, -right, -bottom);
	}

	public Rectangle shrink(Rectangle rectangle, Point delta) {
		return expand(rectangle, -delta.x, -delta.y);
	}

	public Rectangle shrink(Rectangle rectangle, Rectangle inset) {
		return expand(rectangle, -inset.x, -inset.y, -inset.width, -inset.height);
	}

	/**
	 * Converts {@link Color} object to {@link HSB} Object.
	 * 
	 * @param color
	 * @return A {@link HSB} Object.
	 * @since 1.1.0
	 */
	public HSB toHSB(Color color) {
		return new HSB(color.getRGB());
	}

	/**
	 * Converts {@link RGB} object to {@link HSB} object.
	 * 
	 * @param rgb
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public HSB toHSB(RGB rgb) {
		return new HSB(rgb);
	}

	public HSB[] toHSBArray(Color[] colors) {
		HSB[] result = new HSB[colors.length];
		for (int i = 0; i < colors.length; i++) {
			result[i] = new HSB(colors[i].getRGB());
		}
		return result;
	}

	public String toHTMLCode(RGB rgb) {
		return String.format("#%02x%02x%02x", rgb.red, rgb.green, rgb.blue);
	}

	private int[] toIntArray(Point... polygon) {
		int[] array = new int[polygon.length * 2];
		int index = 0;
		for (Point each : polygon) {
			array[index++] = each.x;
			array[index++] = each.y;
		}
		return array;
	}

	public Rectangle toRectangle(Point point) {
		return new Rectangle(point.x, point.y, 0, 0);
	}

	public Point translate(Point point, int dx, int dy) {
		point.x += dx;
		point.y += dy;
		return point;
	}

	public Point translate(Point point, Point delta) {
		return translate(point, delta.x, delta.y);
	}

	public Rectangle translate(Rectangle rectangle, int dx, int dy) {
		rectangle.x += dx;
		rectangle.y += dy;
		return rectangle;
	}

	public Rectangle translate(Rectangle rectangle, Point delta) {
		return translate(rectangle, delta.x, delta.y);
	}

	public Transform translate(Transform me, Point location) {
		me.translate(location.x, location.y);
		return me;
	}

	public Point transpose(Point me) {
		Point copy = getCopy(me);
		me.x = copy.y;
		me.y = copy.x;
		return me;
	}

	public Rectangle transpose(Rectangle me) {
		Rectangle copy = getCopy(me);
		me.x = copy.y;
		me.y = copy.x;
		me.width = copy.height;
		me.height = copy.width;
		return me;
	}

	public Rectangle union(Rectangle rectangle, int x, int y) {
		if (x < rectangle.x) {
			rectangle.width += rectangle.x - x;
			rectangle.x = x;
		} else {
			int right = rectangle.x + rectangle.width;
			if (x >= right) {
				right = x + 1;
				rectangle.width = right - rectangle.x;
			}
		}

		if (y < rectangle.y) {
			rectangle.height += rectangle.y - y;
			rectangle.y = y;
		} else {
			int bottom = rectangle.y + rectangle.height;
			if (y >= bottom) {
				bottom = y + 1;
				rectangle.height = bottom - rectangle.y;
			}
		}

		return rectangle;
	}

	public Rectangle union(Rectangle me, int x, int y, int w, int h) {
		int right = Math.max(me.x + me.width, x + w);
		int bottom = Math.max(me.y + me.height, y + h);
		me.x = Math.min(me.x, x);
		me.y = Math.min(me.y, y);
		me.width = right - me.x;
		me.height = bottom - me.y;
		return me;
	}

	public Rectangle union(Rectangle me, Point point) {
		return union(me, point.x, point.y);
	}

	public Rectangle union(Rectangle me, Rectangle other) {
		return union(me, other.x, other.y, other.width, other.height);
	}

	public boolean intersects(Rectangle rectangle, int x, int y) {
		if (x == rectangle.x || x == rectangle.x + rectangle.width) {
			return rectangle.y <= y && y <= rectangle.y + rectangle.height;
		} else if (y == rectangle.y || y == rectangle.y + rectangle.height) {
			return rectangle.x <= x && x <= rectangle.x + rectangle.width;
		} else
			return false;
	}

	public boolean intersects(Rectangle rectangle, Point point) {
		return intersects(rectangle, point.x, point.y);
	}
}
