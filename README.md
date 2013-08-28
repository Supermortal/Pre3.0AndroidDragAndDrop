Pre3.0AndroidDragAndDrop
========================

An ontouch listener and interfaces for drag/drop in pre-3.0 Android drag and drop

I've included an example project (created for my Android class), to easily demonstrate/test the listener.

Right now, the listener relies on a RelativeLayout(works with a ScrollView though). It would be nice
if I or someone else could figure out a way for this to work with a ListView or other views.

It's very simple to use, and you can see its use in the example project, but here's a brief overview:

You create the DragDropTouchListener, and then add onDrop and onDrag listeners to the instance of the 
DragDropTouchListener like so:

DragDropTouchListener dndListener = new DragDropTouchListener(mainLayout, mainScrollView, 0);
dndListener.setOnDroppedListener(onDrop);
dndListener.setOnDraggedListener(onDrag);

In this example, onDrop would implement the OnDropListener and onDrag would implement the OnDragListener.

One caveat is that you must return either null or an inflated view from OnDrag; this is your avatar for the 
dragged view. This will allow you to customize the avatar any way that you want to.
