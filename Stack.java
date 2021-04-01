/*
In this program, we will create a class called Stack and demonstrate the idea of LIFO used in Data Structure.
*/

import java.util.*;
import java.io.*;
import java.lang.Math.*;

public class Stack<E> implements Serializable   //we have to implement Serializable to be able to save our game
{
	//Data
	private java.util.ArrayList<E> contents;

	//Constructors
	public Stack ()
	{
		//Notice that we are calling the parent's constructor to inherit the EmptyStackException
		super ();
		contents = new java.util.ArrayList<E>();
	}

	//Methods
	//isEmpty method
	public boolean isEmpty()
	{
		return contents.isEmpty();
	}

	//size method
	public int size()
	{
		return contents.size();
	}

	//peek method
	public E peek()
	{
		//throw an exception if the stack is empty
		if (contents.isEmpty() == true)
			throw new IllegalArgumentException("");

		return contents.get(contents.size()-1);
	}


	//pop method
	public E pop()
	{
		//throw an exception if the stack is empty
		if (contents.isEmpty() == true)
			throw new IllegalArgumentException("");

		return contents.remove(contents.size()-1);
	}

	//push method
	public void push(E elt)
	{
		contents.add(elt);
	}

} //end of class
