package kr.co.WhenWhereWho3;

import java.io.Serializable;

public class Movie implements Serializable{
	private String title;		//	��ȭ ����
	private String thumbnail;	//	��ȭ ������ ����
	private String openInfo;	//	��ȭ ������( open_info )
	private String director;	//  ����
	private String [] actor;	//	���
	private String nation;		//	���۱���
	private String genre;		//	�帣
	private String grade;		//	����
	private String homepage;	//  ���Ļ���Ʈ
	private String story;		//	�ٰŸ�
	private String [] photo;	//	����( 5�� ) : tag�� - photo#( # : 1, 2, 3, 4, 5 )
	
	private String when;
	private String where;
	private String with;
	private String comment;
	
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
	public String getWhen() {
		return when;
	}
	public void setWhen(String when) {
		this.when = when;
	}
	public String getWhere() {
		return where;
	}
	public void setWhere(String where) {
		this.where = where;
	}
	public String getWith() {
		return with;
	}
	public void setWith(String with) {
		this.with = with;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	
}
