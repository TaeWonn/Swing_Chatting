package project.search.controller;

import project.search.view.Search;

public class Controller {
	
	Search s = new Search();
	
	//비밀번호 찾기-정보 전달
	public String getId() {

		return s.idStr ;
	}
	public String getName() {
		return s.nameStr;
	}
	
	

}