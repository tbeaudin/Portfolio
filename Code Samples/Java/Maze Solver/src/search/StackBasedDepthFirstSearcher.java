package search;

import java.util.List;
import java.util.Stack;

/**
 * An implementation of a Searcher that performs an iterative search,
 * storing the list of next states in a Stack. This results in a
 * depth-first search.
 * 
 */
public class StackBasedDepthFirstSearcher<T> extends Searcher<T> {
	
	public StackBasedDepthFirstSearcher(SearchProblem<T> searchProblem) {
		super(searchProblem);
	}

	@Override
	public List<T> solve() {
		Stack<T> currentPathStack = new Stack<T>();
		currentPathStack.push(searchProblem.getInitialState());
		visitedStates.add(searchProblem.getInitialState());
		T successor;

		while (currentPathStack.isEmpty() == false) {
			successor = getNextUnvisitedState(currentPathStack.peek());
			if (successor == null) {
				currentPathStack.pop();
			}
			else {
				currentPathStack.push(successor);
				visitedStates.add(successor);
				if (searchProblem.isGoalState(successor)) {
					break;
				}
			}
		}
		return currentPathStack;
	}

	private T getNextUnvisitedState(T state) {
		List<T> successorList = searchProblem.getSuccessors(state);

		for (int i = 0; i < successorList.size(); i++) {
			if (visitedStates.contains(successorList.get(i)) == false) {
				return successorList.get(i);
			}
		}
		return null;
	}
}

	/* DFS (Vertex start) {
	      s = new Stack()
	      s.push(start)
	      while(s not empty) {
 	         v = s.pop();
 	         if(v is not visited)
 	            v.setVisited(true)
 	         visit(v)
 	         while( n=getNextUnvisitedNeighbor(v)) {
 	            stack.push(n)
 	         }
	      } 
	    } */
