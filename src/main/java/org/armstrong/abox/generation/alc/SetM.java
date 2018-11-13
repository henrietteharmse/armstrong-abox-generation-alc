package org.armstrong.abox.generation.alc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.armstrong.abox.closedsets.IndexedSet;
import org.armstrong.abox.reasoner.OWLReasonerFacade;
import org.semanticweb.owlapi.model.OWLClassExpression;

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

public class SetM {
  
  private IndexedSet<OWLClassExpression> setM;
  private boolean permissible;
  private List<String> reasonsWhyImpermissible;
  
  
  private OWLReasonerFacade owlReasoner;
  
  public SetM(OWLReasonerFacade owlReasoner, IndexedSet<OWLClassExpression> setM) {
    super();
    this.setM = setM;
    this.owlReasoner = owlReasoner;
  }
  
  public IndexedSet<OWLClassExpression> getSetM() {
    return setM;
  }
  
  public boolean isPermissible() {
    reasonsWhyImpermissible = new ArrayList<String>();
    permissible = true;
    
//    reasonsWhyImpermissible = isMSimultaneouslySatisfiable(reasonsWhyImpermissible);
    
    for (OWLClassExpression classExpression : setM) {
//      reasonsWhyImpermissible = checkBottom(classExpression, reasonsWhyImpermissible);
      reasonsWhyImpermissible = checkTop(classExpression, reasonsWhyImpermissible);
    }
    return permissible;
  }
  
  public List<String> getReasonsWhyImpermissible() {
    return reasonsWhyImpermissible;
  }
  
  private List<String> checkBottom(OWLClassExpression classExpression, 
      List<String> reasonsWhyImpermissible) {
    
    if (owlReasoner.isNothing(classExpression)) {
      permissible = false;
      reasonsWhyImpermissible.add(formatErrorMsg(classExpression) + 
          " is equivalent to the bottom concept");
    }
    return reasonsWhyImpermissible; 
  }

  private List<String> checkTop(OWLClassExpression classExpression, 
      List<String> reasonsWhyImpermissible) {
    
    if (owlReasoner.isThing(classExpression)) {
      permissible = false;
      reasonsWhyImpermissible.add(formatErrorMsg(classExpression) + 
          " is equivalent to the top concept");
    }
    return reasonsWhyImpermissible; 
  }
  
  /** 
   * 
   * @param reasonsWhyImpermissible
   * @return
   */
  private List<String> isMSimultaneouslySatisfiable(List<String> reasonsWhyImpermissible) {
    OWLClassExpression intersectionOf = owlReasoner.getIntersectionOf(getSetM());
    if (!owlReasoner.isSatisfiable(intersectionOf)) {
      permissible = false;      
      Map<OWLClassExpression, Set<OWLClassExpression>> disjointnessMap = 
          owlReasoner.getDisjointnessMap(setM);
      reasonsWhyImpermissible.add("Concepts in M are not simultaneously satisfiable.");      
      for (OWLClassExpression classExpression : disjointnessMap.keySet()) {
        StringBuffer strBuffer = new StringBuffer(formatErrorMsg(classExpression) + " disjoint with ");        
        for (OWLClassExpression disjointClassExpression : disjointnessMap.get(classExpression)) {
          strBuffer.append(formatErrorMsg(disjointClassExpression));
          strBuffer.append(" ");
        }
        strBuffer.append(".");
        reasonsWhyImpermissible.add(strBuffer.toString());
      }
      
    }
    return reasonsWhyImpermissible;
  } 
  
  private String formatErrorMsg(OWLClassExpression classExpression) {
    StringBuffer msg = new StringBuffer();
    
    msg.append("ClassExpressionType = " + classExpression.getClassExpressionType().getShortForm());
    msg.append(" with signature ");
    
    classExpression
      .signature()
      .forEach(p -> msg.append(p.getIRI().toString() + " "));
    
    return msg.toString();
  }
}
