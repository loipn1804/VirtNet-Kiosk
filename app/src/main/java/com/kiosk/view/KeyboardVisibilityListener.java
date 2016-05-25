package com.kiosk.view;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;

public class KeyboardVisibilityListener {

	public interface OnKeyboardVisibilityListener {
		void onVisibilityChanged(boolean visible);
	}

	public final void setKeyboardListener(final OnKeyboardVisibilityListener listener, final Activity activity, int rootViewId) {
		final View root = activity.findViewById(rootViewId);
		root.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				int heightDiff = root.getRootView().getHeight() - root.getHeight();

				Rect rectgle = new Rect();
				Window window = activity.getWindow();
				window.getDecorView().getWindowVisibleDisplayFrame(rectgle);
				int contentViewTop = window.findViewById(Window.ID_ANDROID_CONTENT).getTop();

//				if (heightDiff <= contentViewTop) {
//					// Soft KeyBoard Hidden
//					listener.onVisibilityChanged(false);
//				} else {
//					listener.onVisibilityChanged(true);
//					// Soft KeyBoard Shown
//				}

				if (root.getHeight() > rectgle.bottom + 100) {
					// Soft KeyBoard Hidden
					listener.onVisibilityChanged(true);
				} else {
					listener.onVisibilityChanged(false);
					// Soft KeyBoard Shown
				}
			}
		});
	}
}
