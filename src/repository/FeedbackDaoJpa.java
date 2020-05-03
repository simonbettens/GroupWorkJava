package repository;

import domein.Feedback;
import domein.Media;

public class FeedbackDaoJpa extends GenericDaoJpa<Feedback> implements FeedbackDao {

	public FeedbackDaoJpa() {
		super(Feedback.class);
		// TODO Auto-generated constructor stub
	}

}
