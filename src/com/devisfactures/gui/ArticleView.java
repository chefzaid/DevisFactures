package com.devisfactures.gui;

import java.util.ArrayList;

import com.devisfactures.dao.ArticleDAO;
import com.devisfactures.model.Article;
import com.devisfactures.service.DBService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ArticleView extends Activity {
	
	private Article article = new Article();
	private ArticleDAO aDAO = new ArticleDAO();
	private ArrayList<Article> list = new ArrayList<Article>();
	private int index = 0;
	
	private final int RECHERCHE = 1;
	private final int FILTRE = 2;
	private final int TRI = 3;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.article);
		
		if(!DBService.isTableEmpty("Article")){
			list = aDAO.retrieve();
			index = list.size() - 1;
			updateScreen();
		}else
			clearComponents();
	   
		Button btnVider = (Button)findViewById(R.id.btnArticleVider);
		btnVider.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				clearComponents();
		}});
		
		Button btnAjouter = (Button)findViewById(R.id.btnArticleAjouter);
		btnAjouter.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				try{
					initArticle();
				}catch(Exception e){
					Toast.makeText(ArticleView.this, "Erreur de saisie, " +
							"ajout annulé !", 
			    			Toast.LENGTH_SHORT).show();
					return;
				}
				try{
					aDAO.create(article);
				}catch(Exception e){
					Toast.makeText(ArticleView.this, "Erreur de contrainte, " +
							"ajout annulé !", Toast.LENGTH_SHORT).show();
					return;
				}
				list = aDAO.retrieve();
				index = list.size() - 1;
				updateScreen();
				Toast.makeText(ArticleView.this, "Ajout effectué", 
		    			Toast.LENGTH_SHORT).show();
		}});
		
		Button btnModifier = (Button)findViewById(R.id.btnArticleModifier);
		btnModifier.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				try{
					initArticle();
				}catch(Exception e){
					Toast.makeText(ArticleView.this, "Erreur de saisie, " +
							"modification annulée !", 
			    			Toast.LENGTH_SHORT).show();
					return;
				}
				try{
					aDAO.update(article);
				}catch(Exception e){
					Toast.makeText(ArticleView.this, "Erreur de contrainte, " + 
							"modification annulée !", 
							Toast.LENGTH_SHORT).show();
					return;
				}
				list = aDAO.retrieve();
				updateScreen();
				Toast.makeText(ArticleView.this, "Modification effectuée", 
		    			Toast.LENGTH_SHORT).show();
		}});
		
		Button btnSuppr = (Button)findViewById(R.id.btnArticleSupprimer);
		btnSuppr.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				initArticle();
				try{
					aDAO.delete(article.getIdArticle().toString());
				}catch(Exception e){
					Toast.makeText(ArticleView.this, "Erreur de contrainte, " + 
							"suppression annulée !", 
							Toast.LENGTH_SHORT).show();
					return;
				}
				list = aDAO.retrieve();
				if(index > 0)
					index--;
				else if(index == 0 && list.size() == 0)
					clearComponents();
				updateScreen();
				Toast.makeText(ArticleView.this, "Suppression effectuée", 
		    			Toast.LENGTH_SHORT).show();
		}});
		
		Button btnPrem = (Button)findViewById(R.id.btnArticlePremier);
		btnPrem.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				index = 0;
				updateScreen();
		}});
		
		Button btnDern = (Button)findViewById(R.id.btnArticleDernier);
		btnDern.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				index = list.size() - 1;
				updateScreen();
		}});
		
		Button btnPrec = (Button)findViewById(R.id.btnArticlePrecedent);
		btnPrec.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(index != 0)
					index--;
				updateScreen();
		}});
		
		Button btnSuiv = (Button)findViewById(R.id.btnArticleSuivant);
		btnSuiv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(index != list.size() - 1)
					index++;
				updateScreen();
		}});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.articlemenu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.optionArticleRecherchercher :
			Intent optionRechercherIntent = new Intent(ArticleView.this, 
					OptionRechercherView.class);
			startActivityForResult(optionRechercherIntent, RECHERCHE);
			return true;
		case R.id.optionArticleFiltrer :
			Intent optionFiltrerIntent = new Intent(ArticleView.this, 
					OptionFiltrerArticleView.class);
			startActivityForResult(optionFiltrerIntent, FILTRE);
			return true;
		case R.id.optionArticleTrier :
			Intent optionTrierIntent = new Intent(ArticleView.this, 
					OptionTrierArticleView.class);
			startActivityForResult(optionTrierIntent, TRI);
			return true;
		case R.id.optionArticleAnnulerFiltre :
			list.clear();
			list = aDAO.retrieve();
			index = list.size() - 1;
			updateScreen();
			return true;
		default :
			 return super.onOptionsItemSelected(item);
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, 
			Intent data){
		if(resultCode == RESULT_OK){
			switch(requestCode){
			case RECHERCHE :
				Article a = (Article)data.getExtras().get("article");
				index = getIndexOf(a);
				updateScreen();
				break;
			case FILTRE :
				@SuppressWarnings("unchecked")
				ArrayList<Article> filteredArticles = 
					(ArrayList<Article>)data.getExtras()
					.get("filteredArticles");
				list.clear();
				list = filteredArticles;
				index = list.size() - 1;
				updateScreen();
				break;
			case TRI :
				@SuppressWarnings("unchecked")
				ArrayList<Article> sortedArticles = (
						ArrayList<Article>)data.getExtras()
						.get("sortedArticles");
				list.clear();
				list = sortedArticles;
				index = list.size() - 1;
				updateScreen();
				break;
			}
		}
	}

	
	private void initArticle(){
		article.setIdArticle(((EditText)findViewById(
				R.id.txtArticleId)).getText());
		article.setLibelle(((EditText)findViewById(
				R.id.txtArticleLibelle)).getText());
		
		article.setPrix(Float.parseFloat(((EditText)findViewById(
				R.id.txtArticlePrixHT)).getText().toString()));
		article.setQtteEnStock(Integer.parseInt(((EditText)findViewById(
				R.id.txtArticleQtte)).getText().toString()));
		article.setNotes(((EditText)findViewById(
				R.id.txtArticleNotes)).getText());
	}
	
	private void updateScreen(){
		if(!list.isEmpty()){
			Article c = list.get(index);
			fillComponents(c);
			changeButtonsState();
		}else
			changeButtonsState();
	}
	
	private void changeButtonsState(){
		if(index == 0){
			((Button)findViewById(R.id.btnArticleSuivant)).setEnabled(true);
			((Button)findViewById(R.id.btnArticleDernier)).setEnabled(true);
			((Button)findViewById(R.id.btnArticlePrecedent)).setEnabled(false);
			((Button)findViewById(R.id.btnArticlePremier)).setEnabled(false);
		}else if(index == list.size() - 1 || index == list.size()){
			((Button)findViewById(R.id.btnArticleSuivant)).setEnabled(false);
			((Button)findViewById(R.id.btnArticleDernier)).setEnabled(false);
			((Button)findViewById(R.id.btnArticlePrecedent)).setEnabled(true);
			((Button)findViewById(R.id.btnArticlePremier)).setEnabled(true);
		}else{
			((Button)findViewById(R.id.btnArticlePremier)).setEnabled(true);
			((Button)findViewById(R.id.btnArticleDernier)).setEnabled(true);
			((Button)findViewById(R.id.btnArticlePrecedent)).setEnabled(true);
			((Button)findViewById(R.id.btnArticleSuivant)).setEnabled(true);
		}
		
		if(list.size() == 1 || list.size() == 0){ 
			((Button)findViewById(R.id.btnArticleSuivant)).setEnabled(false);
			((Button)findViewById(R.id.btnArticleDernier)).setEnabled(false);
			((Button)findViewById(R.id.btnArticlePrecedent)).setEnabled(false);
			((Button)findViewById(R.id.btnArticlePremier)).setEnabled(false);
		}
		if(index == 1){
			((Button)findViewById(R.id.btnArticlePrecedent)).setEnabled(true);
			((Button)findViewById(R.id.btnArticlePremier)).setEnabled(true);
		}
	}
	
	private void fillComponents(Article a){
		((EditText)findViewById(R.id.txtArticleId)).setText(a.getIdArticle());
		((EditText)findViewById(R.id.txtArticleLibelle))
				.setText(a.getLibelle());
		((EditText)findViewById(R.id.txtArticlePrixHT)).setText(
				String.valueOf(a.getPrix()));
		((EditText)findViewById(R.id.txtArticlePrixTTC)).setText(
				String.valueOf(DBService.prixTTC(Float.parseFloat(
				((EditText)findViewById(R.id.txtArticlePrixHT))
				.getText().toString()))));
		((EditText)findViewById(R.id.txtArticleQtte)).setText(
				String.valueOf(a.getQtteEnStock()));
		((EditText)findViewById(R.id.txtArticleNotes)).setText(a.getNotes());
		index = getIndexOf(a);
	}
	
	private void clearComponents(){
		String newId = new String();
		if(aDAO.getLastIdArticle() != null){
			String str = aDAO.getLastIdArticle().substring(1);
			int id = Integer.parseInt(str) + 1;
			newId = "A" + String.format("%03d", id);
		}else
			newId = "A001";
	
		((EditText)findViewById(R.id.txtArticleId)).setText(newId);
		((EditText)findViewById(R.id.txtArticleLibelle)).setText("");
		((EditText)findViewById(R.id.txtArticlePrixHT)).setText("");
		((EditText)findViewById(R.id.txtArticlePrixTTC)).setText(
				"Auto-calculé");
		((EditText)findViewById(R.id.txtArticleQtte)).setText("");
		((EditText)findViewById(R.id.txtArticleNotes)).setText("");
		((EditText)findViewById(R.id.txtArticleLibelle)).requestFocus();
		index = list.size();
		changeButtonsState();
	}
	
	private int getIndexOf(Article a){
		for(int i=0; i<list.size(); i++){
			if(list.get(i).getIdArticle().equals(a.getIdArticle())){
				return i;
			}
		}
		return 0;
	}

}
