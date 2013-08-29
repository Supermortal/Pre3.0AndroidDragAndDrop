package com.namespacemedia.CS3270A6;

import android.view.View;

public interface OnDragListener {
	
	//the user must return a View (or null) to be used as the helper/clone/avatar in the drag motion
	public View onDrag(View draggedView);

}
