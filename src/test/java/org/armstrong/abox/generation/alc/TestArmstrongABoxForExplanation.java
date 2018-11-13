package org.armstrong.abox.generation.alc;

import static org.semanticweb.owlapi.model.ClassExpressionType.OBJECT_ALL_VALUES_FROM;
import static org.semanticweb.owlapi.model.ClassExpressionType.OWL_CLASS;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;

import org.armstrong.abox.closedsets.IndexedSet;
import org.armstrong.abox.closedsets.OrderedSet;
import org.armstrong.abox.generation.alc.exception.MImpermissibleException;
import org.armstrong.abox.reasoner.OWLReasonerFacadeImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
/*
 * ArmstrongABoxes: A library for generating Armstrong ABoxes.
 * 
 * MIT License
 *
 * Copyright (c) 2017 Henriette Harmse
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

@RunWith(Parameterized.class)
public class TestArmstrongABoxForExplanation {
	private static Logger logger = LoggerFactory.getLogger(TestArmstrongABoxForExplanation.class);
	// Why This Failure marker
	private static final Marker WTF_MARKER = MarkerFactory.getMarker("WTF");
	private static final Path PATH = Paths.get(".").toAbsolutePath().normalize();
	
	private ArmstrongABox armstrongABox;
	private String outputFile;

	public TestArmstrongABoxForExplanation(ArmstrongABox armstrongABox, String outputFile) {
		super();
		this.armstrongABox = armstrongABox;
		this.outputFile = outputFile;
	}

	@Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] {
		  testDataForPetOntology()
		  });
	}

	@Test
	public void testConceptualExploration() {
		try {
			armstrongABox.conceptualExploration();
			IRI saveOntologyToFile = IRI
					.create(outputFile);
			
			armstrongABox.getReasoner().saveOntology(saveOntologyToFile);

			logger.trace("Armstrong ABox = " + armstrongABox.getExemplars());
		} catch (MImpermissibleException mi) {
		  logger.trace("Reasons why M is impermissible");
		  for (String reason: mi.getReasons()) {
		    logger.trace(reason);  
      }		  
		} catch (Throwable t) {
			logger.trace(WTF_MARKER, t.getMessage(), t);
		}
	}

	private static Object[] testDataForPetOntology() {
		return new Object[] { getContextForPetOntology(
		    "file:" + PATH.toFile().getAbsolutePath() + "/src/test/resources/PetOntology.owl"),
				"file:" + PATH.toFile().getAbsolutePath() + "/src/test/resources/PetOntologyArmstrongABox.owl"};
	}
	
	private static ArmstrongABox getContextForPetOntology(String filename) {
	  
		IRI ontologyFile = IRI.create(filename);
		OWLReasonerFacadeImpl owlReasoner = null;
		try {
			owlReasoner = new OWLReasonerFacadeImpl(ontologyFile);
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.trace(e.getStackTrace().toString());
		}

		IndexedSet<OWLClassExpression> setM = new OrderedSet<OWLClassExpression>();
		
//		OWLClassExpression personClass = owlReasoner.getOWLClassExpression("Person", OWL_CLASS);
		OWLClassExpression dogClass = owlReasoner.getOWLClassExpression("Dog", OWL_CLASS);
		
		OWLClassExpression dogLoverClass = owlReasoner.getOWLClassExpression("DogLover", OWL_CLASS);
		
		OWLClassExpression ownsOnlyDogClass = 
		    owlReasoner.getOWLClassExpression("owns", "Dog", OBJECT_ALL_VALUES_FROM);
					
//		setM.add(personClass);
		setM.add(dogClass);
		setM.add(dogLoverClass);
		setM.add(ownsOnlyDogClass);
		
		ArmstrongABox armstrongABox = new ArmstrongABoxImpl(owlReasoner, setM);
		return armstrongABox;
	}	
}
