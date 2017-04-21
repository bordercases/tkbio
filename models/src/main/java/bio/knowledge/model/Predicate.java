package bio.knowledge.model;

import bio.knowledge.model.core.IdentifiedEntity;

public interface Predicate extends IdentifiedEntity {

	String getAccessionId();

	String getName();

	void setName(String string);

}