package discordbot.stattrack;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public class UserStats {

	//	private IUser user;
	private Map<Stat, Integer> statsMap;

	public enum Stat {
		MESSAGES_SENT,
		MESSAGES_REMOVED
	}

	public UserStats() {
		statsMap = Collections.synchronizedMap(new EnumMap<Stat, Integer>(Stat.class));
		for (Stat stat : Stat.values()) {
			statsMap.put(stat, 0);
		}
	}

	public void addStat(Stat stat) {
		statsMap.put(stat, 0);
	}

	public void setStat(Stat stat, int value) {
		statsMap.put(stat, value);
	}

	public void removeStat(Stat stat) {
		statsMap.remove(stat);
	}

	public Map<Stat, Integer> getStatsMap() {
		return statsMap;
	}

	public void setStatsMap(Map<Stat, Integer> statsMap) {
		this.statsMap = statsMap;
	}

	public void increase(Stat stat) {
		if(statsMap.containsKey(stat)) {
			statsMap.put(stat, statsMap.get(stat).intValue()+1);
		} else {
			statsMap.put(stat, 1);
		}
	}

	public void decrease(Stat stat) {
		if(statsMap.containsKey(stat)) {
			if(statsMap.get(stat).intValue() > 0) {
				statsMap.put(stat, statsMap.get(stat).intValue()-1);
			}
		} else {
			statsMap.put(stat, 0);			
		}

	}

}
