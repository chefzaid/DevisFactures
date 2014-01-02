package com.devisfactures.gui;

import java.util.ArrayList;

import com.devisfactures.dao.FactureDAO;
import com.devisfactures.model.Facture;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;

public class OptionTrierFactureView extends Activity{
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.optiontrierfacture);
		
		Button btn = (Button)findViewById(R.id.btnOptionFiltrerFacture);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FactureDAO dDAO = new FactureDAO();
				ArrayList<Facture> list = new ArrayList<Facture>();
				String col = new String();
				String order = new String();

				if(((RadioButton)findViewById(
						R.id.radioOptionTrierFactureEtat)).isChecked())
					col = "Paye";
				else if(((RadioButton)findViewById(
						R.id.radioOptionTrierFactureDate)).isChecked())
					col = "Date";
				
				if(((RadioButton)findViewById(
						R.id.radioOptionTrierFactureAsc)).isChecked())
					order = "ASC";
				else if(((RadioButton)findViewById(
						R.id.radioOptionTrierFactureDesc)).isChecked())
					order = "DESC";
				
				list = dDAO.sort(col, order);
				if(list != null){
					setResult(RESULT_OK, getIntent().putExtra(
							"sortedFactures", list));
					finish();
				}
		}});
	}

}
