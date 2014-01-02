package com.devisfactures.gui;

import java.util.ArrayList;

import com.devisfactures.dao.ArticleDAO;
import com.devisfactures.model.Article;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class OptionFiltrerArticleView extends Activity{
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.optionfiltrerarticle);
		
		Button btn = (Button)findViewById(R.id.btnOptionFiltrerArticle);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ArticleDAO aDAO = new ArticleDAO();
				ArrayList<Article> list = new ArrayList<Article>();
				String value = ((EditText)findViewById(
						R.id.txtOptionFiltrerArticle)).getText().toString();

				if(((RadioButton)findViewById(
						R.id.radioOptionFiltrerArticleQtte)).isChecked()){
					try{
						list = aDAO.filterQtte(Integer.parseInt(value));
					}catch(Exception e){
						Toast.makeText(OptionFiltrerArticleView.this, 
								"Quantité invalide, filtrage annulé !", 
				    			Toast.LENGTH_SHORT).show();
					}
				}else if(((RadioButton)findViewById(
						R.id.radioOptionFiltrerArticlePrix)).isChecked()){
					try{
						list = aDAO.filterPrix(Float.parseFloat(value));
					}catch(Exception e){
						Toast.makeText(OptionFiltrerArticleView.this, 
								"Prix invalide, filtrage annulé !", 
				    			Toast.LENGTH_SHORT).show();
					}
				}
				
				if(list != null){
					setResult(RESULT_OK, getIntent().putExtra(
							"filteredArticles", list));
					finish();
				}else
					Toast.makeText(OptionFiltrerArticleView.this, 
							"Aucun résultat !", Toast.LENGTH_SHORT).show();
		}});
	}

}
