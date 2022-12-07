package structures;

/**
 * EmptyCollectionException represents the situation in which a collection 
 * is empty.
 * 
 * @version 1.0, 8/19/08
 */


public class EmptyCollectionException extends RuntimeException
{
   /**
    * Sets up this exception with an appropriate message.
    *
    * @param collection  the custom message for the exception
    */
   public EmptyCollectionException (String collection)
   {
      super ("The " + collection + " is empty.");
   }
}

