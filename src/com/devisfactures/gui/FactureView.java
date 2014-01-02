package com.devisfactures.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import com.devisfactures.dao.ArticleDAO;
import com.devisfactures.dao.ClientDAO;
import com.devisfactures.dao.FactureArticleDAO;
import com.devisfactures.dao.FactureDAO;
import com.devisfactures.dao.InfosDAO;
import com.devisfactures.model.Article;
import com.devisfactures.model.Client;
import com.devisfactures.model.Facture;
import com.devisfactures.model.Infos;
import com.devisfactures.service.DBService;
import com.devisfactures.service.GeneratePDF;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class FactureView extends Activity {
	
	private Facture facture = new Facture();
	private FactureDAO fDAO = new FactureDAO();
	private ArrayList<Facture> list = new ArrayList<Facture>();
	private int index = 0;
	
	public static CharSequence currentIdFacture = "";
	public static CharSequence currentIdClient = "";
	public static boolean update = false;
	
	private final int RECHERCHE = 1;
	private final int FILTRE = 2;
	private final int TRI = 3;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.facture);
		
		if(!DBService.isTableEmpty("Facture")){
			list = fDAO.retrieve();
			index = list.size() - 1;
			updateScreen();
			updatePrix();
		}else
			clearComponents();

		
		Button btnVider = (Button)findViewById(R.id.btnFactureVider);
		btnVider.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				clearComponents();
		}});
		
		Button btnAjouter = (Button)findViewById(R.id.btnFactureAjouter);
		btnAjouter.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				try{
					initFacture();
				}catch(Exception e){
					Toast.makeText(FactureView.this, "Date invalide, " +
							"ajout annulé !", Toast.LENGTH_SHORT).show();
					return;
				}
				try{
					fDAO.create(facture);
				}catch(Exception e){
					Toast.makeText(FactureView.this, "Erreur de contrainte," +
							" ajout annulée !", Toast.LENGTH_SHORT).show();
					return;
				}
				list = fDAO.retrieve();
				index = list.size() - 1;
				updateScreen();
				((EditText)findViewById(R.id.txtFacturePrixHT)).setText(
						"Auto-calculé");
				Toast.makeText(FactureView.this, "Ajout effectué", 
		    			Toast.LENGTH_SHORT).show();
		}});
		
		Button btnModifier = (Button)findViewById(R.id.btnFactureModifier);
		btnModifier.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				try{
					initFacture();
				}catch(Exception e){
					Toast.makeText(FactureView.this, "Date invalide, " +
							"modification annulée !", 
			    			Toast.LENGTH_SHORT).show();
					return;
				}
				try{
					fDAO.update(facture);
				}catch(Exception e){
					Toast.makeText(FactureView.this, "Erreur de contrainte," +
							" modification annulée !", 
							Toast.LENGTH_SHORT).show();
					return;
				}
				list = fDAO.retrieve();
				updateScreen();
				Toast.makeText(FactureView.this, "Modification effectuée", 
		    			Toast.LENGTH_SHORT).show();
		}});
		
		Button btnSuppr = (Button)findViewById(R.id.btnFactureSupprimer);
		btnSuppr.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				initFacture();
				try{
					fDAO.delete(facture.getIdFacture().toString());
				}catch(Exception e){
					Toast.makeText(FactureView.this, "Erreur de contrainte," +
							" suppression annulée !", 
							Toast.LENGTH_SHORT).show();
					return;
				}
				list = fDAO.retrieve();
				if(index > 0)
					index--;
				else if(index == 0 && list.size() == 0)
					clearComponents();
				updateScreen();
				Toast.makeText(FactureView.this, "Suppression effectuée", 
		    			Toast.LENGTH_SHORT).show();
		}});
		
		Button btnPrem = (Button)findViewById(R.id.btnFacturePremier);
		btnPrem.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				index = 0;
				updateScreen();
		}});
		
		Button btnDern = (Button)findViewById(R.id.btnFactureDernier);
		btnDern.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				index = list.size() - 1;
				updateScreen();
		}});
		
		Button btnPrec = (Button)findViewById(R.id.btnFacturePrecedent);
		btnPrec.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(index != 0)
					index--;
				updateScreen();
		}});
		
		Button btnSuiv = (Button)findViewById(R.id.btnFactureSuivant);
		btnSuiv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(index != list.size() - 1)
					index++;
				updateScreen();
		}});
		
		Spinner s = (Spinner)findViewById(R.id.spinnerFactureClient);
		ClientDAO cDAO = new ClientDAO();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 
				android.R.layout.simple_spinner_item, cDAO.retrieveArray());
		s.setAdapter(adapter);
		s.setPrompt("Choisissez un client :");
		s.setOnItemSelectedListener(new OnItemSelectedListener(){
			@Override
			public void onNothingSelected(AdapterView<?> arg0){}
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, 
					int arg2, long arg3){
				String str[] = arg0.getSelectedItem().toString().split(" - ");
				((EditText)findViewById(R.id.txtFactureClient)).setText(str[0]);
				if(index != list.size())
					updateScreen();
		}});
	}

	public boolean onCreateOptionsMenu(Menu menu){
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.facturemenu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		ClientDAO cDAO = new ClientDAO();
		
		switch (item.getItemId()) {
		case R.id.optionFactureRecherchercher :
			Intent optionRechercherIntent = new Intent(FactureView.this, 
					OptionRechercherView.class);
			startActivityForResult(optionRechercherIntent, RECHERCHE);
			return true;
		case R.id.optionFactureFiltrer :
			Intent optionFiltrerIntent = new Intent(FactureView.this, 
					OptionFiltrerFactureView.class);
			startActivityForResult(optionFiltrerIntent, FILTRE);
			return true;
		case R.id.optionFactureTrier :
			Intent optionTrierIntent = new Intent(FactureView.this,
					OptionTrierFactureView.class);
			startActivityForResult(optionTrierIntent, TRI);
			return true;
		case R.id.optionFactureAnnulerFiltre :
			list.clear();
			list = fDAO.retrieve();
			index = list.size() - 1;
			updateScreen();
			return true;
		case R.id.optionFactureAfficherClient :
			initFacture();
			currentIdClient = facture.getIdClient();
			Intent clientIntent = new Intent(FactureView.this, ClientView.class);
			startActivity(clientIntent);
			return true;
		case R.id.optionFacturePDF :
			generatePDF();
			return true;
		case R.id.optionFactureEmail :
			File file = new File("/sdcard/Facture_" + facture.getIdFacture() + 
					".pdf");
			InfosDAO iDAO = new InfosDAO();
			if(!file.exists())
				generatePDF();
			initFacture();
			Infos infos= iDAO.retrieve();
			String msg = "Bonjour,\n\nVeuillez retrouver ci-joint le facture" +
					" que vous avez demandé en date le " 
				+ DBService.formatDate(facture.getDate()) + ".\n\n\n" +
						"Cordialement,\n\n" + infos.getRaisonSociale() + 
						"\n" + infos.getTel();
			String mailto[] = new String[]{ cDAO.find(facture.getIdClient().toString()).
					getEmail().toString() };
			Uri uri = Uri.parse(file.toString());
			Intent emailIntent = new Intent(Intent.ACTION_SEND);
			emailIntent.putExtra(Intent.EXTRA_EMAIL, mailto);
			emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Facture " + 
					infos.getRaisonSociale().toString());
			emailIntent.putExtra(Intent.EXTRA_TEXT, msg);
			emailIntent.setType("application/pdf");
			emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
			startActivity(Intent.createChooser(emailIntent, 
					"Envoyer l'email en utilisant :"));
			return true;
		case R.id.optionFactureAppel :
			initFacture();
			Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(
					"tel:" + cDAO.find(facture.getIdClient().toString()).
					getTel().toString().trim())); 
			startActivity(callIntent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, 
			Intent data){
		if(resultCode == RESULT_OK){
			switch(requestCode){
			case RECHERCHE :
				Facture f = (Facture)data.getExtras().get("facture");
				index = getIndexOf(f);
				updateScreen();
				break;
			case FILTRE :
				@SuppressWarnings("unchecked")
				ArrayList<Facture> filteredFacture = 
						(ArrayList<Facture>)
						data.getExtras().get("filteredFacture");
				list.clear();
				list = filteredFacture;
				index = list.size() - 1;
				updateScreen();
				break;
			case TRI :
				@SuppressWarnings("unchecked")
				ArrayList<Facture> sortedFacture = 
						(ArrayList<Facture>)
						data.getExtras().get("sortedFacture");
				list.clear();
				list = sortedFacture;
				index = list.size() - 1;
				updateScreen();
				break;
			}
		}
	}
	
	protected void onResume(){
		super.onResume();
		if(update == true){
			updatePrix();
		}
	 }
	
	private void initFacture(){
		facture.setIdFacture(((EditText)findViewById(R.id.txtFactureNum))
				.getText());
		facture.setDate(DBService.parseDate(((EditText)findViewById(
				R.id.txtFactureDate)).getText().toString()));
		facture.setIdClient(((EditText)findViewById(
				R.id.txtFactureClient)).getText());
		try{
			facture.setPrixHT(Float.parseFloat(((EditText)findViewById(
					R.id.txtFacturePrixHT)).getText().toString()));
		}catch(Exception e){
			facture.setPrixHT(0);
		}
		facture.setPaye(((CheckBox)findViewById(
				R.id.ckboxFacturePaye)).isChecked());
		facture.setNotes(((EditText)findViewById(R.id.txtFactureNotes))
				.getText());
	}
	
	private void updateScreen(){
		if(!list.isEmpty()){
			Facture f = list.get(index);
			fillComponents(f);
			changeButtonsState();
		}else
			changeButtonsState();
	}
	
	private void changeButtonsState(){
		if(index == 0){
			((Button)findViewById(R.id.btnFactureSuivant)).setEnabled(true);
			((Button)findViewById(R.id.btnFactureDernier)).setEnabled(true);
			((Button)findViewById(R.id.btnFacturePrecedent)).setEnabled(false);
			((Button)findViewById(R.id.btnFacturePremier)).setEnabled(false);
		}else if(index == list.size() - 1 || index == list.size()){
			((Button)findViewById(R.id.btnFactureSuivant)).setEnabled(false);
			((Button)findViewById(R.id.btnFactureDernier)).setEnabled(false);
			((Button)findViewById(R.id.btnFacturePrecedent)).setEnabled(true);
			((Button)findViewById(R.id.btnFacturePremier)).setEnabled(true);
		}else{
			((Button)findViewById(R.id.btnFacturePremier)).setEnabled(true);
			((Button)findViewById(R.id.btnFactureDernier)).setEnabled(true);
			((Button)findViewById(R.id.btnFacturePrecedent)).setEnabled(true);
			((Button)findViewById(R.id.btnFactureSuivant)).setEnabled(true);
		}
		
		if(list.size() == 1 || list.size() == 0){ 
			((Button)findViewById(R.id.btnFactureSuivant)).setEnabled(false);
			((Button)findViewById(R.id.btnFactureDernier)).setEnabled(false);
			((Button)findViewById(R.id.btnFacturePrecedent)).setEnabled(false);
			((Button)findViewById(R.id.btnFacturePremier)).setEnabled(false);
		}
		if(index == 1){
			((Button)findViewById(R.id.btnFacturePrecedent)).setEnabled(true);
			((Button)findViewById(R.id.btnFacturePremier)).setEnabled(true);
		}
	}
	
	private void fillComponents(Facture d){
		((Spinner)findViewById(R.id.spinnerFactureClient)).setEnabled(false);
		((EditText)findViewById(R.id.txtFactureNum)).setText(d.getIdFacture());
		((EditText)findViewById(R.id.txtFactureDate)).setText(
				DBService.formatDate(d.getDate()));
		((EditText)findViewById(R.id.txtFactureClient)).setText(
				d.getIdClient());
		((EditText)findViewById(R.id.txtFacturePrixHT)).setText(String.valueOf(
				d.getPrixHT()));
		((CheckBox)findViewById(R.id.ckboxFacturePaye))
				.setChecked(d.isPaye());
		((EditText)findViewById(R.id.txtFactureNotes)).setText(d.getNotes());
		initFacture();
		index = getIndexOf(d);
		currentIdFacture = ((EditText)findViewById(R.id.txtFactureNum))
				.getText();
	}
	
	private void clearComponents(){
		String newId = new String();
		if(fDAO.getLastIdFacture() != null){
			String str[] = fDAO.getLastIdFacture().split("-");
			int id = Integer.parseInt(str[1]) + 1;
			newId = "F" + (new Date().getYear()+1900) + "-" + 
					String.format("%03d", id);
		}else
			newId = "F" + (new Date().getYear()+1900) + "-001";

		((EditText)findViewById(R.id.txtFactureNum)).setText(newId);
		((EditText)findViewById(R.id.txtFactureDate)).setText(
				DBService.formatDate(new Date()));
		((Spinner)findViewById(R.id.spinnerFactureClient)).setEnabled(true);		
		((EditText)findViewById(R.id.txtFactureClient)).setText("");
		((EditText)findViewById(R.id.txtFacturePrixHT)).setText(
				"Auto-calculé");
		((EditText)findViewById(R.id.txtFacturePrixTTC)).setText(
				"Auto-calculé");
		((CheckBox)findViewById(R.id.ckboxFacturePaye)).setChecked(false);
		((EditText)findViewById(R.id.txtFactureNotes)).setText("");
		((EditText)findViewById(R.id.txtFactureDate)).requestFocus();
		currentIdFacture = newId;
		index = list.size();
		changeButtonsState();
	}
	
	private void updatePrix(){
		FactureArticleDAO faDAO = new FactureArticleDAO();
		float prixTotalHT = faDAO.findPrixTotal(facture.getIdFacture()
				.toString());
		if(prixTotalHT != 0){
			((EditText)findViewById(R.id.txtFacturePrixHT)).setText(
					String.valueOf(prixTotalHT));
			((EditText)findViewById(R.id.txtFacturePrixTTC)).setText(
					String.valueOf(DBService.prixTTC(prixTotalHT)));
		}else{
			((EditText)findViewById(R.id.txtFacturePrixHT))
					.setText("Auto-calculé");
			((EditText)findViewById(R.id.txtFacturePrixTTC))
					.setText("Auto-calculé");
		}
		initFacture();
		
		fDAO.update(facture);
		list = fDAO.retrieve();
		updateScreen();
	}
	
	private int getIndexOf(Facture d){
		for(int i=0; i<list.size(); i++){
			if(list.get(i).getIdFacture().equals(d.getIdFacture()))
				return i;
		}
		return 0;
	}

	private void generatePDF(){
		final ProgressDialog dialog = new ProgressDialog(this);
		dialog.setMessage("Conversion en cours...");
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.show();
		
		Thread background = new Thread (new Runnable(){
			public void run(){		
				FactureArticleDAO daDAO = new FactureArticleDAO();
		  		ClientDAO cDAO = new ClientDAO();
		  		InfosDAO iDAO = new InfosDAO();
		  		
		  		initFacture();
		  		ArticleDAO aDAO = new ArticleDAO();
		  		Infos src = iDAO.retrieve();
		  		Client clt = cDAO.find(facture.getIdClient().toString());
		  		ArrayList<String> articleIds = daDAO.findArticles(
		  				facture.getIdFacture().toString());
		  		ArrayList<Article> articles = 
		  				new ArrayList<Article>();
		  		ArrayList<Integer> qttes = new ArrayList<Integer>();
		  		
		  		for(int i=0; i< articleIds.size(); i++){
		  			articles.add(aDAO.find(articleIds.get(i)));
		  			qttes.add(daDAO.findQtte(facture.getIdFacture()
		  					.toString(), articleIds.get(i)));
		  		}
		  		GeneratePDF.createFacture(src, clt, facture, articles, qttes);
		  		dialog.dismiss();
		}});
		background.start();
	}

}
