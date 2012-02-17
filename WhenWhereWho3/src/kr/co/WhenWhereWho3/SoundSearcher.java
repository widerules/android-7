package kr.co.WhenWhereWho3;

public class SoundSearcher {
	private static final char HANGUL_BEGIN_UNICODE = 44032; // ��
	private static final char HANGUL_LAST_UNICODE = 55203; // �R
	private static final char HANGUL_BASE_UNIT = 588;// ������ ���� ������ ���ڼ�
	// ����
	private static final char[] INITIAL_SOUND = { '��', '��', '��', '��', '��', '��',
			'��', '��', '��', '��', '��', '��', '��', '��', '��', '��', '��', '��', '��' };

	/**
	 * �ش� ���ڰ� INITIAL_SOUND���� �˻�.
	 * 
	 * @param searchar
	 * @return
	 */
	private static boolean isInitialSound(char searchar) {
		for (char c : INITIAL_SOUND) {
			if (c == searchar) {
				return true;
			}
		}
		return false;
	}

	/**
	 * �ش� ������ ������ ��´�.
	 * 
	 * @param c
	 *            �˻��� ����
	 * @return
	 */
	private static char getInitialSound(char c) {
		int hanBegin = (c - HANGUL_BEGIN_UNICODE);
		int index = hanBegin / HANGUL_BASE_UNIT;
		return INITIAL_SOUND[index];
	}

	/**
	 * �ش� ���ڰ� �ѱ����� �˻�
	 * 
	 * @param c
	 *            ���� �ϳ�
	 * @return
	 */
	private static boolean isHangul(char c) {
		return HANGUL_BEGIN_UNICODE <= c && c <= HANGUL_LAST_UNICODE;
	}

	/**
	 * ������.
	 */
	public SoundSearcher() {
	}

	/**
	 * * �˻��� �Ѵ�. �ʼ� �˻� �Ϻ� ������.
	 * 
	 * @param value
	 *            : �˻� ��� ex> �ʼ��˻��մϴ�
	 * @param search
	 *            : �˻��� ex> ���ˤ��դ�
	 * @return ��Ī �Ǵ°� ã���� true ��ã���� false.
	 */
	public static boolean matchString(String value, String search) {
		int t = 0;
		int seof = value.length() - search.length();
		int slen = search.length();
		if (seof < 0)
			return false; // �˻�� �� ��� false�� �����Ѵ�.
		for (int i = 0; i <= seof; i++) {
			t = 0;
			while (t < slen) {
				if (isInitialSound(search.charAt(t)) == true
						&& isHangul(value.charAt(i + t))) {
					// ���� ���� char�� �ʼ��̰� value�� �ѱ��̸�
					if (getInitialSound(value.charAt(i + t)) == search
							.charAt(t))
						// ������ �ʼ����� ������ ���Ѵ�
						t++;
					else
						break;
				} else {
					// char�� �ʼ��� �ƴ϶��
					if (value.charAt(i + t) == search.charAt(t))
						// �׳� ������ ���Ѵ�.
						t++;
					else
						break;
				}
			}
			if (t == slen)
				return true; // ��� ��ġ�� ����� ã���� true�� �����Ѵ�.
		}
		return false; // ��ġ�ϴ� ���� ã�� �������� false�� �����Ѵ�.
	}

}
