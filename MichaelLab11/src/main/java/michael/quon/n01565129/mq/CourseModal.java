// Michael Quon N01565129
package michael.quon.n01565129.mq;

public class CourseModal {

    private String courseId;
    private String courseName;
    private String courseDescription;

    // Default constructor (no-argument constructor)
    public CourseModal() {
        // Required by Firebase for deserialization
    }

    // Constructor with courseId, courseName, and courseDescription
    public CourseModal(String courseId, String courseName, String courseDescription) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseDescription = courseDescription;
    }

    // Getter method for courseId
    public String getCourseId() {
        return courseId;
    }

    // Getter method for courseName
    public String getCourseName() {
        return courseName;
    }

    // Setter method for courseName
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    // Getter method for courseDescription
    public String getCourseDescription() {
        return courseDescription;
    }

    // Setter method for courseDescription
    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }
}

