package com.devisfactures.gui;

import java.util.ArrayList;

import com.devisfactures.service.DBService;
import com.devisfactures.dao.ArticleDAO;
import com.devisfactures.dao.DevisArticleDAO;
import com.devisfactures.dao.DevisDAO;
import com.devisfactures.model.DevisArticle;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class DevisArticleView extends Activity {
	
	DevisArticle devisArticle = new DevisArticle();
	DevisArticleDAO daDAO = new DevisArticleDAO();
	ArticleDAO aDAO = new ArticleDAO();
	ArrayList<DevisArticle> list = new ArrayList<DevisArticle>();
	int index = 0;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.devisarticle);

		if(!DBService.isTableEmpty("DevisArticle") && 
				(DevisView.currentIdDevis != null || DevisView.currentIdDevis != "")){
			list = daDAO.retrieve();
			index = list.size() - 1;
			updateScreen();
		}else
			clearComponents();

		Button btnVider = (Button)findViewById(R.id.btnDevisArticleVider);
		btnVider.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				clearComponents();
		}});
		
		Button btnAjouter = (Button)findViewById(R.id.btnDevisArticleAjouter);
		btnAjouter.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				try{
					initDevisArticle();
				}catch(Exception e){
					Toast.makeText(DevisArticleView.this, "Quantité invalide, " +
							"ajout annulé !", Toast.LENGTH_SHORT).show();
					return;
				}
				try{
					daDAO.create(devisArticle);	
				}catch(Exception e){
					Toast.makeText(DevisArticleView.this, "Erreur de contrainte, " +
							"ajout annulé !", Toast.LENGTH_SHORT).show();
					return;
				}
				list = daDAO.retrieve();
				index = list.size() - 1;
				updateScreen();
				
				DevisDAO dDAO = new DevisDAO();
				dDAO.updatePrix(DevisView.currentIdDevis.toString(), 
						daDAO.findPrixTotal(DevisView.currentIdDevis.toString()));
				Toast.makeText(DevisArticleView.this, "Ajout effectué", 
		    			Toast.LENGTH_SHORT).show();
		}});
		
		Button btnModifier = (Button)findViewById(R.id.btnDevisArticleModifier);
		btnModifier.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				try{
					initDevisArticle();
				}catch(Exception e){
					Toast.makeText(DevisArticleView.this, "Quantité invalide, " +
							"modification annulée", Toast.LENGTH_SHORT).show();
					return;
				}
				try{
					daDAO.update(devisArticle);
				}catch(Exception e){
					Toast.makeText(DevisArticleView.this, "Erreur de contrainte, " +
							"modification annulé !", Toast.LENGTH_SHORT).show();
					return;
				}
				list = daDAO.retrieve();
				updateScreen();
				
				DevisDAO dDAO = new DevisDAO();
				dDAO.updatePrix(DevisView.currentIdDevis.toString(), 
						daDAO.findPrixTotal(DevisView.currentIdDevis.toString()));
				Toast.makeText(DevisArticleView.this, "Modification effectuée", 
		    			Toast.LENGTH_SHORT).show();
		}});
		
		Button btnSuppr = (Button)findViewById(R.id.btnDevisArticleSupprimer);
		btnSuppr.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				initDevisArticle();
				try{
					daDAO.delete(devisArticle.getIdDevis().toString(), 
							devisArticle.getIdArticle().toString());
				}catch(Exception e){
					Toast.makeText(DevisArticleView.this, "Erreur de contrainte, " +
							"suppression annulé !", Toast.LENGTH_SHORT).show();
					return;
				}
				list = daDAO.retrieve();
				if(index > 0)
					index--;
				else if(index == 0 && list.size() == 0)
					clearComponents();
				updateScreen();
				
				DevisDAO dDAO = new DevisDAO();
				dDAO.updatePrix(DevisView.currentIdDevis.toString(), 
						daDAO.findPrixTotal(DevisView.currentIdDevis.toString()));
				Toast.makeText(DevisArticleView.this, "Suppression effectuée", 
		    			Toast.LENGTH_SHORT).show();
		}});
		
		Button btnPrem = (Button)findViewById(R.id.btnDevisArticlePremier);
		btnPrem.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				index = 0;
				updateScreen();
		}});
		
		Button btnDern = (Button)findViewById(R.id.btnDevisArticleDernier);
		btnDern.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				index = list.size() - 1;
				updateScreen();
		}});
		
		Button btnPrec = (Button)findViewById(R.id.btnDevisArticlePrecedent);
		btnPrec.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(index != 0)
					index--;
				updateScreen();
		}});
		
		Button btnSuiv = (Button)findViewById(R.id.btnDevisArticleSuivant);
		btnSuiv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(index != list.size() - 1)
					index++;
				updateScreen();
		}});
		
		final Spinner s = (Spinner)findViewById(R.id.spinnerDevisArticle);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 
				android.R.layout.simple_spinner_item, aDAO.retrieveArray());
		s.setAdapter(adapter);
		s.setPrompt("Choisissez un article :");
		s.setOnItemSelectedListener(new OnItemSelectedListener(){
			@Override
			public void onNothingSelected(AdapterView<?> arg0){}
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, 
					int arg2, long arg3){
				String str[] = arg0.getSelectedItem().toString().split(" - ");
				((EditText)findViewById(R.id.txtDevisArticleId))
						.setText(str[0]);
				((EditText)findViewById(R.id.txtDevisArticleLibelle))
						.setText(str[1]);
				((EditText)findViewById(R.id.txtDevisArticlePrixHT)).setText(
						aDAO.findLibellePrix(((EditText)findViewById(
						R.id.txtDevisArticleId)).getText().toString())[1]);
				((EditText)findViewById(R.id.txtDevisArticlePrixTTC)).setText(
						String.valueOf(DBService.prixTTC(Float.parseFloat(
						((EditText)findViewById(R.id.txtDevisArticlePrixHT))
						.getText().toString()))));
				if(index != list.size())
					updateScreen();
				((EditText)findViewById(R.id.txtDevisArticleQtte))
						.requestFocus();
		}});

	}
	
	protected void onPause(){
		super.onPause();
		DevisView.update = true;
	}
	
	public void initDevisArticle(){
		devisArticle.setIdDevis(DevisView.currentIdDevis);
		devisArticle.setIdArticle(((EditText)findViewById(
				R.id.txtDevisArticleId)).getText());
		
		try{
			devisArticle.setQtte(Integer.parseInt(((EditText)findViewById(
					R.id.txtDevisArticleQtte)).getText().toString()));
		}catch(Exception e){
			Toast.makeText(DevisArticleView.this, "Veuillez entrez une valeur " +
					"entière comme quantité", Toast.LENGTH_SHORT).show();
			return;
		}
	}
	
	private void updateScreen(){
		if(!list.isEmpty()){
			DevisArticle da = list.get(index);
			fillComponents(da);
			changeButtonsState();
		}else
			changeButtonsState();
	}
	
	private void changeButtonsState(){
		if(index == 0){
			((Button)findViewById(R.id.btnDevisArticleSuivant))
					.setEnabled(true);
			((Button)findViewById(R.id.btnDevisArticleDernier))
					.setEnabled(true);
			((Button)findViewById(R.id.btnDevisArticlePrecedent))
					.setEnabled(false);
			((Button)findViewById(R.id.btnDevisArticlePremier))
					.setEnabled(false);
		}else if(index == list.size() - 1 || index == list.size()){
			((Button)findViewById(R.id.btnDevisArticleSuivant))
					.setEnabled(false);
			((Button)findViewById(R.id.btnDevisArticleDernier))
					.setEnabled(false);
			((Button)findViewById(R.id.btnDevisArticlePrecedent))
					.setEnabled(true);
			((Button)findViewById(R.id.btnDevisArticlePremier))
					.setEnabled(true);
		}else{
			((Button)findViewById(R.id.btnDevisArticlePremier))
					.setEnabled(true);
			((Button)findViewById(R.id.btnDevisArticleDernier))
					.setEnabled(true);
			((Button)findViewById(R.id.btnDevisArticlePrecedent))
					.setEnabled(true);
			((Button)findViewById(R.id.btnDevisArticleSuivant))
					.setEnabled(true);
		}
		
		if(list.size() == 1 || list.size() == 0){ 
			((Button)findViewById(R.id.btnDevisArticleSuivant))
					.setEnabled(false);
			((Button)findViewById(R.id.btnDevisArticleDernier))
					.setEnabled(false);
			((Button)findViewById(R.id.btnDevisArticlePrecedent))
					.setEnabled(false);
			((Button)findViewById(R.id.btnDevisArticlePremier))
					.setEnabled(false);
		}
		if(index == 1){
			((Button)findViewById(R.id.btnDevisArticlePrecedent))
					.setEnabled(true);
			((Button)findViewById(R.id.btnDevisArticlePremier))
					.setEnabled(true);
		}
	}
	
	private void fillComponents(DevisArticle da){
		((Spinner)findViewById(R.id.spinnerDevisArticle)).setEnabled(false);
		((EditText)findViewById(R.id.txtDevisArticleId))
				.setText(da.getIdArticle());
		((EditText)findViewById(R.id.txtDevisArticleLibelle)).setText(
				aDAO.findLibellePrix(da.getIdArticle().toString())[0]);
		((EditText)findViewById(R.id.txtDevisArticleQtte))
				.setText(String.valueOf(da.getQtte()));
		((EditText)findViewById(R.id.txtDevisArticlePrixHT)).setText(
				aDAO.findLibellePrix(da.getIdArticle().toString())[1]);
		((EditText)findViewById(R.id.txtDevisArticlePrixTTC)).setText(
				String.valueOf(DBService.prixTTC(Float.parseFloat(
				aDAO.findLibellePrix(da.getIdArticle().toString())[1]))));
		index = getIndexOf(da);
	}

	private int getIndexOf(DevisArticle da){
		for(int i=0; i<list.size(); i++){
			if(list.get(i).getIdDevis().equals(da.getIdDevis()) && 
					list.get(i).getIdArticle().equals(da.getIdArticle()))
				return i;
		}
		return 0;
	}
	
	public void clearComponents(){
		((Spinner)findViewById(R.id.spinnerDevisArticle)).setEnabled(true);
		((EditText)findViewById(R.id.txtDevisArticleId)).setText("");
		((EditText)findViewById(R.id.txtDevisArticleLibelle)).setText("");
		((EditText)findViewById(R.id.txtDevisArticleQtte)).setText("");
		((EditText)findViewById(R.id.txtDevisArticlePrixHT)).setText("");
		((EditText)findViewById(R.id.txtDevisArticlePrixTTC)).setText("");
		((EditText)findViewById(R.id.txtDevisArticleQtte)).requestFocus();
		index = list.size();
		changeButtonsState();
	}
}