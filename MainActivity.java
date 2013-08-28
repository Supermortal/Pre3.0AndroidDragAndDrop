package com.namespacemedia.CS3270A6;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private DragDropTouchListener dndListener;
	private OnDropListener onDrop = new OnDropListener() {

		@Override
		public void onDrop(View draggedView, View droppedView) {
			
			LinearLayout dropView = (LinearLayout) droppedView;			
			TextView dropPlayer = (TextView) dropView.findViewById(R.id.playerNameText);
			String dropBattingOrder = ((TextView) dropView.findViewById(R.id.battingOrderText)).getText().toString();
			
			Log.i("Drop", dropPlayer.getText().toString());
			
			LinearLayout dragView = (LinearLayout) draggedView;
			String dragBattingOrder = ((TextView) dragView.findViewById(R.id.battingOrderText)).getText().toString();
			
			String storedDragPlayer = players.getString(dragBattingOrder, null);
			String storedDropPlayer = players.getString(dropBattingOrder, null);
			
			SharedPreferences.Editor e = players.edit();
			
			e.putString(dragBattingOrder, storedDropPlayer);
			e.putString(dropBattingOrder, storedDragPlayer);
			
			e.apply();
			
			createPlayerList();
			
		}
		
	};
	private OnDragListener onDrag = new OnDragListener() {

		@Override
		public View onDrag(View draggedView) {
			
			LinearLayout view = (LinearLayout) draggedView;
					
			TextView oldOrder = (TextView) view.getChildAt(0);
			TextView oldPlayer = (TextView) view.getChildAt(1);
			
			Log.i("Drag", oldPlayer.getText().toString());
			
			LayoutInflater inflater = (LayoutInflater) getSystemService
				      (Context.LAYOUT_INFLATER_SERVICE);
			LinearLayout avatar = (LinearLayout) inflater.inflate(R.layout.ballplayer, null);
			avatar.setBackgroundColor(getResources().getColor(R.color.transparent_grey));
			
			TextView newOrder = (TextView) avatar.findViewById(R.id.battingOrderText);	
			newOrder.setText(oldOrder.getText());
			
			TextView newPlayer = (TextView) avatar.findViewById(R.id.playerNameText);	
			newPlayer.setText(oldPlayer.getText());
			
			Button edit = (Button) avatar.findViewById(R.id.editButton);
			edit.setVisibility(View.INVISIBLE);
			
			return avatar;
			
		}
		
	};
		
	private View.OnClickListener addPlayerListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			LayoutInflater inflater = (LayoutInflater) getSystemService
				      (Context.LAYOUT_INFLATER_SERVICE);
			AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);
			
			final View view = inflater.inflate(R.layout.add_player, null);		
			LinearLayout delLayout = (LinearLayout) view.findViewById(R.id.deleteLayout);
			delLayout.setVisibility(View.GONE);
			
			b.setView(view);
			
			b.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					String playerName = ((EditText) view.findViewById(R.id.playerText)).getText().toString();
					String playerPosition = ((EditText) view.findViewById(R.id.positionText)).getText().toString();
					String playerBattingOrder = ((EditText) view.findViewById(R.id.orderText)).getText().toString();
					
					SharedPreferences.Editor e = players.edit();		
					e.putString(playerBattingOrder, playerName + " (" + playerPosition + ")");
					
					e.apply();
					
					createPlayerList();
					
				}
				
			});
			
			b.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					dialog.cancel();
					
				}
				
			});
			
			Dialog d = b.create();
			d.show();
			
		}
		
	};
	private View.OnClickListener editButtonClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			Button editButton = (Button) v;
			LinearLayout linLayout = (LinearLayout) editButton.getParent();
			
			final String battingOrder = ((TextView) linLayout.findViewById(R.id.battingOrderText)).getText().toString();
			String player = ((TextView) linLayout.findViewById(R.id.playerNameText)).getText().toString();
			
			String playerName = player.substring(0, player.indexOf("(") - 1);
			String position = player.substring(player.indexOf("(") + 1, player.indexOf(")"));
		
			LayoutInflater inflater = (LayoutInflater) getSystemService
				      (Context.LAYOUT_INFLATER_SERVICE);
			AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);
			
			final View view = inflater.inflate(R.layout.add_player, null);		
			((TextView) view.findViewById(R.id.playerText)).setText(playerName);
			((TextView) view.findViewById(R.id.positionText)).setText(position);
			((TextView) view.findViewById(R.id.orderText)).setText(battingOrder);
			
			b.setView(view);
			
			b.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					String playerName = ((EditText) view.findViewById(R.id.playerText)).getText().toString();
					String playerPosition = ((EditText) view.findViewById(R.id.positionText)).getText().toString();
					String newBattingOrder = ((EditText) view.findViewById(R.id.orderText)).getText().toString();
					CheckBox deletePlayerBox = ((CheckBox) view.findViewById(R.id.deleteBox));
					
					SharedPreferences.Editor e = players.edit();
					
					e.remove(battingOrder);
			
					if (!deletePlayerBox.isChecked()) {
						e.putString(newBattingOrder, playerName + " (" + playerPosition + ")");			
					}
						
					e.apply();
					
					createPlayerList();
					
				}
				
			});
			
			b.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					dialog.cancel();
					
				}
				
			});
			
			Dialog d = b.create();
			d.show();
			
		}
		
	};
	
	private SharedPreferences players;
	
	private RelativeLayout mainLayout;
	private ScrollView mainScrollView;
	private Button addPlayerButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);	
		mainScrollView = (ScrollView) findViewById(R.id.mainScrollView);
					
		dndListener = new DragDropTouchListener(mainLayout, mainScrollView, 0);
		dndListener.setOnDroppedListener(onDrop);
		dndListener.setOnDraggedListener(onDrag);
		
		mainScrollView.setOnTouchListener(dndListener);
		
		addPlayerButton = (Button) findViewById(R.id.addButton);
		addPlayerButton.setOnClickListener(addPlayerListener);
		
		players = getSharedPreferences("test", MODE_PRIVATE);
		
		createPlayerList();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void createPlayerList() {
		
		mainLayout.removeAllViews();
		
		LayoutInflater inflater = (LayoutInflater) getSystemService
			      (Context.LAYOUT_INFLATER_SERVICE);
		
		Map<String, ?> p = players.getAll();
		Set<String> ks = p.keySet();
		Iterator<String> i = ks.iterator();
		
		ArrayList<Integer> battingOrders = new ArrayList<Integer>();
		while (i.hasNext()) {
			battingOrders.add(Integer.parseInt(i.next()));
		}
		
		Collections.sort(battingOrders);
		
		Iterator<Integer> iter = battingOrders.iterator();
			
		int index = 1;
		while (iter.hasNext()) {
			
			String key = String.valueOf(iter.next());
			String value = p.get(key).toString();
			
			LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.ballplayer, null);
			layout.setId(index);
			
			TextView battingOrderText = (TextView) layout.findViewById(R.id.battingOrderText);
			TextView playerNameText = (TextView) layout.findViewById(R.id.playerNameText);
			
			Button editButton = (Button) layout.findViewById(R.id.editButton);
			editButton.setOnClickListener(editButtonClickListener);
			
			battingOrderText.setText(key);
			playerNameText.setText(value);
					
			mainLayout.addView(layout);
			
			RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) layout.getLayoutParams();
			lParams.width = 250;
			lParams.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
			
			if (index != 1) {			
				lParams.addRule(RelativeLayout.BELOW, index - 1);
				layout.setLayoutParams(lParams);
			}
			
			index++;
			
		}
		
		mainLayout.invalidate();
		mainScrollView.invalidate();
		
	}
	
}
