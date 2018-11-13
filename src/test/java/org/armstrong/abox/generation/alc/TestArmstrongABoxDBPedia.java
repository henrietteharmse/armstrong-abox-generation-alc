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
public class TestArmstrongABoxDBPedia {
	private static Logger logger = LoggerFactory.getLogger(TestArmstrongABoxDBPedia.class);
	// Why This Failure marker
	private static final Marker WTF_MARKER = MarkerFactory.getMarker("WTF");
	private static final Path PATH = Paths.get(".").toAbsolutePath().normalize();
	
	private ArmstrongABox armstrongABox;
	private String outputFile;

	public TestArmstrongABoxDBPedia(ArmstrongABox armstrongABox, String outputFile) {
		super();
		this.armstrongABox = armstrongABox;
		this.outputFile = outputFile;
	}

	@Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] {
//		  testDataForExistingDisjointness(),
		  testDataForWorldChampion()
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

	private static Object[] testDataForExistingDisjointness() {
		return new Object[] { getContextForExistingDisjointness(
		    "file:" + PATH.toFile().getAbsolutePath() + "/src/test/resources/DBPediaPersonFragment.owl"),
				"file:" + PATH.toFile().getAbsolutePath() + "/src/test/resources/DBPediaPersonFragmentExistingDisjointnessArmstrongABox.owl"};
	}

  private static Object[] testDataForWorldChampion() {
    return new Object[] { getContextForWorldChampion(
        "file:" + PATH.toFile().getAbsolutePath() + "/src/test/resources/DBPediaPersonFragment.owl"),
        "file:" + PATH.toFile().getAbsolutePath() + "/src/test/resources/DBPediaPersonFragmentWorldChampionArmstrongABox.owl"};
  }
  
	private static ArmstrongABox getContextForExistingDisjointness(String filename) {
	  
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
		
		OWLClassExpression agentClass = owlReasoner.getOWLClassExpression("Agent", OWL_CLASS);
		OWLClassExpression personClass = owlReasoner.getOWLClassExpression("Person", OWL_CLASS);
		
		OWLClassExpression activityClass = owlReasoner.getOWLClassExpression("Activity", OWL_CLASS);
					
		setM.add(agentClass);
		setM.add(personClass);
		setM.add(activityClass);
		
		ArmstrongABox armstrongABox = new ArmstrongABoxImpl(owlReasoner, setM);
		return armstrongABox;
	}
	
  private static ArmstrongABox getContextForWorldChampion(String filename) {
    
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
    
    OWLClassExpression agentClass = owlReasoner.getOWLClassExpression("Agent", OWL_CLASS);   
    OWLClassExpression personClass = owlReasoner.getOWLClassExpression("Person", OWL_CLASS);
    OWLClassExpression organizationClass = owlReasoner.getOWLClassExpression("Organization", OWL_CLASS);
    OWLClassExpression familyClass = owlReasoner.getOWLClassExpression("Family", OWL_CLASS);
    OWLClassExpression employerClass = owlReasoner.getOWLClassExpression("Employer", OWL_CLASS);
    OWLClassExpression deityClass = owlReasoner.getOWLClassExpression("Deity", OWL_CLASS);
    
    OWLClassExpression worldChampionRange = 
        owlReasoner.getOWLClassExpression("currentWorldChampion", "Agent", OBJECT_ALL_VALUES_FROM);
    
    setM.add(agentClass);
    setM.add(personClass);
    setM.add(organizationClass);
    setM.add(familyClass);
    setM.add(employerClass);
    setM.add(deityClass);
    setM.add(worldChampionRange);
    
    
    ArmstrongABox armstrongABox = new ArmstrongABoxImpl(owlReasoner, setM);
    return armstrongABox;
  } 	
}
