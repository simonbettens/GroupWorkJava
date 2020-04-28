package repository;

import domein.Media;

public class MediaDaoJpa extends GenericDaoJpa<Media> implements MediaDao{

	public MediaDaoJpa() {
		super(Media.class);
	}

}
