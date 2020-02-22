package ex09;

import java.util.Scanner;
import java.io.File;
import java.lang.reflect.Proxy;
import java.util.Iterator;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.AnnotationImpl;
import javassist.bytecode.annotation.MemberValue;

public class BuffingtonLoader {

	   static String workDir = System.getProperty("user.dir");
	   static String inputDir = workDir + File.separator + "classfiles";
	   static String outputDir = workDir + File.separator + "output";
	   static ClassPool pool;
	   static String[] values = null;
	   
	   public static void main(String[] args) {
		   Scanner input = new Scanner(System.in);
		   
		   System.out.print("Please enter classname, first annotation, and second annotation separated by commas: ");
		   values = input.nextLine().split(",");
		   
		   while (values.length != 3) {
			   System.out.println("[WRN] Invalid input size!!");
			   System.out.print("Please enter classname, first annotation, and second annotation separated by commas: ");
			   values = input.nextLine().split(",");
		   }
		   
		   input.close();
		   
	      try {
	    	  pool = ClassPool.getDefault();
	          pool.insertClassPath(inputDir);

	          CtClass ct = pool.get(values[0]);
	          CtField[] cfs = ct.getFields();

	          process(ct.getAnnotations());
	          System.out.println();
	          
	          for (CtField cf : cfs) {
	        	  process(cf.getAnnotations());
	        	  System.out.println();
	          }
	          
	      } catch (NotFoundException | ClassNotFoundException e) {
	         e.printStackTrace();
	      }
	   }

	   static void process(Object[] annoList) {

	      for (int i = 0; i < annoList.length; i++) {
	         Annotation annotation = getAnnotation(annoList[i]);
	         if (values[1].equals(annotation.getTypeName()) || values[2].equals(annotation.getTypeName())) {
	            showAnnotation(annotation);
	         }
	      }
	      
	   }
	      
      static Annotation getAnnotation(Object obj) {
          // Get the underlying type of a proxy object in java
          AnnotationImpl annotationImpl = //
                (AnnotationImpl) Proxy.getInvocationHandler(obj);
          return annotationImpl.getAnnotation();
       }

       static void showAnnotation(Annotation annotation) {
          Iterator<?> iterator = annotation.getMemberNames().iterator();
          while (iterator.hasNext()) {
             Object keyObj = (Object) iterator.next();
             MemberValue value = annotation.getMemberValue(keyObj.toString());
             System.out.println("[DBG] " + keyObj + ": " + value);
          }

       }
	}
