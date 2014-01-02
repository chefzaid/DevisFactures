package com.devisfactures.gui;

import java.util.ArrayList;

import com.devisfactures.service.DBService;
import com.devisfactures.dao.ArticleDAO;
import com.devisfactures.dao.FactureArticleDAO;
import com.devisfactures.dao.FactureDAO;
import com.devisfactures.model.FactureArticle;

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

public class FactureArticleView extends Activity {
	
	FactureArticle factureArticle = new FactureArticle();
	FactureArticleDAO faDAO = new FactureArticleDAO();
	ArticleDAO aDAO = new ArticleDAO();
	ArrayList<FactureArticle> list = new ArrayList<FactureArticle>();
	int index = 0;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.facturearticle);

		if(!DBService.isTableEmpty("FactureArticle") && 
				(FactureView.currentIdFacture != null || 
						FactureView.currentIdFacture != "")){
			list = faDAO.retrieve();
			index = list.size() - 1;
			updateScreen();
		}else
			clearComponents();

		Button btnVider = (Button)findViewById(R.id.btnFactureArticleVider);
		btnVider.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				clearComponents();
		}});
		
		Button btnAjouter = (Button)findViewById(R.id.btnFactureArticleAjouter);
		btnAjouter.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				try{
					initFactureArticle();
				}catch(Exception e){
					Toast.makeText(FactureArticleView.this, "Quantité invalide, " +
							"ajout annulé !", Toast.LENGTH_SHORT).show();
					return;
				}
				if(aDAO.findQtte(factureArticle.getIdArticle().toString()) 
						>= factureArticle.getQtte()){
					try{
						faDAO.create(factureArticle);
						aDAO.setQtte(factureArticle.getIdArticle().toString(), 
								factureArticle.getQtte());
					}catch(Exception e){
						Toast.makeText(FactureArticleView.this, 
								"Erreur de contrainte, ajout annulée !", 
				    			Toast.LENGTH_SHORT).show();
						return;
					}
					list = faDAO.retrieve();
					index = list.size() - 1;
					updateScreen();
					
					FactureDAO fDAO = new FactureDAO();
					fDAO.updatePrix(FactureView.currentIdFacture.toString(), 
							faDAO.findPrixTotal(
							FactureView.currentIdFacture.toString()));
					Toast.makeText(FactureArticleView.this, "Ajout effectué", 
			    			Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(FactureArticleView.this, "Quantité du stock " +
							"en pénurie, ajout annulé !", 
			    			Toast.LENGTH_SHORT).show();
				}
		}});
		
		Button btnModifier = (Button)findViewById(
				R.id.btnFactureArticleModifier);
		btnModifier.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				try{
					initFactureArticle();
				}catch(Exception e){
					Toast.makeText(FactureArticleView.this, "Quantité invalide, " +
							"modification annulée", Toast.LENGTH_SHORT).show();
					return;
				}
				if(aDAO.findQtte(factureArticle.getIdArticle().toString()) >= 
						factureArticle.getQtte()){
					try{
						faDAO.update(factureArticle);
					}catch(Exception e){
						Toast.makeText(FactureArticleView.this, 
								"Erreur de contrainte, modification annulée !", 
				    			Toast.LENGTH_SHORT).show();
						return;
					}
					list = faDAO.retrieve();
					updateScreen();
					
					FactureDAO fDAO = new FactureDAO();
					fDAO.updatePrix(FactureView.currentIdFacture.toString(), 
							faDAO.findPrixTotal(
							FactureView.currentIdFacture.toString()));
					Toast.makeText(FactureArticleView.this, 
							"Modification effectuée", 
							Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(FactureArticleView.this, 
							"Quantité du stock en pénurie, modification " +
							"annulée !", Toast.LENGTH_SHORT).show();
				}
		}});
		
		Button btnSuppr = (Button)findViewById(
				R.id.btnFactureArticleSupprimer);
		btnSuppr.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				initFactureArticle();
				try{
					faDAO.delete(factureArticle.getIdFacture().toString(), 
							factureArticle.getIdArticle().toString());
				}catch(Exception e){
					Toast.makeText(FactureArticleView.this, 
							"Erreur de contrainte, suppression annulée !", 
			    			Toast.LENGTH_SHORT).show();
					return;
				}
				list = faDAO.retrieve();
				if(index > 0)
					index--;
				else if(index == 0 && list.size() == 0)
					clearComponents();
				updateScreen();
				
				FactureDAO fDAO = new FactureDAO();
				fDAO.updatePrix(FactureView.currentIdFacture.toString(), 
						faDAO.findPrixTotal(
						FactureView.currentIdFacture.toString()));
				Toast.makeText(FactureArticleView.this, "Suppression effectuée", 
		    			Toast.LENGTH_SHORT).show();
		}});
		
		Button btnPrem = (Button)findViewById(R.id.btnFactureArticlePremier);
		btnPrem.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				index = 0;
				updateScreen();
		}});
		
		Button btnDern = (Button)findViewById(R.id.btnFactureArticleDernier);
		btnDern.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				index = list.size() - 1;
				updateScreen();
		}});
		
		Button btnPrec = (Button)findViewById(R.id.btnFactureArticlePrecedent);
		btnPrec.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(index != 0)
					index--;
				updateScreen();
		}});
		
		Button btnSuiv = (Button)findViewById(R.id.btnFactureArticleSuivant);
		btnSuiv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(index != list.size() - 1)
					index++;
				updateScreen();
		}});
		
		final Spinner s = (Spinner)findViewById(R.id.spinnerFactureArticle);
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
				((EditText)findViewById(R.id.txtFactureArticleId))
						.setText(str[0]);
				((EditText)findViewById(R.id.txtFactureArticleLibelle))
						.setText(str[1]);
				((EditText)findViewById(R.id.txtFactureArticlePrixHT)).setText(
						aDAO.findLibellePrix(((EditText)findViewById(
						R.id.txtFactureArticleId)).getText().toString())[1]);
				((EditText)findViewById(R.id.txtFactureArticlePrixTTC))
						.setText(String.valueOf(DBService.prixTTC(
						Float.parseFloat(((EditText)findViewById(
						R.id.txtFactureArticlePrixHT)).getText().toString()))));
				if(index != list.size())
					updateScreen();
				((EditText)findViewById(R.id.txtFactureArticleQtte))
					.requestFocus();
		}});

	}
	
	protected void onPause(){
		super.onPause();
		FactureView.update = true;
	}
	
	public void initFactureArticle(){
		factureArticle.setIdFacture(FactureView.currentIdFacture);
		factureArticle.setIdArticle(((EditText)findViewById(
				R.id.txtFactureArticleId)).getText());
		
		try{
			factureArticle.setQtte(Integer.parseInt(((EditText)findViewById(
					R.id.txtFactureArticleQtte)).getText().toString()));
		}catch(Exception e){
			Toast.makeText(FactureArticleView.this, "Veuillez entrez une valeur " +
					"entière comme quantité", Toast.LENGTH_SHORT).show();
			return;
		}
	}
	
	private void updateScreen(){
		if(!list.isEmpty()){
			FactureArticle fa = list.get(index);
			fillComponents(fa);
			changeButtonsState();
		}else
			changeButtonsState();
	}
	
	private void changeButtonsState(){
		if(index == 0){
			((Button)findViewById(R.id.btnFactureArticleSuivant)).
					setEnabled(true);
			((Button)findViewById(R.id.btnFactureArticleDernier)).
					setEnabled(true);
			((Button)findViewById(R.id.btnFactureArticlePrecedent))
					.setEnabled(false);
			((Button)findViewById(R.id.btnFactureArticlePremier)).
					setEnabled(false);
		}else if(index == list.size() - 1 || index == list.size()){
			((Button)findViewById(R.id.btnFactureArticleSuivant)).
					setEnabled(false);
			((Button)findViewById(R.id.btnFactureArticleDernier)).
					setEnabled(false);
			((Button)findViewById(R.id.btnFactureArticlePrecedent)).
					setEnabled(true);
			((Button)findViewById(R.id.btnFactureArticlePremier)).
					setEnabled(true);
		}else{
			((Button)findViewById(R.id.btnFactureArticlePremier)).
					setEnabled(true);
			((Button)findViewById(R.id.btnFactureArticleDernier)).
					setEnabled(true);
			((Button)findViewById(R.id.btnFactureArticlePrecedent)).
					setEnabled(true);
			((Button)findViewById(R.id.btnFactureArticleSuivant)).
					setEnabled(true);
		}
		
		if(list.size() == 1 || list.size() == 0){ 
			((Button)findViewById(R.id.btnFactureArticleSuivant)).
					setEnabled(false);
			((Button)findViewById(R.id.btnFactureArticleDernier)).
					setEnabled(false);
			((Button)findViewById(R.id.btnFactureArticlePrecedent)).
					setEnabled(false);
			((Button)findViewById(R.id.btnFactureArticlePremier)).
					setEnabled(false);
		}
		if(index == 1){
			((Button)findViewById(R.id.btnFactureArticlePrecedent)).
					setEnabled(true);
			((Button)findViewById(R.id.btnFactureArticlePremier)).
					setEnabled(true);
		}
	}
	
	private void fillComponents(FactureArticle fa){
		((Spinner)findViewById(R.id.spinnerFactureArticle)).setEnabled(false);
		((EditText)findViewById(R.id.txtFactureArticleId))
				.setText(fa.getIdArticle());
		((EditText)findViewById(R.id.txtFactureArticleLibelle)).setText(
				aDAO.findLibellePrix(fa.getIdArticle().toString())[0]);
		((EditText)findViewById(R.id.txtFactureArticleQtte))
				.setText(String.valueOf(fa.getQtte()));
		((EditText)findViewById(R.id.txtFactureArticlePrixHT)).setText(
				aDAO.findLibellePrix(fa.getIdArticle().toString())[1]);
		((EditText)findViewById(R.id.txtFactureArticlePrixTTC)).setText(
				String.valueOf(DBService.prixTTC(Float.parseFloat(
				aDAO.findLibellePrix(fa.getIdArticle().toString())[1]))));
		index = getIndexOf(fa);
	}

	private int getIndexOf(FactureArticle fa){
		for(int i=0; i<list.size(); i++){
			if(list.get(i).getIdFacture().equals(fa.getIdFacture()) && 
					list.get(i).getIdArticle().equals(fa.getIdArticle()))
				return i;
		}
		return 0;
	}
	
	public void clearComponents(){
		((Spinner)findViewById(R.id.spinnerFactureArticle)).setEnabled(true);
		((EditText)findViewById(R.id.txtFactureArticleId)).setText("");
		((EditText)findViewById(R.id.txtFactureArticleLibelle)).setText("");
		((EditText)findViewById(R.id.txtFactureArticleQtte)).setText("");
		((EditText)findViewById(R.id.txtFactureArticlePrixHT)).setText("");
		((EditText)findViewById(R.id.txtFactureArticlePrixTTC)).setText("");
		((EditText)findViewById(R.id.txtFactureArticleQtte)).requestFocus();
		index = list.size();
		changeButtonsState();
	}
}