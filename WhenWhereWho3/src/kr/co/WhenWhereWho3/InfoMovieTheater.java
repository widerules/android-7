package kr.co.WhenWhereWho3;


/**
 * @author W3
 * 영화관 위치 및 이름과 reference값을 가진 클래스
 *
 */
public class InfoMovieTheater {
	private double lat;			//	위도
	private double lng;			//	경도
	private String reference;	//	영화관 정보 담고있는 reference키
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	
	@Override
	public String toString() {
		return "InfoMovieTheater [lat=" + lat + ", lng=" + lng + ", name="
				+ ", reference=" + reference + "]";
	}
}
