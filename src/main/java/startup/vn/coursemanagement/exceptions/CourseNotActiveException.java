package startup.vn.coursemanagement.exceptions;

public class CourseNotActiveException extends RuntimeException {
    public CourseNotActiveException(String message) {
        super(message);
    }
}
