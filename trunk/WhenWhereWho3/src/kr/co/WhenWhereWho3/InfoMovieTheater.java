package kr.co.WhenWhereWho3;


/**
 * @author W3
 * ��ȭ�� ��ġ �� �̸��� reference���� ���� Ŭ����
 *
 */
public class InfoMovieTheater {
	private double lat;			//	����
	private double lng;			//	�浵
	private String reference;	//	��ȭ�� ���� ����ִ� referenceŰ
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
