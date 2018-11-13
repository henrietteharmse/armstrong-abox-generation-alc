package org.armstrong.abox.reasoner;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.semanticweb.HermiT.ReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.ClassExpressionType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLRuntimeException;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.parameters.ChangeApplied;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import openllet.owlapi.OWL;
import uk.ac.manchester.cs.jfact.JFactFactory;

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
public class OWLReasonerFacadeImpl implements OWLReasonerFacade {

  private static Logger logger = LoggerFactory.getLogger(OWLReasonerFacadeImpl.class);

  protected OWLOntologyManager manager = null;
  protected OWLDataFactory dataFactory = null;
  protected OWLOntology ontology = null;
  protected OWLReasoner reasoner = null;
  
  
  public OWLReasonerFacadeImpl(IRI ontologyFile) throws OWLOntologyCreationException {
    this(ontologyFile, new ReasonerFactory());
  }

  public OWLReasonerFacadeImpl(IRI ontologyFile, OWLReasoners owlReasoner) 
      throws OWLOntologyCreationException {
    
    this(ontologyFile, getOWLReasonerFactory(owlReasoner));
  } 
  
  public OWLReasonerFacadeImpl(IRI ontologyFile, OWLReasonerFactory reasonerFactory)
      throws OWLOntologyCreationException {
    manager = OWLManager.createOWLOntologyManager();
    ontology = manager.loadOntologyFromOntologyDocument(ontologyFile);
    dataFactory = manager.getOWLDataFactory();
    reasoner = reasonerFactory.createReasoner(ontology);
  }
  
  private static OWLReasonerFactory getOWLReasonerFactory(OWLReasoners owlReasoner) {
    switch (owlReasoner) {
    case JFACT:
      return new JFactFactory();
    case HERMIT:
      return new ReasonerFactory();
    // case OPENLLET:
    // return OpenlletReasonerFactory.getInstance();
    default:
      throw new UnsupportedOperationException("Unknown reasoner = " + owlReasoner);
    }
  }

  protected IRI getOntologyIRI() {
    return ontology.getOntologyID().getOntologyIRI().get();
  }

  @Override
  public OWLClassAssertionAxiom addOWLClassAssertionAxiom(OWLClassExpression owlClassExpression,
      OWLNamedIndividual individual) {
    return addOWLClassAssertionAxiom(owlClassExpression, individual, false);
  }

  @Override
  public OWLClassAssertionAxiom addOWLClassAssertionAxiom(OWLClassExpression owlClassExpression,
      OWLNamedIndividual individual, boolean flush) {
    OWLClassAssertionAxiom owlClassAssertionAxiom = dataFactory
        .getOWLClassAssertionAxiom(owlClassExpression, individual);

    return addClassAssertionAxiom(owlClassAssertionAxiom, flush);
  }

  @Override
  public OWLClassAssertionAxiom addOWLClassAssertionAxiom(
      Set<OWLClassExpression> owlClassExpressions, OWLNamedIndividual individual) {
    return addOWLClassAssertionAxiom(owlClassExpressions, individual, false);
  }

  @Override
  public OWLClassAssertionAxiom addOWLClassAssertionAxiom(
      Set<OWLClassExpression> owlClassExpressions, OWLNamedIndividual individual, boolean flush) {
    OWLClassAssertionAxiom owlClassAssertionAxiom = dataFactory.getOWLClassAssertionAxiom(
        dataFactory.getOWLObjectIntersectionOf(owlClassExpressions), individual);

    return addClassAssertionAxiom(owlClassAssertionAxiom, flush);
  }

  @Override
  public OWLClassAssertionAxiom addOWLNegatedClassAssertionAxiom(
      OWLClassExpression owlClassExpression, OWLNamedIndividual individual) {
    return addOWLNegatedClassAssertionAxiom(owlClassExpression, individual, false);
  }

  @Override
  public OWLClassAssertionAxiom addOWLNegatedClassAssertionAxiom(
      OWLClassExpression owlClassExpression, OWLNamedIndividual individual, boolean flush) {
    OWLClassAssertionAxiom owlClassAssertionAxiom = dataFactory
        .getOWLClassAssertionAxiom(owlClassExpression.getObjectComplementOf(), individual);

    return addClassAssertionAxiom(owlClassAssertionAxiom, flush);
  }

  @Override
  public OWLClassAssertionAxiom addOWLNegatedClassAssertionAxiom(
      Set<OWLClassExpression> owlClassExpressions, OWLNamedIndividual individual) {
    return addOWLNegatedClassAssertionAxiom(owlClassExpressions, individual, false);
  }

  @Override
  public OWLClassAssertionAxiom addOWLNegatedClassAssertionAxiom(
      Set<OWLClassExpression> owlClassExpressions, OWLNamedIndividual individual, boolean flush) {
    OWLClassAssertionAxiom owlClassAssertionAxiom = dataFactory.getOWLClassAssertionAxiom(
        dataFactory.getOWLObjectIntersectionOf(owlClassExpressions).getObjectComplementOf(),
        individual);

    return addClassAssertionAxiom(owlClassAssertionAxiom, flush);
  }

  private OWLClassAssertionAxiom addClassAssertionAxiom(
      OWLClassAssertionAxiom owlClassAssertionAxiom, boolean flush) {
    Optional<OWLClassAssertionAxiom> owlClassAssertionAxiomOptional = Optional.empty();
    if (!ontologyContainsClassAssertionAxiom(owlClassAssertionAxiom)) {
      ChangeApplied changeApplied = manager.addAxiom(ontology, owlClassAssertionAxiom);
      if (changeApplied.equals(ChangeApplied.SUCCESSFULLY)) {
        owlClassAssertionAxiomOptional = Optional.of(owlClassAssertionAxiom);
        if (flush) {
          reasoner.flush();
        }
      }
    }

    if (!owlClassAssertionAxiomOptional.isPresent()) {
      throw new OWLRuntimeException(
          "Could not add negated class assertion = " + owlClassAssertionAxiom + ".");
    }

    return owlClassAssertionAxiomOptional.get();
  }

  @Override
  public OWLClassAssertionAxiom getOWLClassAssertionAxiom(OWLClassExpression owlClassExpression,
      OWLNamedIndividual individual) {

    return dataFactory.getOWLClassAssertionAxiom(owlClassExpression, individual);
  }

  @Override
  public OWLClassAssertionAxiom getOWLNegatedClassAssertionAxiom(
      OWLClassExpression owlClassExpression, OWLNamedIndividual individual) {

    return dataFactory.getOWLClassAssertionAxiom(owlClassExpression.getObjectComplementOf(),
        individual);
  }

  @Override
  public OWLClassAssertionAxiom getOWLNegatedClassAssertionAxiom(
      Set<OWLClassExpression> owlClassExpressions, OWLNamedIndividual individual) {

    return dataFactory.getOWLClassAssertionAxiom(
        dataFactory.getOWLObjectIntersectionOf(owlClassExpressions).getObjectComplementOf(),
        individual);
  }

  @Override
  public void removeAxiom(OWLAxiom owlAxiom) {
    ontology.remove(owlAxiom);
  }

  @Override
  public boolean isConsistent() {
    return reasoner.isConsistent();
  }

  @Override
  public void saveOntology(IRI ontologyFile) throws OWLOntologyStorageException {
    ontology.saveOntology(ontologyFile);
  }
  
  @Override
  public OWLClass getOWLThing() {
    return dataFactory.getOWLThing();
  }

  @Override
  public OWLClassExpression getOWLClassExpression(String name,
      ClassExpressionType classExpressionType) {
    
    logger.trace("getOntologyIRI().getIRIString()#name = " + getOntologyIRI().getIRIString() + "#"
        + name);
    IRI iri = IRI.create(getOntologyIRI().getIRIString() + "#" + name);

    switch (classExpressionType) {
    case OWL_CLASS:
      return dataFactory.getOWLClass(iri);
    
    case OBJECT_SOME_VALUES_FROM:
      OWLObjectPropertyExpression propertyExpressionSomeValuesFrom = dataFactory.getOWLObjectProperty(iri);
      return dataFactory.getOWLObjectSomeValuesFrom(propertyExpressionSomeValuesFrom, dataFactory.getOWLThing());
      
    case OBJECT_ALL_VALUES_FROM:
      OWLObjectPropertyExpression propertyExpressionAllValuesFrom = dataFactory.getOWLObjectProperty(iri);
      return dataFactory.getOWLObjectAllValuesFrom(propertyExpressionAllValuesFrom, dataFactory.getOWLThing());
      
    default:
      throw new UnsupportedOperationException(
          "ClassExpressionType = " + classExpressionType + " not implemented.");
    }
  }

  @Override
  public OWLClassExpression getOWLClassExpression(String propertyName, String className,
      ClassExpressionType classExpressionType) {
    
    logger.trace("getOntologyIRI().getIRIString()#propertyName = " + getOntologyIRI().getIRIString() + "#"
        + propertyName);
    logger.trace("getOntologyIRI().getIRIString()#className = " + getOntologyIRI().getIRIString() + "#"
        + className);    
    
    IRI propertyIRI = IRI.create(getOntologyIRI().getIRIString() + "#" + propertyName);
    IRI classIRI = IRI.create(getOntologyIRI().getIRIString() + "#" + className);

    switch (classExpressionType) {
    case OWL_CLASS:
      return dataFactory.getOWLClass(classIRI);
    
    case OBJECT_ALL_VALUES_FROM:     
      OWLObjectPropertyExpression propertyExpressionAllValuesFrom = 
        dataFactory.getOWLObjectProperty(propertyIRI);
      OWLClassExpression classExpressionAllValuesFrom = dataFactory.getOWLClass(classIRI);
      return dataFactory.getOWLObjectAllValuesFrom(propertyExpressionAllValuesFrom, 
          classExpressionAllValuesFrom);
      
    case OBJECT_SOME_VALUES_FROM:     
      OWLObjectPropertyExpression propertyExpressionSomeValuesFrom = 
        dataFactory.getOWLObjectProperty(propertyIRI);
      OWLClassExpression classExpressionSomeValuesFrom = dataFactory.getOWLClass(classIRI);
      return dataFactory.getOWLObjectSomeValuesFrom(propertyExpressionSomeValuesFrom, 
          classExpressionSomeValuesFrom);      
    default:
      throw new UnsupportedOperationException(
          "ClassExpressionType = " + classExpressionType + " not implemented.");
    }
  }     

  
  @Override
  public OWLClassExpression getOWLClassExpression(String className1, 
      ClassExpressionType classExpressionType1, String className2, 
      ClassExpressionType classExpressionType2, ClassExpressionType operationExpressionType) {
    
    OWLClassExpression classExpression1 = getOWLClassExpression(className1, classExpressionType1);
    OWLClassExpression classExpression2 = getOWLClassExpression(className2, classExpressionType2);
    Set<OWLClassExpression> operands = new HashSet<OWLClassExpression>();
    operands.add(classExpression1);
    operands.add(classExpression2);

    switch (operationExpressionType) {
    case OBJECT_UNION_OF:
      return dataFactory.getOWLObjectUnionOf(operands);
      
    case OBJECT_INTERSECTION_OF:
      return dataFactory.getOWLObjectIntersectionOf(operands);
      
    default:
      throw new UnsupportedOperationException(
          "ClassExpressionType = " + operationExpressionType + " not implemented.");
    }
  }     

  
  public OWLClassExpression getOWLClassExpression(OWLClassExpression owlClassExpression1,
      OWLClassExpression owlClassExpression2, ClassExpressionType operationExpressionType) {
    
    Set<OWLClassExpression> operands = new HashSet<OWLClassExpression>();
    operands.add(owlClassExpression1);
    operands.add(owlClassExpression2);

    switch (operationExpressionType) {
    case OBJECT_UNION_OF:
      return dataFactory.getOWLObjectUnionOf(operands);
      
    case OBJECT_INTERSECTION_OF:
      return dataFactory.getOWLObjectIntersectionOf(operands);
      
    default:
      throw new UnsupportedOperationException(
          "ClassExpressionType = " + operationExpressionType + " not implemented.");
    }
  }  
  
  @Override
  public boolean isEntailed(OWLAxiom owlAxiom) {
    if (!reasoner.isEntailmentCheckingSupported(owlAxiom.getAxiomType())) {
      logger.error("Entailment not supported by reasoner " + reasoner.getReasonerName() + ".");
      throw new UnsupportedOperationException(
          "Entailment not supported by reasoner=" + reasoner.getReasonerName() + ".");
    }

    return reasoner.isEntailed(owlAxiom);
  }
  
  @Override
  public boolean isTautology(OWLSubClassOfAxiom owlSubClassOfAxiom) {
    boolean result = false;
    
    OWLClassExpression subClass = owlSubClassOfAxiom.getSubClass();
    OWLClassExpression superClass = owlSubClassOfAxiom.getSuperClass();
    
    logger.trace("~~~ Begin TAUTOLOGY check: subClass = " + subClass + " superClass = " + superClass);
      
    // Check whether subclass is bottom
    if (!reasoner.isSatisfiable(owlSubClassOfAxiom.getSubClass())) {
      logger.trace("~~~ " + owlSubClassOfAxiom + " is a tautology because of bottom.");
      result = true;    
    } 
    // Check whether superclass is top
    else if (isEquivalent(owlSubClassOfAxiom.getSuperClass(), dataFactory.getOWLThing())) {
      logger.trace("~~~ " + owlSubClassOfAxiom + " is a tautology because of top.");
      result = true;
    } 
    else if (isEquivalentWithSameSignature(
        owlSubClassOfAxiom.getSubClass(), owlSubClassOfAxiom.getSuperClass())) {
      logger.trace("~~~ SUBSUMEE = "  + owlSubClassOfAxiom.getSubClass() + " SUBSUMER = " 
         + owlSubClassOfAxiom.getSuperClass() +
          " is a TAUTOLOGY because the subsumee and subsumer are equivalent.");
      result = true;
    } else if (isTautologyBasedOnDisjunction(owlSubClassOfAxiom)) {
      logger.trace("~~~ SUBSUMEE = "  + owlSubClassOfAxiom.getSubClass() + " SUBSUMER = " 
          + owlSubClassOfAxiom.getSuperClass() +
           " is a TAUTOLOGY based on disjunction.");      
       result = true;
    } 
    else if (isTautologyBasedOnConjunction(owlSubClassOfAxiom)) {
      logger.trace("~~~ SUBSUMEE = "  + owlSubClassOfAxiom.getSubClass() + " SUBSUMER = " 
          + owlSubClassOfAxiom.getSuperClass() +
           " is a TAUTOLOGY based on conjunction.");      
      result = true;
    }
 
    logger.trace("~~~ End TAUTOLOGY check: " + result);
    return result;
  }
  
  private boolean isTautologyBasedOnDisjunction(OWLSubClassOfAxiom owlSubClassOfAxiom) {
    boolean result = false;
    
    Set<OWLClassExpression> subClassAsDisjunctSet = owlSubClassOfAxiom.getSubClass().asDisjunctSet();
    Set<OWLClassExpression> superClassAsDisjunctSet = owlSubClassOfAxiom.getSuperClass().asDisjunctSet();

    logger.trace("subClassAsDisjunctSet = " + subClassAsDisjunctSet);
    logger.trace("superClassAsDisjunctSet = " + superClassAsDisjunctSet);
    
    result = superClassAsDisjunctSet.containsAll(subClassAsDisjunctSet);

    return result;
  }

  private boolean isTautologyBasedOnConjunction(OWLSubClassOfAxiom owlSubClassOfAxiom) {
    boolean result = false;
    
    Set<OWLClassExpression> subClassAsConjunctSet = owlSubClassOfAxiom.getSubClass().asConjunctSet();
    Set<OWLClassExpression> superClassAsConjunctSet = owlSubClassOfAxiom.getSuperClass().asConjunctSet();
    
    logger.trace("subClassAsConjunctSet = " + subClassAsConjunctSet);
    logger.trace("superClassAsConjunctSet = " + superClassAsConjunctSet);    
    
    result = subClassAsConjunctSet.containsAll(superClassAsConjunctSet);

    return result;
  } 
  // Is owlClassExpression1(C) and owlClassExpression2(D) equivalent?
  // C \equiv D iff 
  // C \sqcap \neg D \equiv \bot and
  // D \sqcap \neg C \equiv \bot
  private boolean isEquivalent(OWLClassExpression owlClassExpression1, 
      OWLClassExpression owlClassExpression2) {
    
    boolean result = false;
 
    logger.trace("Begin EQUIVALENCE check: expr1 = " + owlClassExpression1 + 
        " expr2 = " + owlClassExpression2);
    
    result = !reasoner.isSatisfiable(dataFactory.getOWLObjectIntersectionOf(
        owlClassExpression1, owlClassExpression2.getObjectComplementOf())) &&
        !reasoner.isSatisfiable(dataFactory.getOWLObjectIntersectionOf(
            owlClassExpression2, owlClassExpression1.getObjectComplementOf()));        

    logger.trace("End EQUIVALENCE check: isEquivalent = " + result);   

    return result;
  }
  
  private boolean isEquivalentWithSameSignature(OWLClassExpression owlClassExpression1, 
  OWLClassExpression owlClassExpression2) {
    boolean result = false;
    
    Set<OWLClassExpression> owlClassAsConjunctSet1 = owlClassExpression1.asConjunctSet();
    Set<OWLClassExpression> owlClassAsConjunctSet2 = owlClassExpression2.asConjunctSet();
    
    result = isEquivalent(owlClassExpression1, owlClassExpression2) &&
        owlClassAsConjunctSet1.containsAll(owlClassAsConjunctSet2) &&
            owlClassAsConjunctSet2.containsAll(owlClassAsConjunctSet1);
    
    return result;
  }
    

  
  @Override
  public boolean isContradiction(OWLSubClassOfAxiom owlSubClassOfAxiom) {
    boolean result = false;
    
    if (owlSubClassOfAxiom.getSuperClass().isBottomEntity()) {
      result = true;
    }   
    
    return result;
  }

  @Override
  public boolean isSatisfiable(OWLClassExpression owlClassExpression) {
    return reasoner.isSatisfiable(owlClassExpression);
  }
  
  @Override
  public boolean isThing(OWLClassExpression classExpression) {
    return reasoner
        .equivalentClasses(classExpression)
        .anyMatch(p -> dataFactory.getOWLThing().equals(p));
  }

  @Override
  public boolean isNothing(OWLClassExpression classExpression) {
    return reasoner
        .equivalentClasses(classExpression)
        .anyMatch(p -> dataFactory.getOWLNothing().equals(p));
  }   
  
  @Override
  public OWLSubClassOfAxiom createAxiom(Set<OWLClassExpression> leftHandSide, 
      Set<OWLClassExpression> rightHandSide, Set<OWLClassExpression> setM) {
    return dataFactory.getOWLSubClassOfAxiom(createConcept(leftHandSide, setM),
        createConcept(rightHandSide, setM));
  }
  
  @Override
  public OWLClassExpression createConcept(Set<OWLClassExpression> owlClassExpressions,
      Set<OWLClassExpression> setM) {
    OWLClassExpression concept;

    if (owlClassExpressions.isEmpty()) 
      concept = dataFactory.getOWLObjectUnionOf(setM);
    else if (owlClassExpressions.size() == 1) {
      Optional<OWLClassExpression> optionalClass = owlClassExpressions.stream().findFirst();
      concept = optionalClass.get();
    }
    else    
      concept = dataFactory.getOWLObjectIntersectionOf(owlClassExpressions);

    return concept;
  } 
  
  @Override
  public OWLClassExpression getIntersectionOf(Set<OWLClassExpression> owlClassExpressions) {
    return dataFactory.getOWLObjectIntersectionOf(owlClassExpressions);
  }
 
  
  @Override
  public OWLNamedIndividual getFreshIndividual(String shortname) {
    IRI freshIndividualIRI = IRI.create(getOntologyIRI() + "#" + shortname);
    return dataFactory.getOWLNamedIndividual(freshIndividualIRI);
  }  

  private boolean ontologyContainsClassAssertionAxiom(
      OWLClassAssertionAxiom owlClassAssertionAxiom) {
    boolean individualOfTypeExist = reasoner
        .instances(owlClassAssertionAxiom.getClassExpression(), false)
        .anyMatch(p -> owlClassAssertionAxiom.getIndividual().asOWLNamedIndividual().equals(p));
    return individualOfTypeExist;
  }
  
  @Override
  public Map<OWLClassExpression, Set<OWLClassExpression>> getDisjointnessMap(
      Set<OWLClassExpression> classExpressions) {
    
    Map<OWLClassExpression, Set<OWLClassExpression>> disjointnessMap = 
        new HashMap<OWLClassExpression, Set<OWLClassExpression>>();
    
    for (OWLClassExpression owlClassExpression : classExpressions) {
      disjointnessMap.put(owlClassExpression, reasoner
        .disjointClasses(owlClassExpression)
        .collect(Collectors.toSet()));
    }
    
    return disjointnessMap;
  }
  
  @Override
  public OWLAnnotationAssertionAxiom addAnnotation(OWLNamedIndividual namedIndividual, 
      String comment) {
    OWLAnnotationAssertionAxiom annotationAssertionAxiom = 
        dataFactory.getOWLAnnotationAssertionAxiom(namedIndividual.getIRI(), 
            dataFactory.getRDFSComment(comment)); 
    Optional<OWLAnnotationAssertionAxiom> owlAnnotationAssertionAxiomOptional = Optional.empty();
    ChangeApplied changeApplied = manager.addAxiom(ontology, annotationAssertionAxiom);
    if (changeApplied.equals(ChangeApplied.SUCCESSFULLY)) {
      owlAnnotationAssertionAxiomOptional = Optional.of(annotationAssertionAxiom);
    }
    if (!owlAnnotationAssertionAxiomOptional.isPresent()) {
      throw new OWLRuntimeException(
          "Could not add annotion assertion axiom = " + annotationAssertionAxiom + ".");
    }

    return owlAnnotationAssertionAxiomOptional.get();
  } 
}
