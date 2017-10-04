/**
 * Created by Chris on 29/10/2016.
 */


public class Lesson {

    private int semester;
    private String lesson_title;
    private String professor;
    private String department;
    private String comments;
    private String time;
    private String room;
    private String day;

    public Lesson() {
    	
    }

    public Lesson(int semester, String lesson_title, String professor, String department,
    		String comments, String time, String room, String day) {
        this.semester = semester;
        this.lesson_title = lesson_title;
        this.professor = professor;
        this.department = department;
        this.comments = comments;
        this.time = time;
        this.room = room;
        this.day = day;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public String getLessonTitle() {
        return lesson_title;
    }

    public void setLessonTitle(String lesson_title) {
        this.lesson_title = lesson_title;
    }

    public String getComments() {
        return comments;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getRoom() {
        return room;
    }

//    @Override
//    public String toString() {
//        return "ΕΞΑΜΗΝΟ: " + semester + ", "  +
//               "ΜΑΘΗΜΑ: " + lesson_title + ", " + 
//               "ΚΑΘΗΓΗΤΗΣ: " + professor + ", " +
//               "ΤΜΗΜΑ: " + department + ", " +
//               "ΣΧΟΛΙΑ: " + comments + ", " +
//               "ΩΡΑ: " + time + ", " +
//               "ΑΙΘΟΥΣΑ: " + room + ", " +
//               "ΗΜΕΡΑ: " + day;
//    }
    
    @Override
    public String toString() {
        return semester + ", "  +
               lesson_title + ", " + 
               professor + ", " +
               department + ", " +
               comments + ", " +
               time + ", " +
               room + ", " +
               day;
    }

}
