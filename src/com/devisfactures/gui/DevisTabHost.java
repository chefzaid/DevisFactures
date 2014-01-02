package com.devisfactures.gui;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class DevisTabHost extends TabActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.devistabhost);

		TabHost tabHost = getTabHost();
		TabHost.TabSpec spec;
		Intent intent;

		intent = new Intent().setClass(this, DevisView.class);
		spec = tabHost.newTabSpec("devis").setIndicator("Devis", 
				getResources().getDrawable(R.drawable.business))
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, DevisArticleView.class).addFlags(
				Intent.FLAG_ACTIVITY_CLEAR_TOP);
		spec = tabHost.newTabSpec("devisarticle").setIndicator(
				"Articles", getResources().getDrawable(R.drawable.article)
				).setContent(intent);
		tabHost.addTab(spec);

		tabHost.setCurrentTab(0);
	}
	
}
