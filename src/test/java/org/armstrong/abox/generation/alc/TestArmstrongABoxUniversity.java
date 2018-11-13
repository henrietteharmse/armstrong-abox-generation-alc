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
public class TestArmstrongABoxUniversity {
	private static Logger logger = LoggerFactory.getLogger(TestArmstrongABoxUniversity.class);
	// Why This Failure marker
	private static final Marker WTF_MARKER = MarkerFactory.getMarker("WTF");
	private static final Path PATH = Paths.get(".").toAbsolutePath().normalize();
	
	private ArmstrongABox armstrongABox;
	private String outputFile;

	public TestArmstrongABoxUniversity(ArmstrongABox armstrongABox, String outputFile) {
		super();
		this.armstrongABox = armstrongABox;
		this.outputFile = outputFile;
	}

	@Parameters  
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] {
		  // Analyse Person and Course
		  analyse_Person_Course(),
		  analyse_PersonORCourse(),
		  analyse_PersonANDCourse(),
		  analyse_Person_Course_PersonORCourse(),
		  analyse_Person_Course_PersonANDCourse(),
		  analyse_Person_Course_PersonORCourse_PersonANDCourse(),
		  
		  // Analyse Person, Student and Teacher
      analyse_Person_Student_Teacher(),		  
		  analyse_Person_StudentORTeacher(),
      analyse_Person_StudentANDTeacher(),
      analyse_Person_Student_Teacher_StudentANDTeacher(),
		  analyse_Person_Student_Teacher_StudentORTeacher(),
		  analyse_Person_StudentORTeacher_StudentANDTeacher(),
		  analyse_Person_Student_Teacher_StudentORTeacher_StudentANDTeacher(),
		  analyse_Person_StudentORTeacher_RevisedEquiv(),
		  analyse_Person_StudentORTeacher_RevisedSqsubseteq(),
		  analyse_Person_Student_Teacher_StudentORTeacher_StudentANDTeacher_Revised(),

      // Analyse Student and Teacher
      analyse_Student_Teacher(),
		  analyse_Student_Teacher_StudentANDTeacher(),
		  
		  // Analyse GCIs in which Person appears
		  analyse_AttendsSomeThing_SQSUBSETEQ_Person(),
		  analyse_AttendsSomeThing_AttendsSomeCourse_Person_Student(),		  
		  analyse_AttendsSomeThing_SQSUBSETEQ_Person_Revised(),
		  analyse_AttendsSomeThing_AttendsSomeCourse_Person_Student_Revised(),
		  
      // Analyse Student concept
		  analyse_Student_UGStudentORPGStudent(),
		  analyse_UGStudent_PGStudent(),
		  analyse_Student_UGStudentANDPGStudent(),
		  
		  // Analyse UGStudent concept
		  analyse_UGStudent_StudentANDOnlyAttendsUGCourse(),
		  analyse_UGStudentANDNotAttendsSomeUGCourse(),
		  analyse_PGStudent_StudentANDAttendsSomePGCourse(),
		  analyse_PGStudent_StudentANDAttendsSomePGCourse_Revised()		  
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

  private static Object[] analyse_PersonORCourse() {
    return new Object[] { getContext_PersonORCourse(
        "file:" + PATH.toFile().getAbsolutePath() + "/src/test/resources/University.owl"),
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/UniversityArmstrongABox-PersonORCourse.owl"};
  }

  private static Object[] analyse_PersonANDCourse() {
    return new Object[] { getContext_PersonORCourse(
        "file:" + PATH.toFile().getAbsolutePath() + "/src/test/resources/University.owl"),
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/UniversityArmstrongABox-PersonANDCourse.owl"};
  }
  
  private static Object[] analyse_Person_Course() {
    return new Object[] { getContext_Person_Course(
        "file:" + PATH.toFile().getAbsolutePath() + "/src/test/resources/University.owl"),
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/UniversityArmstrongABox-Person_Course.owl"};
  }
  
  private static Object[] analyse_Person_Course_PersonORCourse() {
    return new Object[] { getContext_Person_Course_PersonORCourse(
        "file:" + PATH.toFile().getAbsolutePath() + "/src/test/resources/University.owl"),
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/UniversityArmstrongABox-Person_Course_PersonORCourse.owl"};
  }

  private static Object[] analyse_Person_Course_PersonANDCourse() {
    return new Object[] { getContext_Person_Course_PersonANDCourse(
        "file:" + PATH.toFile().getAbsolutePath() + "/src/test/resources/University.owl"),
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/UniversityArmstrongABox-Person_Course_PersonANDCourse.owl"};
  }
  
  private static Object[] analyse_Person_Course_PersonORCourse_PersonANDCourse() {
    return new Object[] { getContext_Person_Course_PersonANDCourse(
        "file:" + PATH.toFile().getAbsolutePath() + "/src/test/resources/University.owl"),
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/UniversityArmstrongABox-Person_Course_PersonORCourse_PersonANDCourse.owl"};
  }   

  private static Object[] analyse_Student_Teacher() {
    return new Object[] { getContext_Student_Teacher(
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/University-Person_EQUIV_StudentORTeacher.owl"),
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/UniversityArmstrongABox-Student_Teacher.owl"};
  }  

  private static Object[] analyse_Student_Teacher_StudentANDTeacher() {
    return new Object[] { getContext_Student_Teacher_StudentANDTeacher(
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/University-Person_EQUIV_StudentORTeacher.owl"),
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/UniversityArmstrongABox-Student_Teacher_StudentANDTeacher.owl"};
  } 
  
  private static Object[] analyse_Person_Student_Teacher() {
    return new Object[] { getContext_Person_Student_Teacher(
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/University.owl"),
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/UniversityArmstrongABox-Person_Student_Teacher.owl"};
  }
  
  private static Object[] analyse_Person_StudentORTeacher() {
    return new Object[] { getContext_Person_StudentORTeacher(
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/University.owl"),
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/UniversityArmstrongABox-Person_StudentORTeacher.owl"};
  }
  
  private static Object[] analyse_Person_StudentANDTeacher() {
    return new Object[] { getContext_Person_StudentANDTeacher(
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/University-Person_EQUIV_StudentORTeacher.owl"),
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/UniversityArmstrongABox-Person_StudentANDTeacher.owl"};
  }

  private static Object[] analyse_Person_Student_Teacher_StudentANDTeacher() {
    return new Object[] { getContext_Person_Student_Teacher_StudentANDTeacher(
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/University-Person_EQUIV_StudentORTeacher.owl"),
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/UniversityArmstrongABox-Person_Student_Teacher_StudentANDTeacher.owl"};
  }
  
  private static Object[] analyse_Person_StudentORTeacher_StudentANDTeacher() {
    return new Object[] { getContext_Person_StudentORTeacher_StudentANDTeacher(
        "file:" + PATH.toFile().getAbsolutePath() + "/src/test/resources/University.owl"),
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/UniversityArmstrongABox-Person_StudentORTeacher_StudentANDTeacher.owl"};
  }
  
  private static Object[] analyse_Person_Student_Teacher_StudentORTeacher() {
    return new Object[] { getContext_Person_Student_Teacher_StudentORTeacher(
        "file:" + PATH.toFile().getAbsolutePath() + "/src/test/resources/University.owl"),
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/UniversityArmstrongABox-Person_Student_Teacher_StudentORTeacher.owl"};
  }
  
  private static Object[] analyse_Person_Student_Teacher_StudentORTeacher_StudentANDTeacher() {
    return new Object[] { getContext_Person_Student_Teacher_StudentORTeacher_StudentANDTeacher(
        "file:" + PATH.toFile().getAbsolutePath() + "/src/test/resources/University.owl"),
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/UniversityArmstrongABox-Person_Student_Teacher_StudentORTeacher_StudentANDTeacher.owl"};
  }  
  
  private static Object[] analyse_Person_StudentORTeacher_RevisedEquiv() {
    return new Object[] { getContext_Person_StudentORTeacher(
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/University-Person_EQUIV_StudentORTeacher.owl"),
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/UniversityArmstrongABox-Person_StudentORTeacher_Revised.owl"};
  }

  private static Object[] analyse_Person_StudentORTeacher_RevisedSqsubseteq() {
    return new Object[] { getContext_Person_StudentORTeacher(
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/University-Person_StudentORTeacher_SQSUBSETEQ.owl"),
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/UniversityArmstrongABox-Person_StudentORTeacher_RevisedSqsubseteq.owl"};
  }  

  private static Object[] analyse_Person_Student_Teacher_StudentORTeacher_StudentANDTeacher_Revised() {
    return new Object[] { getContext_Person_Student_Teacher_StudentORTeacher_StudentANDTeacher(
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/University-Person_EQUIV_StudentORTeacher.owl"),
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/UniversityArmstrongABox-Person_Student_Teacher_StudentORTeacher_StudentANDTeacher_Revised.owl"};
  }
  
  private static Object[] analyse_AttendsSomeThing_SQSUBSETEQ_Person() {
    return new Object[] { getContext_AttendsSomeThing_SQSUBSETEQ_Person(
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/University-Person_EQUIV_StudentORTeacher.owl"),
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/UniversityArmstrongABox-AttendsSomeThing_SQSUBSETEQ_Person.owl"};
  } 

  private static Object[] analyse_AttendsSomeThing_AttendsSomeCourse_Person_Student() {
    return new Object[] { getContext_AttendsSomeThing_AttendsSomeCourse_Person_Student(
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/University-Person_EQUIV_StudentORTeacher.owl"),
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/UniversityArmstrongABox-AttendsSomeThing_AttendsSomeCourse_Person_Student.owl"};
  }
  
  private static Object[] analyse_AttendsSomeThing_SQSUBSETEQ_Person_Revised() {
    return new Object[] { getContext_AttendsSomeThing_SQSUBSETEQ_Person(
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/University-OnlyStudentsAttendsSomethingsThatAreCourses.owl"),
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/UniversityArmstrongABox-AttendsSomeThing_SQSUBSETEQ_Person_Revised.owl"};
  }  

  
  private static Object[] analyse_AttendsSomeThing_AttendsSomeCourse_Person_Student_Revised() {
    return new Object[] { getContext_AttendsSomeThing_AttendsSomeCourse_Person_Student(
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/University-OnlyStudentsAttendsSomethingsThatAreCourses.owl"),
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/UniversityArmstrongABox-AttendsSomeThing_AttendsSomeCourse_Person_Student.owl"};
  }
  
  
  private static Object[] analyse_Student_UGStudentORPGStudent() {
    return new Object[] { getContext_Student_UGStudentORPGStudent(
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/University-OnlyStudentsAttendsSomethingsThatAreCourses.owl"),
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/UniversityArmstrongABox-Student_UGStudentORPGStudent.owl"};
  }
  
  private static Object[] analyse_UGStudent_PGStudent() {
    return new Object[] { getContext_UGStudent_PGStudent(
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/University-Student_EQUIV_UGStudentORPGStudent.owl"),
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/UniversityArmstrongABox-UGStudent_PGStudent.owl"};
  }
  
  private static Object[] analyse_Student_UGStudentANDPGStudent() {
    return new Object[] { getContext_Student_UGStudentANDPGStudent(
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/University-Student_EQUIV_UGStudentORPGStudent.owl"),
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/UniversityArmstrongABox-Student_UGStudentANDPGStudent.owl"};
  }
  
  private static Object[] analyse_UGStudent_StudentANDOnlyAttendsUGCourse() {
    return new Object[] { getContext_UGStudent_StudentANDAttendsOnlyUGCourse(
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/University-Student_EQUIV_UGStudentORPGStudent.owl"),
        "file:" + PATH.toFile().getAbsolutePath() + 
          "/src/test/resources/UniversityArmstrongABox-UGStudent_StudentANDOnlyAttendsUGCourse.owl"};
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
  
  private static ArmstrongABox getContext_PersonORCourse(String filename) {    
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
    
    OWLClassExpression personOrCourse = owlReasoner.getOWLClassExpression(
        "Person", OWL_CLASS, "Course", OWL_CLASS, ClassExpressionType.OBJECT_UNION_OF);
    
    setM.add(personOrCourse);
    
    ArmstrongABox armstrongABox = new ArmstrongABoxImpl(owlReasoner, setM);
    return armstrongABox;
  }  
  
  private static ArmstrongABox getContext_Person_Course(String filename) {    
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
    OWLClassExpression courseClass = owlReasoner.getOWLClassExpression("Course", OWL_CLASS);
    
    setM.add(personClass);
    setM.add(courseClass);
    
    ArmstrongABox armstrongABox = new ArmstrongABoxImpl(owlReasoner, setM);
    return armstrongABox;
  }
  
  private static ArmstrongABox getContext_Person_Course_PersonORCourse(String filename) {    
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
    OWLClassExpression courseClass = owlReasoner.getOWLClassExpression("Course", OWL_CLASS);
    OWLClassExpression personOrCourse = owlReasoner.getOWLClassExpression(
        "Person", OWL_CLASS, "Course", OWL_CLASS, ClassExpressionType.OBJECT_UNION_OF);    
    
    setM.add(personClass);
    setM.add(courseClass);
    setM.add(personOrCourse);
    
    ArmstrongABox armstrongABox = new ArmstrongABoxImpl(owlReasoner, setM);
    return armstrongABox;
  }  

  private static ArmstrongABox getContext_Person_Course_PersonANDCourse(String filename) {
    
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
    OWLClassExpression courseClass = owlReasoner.getOWLClassExpression("Course", OWL_CLASS);
    OWLClassExpression personAndCourse = owlReasoner.getOWLClassExpression(
        "Person", OWL_CLASS, "Course", OWL_CLASS, ClassExpressionType.OBJECT_INTERSECTION_OF);    
    
    setM.add(personClass);
    setM.add(courseClass);
    setM.add(personAndCourse);
    
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
  
  
  private static ArmstrongABox getContext_Student_Teacher_StudentANDTeacher(String filename) {
    
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
    OWLClassExpression studentAndTeacher = owlReasoner.getOWLClassExpression(
        "Student", OWL_CLASS, "Teacher", OWL_CLASS, ClassExpressionType.OBJECT_INTERSECTION_OF);
    
    setM.add(studentClass);
    setM.add(teacherClass);
    setM.add(studentAndTeacher);
    
    ArmstrongABox armstrongABox = new ArmstrongABoxImpl(owlReasoner, setM);
    return armstrongABox;
  }  
  
  private static ArmstrongABox getContext_Person_Student_Teacher(String filename) {
    
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
    OWLClassExpression studentClass = owlReasoner.getOWLClassExpression("Student", OWL_CLASS);
    OWLClassExpression teacherClass = owlReasoner.getOWLClassExpression("Teacher", OWL_CLASS);
    
    setM.add(personClass);
    setM.add(studentClass);
    setM.add(teacherClass);
    
    ArmstrongABox armstrongABox = new ArmstrongABoxImpl(owlReasoner, setM);
    return armstrongABox;
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
  
  private static ArmstrongABox getContext_Person_Student_Teacher_StudentANDTeacher(String filename) {
    
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
    OWLClassExpression studentClass = owlReasoner.getOWLClassExpression("Student", OWL_CLASS);
    OWLClassExpression teacherClass = owlReasoner.getOWLClassExpression("Teacher", OWL_CLASS);
    OWLClassExpression studentAndTeacher = owlReasoner.getOWLClassExpression(
        "Student", OWL_CLASS, "Teacher", OWL_CLASS, ClassExpressionType.OBJECT_INTERSECTION_OF);    
    
    setM.add(personClass);
    setM.add(studentClass);
    setM.add(teacherClass);
    setM.add(studentAndTeacher);
    
    ArmstrongABox armstrongABox = new ArmstrongABoxImpl(owlReasoner, setM);
    return armstrongABox;
  }  
  
  private static ArmstrongABox getContext_Person_StudentORTeacher_StudentANDTeacher(String filename) {
    
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
    OWLClassExpression studentAndTeacher = owlReasoner.getOWLClassExpression(
        "Student", OWL_CLASS, "Teacher", OWL_CLASS, ClassExpressionType.OBJECT_INTERSECTION_OF);    
    
    setM.add(personClass);
    setM.add(studentOrTeacher);
    setM.add(studentAndTeacher);
    
    ArmstrongABox armstrongABox = new ArmstrongABoxImpl(owlReasoner, setM);
    return armstrongABox;
  }
  
  private static ArmstrongABox getContext_Person_Student_Teacher_StudentORTeacher(String filename) {
    
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
    OWLClassExpression studentClass = owlReasoner.getOWLClassExpression("Student", OWL_CLASS);
    OWLClassExpression teacherClass = owlReasoner.getOWLClassExpression("Teacher", OWL_CLASS);
    
    OWLClassExpression studentOrTeacher = owlReasoner.getOWLClassExpression(
        "Student", OWL_CLASS, "Teacher", OWL_CLASS, ClassExpressionType.OBJECT_UNION_OF);    
    
    setM.add(personClass);
    setM.add(studentClass);
    setM.add(teacherClass);
    setM.add(studentOrTeacher);
    
    ArmstrongABox armstrongABox = new ArmstrongABoxImpl(owlReasoner, setM);
    return armstrongABox;
  }  
  
  private static ArmstrongABox getContext_Person_Student_Teacher_StudentORTeacher_StudentANDTeacher(String filename) {
    
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
    OWLClassExpression studentClass = owlReasoner.getOWLClassExpression("Student", OWL_CLASS);
    OWLClassExpression teacherClass = owlReasoner.getOWLClassExpression("Teacher", OWL_CLASS);
    OWLClassExpression studentOrTeacher = owlReasoner.getOWLClassExpression(
        "Student", OWL_CLASS, "Teacher", OWL_CLASS, ClassExpressionType.OBJECT_UNION_OF);    
    OWLClassExpression studentAndTeacher = owlReasoner.getOWLClassExpression(
        "Student", OWL_CLASS, "Teacher", OWL_CLASS, ClassExpressionType.OBJECT_INTERSECTION_OF);    
    
    setM.add(personClass);
    setM.add(studentClass);
    setM.add(teacherClass);
    setM.add(studentOrTeacher);
    setM.add(studentAndTeacher);
    
    ArmstrongABox armstrongABox = new ArmstrongABoxImpl(owlReasoner, setM);
    return armstrongABox;
  }  
  
  
  private static ArmstrongABox getContext_AttendsSomeThing_SQSUBSETEQ_Person(String filename) {
    
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
    OWLClassExpression personClass = owlReasoner.getOWLClassExpression("Person", OWL_CLASS);
     
    setM.add(attendsSomeThingClass);
    setM.add(personClass);
    
    ArmstrongABox armstrongABox = new ArmstrongABoxImpl(owlReasoner, setM);
    return armstrongABox;
  } 

  private static ArmstrongABox getContext_AttendsSomeThing_AttendsSomeCourse_Person_Student(String filename) {
    
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
    OWLClassExpression personClass = owlReasoner.getOWLClassExpression("Person", OWL_CLASS);
    OWLClassExpression studentClass = owlReasoner.getOWLClassExpression("Student", OWL_CLASS);
    
    setM.add(attendsSomeThingClass);
    setM.add(attendsSomeCourseClass);
    setM.add(personClass);
    setM.add(studentClass);
    
    ArmstrongABox armstrongABox = new ArmstrongABoxImpl(owlReasoner, setM);
    return armstrongABox;
  }
  
  
  private static ArmstrongABox getContext_Student_UGStudentORPGStudent(String filename) {
    
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
    OWLClassExpression ugStudentORPGStudentClass = owlReasoner.getOWLClassExpression(
        "UGStudent", OWL_CLASS, "PGStudent", OWL_CLASS, ClassExpressionType.OBJECT_UNION_OF);    
    
    setM.add(studentClass);
    setM.add(ugStudentORPGStudentClass);
    
    ArmstrongABox armstrongABox = new ArmstrongABoxImpl(owlReasoner, setM);
    return armstrongABox;
  }  

  private static ArmstrongABox getContext_UGStudent_PGStudent(String filename) {
    
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
    OWLClassExpression pgStudentClass = owlReasoner.getOWLClassExpression("PGStudent", OWL_CLASS);
        
    setM.add(ugStudentClass);
    setM.add(pgStudentClass);
    
    ArmstrongABox armstrongABox = new ArmstrongABoxImpl(owlReasoner, setM);
    return armstrongABox;
  }
  
  private static ArmstrongABox getContext_Student_UGStudentANDPGStudent(String filename) {
    
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
    OWLClassExpression ugStudentANDPGStudentClass = owlReasoner.getOWLClassExpression(
        "UGStudent", OWL_CLASS, "PGStudent", OWL_CLASS, ClassExpressionType.OBJECT_INTERSECTION_OF);    
    
    
    setM.add(studentClass);
    setM.add(ugStudentANDPGStudentClass);
    
    ArmstrongABox armstrongABox = new ArmstrongABoxImpl(owlReasoner, setM);
    return armstrongABox;
  }  

  private static ArmstrongABox getContext_UGStudent_StudentANDAttendsOnlyUGCourse(String filename) {
    
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
    
    OWLClassExpression studentClass = owlReasoner.getOWLClassExpression("Student", OWL_CLASS);
    OWLClassExpression attendsOnlyUGCourse = owlReasoner.getOWLClassExpression("attends", "UGCourse", 
        ClassExpressionType.OBJECT_ALL_VALUES_FROM);
    OWLClassExpression studentANDAttendsOnlyUGCourse = owlReasoner.getOWLClassExpression(
        studentClass, attendsOnlyUGCourse, ClassExpressionType.OBJECT_INTERSECTION_OF);
    
    
    setM.add(ugStudentClass);
    setM.add(studentANDAttendsOnlyUGCourse);
    
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
