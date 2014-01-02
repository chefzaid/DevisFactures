package com.devisfactures.gui;

import com.devisfactures.dao.ArticleDAO;
import com.devisfactures.dao.ClientDAO;
import com.devisfactures.dao.DevisDAO;
import com.devisfactures.dao.FactureDAO;
import com.devisfactures.model.Article;
import com.devisfactures.model.Client;
import com.devisfactures.model.Devis;
import com.devisfactures.model.Facture;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class OptionRechercherView extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.optionrechercher);
		
		Button btn = (Button)findViewById(R.id.btnOptionRechercher);
		btn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				String callingActivity = OptionRechercherView.this
						.getCallingActivity().getClassName();
				
				if(callingActivity.equals(ClientView.class.getName())){
					ClientDAO cDAO = new ClientDAO();
					Client c = cDAO.find(((EditText)findViewById(
							R.id.txtOptionRechercher)).getText().toString());
					if(c != null){
						setResult(RESULT_OK, getIntent().putExtra("client", c));
						finish();
					}
				}else if(callingActivity.equals(ArticleView.class.getName())){
					ArticleDAO aDAO = new ArticleDAO();
					Article a = aDAO.find(((EditText)findViewById(
							R.id.txtOptionRechercher)).getText().toString());
					if(a != null){
						setResult(RESULT_OK, getIntent().putExtra(
								"article", a));
						finish();
					}
				}else if(callingActivity.equals(DevisTabHost.class.getName())){
					DevisDAO dDAO = new DevisDAO();
					Devis d = dDAO.find(((EditText)findViewById(
							R.id.txtOptionRechercher)).getText().toString());
					if(d != null){
						setResult(RESULT_OK, getIntent().putExtra("devis", d));
						finish();
					}
				}else if(callingActivity.equals(FactureTabHost.class
						.getName())){
					FactureDAO fDAO = new FactureDAO();
					Facture f = fDAO.find(((EditText)findViewById(
							R.id.txtOptionRechercher)).getText().toString());
					if(f != null){
						setResult(RESULT_OK, getIntent().putExtra(
								"facture", f));
						finish();
					}else
						Toast.makeText(OptionRechercherView.this, 
								"Aucun résultat !", Toast.LENGTH_SHORT).show();
				}
		}});

	}
}
