package com.devisfactures.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import com.devisfactures.dao.ArticleDAO;
import com.devisfactures.dao.ClientDAO;
import com.devisfactures.dao.DevisArticleDAO;
import com.devisfactures.dao.DevisDAO;
import com.devisfactures.dao.FactureDAO;
import com.devisfactures.dao.InfosDAO;
import com.devisfactures.model.Article;
import com.devisfactures.model.Client;
import com.devisfactures.model.Devis;
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

public class DevisView extends Activity {
	
	private Devis devis = new Devis();
	private DevisDAO dDAO = new DevisDAO();
	private ArrayList<Devis> list = new ArrayList<Devis>();
	private int index = 0;
	
	public static CharSequence currentIdDevis = "";
	public static CharSequence currentIdClient = "";
	public static boolean update = false;
	
	private final int RECHERCHE = 1;
	private final int FILTRE = 2;
	private final int TRI = 3;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.devis);
		
		if(!DBService.isTableEmpty("Devis")){
			list = dDAO.retrieve();
			index = list.size() - 1;
			updateScreen();
			updatePrix();
		}else
			clearComponents();

		
		Button btnVider = (Button)findViewById(R.id.btnDevisVider);
		btnVider.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				clearComponents();
		}});
		
		Button btnAjouter = (Button)findViewById(R.id.btnDevisAjouter);
		btnAjouter.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				try{
					initDevis();
				}catch(Exception e){
					Toast.makeText(DevisView.this, "Date invalide, ajout annulé !", 
			    			Toast.LENGTH_SHORT).show();
					return;
				}
				try{
					dDAO.create(devis);
				}catch(Exception e){
					Toast.makeText(DevisView.this, "Erreur de contrainte," + 
							" ajout annulé !", Toast.LENGTH_SHORT).show();
					return;
				}
				list = dDAO.retrieve();
				index = list.size() - 1;
				updateScreen();
				((EditText)findViewById(R.id.txtDevisPrixHT)).setText(
						"Auto-calculé");
				Toast.makeText(DevisView.this, "Ajout effectué", 
		    			Toast.LENGTH_SHORT).show();
		}});
		
		Button btnModifier = (Button)findViewById(R.id.btnDevisModifier);
		btnModifier.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				try{
					initDevis();
				}catch(Exception e){
					Toast.makeText(DevisView.this, "Date invalide, " +
							"modification annulée !", 
			    			Toast.LENGTH_SHORT).show();
					return;
				}
				try{
					dDAO.update(devis);
				}catch(Exception e){
					Toast.makeText(DevisView.this, "Erreur de contrainte," + 
							" modification annulée !", 
			    			Toast.LENGTH_SHORT).show();
					return;
				}
				list = dDAO.retrieve();
				updateScreen();
				Toast.makeText(DevisView.this, "Modification effectuée", 
		    			Toast.LENGTH_SHORT).show();
		}});
		
		Button btnSuppr = (Button)findViewById(R.id.btnDevisSupprimer);
		btnSuppr.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				initDevis();
				try{
					dDAO.delete(devis.getIdDevis().toString());
				}catch(Exception e){
					Toast.makeText(DevisView.this, "Erreur de contrainte," +
							" suppression annulée !", 
			    			Toast.LENGTH_SHORT).show();
					return;
				}
				list = dDAO.retrieve();
				if(index > 0)
					index--;
				else if(index == 0 && list.size() == 0)
					clearComponents();
				updateScreen();
				Toast.makeText(DevisView.this, "Suppression effectuée", 
		    			Toast.LENGTH_SHORT).show();
		}});
		
		Button btnPrem = (Button)findViewById(R.id.btnDevisPremier);
		btnPrem.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				index = 0;
				updateScreen();
		}});
		
		Button btnDern = (Button)findViewById(R.id.btnDevisDernier);
		btnDern.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				index = list.size() - 1;
				updateScreen();
		}});
		
		Button btnPrec = (Button)findViewById(R.id.btnDevisPrecedent);
		btnPrec.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(index != 0)
					index--;
				updateScreen();
		}});
		
		Button btnSuiv = (Button)findViewById(R.id.btnDevisSuivant);
		btnSuiv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(index != list.size() - 1)
					index++;
				updateScreen();
		}});
		
		Spinner s = (Spinner)findViewById(R.id.spinnerDevisClient);
		ClientDAO cDAO = new ClientDAO();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(DevisView.this, 
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
				((EditText)findViewById(R.id.txtDevisClient)).setText(str[0]);
				if(index != list.size())
					updateScreen();
		}});
	}

	public boolean onCreateOptionsMenu(Menu menu){
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.devismenu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		ClientDAO cDAO = new ClientDAO();
		
		switch (item.getItemId()) {
		case R.id.optionDevisRecherchercher :
			Intent optionRechercherIntent = new Intent(DevisView.this, 
					OptionRechercherView.class);
			startActivityForResult(optionRechercherIntent, RECHERCHE);
			return true;
		case R.id.optionDevisFiltrer :
			Intent optionFiltrerIntent = new Intent(DevisView.this, 
					OptionFiltrerDevisView.class);
			startActivityForResult(optionFiltrerIntent, FILTRE);
			return true;
		case R.id.optionDevisTrier :
			Intent optionTrierIntent = new Intent(DevisView.this,
					OptionTrierDevisView.class);
			startActivityForResult(optionTrierIntent, TRI);
			return true;
		case R.id.optionDevisAnnulerFiltre :
			list.clear();
			list = dDAO.retrieve();
			index = list.size() - 1;
			updateScreen();
			return true;
		case R.id.optionDevisAfficherClient :
			initDevis();
			currentIdClient = devis.getIdClient();
			Intent clientIntent = new Intent(DevisView.this, ClientView.class);
			startActivity(clientIntent);
			return true;
		case R.id.optionDevisConvFacture :
			initDevis();
			Facture facture = new Facture();
			FactureDAO fDAO = new FactureDAO();
			DevisArticleDAO daDAO = new DevisArticleDAO();
			String newIdFacture = new String();
			if(fDAO.getLastIdFacture() != null){
				String str[] = fDAO.getLastIdFacture().split("-");
				int id = Integer.parseInt(str[1]) + 1;
				newIdFacture = "F" + (new Date().getYear()+1900) + "-" + 
					String.format("%03d", id);
			}else
				newIdFacture = "F" + (new Date().getYear()+1900) + "-001";
			
			facture.setIdFacture(newIdFacture);
			facture.setDate(devis.getDate());
			facture.setPrixHT(devis.getPrixHT());
			facture.setPaye(false);
			facture.setIdClient(devis.getIdClient());
			facture.setNotes(devis.getNotes());

			fDAO.create(facture);
			if(daDAO.convFacture(newIdFacture.toString(), devis.getIdDevis()
					.toString()) == false){
				fDAO.delete(facture.getIdFacture().toString());
				fillComponents(devis);
				Toast.makeText(DevisView.this, "Quantité du stock en pénurie, " +
						"conversion annulé !", Toast.LENGTH_SHORT).show();
			}else{
				fillComponents(devis);
				((CheckBox)findViewById(R.id.ckboxDevisFacture))
						.setChecked(true);
				initDevis();
				dDAO.update(devis);
			}
			return true;
		case R.id.optionDevisPDF :
			generatePDF();
			return true;
		case R.id.optionDevisEmail :
			File file = new File("/sdcard/Devis_" + devis.getIdDevis() + 
					".pdf");
			InfosDAO iDAO = new InfosDAO();
			if(!file.exists())
				generatePDF();
			initDevis();
			Infos infos= iDAO.retrieve();
			String msg = "Bonjour,\n\nVeuillez retrouver ci-joint le devis" +
					" que vous avez demandé en date le " 
				+ DBService.formatDate(devis.getDate()) + ".\n\n\n" +
						"Cordialement,\n\n" + infos.getRaisonSociale() + 
						"\n" + infos.getTel();
			String mailto[] = new String[]{ cDAO.find(devis.getIdClient().toString()).
					getEmail().toString() };
			Uri uri = Uri.parse(file.toString());
			Intent emailIntent = new Intent(Intent.ACTION_SEND);
			emailIntent.putExtra(Intent.EXTRA_EMAIL, mailto);
			emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Devis " + 
					infos.getRaisonSociale().toString());
			emailIntent.putExtra(Intent.EXTRA_TEXT, msg);
			emailIntent.setType("application/pdf");
			emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
			startActivity(Intent.createChooser(emailIntent, 
					"Envoyer l'email en utilisant :"));
			return true;
		case R.id.optionDevisAppel :
			initDevis();
			Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(
					"tel:" + cDAO.find(devis.getIdClient().toString()).getTel()
					.toString().trim())); 
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
				Devis d = (Devis)data.getExtras().get("devis");
				index = getIndexOf(d);
				updateScreen();
				break;
			case FILTRE :
				@SuppressWarnings("unchecked")
				ArrayList<Devis> filteredDevis = (ArrayList<Devis>)
						data.getExtras().get("filteredDevis");
				list.clear();
				list = filteredDevis;
				index = list.size() - 1;
				updateScreen();
				break;
			case TRI :
				@SuppressWarnings("unchecked")
				ArrayList<Devis> sortedDevis = (ArrayList<Devis>)
						data.getExtras().get("sortedDevis");
				list.clear();
				list = sortedDevis;
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
	
	private void initDevis(){
		devis.setIdDevis(((EditText)findViewById(R.id.txtDevisNum)).getText());
		devis.setDate(DBService.parseDate(((EditText)findViewById(
				R.id.txtDevisDate)).getText().toString()));
		devis.setIdClient(((EditText)findViewById(
				R.id.txtDevisClient)).getText());
		try{
			devis.setPrixHT(Float.parseFloat(((EditText)findViewById(
					R.id.txtDevisPrixHT)).getText().toString()));
		}catch(Exception e){
			devis.setPrixHT(0);
		}
		devis.setFacture(((CheckBox)findViewById(
				R.id.ckboxDevisFacture)).isChecked());
		devis.setNotes(((EditText)findViewById(R.id.txtDevisNotes)).getText());
	}
	
	private void updateScreen(){
		if(!list.isEmpty()){
			Devis d = list.get(index);
			fillComponents(d);
			changeButtonsState();
		}else
			changeButtonsState();
	}
	
	private void changeButtonsState(){
		if(index == 0){
			((Button)findViewById(R.id.btnDevisSuivant)).setEnabled(true);
			((Button)findViewById(R.id.btnDevisDernier)).setEnabled(true);
			((Button)findViewById(R.id.btnDevisPrecedent)).setEnabled(false);
			((Button)findViewById(R.id.btnDevisPremier)).setEnabled(false);
		}else if(index == list.size() - 1 || index == list.size()){
			((Button)findViewById(R.id.btnDevisSuivant)).setEnabled(false);
			((Button)findViewById(R.id.btnDevisDernier)).setEnabled(false);
			((Button)findViewById(R.id.btnDevisPrecedent)).setEnabled(true);
			((Button)findViewById(R.id.btnDevisPremier)).setEnabled(true);
		}else{
			((Button)findViewById(R.id.btnDevisPremier)).setEnabled(true);
			((Button)findViewById(R.id.btnDevisDernier)).setEnabled(true);
			((Button)findViewById(R.id.btnDevisPrecedent)).setEnabled(true);
			((Button)findViewById(R.id.btnDevisSuivant)).setEnabled(true);
		}
		
		if(list.size() == 1 || list.size() == 0){ 
			((Button)findViewById(R.id.btnDevisSuivant)).setEnabled(false);
			((Button)findViewById(R.id.btnDevisDernier)).setEnabled(false);
			((Button)findViewById(R.id.btnDevisPrecedent)).setEnabled(false);
			((Button)findViewById(R.id.btnDevisPremier)).setEnabled(false);
		}
		if(index == 1){
			((Button)findViewById(R.id.btnDevisPrecedent)).setEnabled(true);
			((Button)findViewById(R.id.btnDevisPremier)).setEnabled(true);
		}
	}
	
	private void fillComponents(Devis d){
		((Spinner)findViewById(R.id.spinnerDevisClient)).setEnabled(false);
		((EditText)findViewById(R.id.txtDevisNum)).setText(d.getIdDevis());
		((EditText)findViewById(R.id.txtDevisDate)).setText(
				DBService.formatDate(d.getDate()));
		((EditText)findViewById(R.id.txtDevisClient)).setText(d.getIdClient());
		((EditText)findViewById(R.id.txtDevisPrixHT)).setText(
				String.valueOf(d.getPrixHT()));
		((CheckBox)findViewById(R.id.ckboxDevisFacture))
				.setChecked(d.isFacture());
		((EditText)findViewById(R.id.txtDevisNotes)).setText(d.getNotes());
		initDevis();
		index = getIndexOf(d);
		currentIdDevis = ((EditText)findViewById(R.id.txtDevisNum)).getText();
	}
	
	private void clearComponents(){
		String newId = new String();
		if(dDAO.getLastIdDevis() != null){
			String str[] = dDAO.getLastIdDevis().split("-");
			int id = Integer.parseInt(str[1]) + 1;
			newId = "D" + (new Date().getYear()+1900) + "-" + 
					String.format("%03d", id);
		}else
			newId = "D" + (new Date().getYear()+1900) + "-001";

		((EditText)findViewById(R.id.txtDevisNum)).setText(newId);
		((EditText)findViewById(R.id.txtDevisDate)).setText(
				DBService.formatDate(new Date()));
		((Spinner)findViewById(R.id.spinnerDevisClient)).setEnabled(true);	
		((EditText)findViewById(R.id.txtDevisClient)).setText("");
		((EditText)findViewById(R.id.txtDevisPrixHT)).setText("Auto-calculé");
		((EditText)findViewById(R.id.txtDevisPrixTTC)).setText("Auto-calculé");
		((CheckBox)findViewById(R.id.ckboxDevisFacture)).setChecked(false);
		((EditText)findViewById(R.id.txtDevisNotes)).setText("");
		((EditText)findViewById(R.id.txtDevisDate)).requestFocus();
		currentIdDevis = newId;
		index = list.size();
		changeButtonsState();
	}
	
	private void updatePrix(){
		DevisArticleDAO daDAO = new DevisArticleDAO();
		float prixTotalHT = daDAO.findPrixTotal(devis.getIdDevis()
				.toString());
		if(prixTotalHT != 0){
			((EditText)findViewById(R.id.txtDevisPrixHT)).setText(
					String.valueOf(prixTotalHT));
			((EditText)findViewById(R.id.txtDevisPrixTTC)).setText(
					String.valueOf(DBService.prixTTC(prixTotalHT)));
		}else{
			((EditText)findViewById(R.id.txtDevisPrixHT))
					.setText("Auto-calculé");
			((EditText)findViewById(R.id.txtDevisPrixTTC))
					.setText("Auto-calculé");
		}
		initDevis();
		dDAO.update(devis);
		list = dDAO.retrieve();
		updateScreen();
	}
	
	private int getIndexOf(Devis d){
		for(int i=0; i<list.size(); i++){
			if(list.get(i).getIdDevis().equals(d.getIdDevis()))
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
				DevisArticleDAO daDAO = new DevisArticleDAO();
		  		ClientDAO cDAO = new ClientDAO();
		  		InfosDAO iDAO = new InfosDAO();
		  		
		  		initDevis();
		  		ArticleDAO aDAO = new ArticleDAO();
		  		Infos src = iDAO.retrieve();
		  		Client clt = cDAO.find(devis.getIdClient().toString());
		  		ArrayList<String> articleIds = daDAO.findArticles(
		  				devis.getIdDevis().toString());
		  		ArrayList<Article> articles = 
		  				new ArrayList<Article>();
		  		ArrayList<Integer> qttes = new ArrayList<Integer>();
		  		
		  		for(int i=0; i< articleIds.size(); i++){
		  			articles.add(aDAO.find(articleIds.get(i)));
		  			qttes.add(daDAO.findQtte(devis.getIdDevis()
		  					.toString(), articleIds.get(i)));
		  		}
		  		GeneratePDF.createDevis(src, clt, devis, articles, qttes);
		  		dialog.dismiss();
		}});
		background.start();
	}

}
