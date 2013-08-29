package com.namespacemedia.CS3270A6;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class DragDropTouchListener implements OnTouchListener {
	
	private RelativeLayout relLayout;
	private ScrollView scrollView;
	
	//View to represent the helper/avatar of a dragged view
	private View avatar;
	private boolean hasAvatar = false;;
	
	//this value is added to the width and height of the drop target to make it easier to hit
	private int marginOfError = 15;
	
	//these values represent the initial values used in calculating the avatar's position
	private int _xDelta = 0;
	private int _yDelta = 0;
	
	private View draggedView = null;
	private View droppedView = null;
	
	private OnDropListener onDrop = null;
	private OnDragListener onDrag = null;
	
	public DragDropTouchListener (RelativeLayout relLayout, ScrollView scrollView, int marginOfError) {
		
		this.relLayout = relLayout;
		this.scrollView = scrollView;
		this.marginOfError = marginOfError;
		
	}
	
	public DragDropTouchListener (RelativeLayout relLayout, ScrollView scrollView) {
		
		this.relLayout = relLayout;
		this.scrollView = scrollView;
		
	}
	
	public DragDropTouchListener (RelativeLayout relLayout) {
		
		this.relLayout = relLayout;
		
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
	    
		final int rawX = (int) event.getRawX();
	    final int rawY = (int) event.getRawY();
	    
	    final float X = event.getX();
	    final float Y = event.getY();
	    
	    int scrollY = 0;
	    if (scrollView != null)
	    	scrollY = scrollView.getScrollY();
	    
	    int scrollX = 0;
	    if (scrollView != null)
	    	scrollX = scrollView.getScrollX();
	       
	    //drag started
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
				
			droppedView = null;			
			draggedView = findViewByCoord(relLayout, X + scrollX, Y + scrollY, false);
			
			if (draggedView != null) {
											
				//these values work the best out of all that I tried; any help improving them would be nice
				RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) draggedView.getLayoutParams();
	            _xDelta = rawX - draggedView.getLeft() + lParams.leftMargin;
	            _yDelta = rawY - draggedView.getTop() + lParams.topMargin;
	            
	            //gets the returned, inflated view from the user's onDrag method
	            if (onDrag != null)
					avatar = onDrag.onDrag(draggedView);
	            
	            if (avatar != null) {
	            	//set initial layout params for the avatar
					RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(draggedView.getWidth(), draggedView.getHeight());
					p.leftMargin = rawX - _xDelta;
					p.topMargin = rawY - _yDelta;
					avatar.setLayoutParams(p);	
					
					relLayout.addView(avatar);	
					relLayout.invalidate();
					
					hasAvatar = true;
	            }
				          
//				draggedView.setVisibility(View.INVISIBLE);
//				draggedView.invalidate();			
	            
			}
			else {
				//return false here to allow other touch events, such as the ScrollView's scroll
				return false;
			}
			    
		}
		
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
										
			if (draggedView != null && hasAvatar) {
								
				//moves the avatar based on the user's touch position
				RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) avatar.getLayoutParams();
	            layoutParams.leftMargin = rawX - _xDelta;
	            layoutParams.topMargin = rawY - _yDelta;
	            layoutParams.rightMargin = -250;
	            layoutParams.bottomMargin = -250;
	            avatar.setLayoutParams(layoutParams);
			}
			else if (draggedView == null) {
				//return false here to allow other touch events, such as the ScrollView's scroll
				return false;
			}
									
		}
		
		//drop event (if the draggedView exists)
		if (event.getAction() == MotionEvent.ACTION_UP) {
			
			if (draggedView != null) {

				droppedView = findViewByCoord(relLayout, X + scrollX, Y + scrollY, true);	
				
				if (droppedView != null && onDrop != null)
					onDrop.onDrop(draggedView, droppedView);
				
				//destroy avatar
				if (hasAvatar) {
					relLayout.removeView(avatar);
					avatar = null;
				}
				
				draggedView = null;
				hasAvatar = false;
				//relLayout.removeAllViews();
			}
			else {
				//return false here to allow other touch events, such as the ScrollView's scroll
				return false;
			}
		}
		
		return true;
	}
		
	public void setOnDroppedListener(OnDropListener onDrop) {
		this.onDrop = onDrop;
	}
	
	public void setOnDraggedListener(OnDragListener onDrag) {
		this.onDrag = onDrag;
	}
	
	//this method searches the RelativeLayout's children for a view based on the x and y coordinates
	//dontUseCurrent, when set to true makes sure the method doesn't return the avatar as the dropped view
	private View findViewByCoord(RelativeLayout layoutToSearch, float x, float y, boolean dontUseCurrent){
		
	    View view = null;
	    int width = 0;
	    int height = 0;
	    int left = 0;
	    int top = 0;
	    
	    int count = layoutToSearch.getChildCount();    
	    for(int i = 0; i < count; i++) {
	    	
	        view = layoutToSearch.getChildAt(i);
	        
	        width = view.getWidth() + marginOfError;
	        height = view.getHeight() + marginOfError;
	        left = view.getLeft();
	        top = view.getTop();
	        
	        if ((dontUseCurrent && draggedView != null && view.getId() != draggedView.getId()) || !dontUseCurrent) {
	        
		        if (x > left &&
		        		x < (left + width) &&
		        		y < (top + height) &&
		        		y > top) {
		        	
		        	return view;
		        	
		        }	        
	        }
	        
	    }
	    
	    return null;
	}
	
}
