package com.devisfactures.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.devisfactures.gui.R;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FileChooser extends ListActivity {
	
    private File currentDir;
    private FileArrayAdapter adapter;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentDir = new File("/sdcard/");
        fill(currentDir);
    }
    
    private void fill(File f){
    	File[]dirs = f.listFiles();
		this.setTitle("Dossier actuel : "+f.getName());
		List<Option>dir = new ArrayList<Option>();
		List<Option>fls = new ArrayList<Option>();
		try{
			for(File ff: dirs){
				if(ff.isDirectory())
					dir.add(new Option(ff.getName(), "Dossier", 
							ff.getAbsolutePath()));
				else
					fls.add(new Option(ff.getName(),"Taille du fichier : " + 
							ff.length(), ff.getAbsolutePath()));
			 }
		 }catch(Exception e){ }
		Collections.sort(dir);
		Collections.sort(fls);
		dir.addAll(fls);
		if(!f.getName().equalsIgnoreCase("sdcard"))
			dir.add(0,new Option("..", "Dossier parent", f.getParent()));
		adapter = new FileArrayAdapter(FileChooser.this, 
				R.layout.fileview, dir);
		this.setListAdapter(adapter);
    }
    
    @Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Option o = adapter.getItem(position);
		if(o.getData().equalsIgnoreCase("dossier") || 
				o.getData().equalsIgnoreCase("dossier parent")){
			currentDir = new File(o.getPath());
			fill(currentDir);
		}else
			onFileClick(o);
	}
    
    private void onFileClick(Option o){
    	Toast.makeText(this, "Fichier choisis : " + o.getName(), 
    			Toast.LENGTH_SHORT).show();
    	setResult(RESULT_OK, getIntent().putExtra("filename", "" + 
    			o.getPath()));
    	finish();
    }
    
    public class FileArrayAdapter extends ArrayAdapter<Option>{

    	private Context c;
    	private int id;
    	private List<Option>items;
    	
    	public FileArrayAdapter(Context context, int textViewResourceId,
    			List<Option> objects) {
    		super(context, textViewResourceId, objects);
    		c = context;
    		id = textViewResourceId;
    		items = objects;
    	}
    	
    	@Override
    	public View getView(int position, View convertView, ViewGroup parent){
    		 View v = convertView;
    		 if(v == null){
    			 LayoutInflater vi = (LayoutInflater)c.getSystemService(
    					 Context.LAYOUT_INFLATER_SERVICE);
    			 v = vi.inflate(id, null);
    		 }
    		 final Option o = items.get(position);
    		 if(o != null){
    			 TextView t1 = (TextView) v.findViewById(R.id.lblFileChooser1);
    			 TextView t2 = (TextView) v.findViewById(R.id.lblFileChooser2);
    			 if(t1!=null)
    				 t1.setText(o.getName());
    			 if(t2!=null)
    				 t2.setText(o.getData());       
    		 }
    		 return v;
    	}
    	
    	public Option getItem(int i){
    		return items.get(i);
    	}

    }
    
    public class Option implements Comparable<Option>{
    	
    	private String name;
    	private String data;
    	private String path;
    	
    	public Option(String n,String d,String p){
    		name = n;
    		data = d;
    		path = p;
    	}
    	
    	@Override
    	public int compareTo(Option o) {
    		if(this.name != null)
    			return this.name.toLowerCase().compareTo(
    					o.getName().toLowerCase()); 
    		else 
    			throw new IllegalArgumentException();
    	}
    	
    	public String getName(){
    		return name;
    	}
    	public String getData(){
    		return data;
    	}
    	public String getPath(){
    		return path;
    	}
    	
    }
}