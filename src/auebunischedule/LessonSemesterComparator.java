package auebunischedule;

import java.util.Comparator;

public class LessonSemesterComparator implements Comparator<Lesson> {
	
    @Override
    public int compare(Lesson l1, Lesson l2) {
    	return Integer.compareUnsigned(l1.getSemester(), l2.getSemester());
    }
    
}
