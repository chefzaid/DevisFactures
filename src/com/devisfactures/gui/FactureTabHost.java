package com.devisfactures.gui;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class FactureTabHost extends TabActivity {

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.facturetabhost);

		TabHost tabHost = getTabHost();
		TabHost.TabSpec spec;
		Intent intent;

		intent = new Intent().setClass(this, FactureView.class);
		spec = tabHost.newTabSpec("facture").setIndicator("Factures", 
				getResources().getDrawable(R.drawable.business))
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, FactureArticleView.class).addFlags(
				Intent.FLAG_ACTIVITY_CLEAR_TOP);
		spec = tabHost.newTabSpec("facturearticle").setIndicator(
				"Articles", 
				getResources().getDrawable(R.drawable.article))
				.setContent(intent);
		tabHost.addTab(spec);

		tabHost.setCurrentTab(0);
	}
}
