package structures;

public class LinearCollisionHandler <K> implements CollisionHandler <K>{
    private int probeLength;

    /**
  * Constructors to set probeLength to 1, or a different length.
  */
    public LinearCollisionHandler(){
        this.probeLength = 1;
    }

    public LinearCollisionHandler(int probeLength){
        this.probeLength = probeLength;
    }

/**
  * Method starts at index and searches linearly until an open spot
  * is found in the array. This could include index itself.
  * index = (index + probeLength) % size
  */
   public int probe(int index, boolean[] activeArray, int M) {
      //TODO: Implement this method.
      int curIndex = index;
      while (activeArray[curIndex] == true) {
        curIndex = (curIndex + probeLength) % M;
      }
      return curIndex;
   }

  /**
* Start at index and search the array linearly until the target
* is found. Then return the array index of the target. 
* Return -1 if not found.
*/
   public int search(int startIndex, K target, K[] keyArray, boolean [] activeArray, int M){
      //TODO: Implement this method.
      int curIndex = startIndex;
      int bucketsProbed = 0;

      while((activeArray[curIndex] == true || keyArray[curIndex] != null) && bucketsProbed < M) {
        if (keyArray[curIndex].equals(target) && activeArray[curIndex] == true) {
          return curIndex;
        }
        curIndex = (curIndex + probeLength) % M;
        bucketsProbed++;
      }
      return -1;

   }

   public static void main(String[] args) {
    CollisionHandler <String> collisionHdler = new LinearCollisionHandler<>(3);
    ArrayHashTable<String, String> table = new ArrayHashTable<String, String> (collisionHdler);
    String s1;
    String s2;
    for (int i = 0; i < 100; i++) {
      s1 = "" + Math.random();
      s2 = "" + Math.random();
      table.put(s1, s2);
    }
   }
}
