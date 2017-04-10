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

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;

import bio.knowledge.model.Evidence;

/**
 * @author Richard
 *
 */
public interface EvidenceRepository extends GraphRepository<Evidence> {
	
	@Query("MERGE (evidence:Evidence:IdentifiedEntity:DatabaseEntity { accessionId : \"kbe:\"+{evidenceId} }) "
			+"ON CREATE SET "
			+"evidence.uri = \"http://knowledge.bio/evidence/\"+{evidenceId}, "
			+"evidence.accessionId = \"kbe:\"+{evidenceId}, "
			+"evidence.name = \"\","
			+"evidence.description = \"\","
			+"evidence.count = TOINT(\"0\"), "
			+"evidence.versionDate = timestamp(), "
			+"evidence.version = TOINT(\"1\")"
			+"RETURN evidence")
	public Evidence createByEvidenceId(@Param("evidenceId") String evidenceId);
	
	@Query("MATCH (evidence:Evidence:IdentifiedEntity:DatabaseEntity { accessionId : \"kbe:\"+{evidenceId} }) RETURN evidence")
	public Evidence findByEvidenceId(@Param("evidenceId") String evidenceId);
	
}