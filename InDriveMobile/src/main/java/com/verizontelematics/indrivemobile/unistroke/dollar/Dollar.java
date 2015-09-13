package com.verizontelematics.indrivemobile.unistroke.dollar;

/*
 *	Java / Java ME port of the $1 Gesture Recognizer by  
 *	Jacob O. Wobbrock, Andrew D. Wilson, Yang Li.
 * 
 *	A quick port that needs to be polished, documented and optimized...!
 *	Send me an e-mail (address can be found at olwal.com) if you'd like to get an update when the library is updated, 
 *  and feel free to send any updates or changes you make!  
 *
 *	@author Alex Olwal
 *
 *	@version 0.1
 *
 *	@see http://depts.washington.edu/aimgroup/proj/dollar/
 *
 */


import java.util.Vector;

public class Dollar
{
	protected int x, y;
	protected int state;
		
	protected int _key = -1;
	
	protected boolean gesture = true; 
	private Vector<Point> points = new Vector<Point>(1000);
	
	private Recognizer recognizer;
	private Result result = new Result("no gesture", 0, -1);
	
	protected boolean active = true;
	
	protected DollarListener listener = null;

	public static final int GESTURES_DEFAULT = 1;
	public static final int GESTURES_SIMPLE = 2;
	public static final int GESTURES_CIRCLES = 3;
	
	protected int gestureSet;
	
	public Dollar()
	{
		this(GESTURES_DEFAULT);
	}


    public Vector<Point> getPoints(){
        return points;
    }

    public Dollar(int gestureSet)
	{
		this.gestureSet = gestureSet;
		recognizer = new Recognizer(gestureSet);
	}
	
	public void setListener(DollarListener listener)
	{
		this.listener = listener;
	}

    // DRAW ON YOUR OWN.
    /*
	public void render(Graphics g) 
	{
		if (!active)
			return;
		
		Point p1, p2;
				
//		g.setColor(0x999999);
		
		for (int i = 0; i < points.size()-1; i++)
		{
			p1 = (Point)points.elementAt(i);
			p2 = (Point)points.elementAt(i+1);
			g.drawLine((int)p1.X, (int)p1.Y, (int)p2.X, (int)p2.Y);
		}		
	}
	*/
	
	public void addPoint(int x, int y)
	{
		if (!active)
			return;
		points.addElement(new Point(x, y));
	}
	
	public void recognize()
	{
		if (!active)
			return;
		
		if (points.size() == 0)
			return;
		
		result = recognizer.Recognize(points);		

		if (listener != null)
			listener.dollarDetected(this);
	}

	public Rectangle getBoundingBox()
	{
		return recognizer.boundingBox;
	}
	
	public int[] getBounds()
	{
		return recognizer.bounds;
	}
	
	public Point getPosition()
	{
		return recognizer.centroid;
	}
	
	public String getName()
	{		
		return result.Name;
	}
	
	public double getScore()
	{
		return result.Score;
	}

	public int getIndex()
	{
		return result.Index;
	}

	public void setActive(boolean state)
	{
		active = state;
	}
	
	public boolean getActive()
	{
		return active;
	}	
	
	public void pointerPressed(int x, int y)
	{
		clear();
	}
	
	public void pointerReleased(int x, int y)
	{
		recognize();
	}
	
	public void pointerDragged(int x, int y)
	{
		addPoint(x, y);
	}
	
	public void clear()
	{
		points.removeAllElements();
		result.Name = "";
		result.Score = 0;
		result.Index = -1;
	}
	
}


