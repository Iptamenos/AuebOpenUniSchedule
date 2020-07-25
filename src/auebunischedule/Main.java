package auebunischedule;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.ProcessingInstruction;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;


public class Main {
	
	public static String all_departments = "ΟΛΑ ΤΑ ΤΜΗΜΑΤΑ";
	
	public static String department1 = "Πληροφορικής";
	public static String department2 = "Στατιστικής";
	public static String department3 = "Διοικητικής Επιστήμης και Τεχνολογίας";
	public static String department4 = "Οργάνωσης και Διοίκησης Επιχειρήσεων";
	public static String department5 = "Λογιστικής και Χρηματοοικονομικής";
	public static String department6 = "Μάρκετινγκ και Επικοινωνίας";
	public static String department7 = "Διεθνών και Ευρωπαϊκών Οικονομικών Σπουδών";
	public static String department8 = "Οικονομικής Επιστήμης";
	public static String department_shared = "ΚΟΙΝΟ";
	
	public static String schedule_department = "";
	public static int schedule_semester = -1;
	public static String date_string = "";
	
	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);
		int userInput = -1;

		while (userInput < 0 || userInput > 9) {
			System.out.println("*** AUEB DEPARTMENTS ***");
			System.out.println("0. " + all_departments);
			System.out.println("1. " + department1);
			System.out.println("2. " + department2);
			System.out.println("3. " + department3);
			System.out.println("4. " + department4);
			System.out.println("5. " + department5);
			System.out.println("6. " + department6);
			System.out.println("7. " + department7);
			System.out.println("8. " + department8);
			System.out.println("9. " + department_shared);
			System.out.print("Choose your department (0-9): ");
			
			userInput = scanner.nextInt();
			System.out.println();
		}

		switch(userInput) {
			case 0:
				schedule_department = all_departments;
				break;
			case 1:
				schedule_department = department1;
				break;
			case 2:
				schedule_department = department2;
				break;
			case 3:
				schedule_department = department3;
				break;
			case 4:
				schedule_department = department4;
				break;
			case 5:
				schedule_department = department5;
				break;
			case 6:
				schedule_department = department6;
				break;
			case 7:
				schedule_department = department7;
				break;
			case 8:
				schedule_department = department8;
				break;
			case 9:
				schedule_department = department_shared;
				break;
			default:
				schedule_department = department1;
				break;
		}
				
		userInput = -1;
		while (userInput < 0 || userInput > 8) {
			System.out.print("Choose semester (1-8 or 0 for all): ");
			
			// TODO
//			System.out.print("Choose semester (1-8 or 0 for all or a,b,c for multiple semesters): ");
			
			userInput = scanner.nextInt();
			System.out.println();
		}
		scanner.close();
		
		schedule_semester = userInput;
		
//		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date date = new Date();
		date_string = dateFormat.format(date);
		System.out.println("date: " + date_string);  //yyyy/MM/dd
		
		AUEBDownloader downloader = new AUEBDownloader();
		JSONArray schedule_data = downloader.downloadSchedule();
		
		System.out.println("Parsing \".json\" file...");
		List<Lesson> lessons = parseJSON(schedule_data);
		System.out.println();
		
		// sort lessons by title
		Collections.sort(lessons, new LessonTitleComparator());
		// sort lessons by semester
		Collections.sort(lessons, new LessonSemesterComparator());
		// sort lessons by department
		Collections.sort(lessons, new LessonDepartmentComparator());
		
		List<Lesson> selected_lessons = new ArrayList<Lesson>();
		
		// get and print the lessons of the department
		System.out.println("ΕΞΑΜΗΝΟ, ΜΑΘΗΜΑ, ΚΑΘΗΓΗΤΗΣ, ΤΜΗΜΑ, ΣΧΟΛΙΑ, ΩΡΑ, ΑΙΘΟΥΣΑ, ΗΜΕΡΑ");
		for (Lesson lesson: lessons) {
            if (schedule_department.equals(all_departments) ||
            		lesson.getDepartment().equals(schedule_department)) {
            	if (schedule_semester == 0 ||
            			lesson.getSemester() == schedule_semester) {
            		System.out.println(lesson);
            		selected_lessons.add(lesson);
            	}
            }
        }
		System.out.println();
		
		// replace slash "/" with "\/"
		date_string = date_string.replace("//", "////");
		
		String schedule_name = "";
		if (schedule_semester != 0) {
			schedule_name = "Πρόγραμμα " + date_string + " " + schedule_department + " ΕΞΑΜΗΝΟ " + schedule_semester;
		} else {
			schedule_name = "Πρόγραμμα " + date_string + " " + schedule_department;
		}
		
		// write list of sorted lessons to file
		String txtFile = "schedules/txt/" + schedule_name + ".txt";
		createPathToFile(txtFile);
		writeLessonListToTxtFile(selected_lessons, txtFile);
		System.out.println("Writing to \".txt\" file done!");
		
		// write list of sorted lessons to excel file
		String excelFile = "schedules/excel/" + schedule_name + ".xls";
		createPathToFile(excelFile);
		writeLessonListToExcelFile(selected_lessons, excelFile);
		System.out.println("Writing to excel file done!");
		
		// write list of sorted lessons to xml file
		String xmlFile = "schedules/xml/" + schedule_name + ".xml";
		createPathToFile(xmlFile);
		writeLessonListToXmlFile(selected_lessons, xmlFile);
		System.out.println("Writing to \".xml\" file done!");
	}
	
	private static void createPathToFile(String path) {
		File file = new File(path);
		file.getParentFile().mkdirs();
	}

	public static List<Lesson> parseJSON(JSONArray schedule_data) {
        List<Lesson> lessons = new ArrayList<Lesson>();
        try {
            for (int i = 0; i < schedule_data.length(); i++) {
                JSONObject jsonobject = schedule_data.getJSONObject(i);

                // take a look at the contents of the json data
//                System.out.println(jsonobject.toString());
                
                int semester = Integer.parseInt(jsonobject.getString("semester"));
                String lesson_title = jsonobject.getString("Lesson_title");
                String professor = jsonobject.getString("professor");
                String department = jsonobject.getString("Department_title").trim();
                String comments = jsonobject.getString("Lesson_comments");
                String time = jsonobject.getString("time");
                String room = jsonobject.getString("Room");
                String day = jsonobject.getString("Day");
                
                if (comments.equals("")) {
                	comments = "-";
                }
                
                Lesson lesson = new Lesson(semester, lesson_title, professor, department,
                		comments, time, room, day);

                // System.out.println(lesson);
                
                lessons.add(lesson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lessons;
    }
	
	public static void writeLessonListToTxtFile(List<Lesson> lessons, String txtFile) {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(txtFile));
			bw.write("ΕΞΑΜΗΝΟ, ΜΑΘΗΜΑ, ΚΑΘΗΓΗΤΗΣ, ΤΜΗΜΑ, ΣΧΟΛΙΑ, ΩΡΑ, ΑΙΘΟΥΣΑ, ΗΜΕΡΑ" + "\n");
			for (Lesson lesson: lessons) {
	            bw.write(lesson.toString() + "\n");
			}
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void writeLessonListToExcelFile(List<Lesson> lessons, String excelFile) {
		
		WritableWorkbook workbook = null;
        try {
        	
        	workbook = Workbook.createWorkbook(new File(excelFile));
            
            // create an Excel sheet
            WritableSheet excelSheet = workbook.createSheet("Sheet 1", 0);
            
            // adjust the width of the columns
            for (int col=0; col<8; col++) {
	            if (col == 0) {  // Make the semester more narrow than the rest.
	            	excelSheet.setColumnView(col, 13);
	            } else if (col == 1) {  // Make the lesson title column wider than the rest.
	            	excelSheet.setColumnView(col, 55);
	            } else {
		            int widthInChars = 20;
		            excelSheet.setColumnView(col, widthInChars);
	            }
            }
            
            // First, create the headers of the columns of the Excel File.
            
            // Headers format.
            WritableCellFormat headerCellFormat = new WritableCellFormat();
            WritableFont cellHeaderFont = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD);
            headerCellFormat.setFont(cellHeaderFont);
            
            Label semesterHeaderCell = new Label(0, 0, "ΕΞΑΜΗΝΟ", headerCellFormat);
            excelSheet.addCell(semesterHeaderCell);
            
            Label lessonTitleHeaderCell = new Label(1, 0, "ΜΑΘΗΜΑ", headerCellFormat);
            excelSheet.addCell(lessonTitleHeaderCell);
        	
        	Label professorHeaderCell = new Label(2, 0, "ΚΑΘΗΓΗΤΗΣ", headerCellFormat);
            excelSheet.addCell(professorHeaderCell);

        	Label departmentHeaderCell = new Label(3, 0, "ΤΜΗΜΑ", headerCellFormat);
            excelSheet.addCell(departmentHeaderCell);

        	Label commentsHeaderCell = new Label(4, 0, "ΣΧΟΛΙΑ", headerCellFormat);
            excelSheet.addCell(commentsHeaderCell);
            
        	Label timeHeaderCell = new Label(5, 0, "ΩΡΑ", headerCellFormat);
            excelSheet.addCell(timeHeaderCell);
            
            Label roomHeaderCell = new Label(6, 0, "ΑΙΘΟΥΣΑ", headerCellFormat);
            excelSheet.addCell(roomHeaderCell);

        	Label dayHeaderCell = new Label(7, 0, "ΗΜΕΡΑ", headerCellFormat);
            excelSheet.addCell(dayHeaderCell);
            
            // Simple cell format.
            WritableCellFormat cellFormat = new WritableCellFormat();
            WritableFont cellFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD);
            cellFormat.setFont(cellFont);
            
            int lesson_counter = 1;
			for (Lesson lesson: lessons) {
	            
            	int semester = lesson.getSemester();
                String lesson_title = lesson.getLessonTitle();
                String professor = lesson.getProfessor();
                String department = lesson.getDepartment();
                String comments = lesson.getComments();
                String time = lesson.getTime();
                String room = lesson.getRoom();
                String day = lesson.getDay();
                
                Number semesterCell = new Number(0, lesson_counter, semester, cellFormat);
                excelSheet.addCell(semesterCell);

            	Label lessonTitleCell = new Label(1, lesson_counter, lesson_title, cellFormat);
                excelSheet.addCell(lessonTitleCell);
                
            	Label professorCell = new Label(2, lesson_counter, professor, cellFormat);
                excelSheet.addCell(professorCell);

            	Label departmentCell = new Label(3, lesson_counter, department, cellFormat);
                excelSheet.addCell(departmentCell);
                
            	Label commentsCell = new Label(4, lesson_counter, comments, cellFormat);
                excelSheet.addCell(commentsCell);

            	Label timeCell = new Label(5, lesson_counter, time, cellFormat);
                excelSheet.addCell(timeCell);
                
                Label roomCell = new Label(6, lesson_counter, room, cellFormat);
                excelSheet.addCell(roomCell);

            	Label dayCell = new Label(7, lesson_counter, day, cellFormat);
                excelSheet.addCell(dayCell);

                lesson_counter++;
			}
			
			workbook.write();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        } finally {

            if (workbook != null) {
                try {
                	workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (WriteException e) {
                    e.printStackTrace();
                }
            }

        }
		
	}
	
	public static void writeLessonListToXmlFile(List<Lesson> lessons, String xmlFile) {
		
    	try {
            DocumentBuilderFactory dbFactory =
            DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();
            doc.setXmlStandalone(true);
            ProcessingInstruction pi = doc.createProcessingInstruction("xml-stylesheet", "type=\"text/xsl\" href=\"schedules.xsl\"");
            
            doc.appendChild(pi);
            
            // root element
            Element rootElement = doc.createElement("schedule");
            doc.appendChild(rootElement);
            
            // set the department as attribute
//            Attr attrDepartment = doc.createAttribute("department");
//            attrDepartment.setValue(schedule_department);
//            rootElement.setAttributeNode(attrDepartment);

            String previousDepartment = "";
            Element departmentElement = null;
            
			for (Lesson lesson: lessons) {
            	
            	int semester = lesson.getSemester();
                String lesson_title = lesson.getLessonTitle();
                String professor = lesson.getProfessor();
                String department = lesson.getDepartment();
                String comments = lesson.getComments();
                String time = lesson.getTime();
                String room = lesson.getRoom();
                String day = lesson.getDay();
                
                if (!previousDepartment.equals(department)) {
                
	                // department element
	                departmentElement = doc.createElement("department");
	                rootElement.appendChild(departmentElement);
	                
	                // set the department name as attribute
	                Attr attrDepartmentName = doc.createAttribute("name");
	                attrDepartmentName.setValue(department);
	                departmentElement.setAttributeNode(attrDepartmentName);
                
                }
                
                // lesson element
                Element lessonElement = doc.createElement("lesson");
                departmentElement.appendChild(lessonElement);
                
                // set the title as attribute
                Attr attrTitle = doc.createAttribute("title");
                attrTitle.setValue(lesson_title);
                lessonElement.setAttributeNode(attrTitle);
                
                Element semesterElement = doc.createElement("semester");
                semesterElement.appendChild(doc.createTextNode(Integer.toString(semester)));
                lessonElement.appendChild(semesterElement);
                
//	            Element lessonTitleElement = doc.createElement("lesson_title");
//	            lessonTitleElement.appendChild(doc.createTextNode(lesson_title));
//	            lessonElement.appendChild(lessonTitleElement);
                
                Element professorElement = doc.createElement("professor");
                professorElement.appendChild(doc.createTextNode(professor));
                lessonElement.appendChild(professorElement);
                
                Element commentsElement = doc.createElement("comments");
                commentsElement.appendChild(doc.createTextNode(comments));
                lessonElement.appendChild(commentsElement);
                
                Element timeElement = doc.createElement("time");
                timeElement.appendChild(doc.createTextNode(time));
                lessonElement.appendChild(timeElement);
                
                Element roomElement = doc.createElement("room");
                roomElement.appendChild(doc.createTextNode(room));
                lessonElement.appendChild(roomElement);
                
                Element dayElement = doc.createElement("day");
                dayElement.appendChild(doc.createTextNode(day));
                lessonElement.appendChild(dayElement);
                
                previousDepartment = department;
                
			}
			
	  		TransformerFactory transformerFactory = TransformerFactory.newInstance();
	  		Transformer transformer = transformerFactory.newTransformer(); 
	  		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	  		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	  		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
	  		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
	  		DOMSource source = new DOMSource(doc);
	  		StreamResult result = new StreamResult(new File(xmlFile));
	        transformer.transform(source, result);
	        
	        // Output to console for testing
	        //StreamResult consoleResult = new StreamResult(System.out);
	        //transformer.transform(source, consoleResult);
	        
        } catch (Exception e) {
           e.printStackTrace();
        }
        
	}

}
