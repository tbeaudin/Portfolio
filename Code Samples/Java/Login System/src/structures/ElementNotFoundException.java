package structures;

/**
 * ElementNotFoundException represents the situation in which a target element 
 * is not present in a collection
 *
 * @version 1.0, 08/12/08
 */


public class ElementNotFoundException extends RuntimeException
{
   /**
    * Sets up this exception with an appropriate message.
    */
   public ElementNotFoundException (String collection)
   {
      super ("The target element is not in this " + collection);
   }
}
