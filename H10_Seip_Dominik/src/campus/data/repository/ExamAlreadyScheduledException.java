package campus.data.repository;

import campus.data.domain.Exam;

/**
 * @author Kim Berninger
 * @version 1.0.2
 */
public class ExamAlreadyScheduledException extends RuntimeException {
    private static final long serialVersionUID = -5188775407695075742L;

    public ExamAlreadyScheduledException(Exam exam) {
        super(String.format("The exam %s has already been scheduled", exam));
    }
}
