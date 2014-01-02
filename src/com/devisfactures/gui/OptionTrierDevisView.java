package com.devisfactures.gui;

import java.util.ArrayList;

import com.devisfactures.dao.DevisDAO;
import com.devisfactures.model.Devis;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;

public class OptionTrierDevisView extends Activity{
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.optiontrierdevis);
		
		Button btn = (Button)findViewById(R.id.btnOptionTrierDevis);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DevisDAO dDAO = new DevisDAO();
				ArrayList<Devis> list = new ArrayList<Devis>();
				String col = new String();
				String order = new String();

				if(((RadioButton)findViewById(
						R.id.radioOptionTrierDevisEtat)).isChecked())
					col = "Facture";
				else if(((RadioButton)findViewById(
						R.id.radioOptionTrierDevisDate)).isChecked())
					col = "Date";
				
				if(((RadioButton)findViewById(
						R.id.radioOptionTrierDevisAsc)).isChecked())
					order = "ASC";
				else if(((RadioButton)findViewById(
						R.id.radioOptionTrierDevisDesc)).isChecked())
					order = "DESC";
				
				list = dDAO.sort(col, order);
				if(list != null){
					setResult(RESULT_OK, getIntent().putExtra(
							"sortedDevis", list));
					finish();
				}
		}});
	}

}
