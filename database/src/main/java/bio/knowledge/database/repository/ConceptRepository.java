/*-------------------------------------------------------------------------------
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-16 Scripps Institute (USA) - Dr. Benjamin Good
 *                       STAR Informatics / Delphinai Corporation (Canada) - Dr. Richard Bruskiewich
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *-------------------------------------------------------------------------------
 */
package bio.knowledge.database.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;

import bio.knowledge.model.Concept;
import bio.knowledge.model.SemanticGroup;

/**
 * @author Richard
 *
 */
public interface ConceptRepository extends GraphRepository<Concept> {
	
	@Query( "CREATE CONSTRAINT ON (concept:Concept)"
	      + " ASSERT concept.accessionId IS UNIQUE")
	public void createUniqueConstraintOnConceptAccessionId() ;
	
	/**
	 * @return
	 */
	@Query( "MATCH ( concept:Concept ) RETURN concept" )
	public Iterable<Concept>  getConcepts();

	/**
	 * 
	 */
	@Query( "DROP CONSTRAINT ON (concept:Concept)"
	      + " ASSERT concept.accessionId IS UNIQUE")
	public void dropUniqueConstraintOnConceptAccessionId() ;
	
	/**
	 * 
	 */
	@Query( "DROP INDEX ON :Concept(accessionId)")
	public void dropIndexOnConceptAccessionId() ;
	
	/**
	 * @param accessionId
	 * @return Concept identified by the accessionId
	 */
	@Query( "MATCH ( concept:Concept ) WHERE concept.accessionId = {accessionId} RETURN concept")
	public Concept findByAccessionId( @Param("accessionId") String accessionId ) ;
	
	/**
	 * 
	 * @param id of concept 
	 * @return matching Concept
	 */
	@Query( "MATCH ( concept:Concept )"
			+ " WHERE concept.semMedDbConceptId = {id}"
		 + " RETURN concept")
	public Concept findBySemMedDbConceptId( @Param("id")String id ) ;
	
	/**
	 * 
	 * @param id of implicitome concept
	 * @return matching Concept
	 */
	@Query( "MATCH ( concept:Concept ) WHERE concept.implicitomeConceptId = {id}"
		 + " RETURN concept")
	public Concept findByImplicitomeConceptId( @Param("id") String id ) ;
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	@Query( "MATCH ( concept:Concept )"
			+" WHERE concept.semMedDbConceptId = {id}"
			+" RETURN concept" 
			+" UNION"
			+" MATCH ( concept:Concept ) WHERE concept.implicitomeConceptId = {id}"
			+" RETURN concept"
		 )
	public Concept findByConceptId(@Param("id") Long id);

	/**
	 * @param filter string to match (as an embedded substring, non-case-sensitive)
	 * @return count of Concepts entries with names matching the filter
	 */
	@Query(
			"MATCH (concept:Concept) "+
			"WHERE "+
			"    LOWER(concept.name)     CONTAINS LOWER({filter}) OR"+
			"    LOWER(concept.synonyms) CONTAINS LOWER({filter})"+
		    " RETURN count(concept)"
		)
	public long countByNameLikeIgnoreCase( @Param("filter") String filter);
	
	/**
	 * @param filter string to match (as an embedded substring, non-case-sensitive)
	 * @param pageable specification of what page and page size of Concept data entries to return
	 * @return
	 */
	@Query(
			 "MATCH (concept:Concept) "+
			" WHERE "+
			"    LOWER(concept.name)     CONTAINS LOWER({filter}) OR"+
			"    LOWER(concept.synonyms) CONTAINS LOWER({filter})"+
			" RETURN concept"+
			" SKIP  {1}.pageNumber*{1}.pageSize"+
			" LIMIT {1}.pageSize"
	)
	public List<Concept> findByNameLikeIgnoreCase( @Param("filter") String filter, Pageable pageable );

	/**
	 * @param name
	 * @return
	 */
	@Query(
			 "MATCH (concept:Concept) "+
			" WHERE "+
			"    LOWER(concept.name) = LOWER({name}) AND "+
			"    concept.semanticGroup = {semanticGroup}"+
			" RETURN concept"
	)
	public List<Concept> findConceptByNameAndType(
						@Param("name") String name,
						@Param("semanticGroup") SemanticGroup semanticGroup
					);
	/**
	 * @param filter
	 * @return
	 */
	@Query( "MATCH (concept:Concept)"+
			"WHERE "+
			"   concept.usage > 0 AND"+
			"   ( "+
			"     ALL (x IN {filter} WHERE LOWER(concept.name)     CONTAINS LOWER(x)) OR"+
			"     ALL (x IN {filter} WHERE LOWER(concept.synonyms) CONTAINS LOWER(x))"+
			"   ) "+
			"RETURN count(concept)" 
		)
	public long countByInitialSearch(@Param("filter") String[] filter);


	/**
	 * 
	 */
	@Query( "MATCH (concept:Concept) "+
			"  WHERE concept.usage > 0 "+
			"RETURN count(concept)")
	public long countAll();
	
	/**
	 * Right now accountId and groupId are only being used to count the number
	 * of concept maps attached to the library that are visible to the user
	 * (i.e., are public, created by the user, or shared with a group that
	 * the user belongs to).
	 * 
	 * @param filter
	 *            string to match (as an embedded substring, non-case-sensitive)
	 * @param pageable
	 *            specification of what page and page size of data to return
	 * @return
	 */
	@Query( "MATCH path = (concept:Concept)-[:LIBRARY]->(library:Library)"+
			" WHERE"+
			"    concept.usage > 0 AND"+
			"   ( "+
			"     ALL (x IN {filter} WHERE LOWER(concept.name)     CONTAINS LOWER(x)) OR"+
			"     ALL (x IN {filter} WHERE LOWER(concept.synonyms) CONTAINS LOWER(x))"+
			"   )"+
			" OPTIONAL MATCH (library:Library)-[:ASSOCIATED_MAP]->(cm:ConceptMap)"+
			" WHERE "+ ConceptMapArchiveRepository.conceptMapIsPermitted +
			" WITH COUNT(cm) as c, path AS path, concept AS concept, library AS library" +
			" SET library.numberOfVisibleMaps = c" +
			
			" RETURN path"+			
			"    ORDER BY concept.usage DESC"+
			"    SKIP  {1}.pageNumber*{1}.pageSize"+
			"    LIMIT {1}.pageSize"
		)
	public List<Concept> findByInitialSearch(
			@Param("filter") String[] filter,
			Pageable pageable,
			@Param("accountId") String accountId,
			@Param("groupIds") String[] groupIds
	);
	
	/**
	 * 
	 * @param semanticGroups
	 * @param filter string to match (as an embedded substring, non-case-sensitive)
	 * @param pageable specification of what page and page size of data to return
	 * @return
	 */
	@Query( " MATCH path = (concept:Concept)-[:LIBRARY]->(library:Library)"+
			" WHERE"+
			"   concept.usage > 0 AND"+
			"   ( size({semanticGroups}) = 0"+
			"     OR ANY ( x IN {semanticGroups} WHERE LOWER(concept.semanticGroup) CONTAINS LOWER(x) )"+
			"   ) AND"+
			"   ( "+
			"     ALL (x IN {filter} WHERE LOWER(concept.name)     CONTAINS LOWER(x)) OR"+
			"     ALL (x IN {filter} WHERE LOWER(concept.synonyms) CONTAINS LOWER(x))"+
			"   )"+
			" OPTIONAL MATCH (library:Library)-[:ASSOCIATED_MAP]->(cm:ConceptMap)"+
			" WHERE "+ ConceptMapArchiveRepository.conceptMapIsPermitted +
			" WITH COUNT(cm) as c, path AS path, concept AS concept, library AS library" +
			" SET library.numberOfVisibleMaps = c" +
			" RETURN path"+
			" ORDER BY concept.usage DESC"+
			" SKIP  {2}.pageNumber*{2}.pageSize"+
			" LIMIT {2}.pageSize"
		)
	public List<Concept> findByNameLikeIgnoreCase(
			@Param("semanticGroups") ArrayList<String> semanticGroups, 
			@Param("filter") String[] filter, 
			Pageable pageable,
			@Param("accountId") String accountId,
			@Param("groupIds") String[] groupIds
	);

	/**
	 * @param pageable
	 * @param userId
	 * @param groupIds
	 * @return
	 */
	@Query( "MATCH path = (concept:Concept)-[:LIBRARY]->(library:Library)"+
			" WHERE concept.usage > 0"+
			" OPTIONAL MATCH (library:Library)-[:ASSOCIATED_MAP]->(cm:ConceptMap)"+
			" WHERE "+ ConceptMapArchiveRepository.conceptMapIsPermitted +
			" WITH COUNT(cm) as c, path AS path, concept AS concept, library AS library" +
			" SET library.numberOfVisibleMaps = c" +
			" RETURN path"+			
			"    ORDER BY concept.usage DESC"+
			"    SKIP  {0}.pageNumber*{0}.pageSize"+
			"    LIMIT {0}.pageSize"
		)
	public List<Concept> findAllByPage(
			Pageable pageable,
			@Param("accountId") String accountId,
			@Param("groupIds") String[] groupIds
	);
	
}