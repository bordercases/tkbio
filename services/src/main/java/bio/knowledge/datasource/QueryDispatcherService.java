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

package bio.knowledge.datasource;

import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;

import bio.knowledge.model.Concept;
import bio.knowledge.model.SemanticGroup;
import bio.knowledge.model.datasource.ResultSet;

/**
 * @author Richard
 * @version 0.1.0
 */
@Service
public class QueryDispatcherService {
	
	/**
	 * @param queryString to be matched against the SemanticGroup defined namespace
	 * @param type SemanticGroup against which the search is being executed
	 * @return a consolidated ResultSet wrapped in a CompletableFuture
	 * @throws IllegalArgumentException if invalid arguments are given
	 */
	CompletableFuture< ResultSet > searchByCUI( 
			String cui, 
			SemanticGroup type 
	) throws IllegalArgumentException {
		
		throw new IllegalArgumentException("QueryDispatcherService.search() not yet implemented!") ;
	}
	
}