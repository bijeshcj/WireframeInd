package com.verizontelematics.indrivemobile.utils.ui;


import android.view.View;

/**
 * AnimationRunnable static class.
 * @author hughestelematics
 *
 */
public class AnimationRunnable implements Runnable{
	
	public boolean isStopped() {
		return stopped;
	}

	public void setStopped(boolean stopped) {
		this.stopped = stopped;
	}
	private int STEP = -1;
	private int animStep = MAX_STEP;
	private static final int MAX_STEP = 20;
	private static final long STEP_TIME = 50;
	private static final int MAX_OPACITY_VALUE = 255;
	private int originalColor;
	public AnimationRunnable(View aViewToAnimate, int aOriginalColor) {
		viewToAnimate = aViewToAnimate;
		originalColor = aOriginalColor;
	}
	private boolean stopped = false;
	private View   viewToAnimate = null;
	public void run() {
		if(!stopped){
			if (viewToAnimate != null) {				
				viewToAnimate.setBackgroundColor(getColorForAnimStep(animStep));
				animStep = animStep + STEP;
				if (animStep <= 0 || animStep >= MAX_STEP) {
					STEP = -STEP;
				}				
				viewToAnimate.postDelayed(this, STEP_TIME);				
			}
		}
	}
	
	private int getColorForAnimStep(int aStep) {
		// we start with opacity 0 to opacity ff
		float lOpacity = ((float) MAX_OPACITY_VALUE / (float) MAX_STEP)
				* (float) aStep;
		int lColorAlpha = (0 | (int) lOpacity) << 24;
		return lColorAlpha | originalColor;
	}

	public View getViewToAnimate() {
		return viewToAnimate;
	}

	public void setViewToAnimate(View viewToAnimate) {
		this.viewToAnimate = viewToAnimate;
	}
	
}
