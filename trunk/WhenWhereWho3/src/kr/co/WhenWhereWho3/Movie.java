package kr.co.WhenWhereWho3;

import java.io.Serializable;

public class Movie implements Serializable{
	private String title;		//	영화 제목
	private String thumbnail;	//	영화 포스터 사진
	private String openInfo;	//	영화 개봉일( open_info )
	private String director;	//  감독
	private String [] actor;	//	배우
	private String nation;		//	제작국가
	private String genre;		//	장르
	private String grade;		//	평점
	private String homepage;	//  공식사이트
	private String story;		//	줄거리
	private String [] photo;	//	사진( 5장 ) : tag명 - photo#( # : 1, 2, 3, 4, 5 )
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	public String getOpenInfo() {
		return openInfo;
	}
	public void setOpenInfo(String openInfo) {
		this.openInfo = openInfo;
	}
	public String getDirector() {
		return director;
	}
	public void setDirector(String director) {
		this.director = director;
	}
	public String[] getActor() {
		return actor;
	}
	public void setActor(String[] actor) {
		this.actor = actor;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getHomepage() {
		return homepage;
	}
	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}
	public String getStory() {
		return story;
	}
	public void setStory(String story) {
		this.story = story;
	}
	public String[] getPhoto() {
		return photo;
	}
	public void setPhoto(String[] photo) {
		this.photo = photo;
	}
	
}
