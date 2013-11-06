package it.italiangrid.portal.fluka.db.dao.generic;

import java.util.List;

import it.italiangrid.portal.fluka.db.domain.Jobs;

public interface JobsDAO extends GenericDAO<Jobs, Long>{

	List<Jobs> findByOwner(String owner);

	List<Jobs> findByOwnerDN(String dn);

}
