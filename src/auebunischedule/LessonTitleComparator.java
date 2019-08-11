import java.util.Comparator;

public class LessonTitleComparator implements Comparator<Lesson> {
	
    @Override
    public int compare(Lesson l1, Lesson l2) {
    	return l1.getLessonTitle().compareTo(l2.getLessonTitle());
    }
    
}
