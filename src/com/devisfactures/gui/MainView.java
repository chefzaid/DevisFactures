package com.devisfactures.gui;

import com.devisfactures.dao.Connection;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainView extends Activity
{
	private Button btnDevis;
	private Button btnFactures;
	private Button btnArticles;
	private Button btnClients;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		btnDevis = (Button) findViewById(R.id.btnMainDevis);
		btnDevis.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent devisIntent = new Intent(MainView.this, DevisTabHost.class);
				startActivity(devisIntent);
		}});
		btnFactures = (Button) findViewById(R.id.btnMainFactures);
		btnFactures.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent facturesIntent = new Intent(MainView.this, 
						FactureTabHost.class);
				startActivity(facturesIntent);
		}});
		btnArticles = (Button) findViewById(R.id.btnMainArticles);
		btnArticles.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent articlesIntent = new Intent(MainView.this, ArticleView.class);
				startActivity(articlesIntent);
		}});
		btnClients = (Button) findViewById(R.id.btnMainClients);
		btnClients.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent clientsIntent = new Intent(MainView.this, ClientView.class);
				startActivity(clientsIntent);
		}});
		
		Connection.getDB(getApplicationContext());
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.mainmenu, menu);	
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent optionInfosIntent = new Intent(MainView.this, OptionInfosView.class);
		startActivity(optionInfosIntent);
		return true;
	}
	
}
