package com.devisfactures.dao;

import java.util.ArrayList;

public interface IDAO<T> {
	
	public void create(T model);
	public void update(T model);
	public void delete(String... params);
	public ArrayList<T> retrieve();
	
}
