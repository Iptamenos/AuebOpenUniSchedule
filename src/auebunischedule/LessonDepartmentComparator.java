import java.util.Comparator;

public class LessonDepartmentComparator implements Comparator<Lesson> {
	
    @Override
    public int compare(Lesson l1, Lesson l2) {
    	return l1.getDepartment().compareTo(l2.getDepartment());
    }
    
}
