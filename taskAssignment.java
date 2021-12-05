package sanjana.combo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class taskAssignment {

	public static void main(String[] args) {
		// FIRST EXAMPLE SET
		System.out.println("FIRST EXAMPLE SET");
		
		//CREATE SETS OF VERTICES
		//initialize lists for sets of vertices for tasks and employees
		String[] tasksList = {"A", "B", "C", "D", "E"};
		int numOfTasks = tasksList.length;
		int[] employeeList = {1,2,3,4,5};
		
		//CREATE SET OF EDGES
		//create empty set of edges using mapping
		Map<String, ArrayList<Integer>>skills = new HashMap<String, ArrayList<Integer>>();
		for (int i = 0; i < numOfTasks; i++) {
			ArrayList<Integer> tasks = new ArrayList<Integer>(0);
			skills.put(tasksList[i], tasks);
		}
		//add edges
		skills.get(tasksList[0]).add(3);
		skills.get(tasksList[1]).add(3);
		skills.get(tasksList[1]).add(5);
		skills.get(tasksList[2]).add(1);
		skills.get(tasksList[2]).add(2);
		skills.get(tasksList[2]).add(4);
		skills.get(tasksList[2]).add(5);
		skills.get(tasksList[3]).add(1);
		skills.get(tasksList[4]).add(1);
		skills.get(tasksList[4]).add(2);
		skills.get(tasksList[4]).add(3);
		
		
		adjacentMatrix(tasksList, employeeList, skills);
		assignTasks(tasksList, employeeList, skills);
		
		
		
		// SECOND EXAMPLE SET
		System.out.println("\n\n\nSECOND EXAMPLE SET");
		
		//CREATE SETS OF VERTICES
		//initialize lists for sets of vertices for tasks and employees
		String[] tasksList2 = {"A", "B", "C", "D", "E"};
		int numOfTasks2 = tasksList2.length;
		int[] employeeList2 = {1,2,3};
		
		//CREATE SET OF EDGES
		//create empty set of edges using mapping
		Map<String, ArrayList<Integer>>edges2 = new HashMap<String, ArrayList<Integer>>();
		for (int i = 0; i < numOfTasks2; i++) {
			ArrayList<Integer> tasks = new ArrayList<Integer>(0);
			edges2.put(tasksList2[i], tasks);
		}
		//add edges
		edges2.get(tasksList2[0]).add(1);
		edges2.get(tasksList2[0]).add(2);
		edges2.get(tasksList2[0]).add(3);
		edges2.get(tasksList2[1]).add(3);
		edges2.get(tasksList2[2]).add(1);
		edges2.get(tasksList2[2]).add(2);
		edges2.get(tasksList2[3]).add(1);
		edges2.get(tasksList2[4]).add(3);
		
		adjacentMatrix(tasksList2, employeeList2, edges2);
		assignTasks(tasksList2, employeeList2, edges2);
		

	}
	
	//ASSIGNING TASKS
	public static void assignTasks(String[] tasksList, int[] employeeList, Map<String, ArrayList<Integer>> edges) {
		int numOfTasks = tasksList.length;
		int numEmployees = employeeList.length;
		
		//create set to keep track of assigned tasks
		Map<String, Integer> assignedTasks = new HashMap<String, Integer>();
		for (int i=0; i<numOfTasks;i++) {
			assignedTasks.put(tasksList[i], 0);
		}
		
		//modified insertion sort to sort tasks by how many people can do it
		String[] sortedTasks = tasksList.clone();
        for (int i = 1; i < numOfTasks; ++i) {
        	String taskValue = sortedTasks[i];
        	int key = edges.get(taskValue).size();
        	
        	int j = i-1;
        	String compareVal = sortedTasks[j]; 
        	int compareSize = edges.get(compareVal).size();
        	
        	 while (j >= 0 && compareSize > key)
             {
        		 sortedTasks[j + 1] = sortedTasks[j];
                 j = j - 1;
                 if (j>=0) {
                	 compareVal = sortedTasks[j]; 
                     compareSize = edges.get(compareVal).size();
                 }
             }
        	 sortedTasks[j + 1] = taskValue;
        }
		
        
        //create integer list to use as a counter for how many tasks assigned to employees
        int[] numAssignedTasks = new int[numEmployees];
        for (int i=0; i<numEmployees; i++) {
        	numAssignedTasks[i] = 0;
        }
        
        for (int i = 0; i < numOfTasks; ++i) {
        	String val = sortedTasks[i];
        	ArrayList<Integer> employees = edges.get(val);
        	int employeeSize = employees.size();
        	int employeeNum = 0;
        	
        	if (employeeSize == 1) {
        		employeeNum = employees.get(0);
        		assignedTasks.put(val, employeeNum);
        		edges = deleteEmployee(edges, val, employeeNum);
        		numAssignedTasks[employeeNum - 1] += 1;
        	} else {
        		int key = numAssignedTasks[employees.get(0)-1];
        		int numTasksAssigned = key;
        		employeeNum = employees.get(0);
        		
        		for (int j = 1; j < employeeSize; j++) {
        			numTasksAssigned = numAssignedTasks[employees.get(j)-1];
        			
        			if (numTasksAssigned < key) {
        				employeeNum = employees.get(j);
        			}
        		}
        		
        		assignedTasks.put(val, employeeNum);
        		numAssignedTasks[employeeNum - 1] += 1;
        	}
        	

        }
        
        System.out.println("\n");
        //print out assigned tasks - for testing
      	for (int i = 0; i<numOfTasks;i++) {
      		System.out.println("Task " + tasksList[i] + " is assigned to employee " + assignedTasks.get(tasksList[i]));
      	}

      	
	}
	
	public static void adjacentMatrix(String[] tasksList, int[] employeeList, Map<String, ArrayList<Integer>>edges) {
		//MODEL WITH BIPARTITE GRAPH WITH 2D ARRAY
		int numOfTasks = tasksList.length;
		int numEmployees = employeeList.length;
		
		//create 2D array with 0s
		int[][] adjMatrix = new int[numOfTasks][numEmployees];
		for (int i=0; i<numOfTasks; i++) {
			for (int j=0; j<numEmployees; j++) {
				adjMatrix[i][j]=0;
			}
		}

		//add 1s for tasks each employee can do 
		for (int i = 0; i < numOfTasks; i++) {
			ArrayList<Integer> tasks = edges.get(tasksList[i]);
			for (int j = 0; j < tasks.size(); j++) {
				int x = tasks.get(j) - 1;
				adjMatrix[i][x] = 1;
			}
		}
		
		//print out matrix for modeling and testing
		System.out.print("  ");
		for (int j=0; j<numEmployees; j++) {
			System.out.print(employeeList[j]);
		}
		System.out.println("");
		for (int i=0; i<numOfTasks; i++) {
			System.out.print(tasksList[i] + " ");
			for (int j=0; j<numEmployees; j++) {
				System.out.print(adjMatrix[i][j]);
			}
			System.out.print("\n");
		}
		
	}
	
	public static Map<String, ArrayList<Integer>> deleteEmployee(Map<String, ArrayList<Integer>>edges, String value, int employee) {
		ArrayList<Integer> employeeList = edges.get(value);
		int size = employeeList.size();
		
		for(int i=0;i<size;i++) {
			if (employeeList.get(i) == employee) {
				employeeList.remove(i);
			}
		}
		
		edges.put(value, employeeList);
		
		Map<String, ArrayList<Integer>> newEdges = edges;
		return newEdges;
		
	}

}