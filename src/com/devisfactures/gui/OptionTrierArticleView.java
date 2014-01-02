package com.devisfactures.gui;

import java.util.ArrayList;

import com.devisfactures.dao.ArticleDAO;
import com.devisfactures.model.Article;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;

public class OptionTrierArticleView extends Activity{
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.optiontrierarticle);
		
		Button btn = (Button)findViewById(R.id.btnOptionTrierArticle);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ArticleDAO aDAO = new ArticleDAO();
				ArrayList<Article> list = new ArrayList<Article>();
				String col = new String();
				String order = new String();

				if(((RadioButton)findViewById(
						R.id.radioOptionTrierArticleQtte)).isChecked())
					col = "QtteEnStock";
				else if(((RadioButton)findViewById(
						R.id.radioOptionTrierArticlePrix)).isChecked())
					col = "Prix";
					
				if(((RadioButton)findViewById(
						R.id.radioOptionTrierArticleAsc)).isChecked())
					order = "ASC";
				else if(((RadioButton)findViewById(
						R.id.radioOptionTrierArticleDesc)).isChecked())
					order = "DESC";
				
				list = aDAO.sort(col, order);
				if(list != null){
					setResult(RESULT_OK, getIntent().putExtra(
							"sortedArticles", list));
					finish();
				}
		}});
	}

}
