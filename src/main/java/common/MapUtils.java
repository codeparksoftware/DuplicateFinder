package common;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import filesystem.XFile;

public class MapUtils {

	// sort by size the Map

	public static Map<Long, XFile> sortByComparator(Map<Long, XFile> unsortMap, final boolean order) {

		List<Entry<Long, XFile>> list = new LinkedList<Entry<Long, XFile>>(unsortMap.entrySet());

		// Sorting the list based on values
		Collections.sort(list, new Comparator<Entry<Long, XFile>>() {
			public int compare(Entry<Long, XFile> o1, Entry<Long, XFile> o2) {
				if (order) {
					return Long.compare(((XFile) o1.getValue()).size, ((XFile) o2.getValue()).size);
				} else {
					return Long.compare(((XFile) o2.getValue()).size, ((XFile) o1.getValue()).size);

				}
			}
		});

		// Maintaining insertion order with the help of LinkedList
		Map<Long, XFile> sortedMap = new LinkedHashMap<Long, XFile>();
		for (Entry<Long, XFile> entry : list) {

			XFile tmp = entry.getValue();

			sortedMap.put(entry.getKey(), tmp);

		}

		return sortedMap;
	}

}
