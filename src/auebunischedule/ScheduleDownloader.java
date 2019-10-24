package auebunischedule;

import org.json.JSONArray;

public interface ScheduleDownloader {
	public JSONArray downloadSchedule();
	public boolean checkForExistingUpdate();
}
