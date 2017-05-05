package discordbot.stattrack;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import discordbot.BotMain;

public class UserStats {

	//	private IUser user;
	private	Map<Stat, Integer> statsMap;

	public enum Stat {
		MESSAGES_SENT,
		MESSAGES_REMOVED
	}

	public UserStats(Stat stat) {
		statsMap = Collections.synchronizedMap(new EnumMap<Stat, Integer>(Stat.class));
		statsMap.put(stat, 0);
	}

	public int addStat(Stat stat) {
		return statsMap.put(stat, 0);
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

	public int increase(Stat stat) {
		if(statsMap.containsKey(stat)) {
			return statsMap.put(stat, statsMap.get(stat).intValue()+1);
		} else {
			return statsMap.put(stat, 1);			
		}
	}

	public int decrease(Stat stat) {
		if(statsMap.containsKey(stat)) {
			if(statsMap.get(stat).intValue() > 0) {
				return statsMap.put(stat, statsMap.get(stat).intValue()-1);
			} else {
				return 0;
			}
		} else {
			return statsMap.put(stat, 0);			
		}

	}

}
