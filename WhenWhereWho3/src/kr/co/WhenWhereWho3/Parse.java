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
		Log.d("HTML", "htmlParse 진입");

		try {
			//	BufferedReader가 존재할 경우만 동작
			if( br != null ) {
				Log.d("HTML", "BR 존재함");
				String readLine = "";
				StringBuffer result = new StringBuffer();
				String sub = "";
				
				while ( ( readLine = br.readLine() ) != null ) {
					Log.d("내용 : ", readLine);
					result.append( readLine );
				}
				
				int startIndex = result.indexOf(
						"\"movieTitle\":"
						);
				Log.d("HTML", "시작 index : " + startIndex );
				if( startIndex != -1 ) {
					sub = result.substring(startIndex);
					int i = 0;
					while( i < 10 ) {
						int titleStartIndex = sub.indexOf("\"movieTitle\":\"") + 14;
						sub = sub.substring( titleStartIndex );
						Log.d("영화 타이틀", "title : " + sub);
						//	처음으로 따옴표 오기 전까지가 영화의 제목이므로 index를 가져온다.
						int titleEndIndex = sub.indexOf( "\"" );
						Log.d("영화 타이틀", "titleENdIndex : " + titleEndIndex);
						//	영화 제목을 따온다.
						title = sub.substring( 0, titleEndIndex );
						Log.d("최종 영화 제목", "title : " + title);
						titleList.add(title);
						
						//	다음 영화 제목을 찾기 위해 다시 잘라낸다.
						//	영화 제목 뒤부터 다시 찾는다.
						sub = sub.substring(titleEndIndex);
						i++;
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
			//	읽어온 정보를 파싱을 통해 Object로 얻어옴
			JSONObject object = (JSONObject)JSONValue.parseWithException( br );
			Log.d("parse", "제이슨 오브젝트 읽힘");
			
			//	검색 결과가 없을 경우
			if( object.get("channel") == null ){
				Log.d("parse", "검색결과 없음");
				return null;
			}
			
			//	json파일을 읽어온 후 channel로 가져옴
			JSONObject channel = ( JSONObject )( object.get( "channel" ) );
			//	channel안의 내용중 item에 해당하는 내용들을 배열로 가져옴
			JSONArray items = (JSONArray)channel.get("item");
			
			
			//	반복되어져 사용될 것들에 대해서...	
			JSONObject content = null;
			JSONArray resultArray = null;
			
			for( int i = 0; i < items.size(); i++ ) {
				//	영화 객체 생성
				movie = new Movie();
				
				//	i번째 영화 담기
				JSONObject obj = (JSONObject)items.get(i);
				
				/***************************************************************/
				//	i번째 영화에 대한 title 객체
				resultArray = (JSONArray)obj.get("title");
				content = (JSONObject)resultArray.get(0);
				movie.setTitle( content.get("content").toString() );
				/***************************************************************/
				
				/***************************************************************/
				//	i번째 영화에 대한 thumbnail 객체
				resultArray = (JSONArray)obj.get("thumbnail");
				content = (JSONObject)resultArray.get(0);
				//	API에서 제공하는 사진은 사이즈가 작다. 그래서 주소중에 가운데 부분을 바꾸면 큰 사진으로 구할 수 있다.
				//	ex ) http://cfile78.uf.daum.net/C110x160/15533D3C4E7933521B928A 에서 C110x160부분을
				//	R678x0으로 변환해 주는 작업이 필요하다.
				String thumbnailSrc = content.get("content").toString();
				movie.setThumbnail(thumbnailSrc.replace( "C110x160", "R678x0" ));
				/***************************************************************/
				
				/***************************************************************/
				//	i번째 영화에 대한 감독 객체
				resultArray = (JSONArray)obj.get("director");
				content = (JSONObject)resultArray.get(0);
				movie.setDirector( content.get("content").toString() );
				/***************************************************************/
				
				
				/***************************************************************/
				//	i번째 영화에 대한 개봉일(open_info) 객체
				resultArray = (JSONArray)obj.get("open_info");
				content = (JSONObject)resultArray.get(0);
				movie.setOpenInfo( content.get("content").toString() );
				/***************************************************************/
				
				/***************************************************************/
				//	i번째 영화에 대한 배우 객체
				resultArray = (JSONArray)obj.get("actor");
				String [] actor = new String[resultArray.size()];
				for( int j = 0; j < resultArray.size(); j++ ) {
					content = (JSONObject)resultArray.get(j);
					actor[j] = content.get("content").toString();
				}
				movie.setActor(actor);
				/***************************************************************/
				
				/***************************************************************/
				//	i 번째 영화에 대한 국가 객체
				resultArray = (JSONArray)obj.get("nation");
				content = (JSONObject)resultArray.get(0);
				movie.setNation( content.get("content").toString() );
				/***************************************************************/

				/***************************************************************/
				//	i번째 영화에 대한 장르 객체
				resultArray = (JSONArray)obj.get("genre");
				content = (JSONObject)resultArray.get(0);
				movie.setGenre( content.get("content").toString() );
				/***************************************************************/
				
				/***************************************************************/
				//	i번째 영화에 대한 평점 객체
				resultArray = (JSONArray)obj.get("grades");
				content = (JSONObject)resultArray.get(0);
				movie.setGrade( content.get("content").toString() );
				/***************************************************************/
				
				/***************************************************************/
				//	i번째 영화에 대한 스틸컷 사진 5장
				//	ex ) http://cfile79.uf.daum.net/C93x70/1165BC4A4E4DF913184317 에서 C93x70부분을
				//	R678x0으로 변환해 주는 작업이 필요하다.
				String [] photo = new String[5];
				for( int j = 0; j < 5; j++ ) {
					resultArray = (JSONArray)obj.get("photo" + ( j + 1 ));
					content = (JSONObject)resultArray.get(0);
					photo[j] = content.get("content").toString().replace("C93x70", "R678x0");
				}
				movie.setPhoto(photo);
				/***************************************************************/
				
				/***************************************************************/
				//	i번째 영화에 대한 줄거리 객체
				resultArray = (JSONArray)obj.get("story");
				content = (JSONObject)resultArray.get(0);
				movie.setStory( content.get("content").toString() );
				/***************************************************************/
				
				
				//	데이터를 전부 집어 넣은 movie 객체를 movieList에 담는다.
				movies.add(movie);
			}
			return movies;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public ArrayList<InfoMovieTheater> locationParse( BufferedReader br ) {
		ArrayList<InfoMovieTheater> infoMovieTheaters = new ArrayList<InfoMovieTheater>();
		try {
			//		읽어온 정보를 파싱을 통해 Object로 얻어옴
			JSONObject object = (JSONObject)JSONValue.parseWithException( br );
			Log.d("parse", "제이슨 오브젝트 읽힘");
			
			//	검색 결과가 없을 경우
			if( object.get("status").toString().equals("ZERO_RESULTS") ){
				Log.d("parse", "검색결과 없음");
				return null;
			}
			
			//	json파일을 읽어온 후 result로 가져옴
			JSONArray results = ( JSONArray )( object.get( "results" ) );
			
			for( int i = 0; i < results.size(); i++ ){
				InfoMovieTheater infoMovieTheater = new InfoMovieTheater();
				
				//	i번째 영화관 가져옴
				JSONObject theater = (JSONObject)results.get(i);
				Log.d("구글 플레이스", "결과 : " + ( (JSONObject)results.get(i) ).get("name").toString() );
				
				/*********************************************************************/
				//	위도, 경도 가져오기
				JSONObject geometry = (JSONObject)theater.get( "geometry" );
				double lat = ( double )Double.parseDouble( ( ( JSONObject )geometry.get( "location" ) ).get( "lat" ).toString( ) );
				Log.d("구글 플레이스", "위도 : " + lat);
				infoMovieTheater.setLat(lat);
				
				double lng = ( double )Double.parseDouble( ( ( JSONObject )geometry.get( "location" ) ).get( "lng" ).toString( ) );
				Log.d("구글 플레이스", "경도 : " + lng);
				infoMovieTheater.setLng(lng);
				/*********************************************************************/
				
				/*********************************************************************/
				String ref = theater.get( "reference" ).toString();
				Log.d("구글 플레이스", "Reference : " + ref);
				infoMovieTheater.setReference(ref);
				/*********************************************************************/
				
				infoMovieTheaters.add(infoMovieTheater);
			}
			br.close();
			return infoMovieTheaters;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}



