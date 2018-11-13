package org.armstrong.abox.generation.alc;

import static org.semanticweb.owlapi.model.ClassExpressionType.OWL_CLASS;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;

import org.armstrong.abox.closedsets.IndexedSet;
import org.armstrong.abox.closedsets.OrderedSet;
import org.armstrong.abox.generation.alc.ArmstrongABox;
import org.armstrong.abox.generation.alc.ArmstrongABoxImpl;
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
public class TestArmstrongABoxNumberAttributes {
	private static Logger logger = LoggerFactory.getLogger(TestArmstrongABoxNumberAttributes.class);
	// Why This Failure marker
	private static final Marker WTF_MARKER = MarkerFactory.getMarker("WTF");
	private static final Path PATH = Paths.get(".").toAbsolutePath().normalize();
	
	private ArmstrongABox armstrongABox;
	private String outputFile;

	public TestArmstrongABoxNumberAttributes(ArmstrongABox armstrongABox, String outputFile) {
		super();
		this.armstrongABox = armstrongABox;
		this.outputFile = outputFile;
	}

	@Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] {
//		  incompleteNumericFormalContext1(),
		  incompleteNumericFormalContext2(),
//		  incompleteNumericFormalContext3(),
//		  incompleteNumericFormalContext4() 
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

	private static Object[] completedNumericFormalContext() {
		return new Object[] { getContextForNumericOntology1(
				"file:" + PATH.toFile().getAbsolutePath() + 
				  "/src/test/resources/NumericOntologyUpdated.owl")
				};
	}

	private static Object[] incompleteNumericFormalContext1() {
		return new Object[] { getContextForNumericOntology1(
				"file:" + PATH.toFile().getAbsolutePath() + "/src/test/resources/NumericOntology1.owl"),
		    "file:" + PATH.toFile().getAbsolutePath() + "/src/test/resources/NumericOntologyArmstrongABox1.owl"};
	}

	private static Object[] incompleteNumericFormalContext2() {
		return new Object[] { getContextForNumericOntology2or3(
				"file:" + PATH.toFile().getAbsolutePath() + "/src/test/resources/NumericOntology2.owl"),
		    "file:" + PATH.toFile().getAbsolutePath() + "/src/test/resources/NumericOntologyArmstrongABox2.owl"};
	}
	
	private static Object[] incompleteNumericFormalContext3() {
		return new Object[] { getContextForNumericOntology2or3(
				"file:" + PATH.toFile().getAbsolutePath() + "/src/test/resources/NumericOntology3.owl"),
				"/src/test/resources/NumericOntologyArmstrongABox3.owl"};
	}
	
	private static Object[] incompleteNumericFormalContext4() {
		return new Object[] { getContextForNumericOntology4(
				"file:" + PATH.toFile().getAbsolutePath() + "/src/test/resources/NumericOntology3.owl"),
				"/src/test/resources/NumericOntologyArmstrongABox4.owl"};
	}	
	private static ArmstrongABox getContextForNumericOntology1(String filename) {
		IRI ontologyFile = IRI.create(filename);
		OWLReasonerFacadeImpl owlReasoner = null;
		try {
			owlReasoner = new OWLReasonerFacadeImpl(ontologyFile);
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.trace(e.getStackTrace().toString());
		}

		IndexedSet<OWLClassExpression> classExpressions = new OrderedSet<OWLClassExpression>(
				Arrays.asList(owlReasoner.getOWLClassExpression(NumberAttributes.COMPOSITE.getName(), OWL_CLASS),
						owlReasoner.getOWLClassExpression(NumberAttributes.EVEN.getName(), OWL_CLASS),
						owlReasoner.getOWLClassExpression(NumberAttributes.ODD.getName(), OWL_CLASS),
						owlReasoner.getOWLClassExpression(NumberAttributes.PRIME.getName(), OWL_CLASS),
						owlReasoner.getOWLClassExpression(NumberAttributes.SQUARE.getName(), OWL_CLASS)));
		ArmstrongABox armstrongABox = new ArmstrongABoxImpl(owlReasoner, classExpressions);
		return armstrongABox;
	}

	
	private static ArmstrongABox getContextForNumericOntology2or3(String filename) {
		IRI ontologyFile = IRI.create(filename);
		OWLReasonerFacadeImpl owlReasoner = null;
		try {
			owlReasoner = new OWLReasonerFacadeImpl(ontologyFile);
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.trace(e.getStackTrace().toString());
		}

		IndexedSet<OWLClassExpression> classExpressions = new OrderedSet<OWLClassExpression>(
				Arrays.asList(owlReasoner.getOWLClassExpression(NumberAttributes.COMPOSITE.getName(), OWL_CLASS),
						owlReasoner.getOWLClassExpression(NumberAttributes.EVEN.getName(), OWL_CLASS),
						owlReasoner.getOWLClassExpression(NumberAttributes.SQUARE.getName(), OWL_CLASS)));
		ArmstrongABox armstrongABox = new ArmstrongABoxImpl(owlReasoner, classExpressions);
		return armstrongABox;
	}
	
	private static ArmstrongABox getContextForNumericOntology4(String filename) {
		IRI ontologyFile = IRI.create(filename);
		OWLReasonerFacadeImpl owlReasoner = null;
		try {
			owlReasoner = new OWLReasonerFacadeImpl(ontologyFile);
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.trace(e.getStackTrace().toString());
		}

		IndexedSet<OWLClassExpression> classExpressions = new OrderedSet<OWLClassExpression>(
				Arrays.asList(owlReasoner.getOWLClassExpression(NumberAttributes.COMPOSITE.getName(), OWL_CLASS),
						owlReasoner.getOWLClassExpression(NumberAttributes.ODD.getName(), OWL_CLASS),
						owlReasoner.getOWLClassExpression(NumberAttributes.SQUARE.getName(), OWL_CLASS)));
		ArmstrongABox armstrongABox = new ArmstrongABoxImpl(owlReasoner, classExpressions);
		return armstrongABox;
	}	
}
