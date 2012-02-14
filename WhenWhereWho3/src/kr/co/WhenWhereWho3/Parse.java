package kr.co.WhenWhereWho3;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import android.util.Log;

public class Parse {
	private Movie movie;
	private ArrayList<Movie> movies;
	
	public Parse(){
		this.movie = new Movie();
		this.movies = new ArrayList<Movie>();
	}
	
	public ArrayList<String> htmlParse( BufferedReader br ) {
		String title = "";
		ArrayList<String> titleList = new ArrayList<String>();
		Log.d("HTML", "htmlParse ����");
		try {
			//	BufferedReader�� ������ ��츸 ����
			if( br != null ) {
				Log.d("HTML", "BR ������");
				String readLine = "";
				StringBuffer result = new StringBuffer();
				String sub = "";
				
				while ( ( readLine = br.readLine() ) != null ) {
					result.append( readLine );
				}
				
				int startIndex = result.indexOf(
						"<div class=\"sect_movie\" id = \"tx_ca_movie_tab_open\" >" );
				int endIndex = result.indexOf(
						"<div class=\"sect_movie\" id = \"tx_ca_movie_tab_point\" style='display:none'>" );
				
				//	ã������ �� ���� �κи� �߶� �����´�.
				if( startIndex != -1 && endIndex != -1 ) {
					
					sub = result.substring(startIndex, endIndex - 1);
					
					while( sub.indexOf( "title=\"" ) != -1 ) {
						int titleStartIndex = sub.indexOf("title=\"") + 7;
						//	title=" <- ���� ������ 7���̹Ƿ� �� �ں��� ���� '"' �� ���ڰ� �� ��������
						//	��ȭ�� �����̹Ƿ� �ϴ� �߶󳽴�.
						sub = sub.substring( titleStartIndex );
						Log.d("��ȭ Ÿ��Ʋ", "title : " + sub);
						//	ó������ ����ǥ ���� �������� ��ȭ�� �����̹Ƿ� index�� �����´�.
						int titleEndIndex = sub.indexOf( "\"" );
						Log.d("��ȭ Ÿ��Ʋ", "titleENdIndex : " + titleEndIndex);
						//	��ȭ ������ ���´�.
						title = sub.substring( 0, titleEndIndex );
						titleList.add(title);
						
						//	���� ��ȭ ������ ã�� ���� �ٽ� �߶󳽴�.
						//	��ȭ ���� �ں��� �ٽ� ã�´�.
						sub = sub.substring(titleEndIndex);
					}
					
					return titleList;
				}
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	
	public ArrayList<Movie> jsonParse( BufferedReader br ){
		movies.clear();
		try {
			//	�о�� ������ �Ľ��� ���� Object�� ����
			JSONObject object = (JSONObject)JSONValue.parseWithException( br );
			Log.d("parse", "���̽� ������Ʈ ����");
			
			//	�˻� ����� ���� ���
			if( object.get("channel") == null ){
				Log.d("parse", "�˻���� ����");
				//	�޽��� ��ȣ �Ҵ�
				return null;
			}
			
			//	json������ �о�� �� channel�� ������
			JSONObject channel = ( JSONObject )( object.get( "channel" ) );
			//	channel���� ������ item�� �ش��ϴ� ������� �迭�� ������
			JSONArray items = (JSONArray)channel.get("item");
			
			
			//	�ݺ��Ǿ��� ���� �͵鿡 ���ؼ�...	
			JSONObject content = null;
			JSONArray resultArray = null;
			
			for( int i = 0; i < items.size(); i++ ) {
				//	��ȭ ��ü ����
				movie = new Movie();
				
				//	i��° ��ȭ ���
				JSONObject obj = (JSONObject)items.get(i);
				
				/***************************************************************/
				//	i��° ��ȭ�� ���� title ��ü
				resultArray = (JSONArray)obj.get("title");
				content = (JSONObject)resultArray.get(0);
				movie.setTitle( content.get("content").toString() );
				/***************************************************************/
				
				/***************************************************************/
				//	i��° ��ȭ�� ���� thumbnail ��ü
				resultArray = (JSONArray)obj.get("thumbnail");
				content = (JSONObject)resultArray.get(0);
				//	API���� �����ϴ� ������ ����� �۴�. �׷��� �ּ��߿� ��� �κ��� �ٲٸ� ū �������� ���� �� �ִ�.
				//	ex ) http://cfile78.uf.daum.net/C110x160/15533D3C4E7933521B928A ���� C110x160�κ���
				//	R678x0���� ��ȯ�� �ִ� �۾��� �ʿ��ϴ�.
				String thumbnailSrc = content.get("content").toString();
				movie.setThumbnail(thumbnailSrc.replace( "C110x160", "R678x0" ));
				/***************************************************************/
				
				/***************************************************************/
				//	i��° ��ȭ�� ���� ���� ��ü
				resultArray = (JSONArray)obj.get("director");
				content = (JSONObject)resultArray.get(0);
				movie.setDirector( content.get("content").toString() );
				/***************************************************************/
				
				
				/***************************************************************/
				//	i��° ��ȭ�� ���� ������(open_info) ��ü
				resultArray = (JSONArray)obj.get("open_info");
				content = (JSONObject)resultArray.get(0);
				movie.setOpenInfo( content.get("content").toString() );
				/***************************************************************/
				
				/***************************************************************/
				//	i��° ��ȭ�� ���� ��� ��ü
				resultArray = (JSONArray)obj.get("actor");
				String [] actor = new String[resultArray.size()];
				for( int j = 0; j < resultArray.size(); j++ ) {
					content = (JSONObject)resultArray.get(j);
					actor[j] = content.get("content").toString();
				}
				movie.setActor(actor);
				/***************************************************************/
				
				/***************************************************************/
				//	i ��° ��ȭ�� ���� ���� ��ü
				resultArray = (JSONArray)obj.get("nation");
				content = (JSONObject)resultArray.get(0);
				movie.setNation( content.get("content").toString() );
				/***************************************************************/

				/***************************************************************/
				//	i��° ��ȭ�� ���� �帣 ��ü
				resultArray = (JSONArray)obj.get("genre");
				content = (JSONObject)resultArray.get(0);
				movie.setGenre( content.get("content").toString() );
				/***************************************************************/
				
				/***************************************************************/
				//	i��° ��ȭ�� ���� ���� ��ü
				resultArray = (JSONArray)obj.get("grades");
				content = (JSONObject)resultArray.get(0);
				movie.setGrade( content.get("content").toString() );
				/***************************************************************/
				
				/***************************************************************/
				//	i��° ��ȭ�� ���� ��ƿ�� ���� 5��
				//	ex ) http://cfile79.uf.daum.net/C93x70/1165BC4A4E4DF913184317 ���� C93x70�κ���
				//	R678x0���� ��ȯ�� �ִ� �۾��� �ʿ��ϴ�.
				String [] photo = new String[5];
				for( int j = 0; j < 5; j++ ) {
					resultArray = (JSONArray)obj.get("photo" + ( j + 1 ));
					content = (JSONObject)resultArray.get(0);
					photo[j] = content.get("content").toString().replace("C93x70", "R678x0");
				}
				movie.setPhoto(photo);
				/***************************************************************/
				
				/***************************************************************/
				//	i��° ��ȭ�� ���� �ٰŸ� ��ü
				resultArray = (JSONArray)obj.get("story");
				content = (JSONObject)resultArray.get(0);
				movie.setStory( content.get("content").toString() );
				/***************************************************************/
				
				
				//	�����͸� ���� ���� ���� movie ��ü�� movieList�� ��´�.
				movies.add(movie);
			}
			return movies;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
