package indico.checkin.core.data;

import java.util.Comparator;
public class IndicoRegistrantComparator  implements Comparator<IndicoRegistrant>{

	@Override
	public int compare(IndicoRegistrant o1, IndicoRegistrant o2) {
		return o1.getSurname().toLowerCase().compareTo(o2.getSurname().toLowerCase());
	}
}