package org.armstrong.abox.generation.alc;

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
import org.semanticweb.owlapi.model.ClassExpressionType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

  /*
   * ArmstrongABoxes: A library for generating Armstrong ABoxes. 
   * This class represents a university case study for using Armstrong ABoxes for ALC TBoxes to 
   * refine an ALC TBox. The document "./docs/Application of Armstrong ABoxes for ALC TBoxes.pdf" 
   * explains the thinking behind the test cases implemented here.
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
public class TestArmstrongObjectDiagram {
  private static Logger logger = LoggerFactory.getLogger(TestArmstrongABoxUniversity.class);
  // Why This Failure marker
  private static final Marker WTF_MARKER = MarkerFactory.getMarker("WTF");
  private static final Path PATH = Paths.get(".").toAbsolutePath().normalize();
  
  private ArmstrongABox armstrongABox;
  private String outputFile;

  public TestArmstrongObjectDiagram(ArmstrongABox armstrongABox, String outputFile) {
    super();
    this.armstrongABox = armstrongABox;
    this.outputFile = outputFile;
  }

  @Parameters  
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][] {    
//      analyse_Person_StudentORTeacher(),
//      analyse_Person_StudentORTeacher_Revised(),
//      analyse_Person_StudentANDTeacher(),
//      analyse_Person_StudentANDTeacher_Revised(),       
//      analyse_Student_Teacher(),     
//      
//      // Analyse GCIs in which Person appears
//      analyse_Student_AttendsSomething(),
//      analyse_AttendsSomeThing_AttendsSomeCourse_Student(),
//            
//      // Analyse UGStudent concept
      analyse_UGStudent_ugStudentAttendsSomeThing_ugStudentAttendsSomeUGCourse(),
//      analyse_PGStudent_AttendsSomeThing_AttendsSomePGCourse(),
//      analyse_PGStudent_AttendsSomeThing_AttendsSomePGCourse_Revised(),      
//      analyse_UGStudent_AttendsSomeUGCourse_AttendsSomePGCourse(),
//      analyse_PGStudent_AttendsSomeUGCourse_AttendsSomePGCourse(),
//      analyse_PGStudent_AttendsSomeUGCourse_AttendsSomePGCourse_Revised(),
//      analyse_PGStudent_AttendsSomeUGCourseANDAttendsSomePGCourse(),    
//      analyse_PGStudent_AttendsSomeUGCourseANDAttendsSomePGCourse_Revised(),
//      analyse_UGStudent_AttendsSomePGCourse(),
//      analyse_UGStudent_AttendsSomeUGCourseANDAttendsSomePGCourse(),
//      analyse_UGStudentANDNotAttendsSomeUGCourse(),
//      analyse_PGStudent_StudentANDAttendsSomePGCourse(),
//      analyse_PGStudent_StudentANDAttendsSomePGCourse_Revised()     
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

  private static Object[] analyse_Person_StudentORTeacher() {
    return new Object[] { getContext_Person_StudentORTeacher(
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/UniversityUML.owl"),
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/UniversityOD-Person_StudentORTeacher.owl"};
  } 
  
  private static Object[] analyse_Person_StudentORTeacher_Revised() {
    return new Object[] { getContext_Person_StudentORTeacher(
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/UniversityUML-Person_EQUIV_StudentORTeacher.owl"),
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/UniversityOD-Person_StudentORTeacher_Revised.owl"};
  } 
  
  private static Object[] analyse_Person_StudentANDTeacher() {
    return new Object[] { getContext_Person_StudentANDTeacher(
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/UniversityUML-Person_EQUIV_StudentORTeacher.owl"),
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/UniversityOD-Person_StudentANDTeacher.owl"};
  }  

  
  private static Object[] analyse_Person_StudentANDTeacher_Revised() {
    return new Object[] { getContext_Person_StudentANDTeacher(
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/UniversityUML-StudentsANDTeachers_Intersect.owl"),
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/UniversityOD-Person_StudentANDTeacher_Revised.owl"};
  } 
  
  private static Object[] analyse_Student_Teacher() {
    return new Object[] { getContext_Student_Teacher(
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/UniversityUML-StudentsANDTeachers_Intersect.owl"),
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/UniversityOD-Student_Teacher.owl"};
  }   
  
  private static Object[] analyse_Student_AttendsSomething() {
    return new Object[] { getContext_Student_AttendsSomething(
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/UniversityUML-StudentsANDTeachers_Intersect.owl"),
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/UniversityOD-Student_AttendsSomeThing.owl"};
  } 
  
  private static Object[] analyse_AttendsSomeThing_AttendsSomeCourse_Student() {
    return new Object[] { getContext_AttendsSomeThing_AttendsSomeCourse_Student(
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/UniversityUML-StudentsANDTeachers_Intersect.owl"),
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/UniversityOD-Student_AttendsSomeThing_AttendsSomeCourse.owl"};
  }
  
  
  private static Object[] analyse_UGStudent_AttendsSomeUGCourse_AttendsSomePGCourse() {
    return new Object[] { getContext_UGStudent_AttendsSomeUGCourse_AttendsSomePGCourse(
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/UniversityUML-StudentsANDTeachers_Intersect.owl"),
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/UniversityOD-UGStudent_AttendsSomeUGCourse_AttendsSomePGCourse.owl"};
  }

  private static Object[] analyse_PGStudent_AttendsSomeUGCourse_AttendsSomePGCourse() {
    return new Object[] { getContext_PGStudent_AttendsSomeUGCourse_AttendsSomePGCourse(
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/UniversityUML-StudentsANDTeachers_Intersect.owl"),
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/UniversityOD-PGStudent_AttendsSomeUGCourse_AttendsSomePGCourse.owl"};
  }
  
  
  private static Object[] analyse_UGStudent_AttendsSomeThing_AttendsSomeUGCourse() {
    return new Object[] { getContext_UGStudent_AttendsSomeThing_AttendsSomeUGCourse(
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/UniversityUML-StudentsANDTeachers_Intersect.owl"),
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/UniversityOD-UGStudent_AttendsSomeThing_AttendsSomeUGCourse.owl"};
  }

  private static Object[] analyse_UGStudent_ugStudentAttendsSomeThing_ugStudentAttendsSomeUGCourse() {
    return new Object[] { getContext_UGStudent_ugStudentAttendsSomeThing_ugStudentAttendsSomeUGCourse(
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/UniversityUML-StudentsANDTeachers_Intersect.owl"),
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/UniversityOD-UGStudent_ugStudentAttendsSomeThing_ugStudentAttendsSomeUGCourse.owl"};
  }  
  
  private static Object[] analyse_UGStudent_AttendsSomeUGCourseANDAttendsSomePGCourse() {
    return new Object[] { getContext_UGStudent_AttendsSomeUGCourseANDAttendsSomePGCourse(
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/UniversityUML-StudentsANDTeachers_Intersect.owl"),
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/UniversityOD-UGStudent_AttendsSomeUGCourseANDAttendsSomePGCourse.owl"};
  }
  
  
  private static Object[] analyse_PGStudent_AttendsSomeThing_AttendsSomePGCourse() {
    return new Object[] { getContext_PGStudent_AttendsSomeThing_AttendsSomePGCourse(
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/UniversityUML-StudentsANDTeachers_Intersect.owl"),
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/UniversityOD-PGStudent_AttendsSomeThing_AttendsSomePGCourse.owl"};
  }

  private static Object[] analyse_PGStudent_AttendsSomeThing_AttendsSomePGCourse_Revised() {
    return new Object[] { getContext_PGStudent_AttendsSomeThing_AttendsSomePGCourse(
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/UniversityUML-PGStudentCanAttendUGCourse.owl"),
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/UniversityOD-PGStudent_AttendsSomeThing_AttendsSomePGCourse_Revised.owl"};
  }  
  
  private static Object[] analyse_PGStudent_AttendsSomeUGCourseANDAttendsSomePGCourse() {
    return new Object[] { getContext_PGStudent_AttendsSomeUGCourseANDAttendsSomePGCourse(
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/UniversityUML-StudentsANDTeachers_Intersect.owl"),
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/UniversityOD-PGStudent_AttendsSomeUGCourseANDAttendsSomePGCourse.owl"};
  }  

  
  private static Object[] analyse_PGStudent_AttendsSomeUGCourseANDAttendsSomePGCourse_Revised() {
    return new Object[] { getContext_PGStudent_AttendsSomeUGCourseANDAttendsSomePGCourse(
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/UniversityUML-PGStudentCanAttendUGCourse.owl"),
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/UniversityOD-PGStudent_AttendsSomeUGCourseANDAttendsSomePGCourse_Revised.owl"};
  }   
  
  private static Object[] analyse_PGStudent_AttendsSomeUGCourse_AttendsSomePGCourse_Revised() {
    return new Object[] { getContext_PGStudent_AttendsSomeUGCourse_AttendsSomePGCourse(
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/UniversityUML-PGStudentCanAttendUGCourse.owl"),
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/UniversityOD-PGStudent_AttendsSomeUGCourse_AttendsSomePGCourse_Revised.owl"};
  }  
  
  private static Object[] analyse_UGStudent_AttendsSomePGCourse() {
    return new Object[] { getContext_UGStudent_AttendsSomePGCourse(
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/UniversityUML-StudentsANDTeachers_Intersect.owl"),
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/UniversityOD-UGStudent_AttendsSomePGCourse.owl"};
  }

  
  private static Object[] analyse_UGStudentANDNotAttendsSomeUGCourse() {
    return new Object[] { getContext_UGStudentANDNotAttendsSomeUGCourse(
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/University-Student_EQUIV_UGStudentORPGStudent.owl"),
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/UniversityArmstrongABox-UGStudentANDNotAttendsSomeUGCourse.owl"};
  }  

  private static Object[] analyse_PGStudent_StudentANDAttendsSomePGCourse() {
    return new Object[] { getContext_PGStudent_StudentANDAttendsSomePGCourse(
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/University-PGStudentCanAttendUGCourses.owl"),
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/UniversityArmstrongABox-PGStudent_StudentANDAttendsSomePGCourse.owl"};
  }

  private static Object[] analyse_PGStudent_StudentANDAttendsSomePGCourse_Revised() {
    return new Object[] { getContext_PGStudent_StudentANDAttendsSomePGCourse(
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/University-PGCourse_DISJOINT_UGCourse.owl"),
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/UniversityArmstrongABox-PGStudent_StudentANDAttendsSomePGCourse_Revised.owl"};
  }
  
  
  private static ArmstrongABox getContext_Person_StudentORTeacher(String filename) {
    
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
    
    OWLClassExpression personClass = owlReasoner.getOWLClassExpression("Person", OWL_CLASS);
    OWLClassExpression studentOrTeacher = owlReasoner.getOWLClassExpression(
        "Student", OWL_CLASS, "Teacher", OWL_CLASS, ClassExpressionType.OBJECT_UNION_OF);    
    
    setM.add(personClass);
    setM.add(studentOrTeacher);
    
    ArmstrongABox armstrongABox = new ArmstrongABoxImpl(owlReasoner, setM);
    return armstrongABox;
  } 
  
  private static ArmstrongABox getContext_Person_StudentANDTeacher(String filename) {
    
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
    
    OWLClassExpression personClass = owlReasoner.getOWLClassExpression("Person", OWL_CLASS);
    OWLClassExpression studentAndTeacher = owlReasoner.getOWLClassExpression(
        "Student", OWL_CLASS, "Teacher", OWL_CLASS, ClassExpressionType.OBJECT_INTERSECTION_OF);    
    
    setM.add(personClass);
    setM.add(studentAndTeacher);
    
    ArmstrongABox armstrongABox = new ArmstrongABoxImpl(owlReasoner, setM);
    return armstrongABox;
  } 
  
  private static ArmstrongABox getContext_Student_Teacher(String filename) {
    
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
    
    OWLClassExpression studentClass = owlReasoner.getOWLClassExpression("Student", OWL_CLASS);
    OWLClassExpression teacherClass = owlReasoner.getOWLClassExpression("Teacher", OWL_CLASS);
    
    setM.add(studentClass);
    setM.add(teacherClass);
    
    ArmstrongABox armstrongABox = new ArmstrongABoxImpl(owlReasoner, setM);
    return armstrongABox;
  }     
  
  private static ArmstrongABox getContext_Student_AttendsSomething(String filename) {
    
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
    
    OWLClassExpression studentClass = owlReasoner.getOWLClassExpression("Student", OWL_CLASS);
    OWLClassExpression attendsSomethingClass = owlReasoner.getOWLClassExpression("attends", 
        ClassExpressionType.OBJECT_SOME_VALUES_FROM);
    
    setM.add(attendsSomethingClass);
    setM.add(studentClass);
    
    ArmstrongABox armstrongABox = new ArmstrongABoxImpl(owlReasoner, setM);
    return armstrongABox;
  } 

  private static ArmstrongABox getContext_AttendsSomeThing_AttendsSomeCourse_Student(String filename) {
    
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
    
    OWLClassExpression attendsSomeThingClass = owlReasoner.getOWLClassExpression("attends", 
      ClassExpressionType.OBJECT_SOME_VALUES_FROM);
    OWLClassExpression attendsSomeCourseClass = owlReasoner.getOWLClassExpression("attends", "Course", 
        ClassExpressionType.OBJECT_SOME_VALUES_FROM);
    OWLClassExpression studentClass = owlReasoner.getOWLClassExpression("Student", OWL_CLASS);
    
    setM.add(attendsSomeThingClass);
    setM.add(attendsSomeCourseClass);
    setM.add(studentClass);
    
    ArmstrongABox armstrongABox = new ArmstrongABoxImpl(owlReasoner, setM);
    return armstrongABox;
  }
  
  private static ArmstrongABox getContext_UGStudent_AttendsSomeUGCourse_AttendsSomePGCourse(String filename) {
    
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
    
    OWLClassExpression ugStudentClass = owlReasoner.getOWLClassExpression("UGStudent", OWL_CLASS);    
    OWLClassExpression attendsSomeUGCourse = owlReasoner.getOWLClassExpression("attends", "UGCourse", 
        ClassExpressionType.OBJECT_SOME_VALUES_FROM);
    OWLClassExpression attendsSomePGCourse = owlReasoner.getOWLClassExpression("attends", "PGCourse", 
        ClassExpressionType.OBJECT_SOME_VALUES_FROM);    
    
    setM.add(ugStudentClass);
    setM.add(attendsSomeUGCourse);
    setM.add(attendsSomePGCourse);
    
    ArmstrongABox armstrongABox = new ArmstrongABoxImpl(owlReasoner, setM);
    return armstrongABox;
  } 

  private static ArmstrongABox getContext_PGStudent_AttendsSomeUGCourse_AttendsSomePGCourse(String filename) {
    
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
    
    OWLClassExpression pgStudentClass = owlReasoner.getOWLClassExpression("PGStudent", OWL_CLASS);    
    OWLClassExpression attendsSomeUGCourse = owlReasoner.getOWLClassExpression("attends", "UGCourse", 
        ClassExpressionType.OBJECT_SOME_VALUES_FROM);
    OWLClassExpression attendsSomePGCourse = owlReasoner.getOWLClassExpression("attends", "PGCourse", 
        ClassExpressionType.OBJECT_SOME_VALUES_FROM);    
    
    setM.add(pgStudentClass);
    setM.add(attendsSomeUGCourse);
    setM.add(attendsSomePGCourse);
    
    ArmstrongABox armstrongABox = new ArmstrongABoxImpl(owlReasoner, setM);
    return armstrongABox;
  }   
  
  private static ArmstrongABox getContext_UGStudent_AttendsSomeThing_AttendsSomeUGCourse(String filename) {
    
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
    
    OWLClassExpression ugStudentClass = owlReasoner.getOWLClassExpression("UGStudent", OWL_CLASS);
    OWLClassExpression attendsSomeThing = owlReasoner.getOWLClassExpression("attends", 
        ClassExpressionType.OBJECT_SOME_VALUES_FROM);
    OWLClassExpression attendsSomeUGCourse = owlReasoner.getOWLClassExpression("attends", "UGCourse", 
        ClassExpressionType.OBJECT_SOME_VALUES_FROM);    
    
    setM.add(ugStudentClass);
    setM.add(attendsSomeThing);
    setM.add(attendsSomeUGCourse);
    
    ArmstrongABox armstrongABox = new ArmstrongABoxImpl(owlReasoner, setM);
    return armstrongABox;
  }
  
  private static ArmstrongABox getContext_UGStudent_ugStudentAttendsSomeThing_ugStudentAttendsSomeUGCourse(String filename) {
    
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
    
    OWLClassExpression ugStudentClass = owlReasoner.getOWLClassExpression("UGStudent", OWL_CLASS);
    OWLClassExpression ugStudentAttendsSomeThing = owlReasoner.getOWLClassExpression("ugStudentSttends", 
        ClassExpressionType.OBJECT_SOME_VALUES_FROM);
    OWLClassExpression ugStudentAttendsSomeUGCourse = owlReasoner.getOWLClassExpression("ugStudentAttends", "UGCourse", 
        ClassExpressionType.OBJECT_SOME_VALUES_FROM);    
    
    setM.add(ugStudentClass);
    setM.add(ugStudentAttendsSomeThing);
    setM.add(ugStudentAttendsSomeUGCourse);
    
    ArmstrongABox armstrongABox = new ArmstrongABoxImpl(owlReasoner, setM);
    return armstrongABox;
  }  
  
  private static ArmstrongABox getContext_PGStudent_AttendsSomeThing_AttendsSomePGCourse(String filename) {
    
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
    
    OWLClassExpression pgStudentClass = owlReasoner.getOWLClassExpression("PGStudent", OWL_CLASS);
    OWLClassExpression attendsSomeThing = owlReasoner.getOWLClassExpression("attends", 
        ClassExpressionType.OBJECT_SOME_VALUES_FROM);
    OWLClassExpression attendsSomePGCourse = owlReasoner.getOWLClassExpression("attends", "PGCourse", 
        ClassExpressionType.OBJECT_SOME_VALUES_FROM);    
    
    setM.add(pgStudentClass);
    setM.add(attendsSomeThing);
    setM.add(attendsSomePGCourse);
    
    ArmstrongABox armstrongABox = new ArmstrongABoxImpl(owlReasoner, setM);
    return armstrongABox;
  }
  
  private static ArmstrongABox getContext_UGStudent_AttendsSomePGCourse(String filename) {
    
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
    
    OWLClassExpression ugStudentClass = owlReasoner.getOWLClassExpression("UGStudent", OWL_CLASS);    
    OWLClassExpression attendsSomePGCourse = owlReasoner.getOWLClassExpression("attends", "PGCourse", 
        ClassExpressionType.OBJECT_SOME_VALUES_FROM);    
    
    setM.add(ugStudentClass);
    setM.add(attendsSomePGCourse);
    
    ArmstrongABox armstrongABox = new ArmstrongABoxImpl(owlReasoner, setM);
    return armstrongABox;
  }  
  
  private static ArmstrongABox getContext_UGStudent_AttendsSomeUGCourseANDAttendsSomePGCourse(String filename) {
    
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
    
    OWLClassExpression ugStudentClass = owlReasoner.getOWLClassExpression("UGStudent", OWL_CLASS);    
    OWLClassExpression attendsSomeUGCourse = owlReasoner.getOWLClassExpression("attends", "UGCourse", 
        ClassExpressionType.OBJECT_SOME_VALUES_FROM);
    OWLClassExpression attendsSomePGCourse = owlReasoner.getOWLClassExpression("attends", "PGCourse", 
        ClassExpressionType.OBJECT_SOME_VALUES_FROM);    
    OWLClassExpression attendsSomeUGCourseANDattendsSomePGCourse = owlReasoner.getOWLClassExpression(
        attendsSomeUGCourse, attendsSomePGCourse, ClassExpressionType.OBJECT_INTERSECTION_OF);    
    
    
    setM.add(ugStudentClass);
    setM.add(attendsSomeUGCourseANDattendsSomePGCourse);
    
    ArmstrongABox armstrongABox = new ArmstrongABoxImpl(owlReasoner, setM);
    return armstrongABox;
  }  
  
  private static ArmstrongABox getContext_PGStudent_AttendsSomeUGCourseANDAttendsSomePGCourse(String filename) {
    
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
    
    OWLClassExpression pgStudentClass = owlReasoner.getOWLClassExpression("PGStudent", OWL_CLASS);    
    OWLClassExpression attendsSomeUGCourse = owlReasoner.getOWLClassExpression("attends", "UGCourse", 
        ClassExpressionType.OBJECT_SOME_VALUES_FROM);
    OWLClassExpression attendsSomePGCourse = owlReasoner.getOWLClassExpression("attends", "PGCourse", 
        ClassExpressionType.OBJECT_SOME_VALUES_FROM);    
    OWLClassExpression attendsSomeUGCourseANDattendsSomePGCourse = owlReasoner.getOWLClassExpression(
        attendsSomeUGCourse, attendsSomePGCourse, ClassExpressionType.OBJECT_INTERSECTION_OF);    
    
    
    setM.add(pgStudentClass);
    setM.add(attendsSomeUGCourseANDattendsSomePGCourse);
    
    ArmstrongABox armstrongABox = new ArmstrongABoxImpl(owlReasoner, setM);
    return armstrongABox;
  }  
  
  private static ArmstrongABox getContext_PGStudent_StudentANDAttendsSomePGCourse(String filename) {
    
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
    
    OWLClassExpression pgStudentClass = owlReasoner.getOWLClassExpression("PGStudent", OWL_CLASS);
    
    OWLClassExpression studentClass = owlReasoner.getOWLClassExpression("Student", OWL_CLASS);
    OWLClassExpression attendsSomePGCourse = owlReasoner.getOWLClassExpression("attends", "PGCourse", 
        ClassExpressionType.OBJECT_SOME_VALUES_FROM);
    OWLClassExpression studentANDAttendsSomPGCourse = owlReasoner.getOWLClassExpression(
        studentClass, attendsSomePGCourse, ClassExpressionType.OBJECT_INTERSECTION_OF);
    
    
    setM.add(pgStudentClass);
    setM.add(studentANDAttendsSomPGCourse);
    
    ArmstrongABox armstrongABox = new ArmstrongABoxImpl(owlReasoner, setM);
    return armstrongABox;
  }  
  
  private static ArmstrongABox getContext_UGStudentANDNotAttendsSomeUGCourse(String filename) {
    
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
    
    OWLClassExpression ugStudentClass = owlReasoner.getOWLClassExpression("UGStudent", OWL_CLASS);
    
    OWLClassExpression attendsSomeUGCourse = owlReasoner.getOWLClassExpression("attends", "UGCourse", 
        ClassExpressionType.OBJECT_SOME_VALUES_FROM);
    
    setM.add(ugStudentClass);
    setM.add(attendsSomeUGCourse.getObjectComplementOf());
    
    ArmstrongABox armstrongABox = new ArmstrongABoxImpl(owlReasoner, setM);
    return armstrongABox;
  }  
}  
