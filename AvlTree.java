import java.lang.StringBuilder;
import java.util.Scanner;
import java.io.*;
import java.lang.Math;
import java.awt.Desktop;
import java.util.Scanner;
import java.util.Iterator;




/** 
 * Implementation of an AVL Tree, along with code to test insertions on the tree.
 * 
 *  Based on code written by Mark Allen Weiss in his book 
 *  Data Structures and Algorithm Analysis in Java.
 *
 *  Code for remove is based upon postings at:
 *  http://www.dreamincode.net/forums/topic/214510-working-example-of-avl-tree-remove-method/
 *
 * @author Justin Ethier
 */
class AvlTree<T extends Comparable<? super T>> {
	InputStreamReader isr;
	BufferedReader br;
	Scanner s; //temp
	String archivo; //El archivo que abrirá al terminar de generar el archivo ".svg".
	BufferedWriter bw; //Para escribir el archivo.
  /** 
   * AvlNode is a container class that is used to store each element 
   * (node) of an AVL tree. 
   *  
   * @author Justin Ethier
   */
 //Primer cambio
 // Segundo cambio

  protected static class AvlNode<T> {
    
    /**
     * Node data
     */
    protected T  element;
    
    /**
     * Left child
     */
    protected AvlNode<T>    left;
    
    /**
     * Right child
     */
    protected AvlNode<T>    right;
    
    /**
     * Height of node
     */
    protected int      height;
    
    /**
     * Constructor; creates a node without any children
     * 
     * @param theElement  The element contained in this node
     */
    public AvlNode (T theElement){
      this (theElement, null, null);
    }
    
    /**
     * Constructor; creates a node with children
     * 
     * @param theElement  The element contained in this node
     * @param lt      Left child    
     * @param rt      Right child
     */
    public AvlNode (T theElement, AvlNode<T> lt, AvlNode<T> rt){
      element = theElement;
      left = lt;
      right = rt;
    }
  }

  public AvlNode<T> root;
  
  // TODO: make these optional based on some sort of 'debug' flag?
  // at the very least, make them read-only properties
  public int countInsertions;
  public int countSingleRotations;
  public int countDoubleRotations;
  
  /**
   * Avl Tree Constructor.
   * 
   * Creates an empty tree
   */
  public AvlTree (){
    root = null;
        
    countInsertions = 0;
    countSingleRotations = 0;
    countDoubleRotations = 0;    
  }
  
  /**
   * Determine the height of the given node.
   * 
   * @param t Node
   * @return Height of the given node.
   */
  public int height (AvlNode<T> t){
    return t == null ? -1 : t.height;
  }
  
  /**
   * Find the maximum value among the given numbers.
   * 
   * @param a First number
   * @param b Second number
   * @return Maximum value
   */  
  public int max (int a, int b){
    if (a > b)
      return a;
    return b;
  }
  
  /**
   * Insert an element into the tree.
   * 
   * @param x Element to insert into the tree
   * @return True - Success, the Element was added. 
   *         False - Error, the element was a duplicate.
   */
  public boolean insert (T x){
    try {
      root = insert (x, root); 
      countInsertions++;
      return true;
    } catch(Exception e){ // TODO: catch a DuplicateValueException instead!
      return false;
    }
  }
  
  /**
   * Internal method to perform an actual insertion.
   * 
   * @param x Element to add
   * @param t Root of the tree
   * @return New root of the tree
   * @throws Exception 
   */
  protected AvlNode<T> insert (T x, AvlNode<T> t) throws Exception{
    if (t == null)
      t = new AvlNode<T> (x);
    else if (x.compareTo (t.element) < 0){
      t.left = insert (x, t.left);
      
      if (height (t.left) - height (t.right) == 2){
        if (x.compareTo (t.left.element) < 0){
          t = rotateWithLeftChild (t);
          countSingleRotations++;
        }
        else {
          t = doubleWithLeftChild (t);
          countDoubleRotations++;
        }
      }
    }
    else if (x.compareTo (t.element) > 0){
      t.right = insert (x, t.right);
      
      if ( height (t.right) - height (t.left) == 2)
        if (x.compareTo (t.right.element) > 0){
          t = rotateWithRightChild (t);
          countSingleRotations++;
        }
        else{
          t = doubleWithRightChild (t);
          countDoubleRotations++;
        }
    }
    else {
      throw new Exception("Attempting to insert duplicate value");
    }
    
    t.height = max (height (t.left), height (t.right)) + 1;
    return t;
  }
  
  /**
   * Rotate binary tree node with left child.
   * For AVL trees, this is a single rotation for case 1.
   * Update heights, then return new root.
   * 
   * @param k2 Root of tree we are rotating
   * @return New root
   */
  protected AvlNode<T> rotateWithLeftChild (AvlNode<T> k2){
    AvlNode<T> k1 = k2.left;
    
    k2.left = k1.right;
    k1.right = k2;
    
    k2.height = max (height (k2.left), height (k2.right)) + 1;
    k1.height = max (height (k1.left), k2.height) + 1;
    
    return (k1);
  }
  
  /**
   * Double rotate binary tree node: first left child
   * with its right child; then node k3 with new left child.
   * For AVL trees, this is a double rotation for case 2.
   * Update heights, then return new root.
   * 
   * @param k3 Root of tree we are rotating
   * @return New root
   */
  protected AvlNode<T> doubleWithLeftChild (AvlNode<T> k3){
    k3.left = rotateWithRightChild (k3.left);
    return rotateWithLeftChild (k3);
  }
  
  /**
   * Rotate binary tree node with right child.
   * For AVL trees, this is a single rotation for case 4.
   * Update heights, then return new root.
   * 
   * @param k1 Root of tree we are rotating.
   * @return New root
   */
  protected AvlNode<T> rotateWithRightChild (AvlNode<T> k1){
    AvlNode<T> k2 = k1.right;
    
    k1.right = k2.left;
    k2.left = k1;
    
    k1.height = max (height (k1.left), height (k1.right)) + 1;
    k2.height = max (height (k2.right), k1.height) + 1;
    
    return (k2);
  }

  /**
   * Double rotate binary tree node: first right child
   * with its left child; then node k1 with new right child.
   * For AVL trees, this is a double rotation for case 3.
   * Update heights, then return new root.
   * 
   * @param k1 Root of tree we are rotating
   * @return New root
   */
  protected AvlNode<T> doubleWithRightChild (AvlNode<T> k1){
    k1.right = rotateWithLeftChild (k1.right);
    return rotateWithRightChild (k1);
  }


  /**
   * Serialize the tree to a string using an infix traversal.
   * 
   * In other words, the tree items will be serialized in numeric order. 
   * 
   * @return String representation of the tree
   */
  public String serializeInfix(){
    StringBuilder str = new StringBuilder();
    serializeInfix (root, str, " ");
    return str.toString();
  }

  /**
   * Internal method to infix-serialize a tree.
   * 
   * @param t    Tree node to traverse
   * @param str  Accumulator; string to keep appending items to.
   */
  protected void serializeInfix(AvlNode<T> t, StringBuilder str, String sep){
    if (t != null){
      serializeInfix (t.left, str, sep);
      str.append(t.element.toString());
      str.append(sep);
      serializeInfix (t.right, str, sep);
    }    
  }
  
  /**
   * Serialize the tree to a string using a prefix traversal.
   * 
   * In other words, the tree items will be serialized in the order that
   * they are stored within the tree. 
   * 
   * @return String representation of the tree
   */  
  public String serializePrefix(){
    StringBuilder str = new StringBuilder();
    serializePrefix (root, str, " ");
    return str.toString();
  }
  
  /**
   * Internal method to prefix-serialize a tree.
   * 
   * @param t    Tree node to traverse
   * @param str  Accumulator; string to keep appending items to.
   */  
  private void serializePrefix (AvlNode<T> t, StringBuilder str, String sep){
    if (t != null){
      str.append(t.element.toString());
      str.append(sep);
      serializePrefix (t.left, str, sep);
      serializePrefix (t.right, str, sep);
    }
  }
  
  /**
   * Deletes all nodes from the tree.
   *
   */
  public void makeEmpty(){
    root = null;
  }
  
  /**
   * Determine if the tree is empty.
   * 
   * @return True if the tree is empty 
   */
  public boolean isEmpty(){
    return root == null;
  }



    /**
     * Find the smallest item in the tree.
     * @return smallest item or null if empty.
     */
    public T findMin( )
    {
        if( isEmpty( ) ) return null;

        return findMin( root ).element;
    }

    /**
     * Find the largest item in the tree.
     * @return the largest item of null if empty.
     */
    public T findMax( )
    {
        if( isEmpty( ) ) return null;
        return findMax( root ).element;
    }

    /**
     * Internal method to find the smallest item in a subtree.
     * @param t the node that roots the tree.
     * @return node containing the smallest item.
     */
    private AvlNode<T> findMin(AvlNode<T> t)
    {
        if( t == null )
            return t;

        while( t.left != null )
            t = t.left;
        return t;
    }

    /**
     * Internal method to find the largest item in a subtree.
     * @param t the node that roots the tree.
     * @return node containing the largest item.
     */
    private AvlNode<T> findMax( AvlNode<T> t )
    {
        if( t == null )
            return t;

        while( t.right != null )
            t = t.right;
        return t;
    }


// A version of remove from http://www.dreamincode.net/forums/topic/214510-working-example-of-avl-tree-remove-method/
// but it needs some attention and does not appear to be 100% correct

  /**
   * Remove from the tree. Nothing is done if x is not found.
   * @param x the item to remove.
   */
  public void remove( T x ) {
      root = remove(x, root);
  }

  public AvlNode<T> remove(T x, AvlNode<T> t) {
      if (t==null)    {
          System.out.println("Sorry but you're mistaken, " + t + " doesn't exist in this tree :)\n");
          return null;
      }
      System.out.println("Remove starts... " + t.element + " and " + x);
  
      if (x.compareTo(t.element) < 0 ) {
          t.left = remove(x,t.left);
          int l = t.left != null ? t.left.height : 0;
  
          if((t.right != null) && (t.right.height - l >= 2)) {
              int rightHeight = t.right.right != null ? t.right.right.height : 0;
              int leftHeight = t.right.left != null ? t.right.left.height : 0;
  
              if(rightHeight >= leftHeight)
                  t = rotateWithLeftChild(t);//Primera modificación            
              else
                  t = doubleWithRightChild(t);
          }
      }
      else if (x.compareTo(t.element) > 0) {
          t.right = remove(x,t.right);
          int r = t.right != null ? t.right.height : 0;
          if((t.left != null) && (t.left.height - r >= 2)) {
              int leftHeight = t.left.left != null ? t.left.left.height : 0;
              int rightHeight = t.left.right != null ? t.left.right.height : 0;
              if(leftHeight >= rightHeight)
                  t = rotateWithRightChild(t);               
              else
                  t = doubleWithLeftChild(t);
          }
      }
      /*
         Here, we have ended up when we are node which shall be removed. 
         Check if there is a left-hand node, if so pick out the largest element out, and move down to the root.
       */
      else if(t.left != null) {
          t.element = findMax(t.left).element;
          remove(t.element, t.left);
       
          if((t.right != null) && (t.right.height - t.left.height >= 2)) {
              int rightHeight = t.right.right != null ? t.right.right.height : 0;
              int leftHeight = t.right.left != null ? t.right.left.height : 0;
       
              if(rightHeight >= leftHeight)
                  t = rotateWithLeftChild(t);            
              else
                  t = doubleWithRightChild(t);
          }
      }
       
      else
          t = (t.left != null) ? t.left : t.right;
       
      if(t != null) {
          int leftHeight = t.left != null ? t.left.height : 0;
          int rightHeight = t.right!= null ? t.right.height : 0;
          t.height = Math.max(leftHeight,rightHeight) + 1;
      }
      return t;
  } //End of remove...

  /**
   * Search for an element within the tree. 
   *
   * @param x Element to find
   * @param t Root of the tree
   * @return True if the element is found, false otherwise
   */
  public boolean contains(T x){
    return contains(x, root); 
  }

  /**
   * Internal find method; search for an element starting at the given node.
   *
   * @param x Element to find
   * @param t Root of the tree
   * @return True if the element is found, false otherwise
   */
  protected boolean contains(T x, AvlNode<T> t) {
    if (t == null){
      return false; // The node was not found

    } else if (x.compareTo(t.element) < 0){
      return contains(x, t.left);
    } else if (x.compareTo(t.element) > 0){
      return contains(x, t.right); 
    }

    return true; // Can only reach here if node was found
  }
  
  /***********************************************************************/
  // Diagnostic functions for the tree
  public boolean checkBalanceOfTree(AvlTree.AvlNode<Integer> current) {
    
    boolean balancedRight = true, balancedLeft = true;
    int leftHeight = 0, rightHeight = 0;
    
    if (current.right != null) {
      balancedRight = checkBalanceOfTree(current.right);
      rightHeight = getDepth(current.right);
    }
    
    if (current.left != null) {
      balancedLeft = checkBalanceOfTree(current.left);
      leftHeight = getDepth(current.left);
    }
    
    return balancedLeft && balancedRight && Math.abs(leftHeight - rightHeight) < 2;
  }
  
  public int getDepth(AvlTree.AvlNode<Integer> n) {
    int leftHeight = 0, rightHeight = 0;
    
    if (n.right != null)
      rightHeight = getDepth(n.right);
    if (n.left != null)
      leftHeight = getDepth(n.left);
    
    return Math.max(rightHeight, leftHeight)+1;
  }
  
  public boolean checkOrderingOfTree(AvlTree.AvlNode<Integer> current) {
    if(current.left != null) {
      if(current.left.element.compareTo(current.element) > 0)
        return false;
      else
        return checkOrderingOfTree(current.left);
    } else  if(current.right != null) {
      if(current.right.element.compareTo(current.element) < 0)
        return false;
      else
        return checkOrderingOfTree(current.right);
    } else if(current.left == null && current.right == null)
      return true;
    
    return true;
  }
public static void main(String[] args) {
	/*
  	 System.out.println("Hola!, está es la primera modificación que hago de este código ");
  	 System.out.println("Ingresa los elementos uno por uno de los que quieras que se agreguen al árbol: ");
  	 System.out.println("Cuándo ya sean todos, solo ingresa un -222");
  	 Scanner s = new Scanner(System.in);
  	 int entrada = 0;
  	 AvlTree<Integer> arbolito = new AvlTree<Integer>();
  	 try {
  	 	entrada = s.nextInt();
  	 	while (entrada != -222) {
  	 		arbolito.insert(entrada); // Se agrega al árbol.
  	 		System.out.println("Ingresaste el número " + entrada );
  	 		entrada = s.nextInt();
  	 	}  
  	 } catch (Exception e) {
  	 	System.err.println("Algo hiciste mal, chavo!");
  	 }
  	 System.out.print("Tu árbol in order queda así: ");
  	 System.out.println(arbolito.serializeInfix());
  	 System.out.println("Pre order:");
  	 System.out.println(arbolito.serializePrefix());
  	 */
  	 /*
  	 AvlTree<Integer> arbol = new AvlTree<Integer>();
  	 System.out.println("Bienvenido, este es el nuevo menú un poco más interactivo :)");
  	 int n = menu();
  	 System.out.println(n);
  	 */
  	 AvlTree<Integer> arbol = new AvlTree<Integer>();
  	 System.out.println("Bienvenido, este es el nuevo menú un poco más interactivo :)");
  	 arbol.hazArbolAVL();

  	 
}

public static int menu (){
	System.out.println("Qué deseas hacer ?");
	System.out.println("1.- Agregar elementos al árbol?");
	System.out.println("2.- Eliminar elementos al árbol?");
	System.out.println("3.- Imprimir el árbol");
	System.out.println("4.- Salir ");
	Scanner s = new Scanner (System.in);
	try {
		return s.nextInt();
	} catch (Exception e) {
		System.out.println("Ingresaste mal algo, prro");
		menu(); //Se llama recursivamente
	}
	return 0;
}


	public void hazArbolAVL(){
		//p.dibujaArbolAVL(arbol);
		AvlTree<Integer> arbol2 = new AvlTree<Integer>();
		int [] valores = recibeElementos();
		for (int v :valores) {
			arbol2.insert(v);
		}
		dibujaArbolAVL(arbol2);
	}

	public void dibujaArbolAVL (AvlTree<Integer> arbol){
		archivo = "arbolAVL.svg";
		//Iterator<Integer> it = arbol.iterator();
		int largo=800;
		int ancho=1500;
		int x = (int)(ancho/2);
		int y = 20;
		try {
			bw = new BufferedWriter(new FileWriter(archivo)); //Creamos el archivo para la lista.
			bw.write("<?xml version='1.0' encoding='UTF-8' ?>");
		    bw.write("<svg width='"+ancho+"' height='"+largo+"'>");
  		    bw.write("<g>");
  		    bw.write("<rect x='0' y='0' width='"+ancho+"' height='"+largo+"' fill='black' stroke='black' stroke-width='20' />");
  		    imprimeVertice(arbol.root, x,y,bw,x/2);
  		    bw.write("</g>");
  		    bw.write("</svg>");
  		    bw.close();
		} catch (IOException e) {
			System.out.println("Ocurrió un error.");
		}
		//Proyecto2 p = new Proyecto2();
		abrirarchivo(archivo);
	}

	@SuppressWarnings("unchecked") private void imprimeVertice (AvlNode v,int x,int y, BufferedWriter bw, int a){
		if (v == null) {
			return;
		}
		AvlNode izquierdo = v.left;
		AvlNode derecho = v.right;
		int t =(int)(x/2);
		int s = y+100;
		int t1 =t+(x);
		try {
			if (izquierdo != null) { //Si tienes hijo izquierdo.
				bw.write("<line x1='"+(x-a)+"' y1='"+(s)+"' x2='"+(x)+"' y2='"+(y)+"' stroke = 'brown' stroke-width='5'/>");
			}
			if (derecho != null) { //Si tiene hijo derecho.
				bw.write("<line x1='"+(x+a)+"' y1='"+(s)+"' x2='"+(x)+"' y2='"+(y)+"' stroke = 'brown' stroke-width='5'/>");
			}
			bw.write("<circle cx='"+x+"' cy='"+y+"' r='20' stroke='black' stroke-width='3' fill='green' />");
  			bw.write("<text x='"+x+"' y='"+y+"' font-family='sans-serif' font-size='20' text-anchor='middle' fill='black'>"+v.element+"</text> ");
  			y += 100;
  			imprimeVertice(izquierdo,x-a,y,bw,a/2);
  			imprimeVertice(derecho,x+a,y,bw,a/2);
		} catch (IOException e) {
			System.out.println("Algo salió mal");
		}
		
	}

	public int[] recibeElementos (){
		isr = new InputStreamReader(System.in);
		br = new BufferedReader (isr);
		String entrada = "";
		int [] arreglo1;
		try{
			entrada = br.readLine();
			if (entrada == null) {
				return null;
			}
			entrada = entrada.replaceAll(" ","");
			entrada = entrada.replaceAll(";",",");
			String [] arreglo2 = entrada.split(",");
			entrada = "";
			arreglo1 = new int [arreglo2.length];
			for (int i =0;i<arreglo2.length ;i++ ) {
				arreglo1[i] = Integer.parseInt(arreglo2[i]);
			}
			return arreglo1;
		} catch (Exception e) {
			System.out.println("La cadena no es válida");
		}
		return null;
	}

	public void abrirarchivo(String archivo){ 
     	try {
            File objetofile = new File (archivo);
            Desktop.getDesktop().open(objetofile);
     	}catch (IOException bbb) {
            System.out.println("No está bien el archivo");
     	}
 	} 


  /**
   * Main entry point; contains test code for the tree.
   *
  public static void main () { //String []args){
    AvlTree<Integer> t = new AvlTree<Integer>();
    
    t.insert (new Integer(2));
    t.insert (new Integer(1));
    t.insert (new Integer(4));
    t.insert (new Integer(5));
    t.insert (new Integer(9));
    t.insert (new Integer(3));
    t.insert (new Integer(6));
    t.insert (new Integer(7));
    
    System.out.println ("Infix Traversal:");
    System.out.println(t.serializeInfix());
    
    System.out.println ("Prefix Traversal:");
    System.out.println(t.serializePrefix());
  }*/
}
